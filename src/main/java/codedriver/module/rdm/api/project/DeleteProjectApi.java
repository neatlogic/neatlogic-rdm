/*
 * Copyright(c) 2023 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.module.rdm.api.project;

import codedriver.framework.auth.core.AuthAction;
import codedriver.framework.common.constvalue.ApiParamType;
import codedriver.framework.rdm.auth.label.RDM_BASE;
import codedriver.framework.rdm.dto.ObjectVo;
import codedriver.framework.rdm.exception.CreateObjectSchemaException;
import codedriver.framework.restful.annotation.Description;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.OperationType;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.constvalue.OperationTypeEnum;
import codedriver.framework.restful.core.privateapi.PrivateApiComponentBase;
import codedriver.framework.transaction.core.EscapeTransactionJob;
import codedriver.module.rdm.dao.mapper.IssueMapper;
import codedriver.module.rdm.dao.mapper.ProjectMapper;
import codedriver.module.rdm.service.ProjectService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.DELETE)
@Transactional
public class DeleteProjectApi extends PrivateApiComponentBase {

    @Resource
    private ProjectMapper projectMapper;


    @Resource
    private ProjectService projectService;

    @Resource
    private IssueMapper issueMapper;

    @Override
    public String getName() {
        return "删除项目";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "id", desc = "项目id", isRequired = true, type = ApiParamType.LONG)})
    @Description(desc = "删除项目接口")
    @Override
    public Object myDoService(JSONObject paramObj) {
        Long projectId = paramObj.getLong("id");
        List<ObjectVo> objectList = projectMapper.getObjectDetailByProjectId(projectId);
        for (ObjectVo objectVo : objectList) {
            issueMapper.deleteIssueByObjectId(objectVo.getId());
            projectMapper.deleteObjectById(objectVo.getId());
            EscapeTransactionJob.State s = projectService.dropObjectSchema(objectVo);
            if (!s.isSucceed()) {
                throw new CreateObjectSchemaException(objectVo.getName());
            }
        }
        projectMapper.deleteProjectById(projectId);
        return null;
    }

    @Override
    public String getToken() {
        return "/rdm/project/delete";
    }
}
