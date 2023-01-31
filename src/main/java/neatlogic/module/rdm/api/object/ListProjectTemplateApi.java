/*
 * Copyright(c) 2023 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package neatlogic.module.rdm.api.object;

import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.ProjectTemplateVo;
import neatlogic.framework.restful.annotation.Description;
import neatlogic.framework.restful.annotation.OperationType;
import neatlogic.framework.restful.annotation.Output;
import neatlogic.framework.restful.annotation.Param;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.module.rdm.dao.mapper.ProjectTemplateMapper;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.SEARCH)
public class ListProjectTemplateApi extends PrivateApiComponentBase {

    @Resource
    private ProjectTemplateMapper projectTemplateMapper;

    @Override
    public String getName() {
        return "获取项目模板列表";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Output({@Param(explode = ProjectTemplateVo.class)})
    @Description(desc = "获取项目模板列表接口")
    @Override
    public Object myDoService(JSONObject paramObj) {
        ProjectTemplateVo projectTemplateVo = JSONObject.toJavaObject(paramObj, ProjectTemplateVo.class);
        return projectTemplateMapper.searchProjectTemplate(projectTemplateVo);
    }

    @Override
    public String getToken() {
        return "/rdm/projecttemplate/search";
    }
}
