package neatlogic.module.rdm.event;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.dto.UserVo;
import neatlogic.framework.rdm.dto.IssueAttrVo;
import neatlogic.framework.rdm.dto.IssueVo;
import neatlogic.module.rdm.service.IssueService;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

/** 独立验证需求快照和归属边界，不依赖数据库或 Spring 容器。 */
public class IssueEventObjectAdapterTest {
    /** 检查嵌套对象、动态属性、共享引用、非法类型及重载归属。 */
    public static void main(String[] args) {
        AtomicReference<IssueVo> current = new AtomicReference<>(issue(1L, 2L, 3L));
        IssueService service = (IssueService) Proxy.newProxyInstance(IssueService.class.getClassLoader(),
                new Class<?>[]{IssueService.class}, (proxy, method, arguments) -> current.get());
        IssueEventObjectAdapter adapter = new IssueEventObjectAdapter(service);
        IssueVo source = issue(1L, 2L, 3L);
        source.setCreateDate(new Date(100L));
        source.setTagList(new ArrayList<>(Arrays.asList("原始标签")));
        UserVo user = new UserVo();
        user.setUserName("原始姓名");
        source.setUserList(new ArrayList<>(Arrays.asList(user)));
        IssueAttrVo attr = new IssueAttrVo();
        JSONArray nested = new JSONArray();
        nested.add("原始值");
        JSONObject config = new JSONObject();
        config.put("values", nested);
        config.put("self", config);
        attr.setConfig(config);
        attr.setValueList(new ArrayList<>(Arrays.asList(nested)));
        source.setAttrList(new ArrayList<>(Arrays.asList(attr)));
        IssueVo snapshot = adapter.snapshot(source);
        source.getCreateDate().setTime(200L);
        source.getTagList().set(0, "修改标签");
        user.setUserName("修改姓名");
        nested.set(0, "修改值");
        check(snapshot != source && snapshot.getCreateDate().getTime() == 100L, "日期必须独立复制");
        check("原始标签".equals(snapshot.getTagList().get(0)), "列表必须独立复制");
        check("原始姓名".equals(snapshot.getUserList().get(0).getUserName()), "嵌套用户必须独立复制");
        JSONObject copiedConfig = snapshot.getAttrList().get(0).getConfig();
        check("原始值".equals(copiedConfig.getJSONArray("values").getString(0)), "动态属性必须深拷贝");
        check(copiedConfig.get("self") == copiedConfig, "循环引用必须指向快照");
        check(copiedConfig.get("values") == snapshot.getAttrList().get(0).getValueList().get(0), "共享引用应保留");
        IssueVo withoutId = new IssueVo();
        expectFailure(() -> adapter.getObjectId(withoutId));
        check(!withoutId.hasId(), "校验不得生成需求 ID");
        expectFailure(() -> adapter.validateScope(9L, 3L, source));
        expectFailure(() -> adapter.validateScope(2L, 9L, source));
        attr.setValueList(Arrays.asList(new StringBuilder("不支持的可变对象")));
        expectFailure(() -> adapter.snapshot(source));

        check(adapter.reload(2L, 3L, "1") == current.get(), "重载必须使用业务服务最新结果");
        current.set(issue(1L, 9L, 3L));
        expectFailure(() -> adapter.reload(2L, 3L, "1"));
        current.set(issue(9L, 2L, 3L));
        expectFailure(() -> adapter.reload(2L, 3L, "1"));
        current.set(null);
        check(adapter.reload(2L, 3L, "1") == null, "已删除对象应交由框架识别缺失");
        System.out.println("IssueEventObjectAdapterTest PASSED");
    }

    /** 构建具有明确归属的测试需求。 */
    private static IssueVo issue(Long id, Long projectId, Long appId) {
        IssueVo issue = new IssueVo();
        issue.setId(id);
        issue.setProjectId(projectId);
        issue.setAppId(appId);
        return issue;
    }

    /** 断言无效输入在业务动作之前被拒绝。 */
    private static void expectFailure(Runnable action) {
        try {
            action.run();
        } catch (IllegalArgumentException expected) {
            return;
        }
        throw new AssertionError("预期拒绝无效输入");
    }

    /** 报告具体不满足的快照约束。 */
    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
