package codedriver.module.rdm.exception.projectpriority;

import codedriver.framework.exception.core.ApiRuntimeException;

/**
 * @ClassName ProcessAreaExistException
 * @Description
 * @Auther
 * @Date 2019/12/12 10:04
 **/
public class ProjectPriorityExistException extends ApiRuntimeException {

    public ProjectPriorityExistException() {
        super();
    }

    public ProjectPriorityExistException(String msg) {
        super("优先级名称已存在: " + msg);
    }
}
