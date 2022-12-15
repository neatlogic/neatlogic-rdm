/*
 * Copyright(c) 2022 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.module.rdm.api.processarea;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import codedriver.framework.common.constvalue.ApiParamType;
import codedriver.framework.restful.annotation.Description;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.privateapi.PrivateApiComponentBase;
import codedriver.module.rdm.dao.mapper.ProcessAreaMapper;

/**
 * @ClassName ProcessAreaSearchApi
 * @Description
 * @Auther
 * @Date 2019/12/4 9:52
 **/
@Service
public class ProcessAreaGetApi extends PrivateApiComponentBase {

    @Resource
    private ProcessAreaMapper processAreaMapper;

    @Override
    public String getToken() {
        return "module/rdm/processarea/get";
    }

    @Override
    public String getName() {
        return "根据uuid查询过程域接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "uuid", type = ApiParamType.STRING, desc = "过程域uuid", isRequired = true)})
    @Description(desc = "根据uuid查询过程域接口")
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        JSONObject result = new JSONObject();
        String uuid = jsonObj.getString("uuid");
        result.put("processArea", processAreaMapper.getProcessAreaByUuid(uuid));
        result.put("fieldList", processAreaMapper.getProcessAreaFieldListByUuid(uuid));
        return result;
    }


}
