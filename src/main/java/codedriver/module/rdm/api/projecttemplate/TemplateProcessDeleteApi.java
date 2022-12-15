/*
 * Copyright(c) 2022 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.module.rdm.api.projecttemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import codedriver.framework.common.constvalue.ApiParamType;
import codedriver.framework.restful.annotation.Description;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.privateapi.PrivateApiComponentBase;
import codedriver.module.rdm.dto.TemplateProcessAreaVo;
import codedriver.module.rdm.services.ProjectTemplateService;

/**
 * @program: codedriver
 * @description:
 * @create: 2019-12-17 18:13
 **/
@Service
public class TemplateProcessDeleteApi extends PrivateApiComponentBase {

    @Autowired
    private ProjectTemplateService templateService;

    @Override
    public String getToken() {
        return "module/rdm/projecttemplate/processarea/delete";
    }

    @Override
    public String getName() {
        return "项目模板过程域删除接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "processAreaUuid", type = ApiParamType.STRING, desc = "过程域uuid", isRequired = true),
            @Param(name = "templateUuid", type = ApiParamType.STRING, desc = "模板uuid", isRequired = true),
            @Param(name = "id", type = ApiParamType.LONG, desc = "主键ID", isRequired = true)})
    @Description(desc = "项目模板过程域删除接口")
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        String processAreaUuid = jsonObj.getString("processAreaUuid");
        String templateUuid = jsonObj.getString("templateUuid");
        Long id = jsonObj.getLong("id");
        TemplateProcessAreaVo processAreaVo = new TemplateProcessAreaVo();
        processAreaVo.setProcessAreaUuid(processAreaUuid);
        processAreaVo.setTemplateUuid(templateUuid);
        processAreaVo.setId(id);
        templateService.deleteTemplateProcessArea(processAreaVo);
        return new JSONObject();
    }
}
