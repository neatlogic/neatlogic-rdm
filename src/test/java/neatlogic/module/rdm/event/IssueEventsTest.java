package neatlogic.module.rdm.event;

import neatlogic.framework.rdm.dto.IssueVo;
import neatlogic.framework.rdm.enums.core.AppTypeManager;
import neatlogic.framework.rdm.enums.core.IAppType;
import neatlogic.framework.rdm.event.RdmEventDefinition;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** 在无 Web 容器进程中验证事件常量初始化及应用扩展类型发现。 */
public class IssueEventsTest {
    /** 使用项目真实类路径扫描，不向反射注册表人工注入内置应用。 */
    public static void main(String[] args) {
        IssueEvents provider = new IssueEvents();
        List<RdmEventDefinition<?>> events = provider.getEvents();
        if (events.size() != 4) {
            throw new AssertionError("应注册四个需求事件");
        }
        Set<String> expectedTypes = new HashSet<>();
        for (IAppType appType : AppTypeManager.getAppTypeList()) {
            if (appType.getHasIssue()) {
                expectedTypes.add(appType.getName());
            }
        }
        if (!expectedTypes.containsAll(Arrays.asList("story", "task", "bug"))) {
            throw new AssertionError("无 Web 容器时也必须发现内置需求应用类型");
        }
        Set<String> names = new HashSet<>();
        for (RdmEventDefinition<?> event : events) {
            names.add(event.getName());
            if (event.getObjectClass() != IssueVo.class || !event.getAppTypes().equals(expectedTypes)) {
                throw new AssertionError("需求事件必须绑定 IssueVo 和全部 hasIssue 应用");
            }
        }
        if (!names.equals(new HashSet<>(Arrays.asList("ISSUE_CREATE", "ISSUE_UPDATE",
                "ISSUE_STATUS_CHANGE", "ISSUE_DELETE")))) {
            throw new AssertionError("必须保留已持久化的事件标识");
        }
        if (!IssueEvents.DELETED.isDeleted() || IssueEvents.CREATED.isDeleted()
                || IssueEvents.UPDATED.isDeleted() || IssueEvents.STATUS_CHANGED.isDeleted()) {
            throw new AssertionError("仅删除事件沿用快照");
        }
        System.out.println("IssueEventsTest PASSED");
    }
}
