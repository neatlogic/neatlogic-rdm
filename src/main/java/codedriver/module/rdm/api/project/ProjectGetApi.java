/*
 * Copyright(c) 2022 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.module.rdm.api.project;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;

import codedriver.framework.common.constvalue.ApiParamType;
import codedriver.framework.restful.annotation.Description;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Output;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.privateapi.PrivateApiComponentBase;
import codedriver.module.rdm.dao.mapper.ProjectMapper;
import codedriver.module.rdm.dto.ProjectVo;

/**
 * @ClassName ProjectGetApi
 * @Description
 * @Auther
 * @Date 2019/12/10 17:31
 **/
public class ProjectGetApi extends PrivateApiComponentBase {

    @Resource
    private ProjectMapper projectMapper;

    @Override
    public String getToken() {
        return "module/rdm/project/get";
    }

    @Override
    public String getName() {
        return "根据uuid查询项目接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({
            @Param(name = "uuid", type = ApiParamType.STRING, desc = "项目uuid", isRequired = true),
            @Param(name = "processAreaUuid", type = ApiParamType.STRING, desc = "过程域uuid", isRequired = false)
    })
    @Output({
            @Param(name = "project", type = ApiParamType.JSONOBJECT, desc = "项目信息", explode = ProjectVo.class)
    })
    @Description(desc = "根据uuid查询项目接口")
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        JSONObject result = new JSONObject();
        String projectUuid = jsonObj.getString("uuid");
        result.put("project", projectMapper.getProjectByUuid(projectUuid));
        return result;
    }
}
