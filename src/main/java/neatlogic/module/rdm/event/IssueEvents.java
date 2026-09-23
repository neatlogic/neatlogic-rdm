package neatlogic.module.rdm.event;

import neatlogic.framework.rdm.dto.IssueVo;
import neatlogic.framework.rdm.enums.core.AppTypeManager;
import neatlogic.framework.rdm.enums.core.IAppType;
import neatlogic.framework.rdm.event.IRdmEventDefinitionProvider;
import neatlogic.framework.rdm.event.RdmEventDefinition;
import org.springframework.stereotype.Component;

import java.util.*;

/** 需求模块拥有的强类型事件定义，业务入口直接使用这些常量。 */
@Component
public class IssueEvents implements IRdmEventDefinitionProvider {
    private static final Set<String> APP_TYPES = issueAppTypes();
    public static final RdmEventDefinition<IssueVo> CREATED = new RdmEventDefinition<>(
            "ISSUE_CREATE", "term.rdm.event.issuecreate.label", "term.rdm.event.issuecreate.description", APP_TYPES, "issue", IssueVo.class, false);
    public static final RdmEventDefinition<IssueVo> UPDATED = new RdmEventDefinition<>(
            "ISSUE_UPDATE", "term.rdm.event.issueupdate.label", "term.rdm.event.issueupdate.description", APP_TYPES, "issue", IssueVo.class, false);
    public static final RdmEventDefinition<IssueVo> STATUS_CHANGED = new RdmEventDefinition<>(
            "ISSUE_STATUS_CHANGE", "term.rdm.event.issuestatuschange.label", "term.rdm.event.issuestatuschange.description", APP_TYPES, "issue", IssueVo.class, false);
    public static final RdmEventDefinition<IssueVo> DELETED = new RdmEventDefinition<>(
            "ISSUE_DELETE", "term.rdm.event.issuedelete.label", "term.rdm.event.issuedelete.description", APP_TYPES, "issue", IssueVo.class, true);

    /** 复用应用扩展机制，所有声明管理需求的应用均支持需求事件。 */
    private static Set<String> issueAppTypes() {
        Set<String> types = new LinkedHashSet<>();
        for (IAppType type : AppTypeManager.getAppTypeList()) {
            if (type.getHasIssue()) {
                types.add(type.getName());
            }
        }
        return Collections.unmodifiableSet(types);
    }

    /** 将本模块事件交给统一注册表，保留已有持久化事件标识。 */
    @Override
    public List<RdmEventDefinition<?>> getEvents() {
        return Arrays.asList(CREATED, UPDATED, STATUS_CHANGED, DELETED);
    }

}
