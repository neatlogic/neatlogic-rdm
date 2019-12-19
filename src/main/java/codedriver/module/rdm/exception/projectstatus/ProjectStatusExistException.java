package codedriver.module.rdm.exception.projectstatus;

import codedriver.framework.exception.core.ApiRuntimeException;

/**
 * @ClassName ProjectStatusExistException
 * @Description
 * @Auther
 * @Date 2019/12/17 17:39
 **/
public class ProjectStatusExistException extends ApiRuntimeException {

    public ProjectStatusExistException() {
        super();
    }

    public ProjectStatusExistException(String msg) {
        super("状态名称已存在: " + msg);
    }
}
