package neatlogic.module.rdm.event;

import neatlogic.framework.rdm.dto.IssueVo;
import neatlogic.framework.rdm.event.IRdmEventDefinitionProvider;
import neatlogic.framework.rdm.event.RdmEventDefinition;
import neatlogic.framework.rdm.event.RdmEventManager;
import neatlogic.framework.rdm.event.RdmEventRegistry;
import org.springframework.stereotype.Component;

import java.util.*;

/** 社区模块拥有需求、任务和缺陷事件；IssueVo 仅是执行数据类型。 */
@Component
public class IssueEvents implements IRdmEventDefinitionProvider {
    private static final Set<String> APP_TYPES = Collections.unmodifiableSet(
            new LinkedHashSet<>(Arrays.asList("story", "task", "bug", "testcase", "testplan")));
    private static final List<String> ACTIONS = Arrays.asList("CREATE", "DELETE", "STATUS_CHANGE", "UPDATE");
    public static final RdmEventDefinition<IssueVo> CREATED = definition("story", "CREATE");
    public static final RdmEventDefinition<IssueVo> DELETED = definition("story", "DELETE");
    public static final RdmEventDefinition<IssueVo> STATUS_CHANGED = definition("story", "STATUS_CHANGE");
    public static final RdmEventDefinition<IssueVo> UPDATED = definition("story", "UPDATE");

    /** 事件标识及所属应用显式绑定，不能由数据对象类型推断。 */
    public static RdmEventDefinition<IssueVo> definition(String appType, String action) {
        if (!APP_TYPES.contains(appType) || !ACTIONS.contains(action)) {
            throw new IllegalArgumentException("未知应用事件：" + appType + "/" + action);
        }
        String key = "term.rdm.event." + appType + action.replace("_", "").toLowerCase();
        return new RdmEventDefinition<>(appType.toUpperCase(Locale.ROOT) + "_" + action,
                key + ".label", key + ".description", Collections.singleton(appType),
                "issue", IssueVo.class, "DELETE".equals(action));
    }

    /** 查询当前应用对应的强类型定义，并在注册缺失时跳过可选商业能力。 */
    @SuppressWarnings("unchecked")
    public static RdmEventDefinition<IssueVo> resolve(String appType, String action) {
        if (!APP_TYPES.contains(appType)) {
            return null;
        }
        RdmEventDefinition<?> event = RdmEventRegistry.get(appType.toUpperCase(Locale.ROOT) + "_" + action);
        if (event == null || event.getObjectClass() != IssueVo.class
                || !event.getAppTypes().equals(Collections.singleton(appType))) {
            return null;
        }
        return (RdmEventDefinition<IssueVo>) event;
    }

    /** 保存、删除入口只发布所属应用已注册的事件。 */
    public static void publish(String appType, String action, IssueVo issue) {
        RdmEventDefinition<IssueVo> event = resolve(appType, action);
        if (event != null) {
            RdmEventManager.doEvent(issue.getProjectId(), issue.getAppId(), event, issue);
        }
    }

    /** 列出 IssueVo 插件支持的五种应用生命周期事件。 */
    public static Set<String> lifecycleEventNames() {
        Set<String> names = new LinkedHashSet<>();
        for (String appType : APP_TYPES) {
            for (String action : ACTIONS) {
                names.add(appType.toUpperCase(Locale.ROOT) + "_" + action);
            }
        }
        return names;
    }

    /** 邮件和集成查询只接收显式拥有生命周期事件的应用。 */
    public static boolean supportsAppType(String appType) {
        return APP_TYPES.contains(appType);
    }

    /** 只注册社区模块拥有的应用事件。 */
    @Override
    public List<RdmEventDefinition<?>> getEvents() {
        List<RdmEventDefinition<?>> events = new ArrayList<>();
        events.addAll(Arrays.asList(CREATED, DELETED, STATUS_CHANGED, UPDATED));
        for (String appType : Arrays.asList("task", "bug")) {
            for (String action : ACTIONS) {
                events.add(definition(appType, action));
            }
        }
        return events;
    }
}
