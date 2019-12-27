package codedriver.module.rdm.services;

import codedriver.framework.asynchronization.threadlocal.UserContext;
import codedriver.module.rdm.dao.mapper.ProjectIterationMapper;
import codedriver.module.rdm.dao.mapper.ProjectWorkflowMapper;
import codedriver.module.rdm.dao.mapper.TaskMapper;
import codedriver.module.rdm.dto.*;
import codedriver.module.rdm.exception.projectiteration.ProjectIterationNotExistException;
import codedriver.module.rdm.exception.projectstatus.ProjectStatusNotExistException;
import codedriver.module.rdm.util.UuidUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName TaskCommentServiceImpl
 * @Description
 * @Auther
 * @Date 2019/12/24 10:42
 **/
@Service
@Transactional
public class TaskCommentServiceImpl implements TaskCommentService {

    @Resource
    private TaskMapper taskMapper;

    @Override
    public void saveTaskComment(TaskCommentVo taskCommentVo) {
        if(null != taskCommentVo.getId()){
            taskCommentVo.setCreateUser(UserContext.get().getUserId());
            taskMapper.insertTaskComment(taskCommentVo);
        }else{
            taskCommentVo.setUpdateUser(UserContext.get().getUserId());
            taskMapper.updateTaskComment(taskCommentVo);
        }

        //TODO 评论 @ 通知   内容包含以下格式的字符串 @李嘉诚(lijc)
        List<String> userList = new ArrayList<>();
        String comment = taskCommentVo.getComment();
        Pattern pattern = Pattern.compile("([^@ ]*)\\((.*?)\\)");
        Matcher matcher = pattern.matcher(comment);
        while(matcher.find()) {
            userList.add(matcher.group(1));
        }


    }

    public static void main(String[] args) {
        String comment = "@张三(zhangsan)夫卡了就释放了是否能解决@@李四(lis)";
        Pattern pattern = Pattern.compile("([^@]+)\\((.*?)\\)");
        Matcher matcher = pattern.matcher(comment);
        while(matcher.find()) {
            System.out.println(matcher.group(2));
        }
    }
}