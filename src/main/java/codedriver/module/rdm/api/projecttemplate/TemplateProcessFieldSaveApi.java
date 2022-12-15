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
import codedriver.module.rdm.dto.TemplateProcessAreaFieldVo;
import codedriver.module.rdm.services.ProjectTemplateService;

/**
 * @program: codedriver
 * @description:
 * @create: 2019-12-18 15:25
 **/
@Service
public class TemplateProcessFieldSaveApi extends PrivateApiComponentBase {

    @Autowired
    private ProjectTemplateService templateService;

    @Override
    public String getToken() {
        return "module/rdm/projecttemplate/processfield/save";
    }

    @Override
    public String getName() {
        return "项目模板过程域属性值保存接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "fieldUuid", type = ApiParamType.STRING, desc = "属性uuid", isRequired = true),
            @Param(name = "config", type = ApiParamType.JSONOBJECT, desc = "属性配置", isRequired = true)})
    @Description(desc = "项目模板过程域属性值保存接口")
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        String fieldUuid = jsonObj.getString("fieldUuid");
        JSONObject configObj = jsonObj.getJSONObject("config");
        TemplateProcessAreaFieldVo fieldVo = new TemplateProcessAreaFieldVo();
        fieldVo.setFieldUuid(fieldUuid);
        fieldVo.setConfig(configObj.toJSONString());
        templateService.saveTemplateProcessAreaFieldConfig(fieldVo);
        return new JSONObject();
    }
}
