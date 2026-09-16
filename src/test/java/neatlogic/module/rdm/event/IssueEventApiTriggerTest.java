package neatlogic.module.rdm.event;

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.asynchronization.thread.ModuleInitApplicationListener;
import neatlogic.framework.asynchronization.threadlocal.TenantContext;
import neatlogic.framework.asynchronization.threadlocal.UserContext;
import neatlogic.framework.asynchronization.threadpool.CachedThreadPool;
import neatlogic.framework.dto.UserVo;
import neatlogic.framework.fulltextindex.core.FullTextIndexHandlerFactory;
import neatlogic.framework.fulltextindex.core.IFullTextIndexHandler;
import neatlogic.framework.rdm.dao.mapper.RdmEventMapper;
import neatlogic.framework.rdm.dto.AppVo;
import neatlogic.framework.rdm.dto.IssueVo;
import neatlogic.framework.rdm.dto.ProjectVo;
import neatlogic.framework.rdm.enums.IssueFullTextIndexType;
import neatlogic.framework.rdm.event.*;
import neatlogic.module.rdm.api.issue.DeleteIssueApi;
import neatlogic.module.rdm.api.issue.SaveIssueApi;
import neatlogic.module.rdm.auth.ProjectAuthManager;
import neatlogic.module.rdm.dao.mapper.AppMapper;
import neatlogic.module.rdm.dao.mapper.IssueMapper;
import neatlogic.module.rdm.dao.mapper.ProjectMapper;
import neatlogic.module.rdm.notify.service.RdmNotifyService;
import neatlogic.module.rdm.service.IssueService;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;

/** 运行真实保存与删除 API 分支，用代理隔离数据库和通知发送。 */
public class IssueEventApiTriggerTest {
    private final BlockingQueue<String> published = new LinkedBlockingQueue<>();
    private IssueVo oldIssue;
    private IssueVo currentIssue;
    private final java.util.concurrent.atomic.AtomicBoolean capability = new java.util.concurrent.atomic.AtomicBoolean(true);
    private boolean saved;
    private boolean deleted;
    private boolean indexed;
    private int reads;
    private final SaveIssueApi saveApi = new SaveIssueApi();
    private final DeleteIssueApi deleteApi = new DeleteIssueApi();

    /** 检查真实调用路径及提交回调，不向数据库写入任何内容。 */
    public static void main(String[] args) throws Exception {
        IssueEventApiTriggerTest test = new IssueEventApiTriggerTest();
        try {
            test.initialize();
            test.save(false, false, true, true, "ISSUE_CREATE");
            test.save(true, true, false, true, "ISSUE_STATUS_CHANGE");
            test.save(true, false, true, true, "ISSUE_UPDATE");
            test.save(true, true, true, true, "ISSUE_STATUS_CHANGE", "ISSUE_UPDATE");
            test.save(true, false, false, true);
            test.save(false, false, true, false, "ISSUE_CREATE");
            test.delete(true);
            test.delete(false);
            // 商业能力关闭时普通生命周期及原通知仍完成，事件配置不查询。
            test.capability.set(false);
            test.save(false, false, true, true);
            test.save(true, true, true, true);
            test.delete(true);
            System.out.println("IssueEventApiTriggerTest PASSED");
        } finally {
            if (TransactionSynchronizationManager.isSynchronizationActive()) {
                TransactionSynchronizationManager.clearSynchronization();
            }
            Field pool = CachedThreadPool.class.getDeclaredField("mainThreadPool");
            pool.setAccessible(true);
            ((ExecutorService) pool.get(null)).shutdownNow();
        }
    }

    /** 注入边界代理，事件发布和事务提交回调仍使用生产实现。 */
    private void initialize() throws Exception {
        RdmEventRuntimeCapabilityFactory.register(capability::get);
        ModuleInitApplicationListener.getModuleinitphaser().forceTermination();
        TenantContext.init("rdm-api-event-test");
        UserVo user = new UserVo();
        user.setUuid("event-test");
        user.setUserId("event-test");
        user.setAuthorization("test-token");
        UserContext.init(user, null, "Asia/Hong_Kong");
        AppVo app = new AppVo();
        app.setId(3L);
        app.setProjectId(2L);
        app.setType("story");
        AppMapper apps = proxy(AppMapper.class, (name, args) -> {
            if (name.equals("getAppById")) { return app; }
            return null;
        });
        ProjectVo project = new ProjectVo() {
            /** 测试固定授权，避免测试依赖真实项目成员数据。 */
            @Override public boolean getIsLeader() { return true; }
        };
        ProjectMapper projects = proxy(ProjectMapper.class, (name, args) -> project);
        new ProjectAuthManager(projects);
        IssueService issues = proxy(IssueService.class, (name, args) -> {
            if (name.equals("saveIssue")) {
                IssueVo submitted = IssueVo.class.cast(args[0]);
                submitted.setId(1L);
                saved = true;
                return null;
            }
            if (name.equals("getIssueById")) {
                reads++;
                if (saved) { return currentIssue; }
                return oldIssue;
            }
            throw new AssertionError("非预期需求服务调用：" + name);
        });
        RdmEventObjectAdapterFactory.register(new IssueEventObjectAdapter(issues));
        IssueEvents provider = new IssueEvents();
        for (RdmEventDefinition<?> event : provider.getEvents()) { RdmEventRegistry.register(event); }
        RdmEventMapper events = proxy(RdmEventMapper.class, (name, args) -> {
            if (name.equals("getEventAppById")) { return app; }
            if (name.equals("getHandlerByEvent")) {
                check(Long.valueOf(2L).equals(args[1]) && Long.valueOf(3L).equals(args[2]), "事件项目应用范围错误");
                published.add(String.class.cast(args[0]));
                return Collections.emptyList();
            }
            throw new AssertionError("非预期事件查询：" + name);
        });
        new RdmEventManager(events);
        IssueMapper issueMapper = proxy(IssueMapper.class, (name, args) -> {
            if (name.equals("getIssueStatusById")) { return 10L; }
            if (name.equals("deleteIssueById")) { deleted = true; return 1; }
            return null;
        });
        RdmNotifyService notify = (context, trigger) -> {
            if ("deleted".equals(trigger.getTrigger())) {
                check(deleted && indexed, "删除及索引完成前不应通知");
            }
        };
        inject(saveApi, "issueService", issues);
        inject(saveApi, "appMapper", apps);
        inject(saveApi, "issueMapper", issueMapper);
        inject(saveApi, "rdmNotifyService", notify);
        inject(deleteApi, "issueService", issues);
        inject(deleteApi, "appMapper", apps);
        inject(deleteApi, "issueMapper", issueMapper);
        inject(deleteApi, "projectMapper", projects);
        inject(deleteApi, "rdmNotifyService", notify);
        Field components = FullTextIndexHandlerFactory.class.getDeclaredField("componentMap");
        components.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, IFullTextIndexHandler> handlers = (Map<String, IFullTextIndexHandler>) components.get(null);
        handlers.put(IssueFullTextIndexType.ISSUE.getType(), proxy(IFullTextIndexHandler.class, (name, args) -> {
            if (name.equals("deleteIndex")) { check(deleted, "必须先删除需求再删除索引"); indexed = true; }
            return null;
        }));
    }

