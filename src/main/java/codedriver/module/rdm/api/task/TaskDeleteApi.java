/*
 * Copyright(c) 2022 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.module.rdm.api.task;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import codedriver.framework.restful.core.privateapi.PrivateApiComponentBase;

/**
 * @ClassName TaskDeleteApi
 * @Description
 * @Auther
 * @Date 2019/12/3 15:35
 **/
@Service
public class TaskDeleteApi extends PrivateApiComponentBase {

    @Override
    public String getToken() {
        return "module/rdm/task/delete";
    }

    @Override
    public String getName() {
        return "删除任务接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        return null;
    }

}


