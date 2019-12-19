package codedriver.module.rdm.api.template;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.restful.annotation.Description;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.ApiComponentBase;
import codedriver.module.rdm.dto.TemplateProcessAreaFieldVo;
import codedriver.module.rdm.dto.TemplateProcessAreaVo;
import codedriver.module.rdm.services.ProjectTemplateService;
import codedriver.module.rdm.util.UuidUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: codedriver
 * @description:
 * @create: 2019-12-16 17:56
 **/
@Service
public class TemplateProcessSaveApi extends ApiComponentBase {

    @Autowired
    private ProjectTemplateService projectTemplateService;

    @Override
    public String getToken() {
        return "module/rdm/template/proAreaSave";
    }

    @Override
    public String getName() {
        return "项目模板过程域配置保存接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "id", type = ApiParamType.LONG, desc = "数据主键ID"),
            @Param(name = "processAreaName", type = ApiParamType.STRING, desc = "过程域名称", isRequired = true),
            @Param(name = "processAreaUuid", type = ApiParamType.STRING, desc = "过程域uuid", isRequired = true),
            @Param(name = "templateUuid", type = ApiParamType.STRING, desc = "模板uuid", isRequired = true),
            @Param(name = "processAreaFieldList", explode = TemplateProcessAreaFieldVo[].class)})
    @Description(desc = "项目模板过程域配置保存接口")
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        TemplateProcessAreaVo processAreaVo = new TemplateProcessAreaVo();
        processAreaVo.setTemplateUuid(jsonObj.getString("templateUuid"));
        processAreaVo.setProcessAreaUuid(jsonObj.getString("processAreaUuid"));
        processAreaVo.setProcessAreaName(jsonObj.getString("processAreaName"));
        if (jsonObj.containsKey("id")) {
            processAreaVo.setId(jsonObj.getLong("id"));
        }
        JSONArray processAreaFieldList = jsonObj.getJSONArray("processAreaFieldList");
        List<TemplateProcessAreaFieldVo> fieldVoList = new ArrayList<>();
        for (int i = 0; i < processAreaFieldList.size(); i++) {
            JSONObject processAreaField = processAreaFieldList.getJSONObject(i);
            TemplateProcessAreaFieldVo fieldVo = new TemplateProcessAreaFieldVo();
            fieldVo.setField(processAreaField.getString("field"));
            fieldVo.setFieldName(processAreaField.getString("fieldName"));
            fieldVo.setFieldType(processAreaField.getString("fieldType"));
            fieldVo.setConfig(processAreaField.getString("config"));
            fieldVo.setIsSystem(processAreaField.getInteger("isSystem"));
            fieldVo.setProcessAreaUuid(jsonObj.getString("processAreaUuid"));
            fieldVo.setTemplateUuid(jsonObj.getString("templateUuid"));
            fieldVoList.add(fieldVo);
        }
        processAreaVo.setProcessAreaFieldVoList(fieldVoList);
        projectTemplateService.saveTemplateProcessArea(processAreaVo);
        return new JSONObject();
    }
}
