/*
 * Copyright(c) 2022 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.module.rdm.api.processarea;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import codedriver.framework.asynchronization.threadlocal.UserContext;
import codedriver.framework.common.constvalue.ApiParamType;
import codedriver.framework.restful.annotation.Description;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Output;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.privateapi.PrivateApiComponentBase;
import codedriver.module.rdm.dto.FieldVo;
import codedriver.module.rdm.dto.ProcessAreaVo;
import codedriver.module.rdm.services.ProcessAreaService;

/**
 * @ClassName ProcessAreaSaveApi
 * @Description 保存过程域接口
 * @Auther
 * @Date 2019/12/4 9:52
 **/
@Service
public class ProcessAreaSaveApi extends PrivateApiComponentBase {

    @Resource
    private ProcessAreaService processAreaService;

    @Override
    public String getToken() {
        return "module/rdm/processarea/save";
    }

    @Override
    public String getName() {
        return "保存过程域接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "name", type = ApiParamType.STRING, desc = "过程域名称", isRequired = true),
            @Param(name = "id", type = ApiParamType.STRING, desc = "过程域id", isRequired = false),
            @Param(name = "uuid", type = ApiParamType.STRING, desc = "过程域uuid", isRequired = false),
            @Param(name = "description", type = ApiParamType.STRING, desc = "过程域描述", isRequired = false),
            @Param(name = "fieldList", type = ApiParamType.JSONARRAY, desc = "过程域字段列表", isRequired = false),
    })
    @Output({@Param(name = "uuid", type = ApiParamType.STRING, desc = "过程域uuid")})
    @Description(desc = "保存过程域接口")
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        JSONObject result = new JSONObject();
        ProcessAreaVo processAreaVo = new ProcessAreaVo();
        String name = jsonObj.getString("name");
        String description = jsonObj.getString("description");

        processAreaVo.setName(name);
        processAreaVo.setDescription(description);
        if (jsonObj.containsKey("uuid")) {
            String uuid = jsonObj.getString("uuid");
            processAreaVo.setUuid(uuid);
            processAreaVo.setUpdateUser(UserContext.get().getUserId());
        } else {
            processAreaVo.setCreateUser(UserContext.get().getUserId());
        }


        JSONArray fieldArray = jsonObj.getJSONArray("fieldList");
        if (fieldArray != null && fieldArray.size() > 0) {
            List<FieldVo> fieldList = new ArrayList<>();
            for (Object fieldObject : fieldArray) {
                JSONObject fieldJson = JSONObject.parseObject(fieldObject.toString());
                String field = fieldJson.getString("field");
                String fieldName = fieldJson.getString("name");
                String fieldUuid = fieldJson.getString("uuid");
                String fieldType = fieldJson.getString("type");
                String config = fieldJson.getString("config");
                Integer sort = fieldJson.getInteger("sort");
                fieldList.add(new FieldVo(field, fieldName, fieldUuid, fieldType, config, sort));
            }

            processAreaVo.setFieldList(fieldList);
        }

        String uuid = processAreaService.saveProcessArea(processAreaVo);
        result.put("uuid", uuid);
        return result;
    }

}
