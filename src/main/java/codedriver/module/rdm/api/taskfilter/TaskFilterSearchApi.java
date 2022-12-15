/*
 * Copyright(c) 2022 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.module.rdm.api.taskfilter;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import codedriver.framework.restful.core.privateapi.PrivateApiComponentBase;

/**
 * @ClassName FilterSearchApi
 * @Description
 * @Auther
 * @Date 2019/12/4 9:52
 **/
@Service
public class TaskFilterSearchApi extends PrivateApiComponentBase {

    @Override
    public String getToken() {
        return "module/rdm/taskfilter/search";
    }

    @Override
    public String getName() {
        return "查询过滤器接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        JSONObject data = new JSONObject();
        return data;
    }

}
