package codedriver.module.rdm.api.taskcomment;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.ApiComponentBase;
import codedriver.module.rdm.dto.TaskCommentVo;
import codedriver.module.rdm.services.TaskCommentService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName TaskAddCommentApi
 * @Description
 * @Auther
 * @Date 2019/12/3 15:35
 **/
@Service
public class TaskCommentSaveApi extends ApiComponentBase {

    @Resource
    private TaskCommentService taskCommentService;

    @Override
    public String getToken() {
        return "module/rdm/task/comment/save";
    }

    @Override
    public String getName() {
        return "保存任务评论接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({
            @Param(name = "taskUuid", type = ApiParamType.STRING, desc = "task Uuid", isRequired = true),
            @Param(name = "comment", type = ApiParamType.STRING, desc = "评论", isRequired = true),
            @Param(name = "id", type = ApiParamType.LONG, desc = "comment id", isRequired = false),
    })
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        TaskCommentVo taskCommentVo = new TaskCommentVo();

        Long id = jsonObj.getLong("id");
        String taskUuid = jsonObj.getString("taskUuid");
        String comment = jsonObj.getString("comment");

        taskCommentVo.setId(id);
        taskCommentVo.setTaskUuid(taskUuid);
        taskCommentVo.setComment(comment);
        taskCommentService.saveTaskComment(taskCommentVo);

        return null;
    }

}


