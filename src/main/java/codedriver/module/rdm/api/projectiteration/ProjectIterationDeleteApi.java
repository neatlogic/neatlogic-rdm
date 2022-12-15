/*
 * Copyright(c) 2022 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.module.rdm.api.projectiteration;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import codedriver.framework.common.constvalue.ApiParamType;
import codedriver.framework.restful.annotation.Description;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.privateapi.PrivateApiComponentBase;
import codedriver.module.rdm.dao.mapper.ProjectIterationMapper;

/**
 * @ClassName ProjectIterationDeleteApi
 * @Description 删除项目迭代接口
 * @Auther
 * @Date 2019/12/4 9:52
 **/
@Service
public class ProjectIterationDeleteApi extends PrivateApiComponentBase {

    @Resource
    private ProjectIterationMapper projectIterationMapper;

    @Override
    public String getToken() {
        return "module/rdm/projectiteration/delete";
    }

    @Override
    public String getName() {
        return "删除项目迭代接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({
            @Param(name = "uuid", type = ApiParamType.STRING, desc = "项目迭代uuid", isRequired = true)})
    @Description(desc = "删除项目迭代接口")
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        String projectUuid = jsonObj.getString("projectUuid");
        String uuid = jsonObj.getString("uuid");
        projectIterationMapper.deleteProjectIteration(projectUuid, uuid);
        return null;
    }


}
