package neatlogic.module.rdm.event;

import neatlogic.framework.rdm.dto.IssueVo;
import neatlogic.framework.rdm.event.IRdmEventObjectAdapter;
import neatlogic.module.rdm.service.IssueService;
import org.springframework.stereotype.Component;

import java.util.Objects;

/** 将需求业务对象及加载规则接入事件框架。 */
@Component
public final class IssueEventObjectAdapter implements IRdmEventObjectAdapter<IssueVo> {
    private final IssueService issueService;

    /** 由所属模块初始化业务依赖，事件核心无需引用需求 Service。 */
    public IssueEventObjectAdapter(IssueService issueService) {
        this.issueService = Objects.requireNonNull(issueService, "需求服务不能为空");
    }

    /** 声明插件接收的具体业务类型。 */
    @Override
    public Class<IssueVo> getObjectClass() {
        return IssueVo.class;
    }

    /** 获取跨事件稳定的审计对象类型。 */
    @Override
    public String getObjectType() {
        return "issue";
    }

    /** 仅接受已明确设置的标识，避免事件发布意外生成业务 ID。 */
    @Override
    public String getObjectId(IssueVo object) {
        if (object == null || !object.hasId()) {
            throw new IllegalArgumentException("事件需求对象必须包含已有标识");
        }
        return object.getId().toString();
    }

    /** 为异步发布隔离所有可变字段，保留删除前业务快照。 */
    @Override
    public IssueVo snapshot(IssueVo object) {
        getObjectId(object);
        return IssueEventSnapshot.copy(object);
    }

    /** 在插件事务内重新读取完整需求，并拒绝跨项目或跨应用对象。 */
    @Override
    public IssueVo reload(Long projectId, Long appId, String objectId) {
        IssueVo object = issueService.getIssueById(Long.valueOf(objectId));
        if (object != null) {
            validateScope(projectId, appId, object);
            if (!objectId.equals(getObjectId(object))) {
                throw new IllegalArgumentException("重载需求标识与事件对象不一致");
            }
        }
        return object;
    }

    /** 校验对象归属；应用与项目的数据库关系由框架通过应用 Mapper 共同校验。 */
    @Override
    public void validateScope(Long projectId, Long appId, IssueVo object) {
        getObjectId(object);
        if (projectId == null || appId == null || !projectId.equals(object.getProjectId())
                || !appId.equals(object.getAppId())) {
            throw new IllegalArgumentException("需求对象不属于事件指定的项目和应用");
        }
    }
}
