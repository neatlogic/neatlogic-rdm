/*
 * Copyright(c) 2022 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.module.rdm.api.projectpriority;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import codedriver.framework.common.constvalue.ApiParamType;
import codedriver.framework.restful.annotation.Description;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.privateapi.PrivateApiComponentBase;
import codedriver.module.rdm.dao.mapper.ProjectPriorityMapper;

/**
 * @ClassName ProjectPriorityDeleteApi
 * @Description 删除项目优先级接口
 * @Auther
 * @Date 2019/12/4 9:52
 **/
@Service
public class ProjectPriorityDeleteApi extends PrivateApiComponentBase {

    @Resource
    private ProjectPriorityMapper projectPriorityMapper;

    @Override
    public String getToken() {
        return "module/rdm/projectpriority/delete";
    }

    @Override
    public String getName() {
        return "删除项目优先级接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({
            @Param(name = "projectUuid", type = ApiParamType.STRING, desc = "项目uuid", isRequired = true),
            @Param(name = "processAreaUuid", type = ApiParamType.STRING, desc = "过程域uuid", isRequired = true),
            @Param(name = "uuid", type = ApiParamType.STRING, desc = "优先级uuid", isRequired = true)})
    @Description(desc = "删除项目优先级接口")
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        String projectUuid = jsonObj.getString("projectUuid");
        String processAreaUuid = jsonObj.getString("processAreaUuid");
        String uuid = jsonObj.getString("uuid");
        projectPriorityMapper.deleteProjectPriority(projectUuid, processAreaUuid, uuid);
        return null;
    }


}
