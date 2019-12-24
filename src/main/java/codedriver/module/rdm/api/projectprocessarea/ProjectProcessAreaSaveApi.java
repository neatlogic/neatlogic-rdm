package codedriver.module.rdm.api.projectprocessarea;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.restful.annotation.Description;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.ApiComponentBase;
import codedriver.module.rdm.dto.ProjectProcessAreaFieldVo;
import codedriver.module.rdm.dto.ProjectProcessAreaVo;
import codedriver.module.rdm.dto.TemplateProcessAreaFieldVo;
import codedriver.module.rdm.services.ProjectService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName ProjectFieldSaveApi
 * @Description
 * @Auther
 * @Date 2019/12/4 9:52
 **/
@Service
public class ProjectProcessAreaSaveApi extends ApiComponentBase {

    @Autowired
    private ProjectService projectService;

    @Override
    public String getToken() {
        return "module/rdm/projectprocessarea/save";
    }

    @Override
    public String getName() {
        return "保存项目过程域接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "id", type = ApiParamType.LONG, desc = "数据主键ID"),
            @Param(name = "processAreaName", type = ApiParamType.STRING, desc = "过程域名称", isRequired = true),
            @Param(name = "processAreaUuid", type = ApiParamType.STRING, desc = "过程域uuid", isRequired = true),
            @Param(name = "projectUuid", type = ApiParamType.STRING, desc = "项目uuid", isRequired = true),
            @Param(name = "isEnable", type = ApiParamType.INTEGER, desc = "是否可见，1表示可见，0表示不可见", isRequired = true),
            @Param(name = "processAreaFieldList", explode = ProjectProcessAreaFieldVo[].class)})
    @Description( desc = "保存项目过程域接口")
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        ProjectProcessAreaVo areaVo = new ProjectProcessAreaVo();
        areaVo.setProjectUuid(jsonObj.getString("projectUuid"));
        areaVo.setProcessAreaUuid(jsonObj.getString("processAreaUuid"));
        areaVo.setProcessAreaName(jsonObj.getString("processAreaName"));
        areaVo.setIsEnable(jsonObj.getInteger("isEnable"));
        if (jsonObj.containsKey("id")){
            areaVo.setId(jsonObj.getLong("id"));
        }
        JSONArray areaFieldList = jsonObj.getJSONArray("processAreaFieldList");
        List<ProjectProcessAreaFieldVo> fieldVoList = new ArrayList<>();
        for (int i = 0; i < areaFieldList.size(); i++){
            JSONObject fieldObj = areaFieldList.getJSONObject(i);
            ProjectProcessAreaFieldVo fieldVo = new ProjectProcessAreaFieldVo();
            if (fieldObj.containsKey("id")){
                fieldVo.setId(fieldObj.getLong("id"));
            }
            fieldVo.setField(fieldObj.getString("field"));
            fieldVo.setFieldName(fieldObj.getString("fieldName"));
            fieldVo.setFieldType(fieldObj.getString("fieldType"));
            fieldVo.setConfig(fieldObj.getString("config"));
            fieldVo.setIsSystem(fieldObj.getInteger("isSystem"));
            fieldVo.setIsRequired(fieldObj.getInteger("isRequired"));
            fieldVo.setIsShow(fieldObj.getInteger("isShow"));
            fieldVo.setProjectUuid(jsonObj.getString("projectUuid"));
            fieldVo.setProcessAreaUuid(jsonObj.getString("processAreaUuid"));
            fieldVoList.add(fieldVo);
        }
        areaVo.setFieldList(fieldVoList);
        projectService.saveProjectProcessArea(areaVo);
        return new JSONObject();
    }
}
