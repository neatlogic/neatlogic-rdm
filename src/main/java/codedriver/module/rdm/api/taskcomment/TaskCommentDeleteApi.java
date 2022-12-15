/*
 * Copyright(c) 2022 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.module.rdm.api.taskcomment;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import codedriver.framework.common.constvalue.ApiParamType;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.privateapi.PrivateApiComponentBase;
import codedriver.module.rdm.dao.mapper.TaskMapper;

/**
 * @ClassName TaskCommentDeleteApi
 * @Description 删除任务评论接口
 * @Auther
 * @Date 2019/12/3 15:35
 **/
@Service
public class TaskCommentDeleteApi extends PrivateApiComponentBase {

    @Resource
    private TaskMapper taskMapper;

    @Override
    public String getToken() {
        return "module/rdm/task/comment/delete";
    }

    @Override
    public String getName() {
        return "删除任务评论接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({
            @Param(name = "id", type = ApiParamType.LONG, desc = "comment id", isRequired = true),
    })
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        taskMapper.deleteComment(jsonObj.getLong("id"));
        return null;
    }

}