    /** 验证创建、状态与普通字段分支，以及无字段变动时不发布。 */
    private void save(boolean existing, boolean changedStatus, boolean ordinary, boolean commit, String... expected) throws Exception {
        saved = false;
        reads = 0;
        oldIssue = issue(10L);
        currentIssue = issue(10L);
        if (changedStatus) { currentIssue.setStatus(11L); }
        JSONObject input = new JSONObject();
        input.put("appId", 3L);
        if (existing) { input.put("id", 1L); }
        if (ordinary) { input.put("name", "完整保存对象"); }
        if (changedStatus) { input.put("status", 11L); }
        else if (existing && !ordinary) { input.put("status", 10L); }
        TransactionSynchronizationManager.initSynchronization();
        saveApi.myDoService(input);
        check(saved && reads >= 1, "必须保存后读取完整需求");
        check(published.isEmpty(), "提交前不应查询事件配置");
        check(TransactionSynchronizationManager.getSynchronizations().size() == expected.length, "真实 API 事件分支数量错误");
        complete(commit);
        assertPublished(commit, expected);
    }

    /** 验证删除使用删除前完整对象，提交后无需再次读取已删除需求。 */
    private void delete(boolean commit) throws Exception {
        saved = false;
        deleted = false;
        indexed = false;
        reads = 0;
        oldIssue = issue(10L);
        JSONObject input = new JSONObject();
        input.put("id", 1L);
        TransactionSynchronizationManager.initSynchronization();
        deleteApi.myDoService(input);
        check(deleted && indexed && reads == 1, "删除应只读取一次删除前完整对象");
        check(published.isEmpty(), "删除事务提交前不应发布");
        complete(commit);
        if (capability.get()) { assertPublished(commit, "ISSUE_DELETE"); }
        else { assertPublished(commit); }
        check(reads == 1, "删除事件不应重载数据库对象");
    }

    /** 消费真实提交后查询，回滚必须没有异步发布。 */
    private void assertPublished(boolean commit, String... expected) throws Exception {
        if (commit) {
            Set<String> actual = new HashSet<>();
            for (int i = 0; i < expected.length; i++) {
                actual.add(published.poll(10, TimeUnit.SECONDS));
            }
            check(actual.equals(new HashSet<>(Arrays.asList(expected))), "提交后的事件类型错误：" + actual);
        }
        check(published.poll(100, TimeUnit.MILLISECONDS) == null, "回滚或无变化调用不应发布额外事件");
    }

    /** 仅模拟 Spring 事务通知，真实异步回调由生产框架处理。 */
    private static void complete(boolean commit) {
        for (TransactionSynchronization callback : TransactionSynchronizationManager.getSynchronizations()) {
            if (commit) { callback.afterCommit(); }
            if (commit) { callback.afterCompletion(TransactionSynchronization.STATUS_COMMITTED); }
            else { callback.afterCompletion(TransactionSynchronization.STATUS_ROLLED_BACK); }
        }
        TransactionSynchronizationManager.clearSynchronization();
    }

    /** 构造具有稳定业务身份及范围的完整需求。 */
    private static IssueVo issue(Long status) {
        IssueVo issue = new IssueVo();
        issue.setId(1L); issue.setProjectId(2L); issue.setAppId(3L);
        issue.setName("完整保存对象"); issue.setStatus(status);
        return issue;
    }

    /** 用接口代理替换外部边界，不替换 API 本身或事件引擎。 */
    private static <T> T proxy(Class<T> type, BiFunction<String, Object[], Object> action) {
        return type.cast(Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[]{type},
                (proxy, method, args) -> action.apply(method.getName(), args)));
    }

    /** 向真实 API 实例注入测试边界。 */
    private static void inject(Object target, String name, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(name);
        field.setAccessible(true); field.set(target, value);
    }

    /** 报告违反的业务触发约束。 */
    private static void check(boolean condition, String message) {
        if (!condition) { throw new AssertionError(message); }
    }
}
