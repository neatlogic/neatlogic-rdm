package codedriver.module.rdm.api.taskcomment;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.ApiComponentBase;
import codedriver.module.rdm.dao.mapper.TaskMapper;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName TaskCommentDeleteApi
 * @Description 删除任务评论接口
 * @Auther
 * @Date 2019/12/3 15:35
 **/
@Service
public class TaskCommentDeleteApi extends ApiComponentBase {

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


