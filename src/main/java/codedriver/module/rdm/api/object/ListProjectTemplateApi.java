/*
 * Copyright(c) 2022 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.module.rdm.api.object;

import codedriver.framework.auth.core.AuthAction;
import codedriver.framework.rdm.auth.label.RDM_BASE;
import codedriver.framework.rdm.dto.ProjectTemplateVo;
import codedriver.framework.restful.annotation.Description;
import codedriver.framework.restful.annotation.OperationType;
import codedriver.framework.restful.annotation.Output;
import codedriver.framework.restful.constvalue.OperationTypeEnum;
import codedriver.framework.restful.core.privateapi.PrivateApiComponentBase;
import codedriver.module.rdm.dao.mapper.ProjectTemplateMapper;
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

    @Output({
    })
    @Description(desc = "获取项目模板列表接口")
    @Override
    public Object myDoService(JSONObject paramObj) throws Exception {
        ProjectTemplateVo projectTemplateVo = JSONObject.toJavaObject(paramObj, ProjectTemplateVo.class);
        return projectTemplateMapper.searchProjectTemplate(projectTemplateVo);
    }

    @Override
    public String getToken() {
        return "/rdm/projecttemplate/search";
    }
}
