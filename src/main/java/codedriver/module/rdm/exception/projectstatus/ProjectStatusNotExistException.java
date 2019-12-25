package codedriver.module.rdm.exception.projectstatus;

import codedriver.framework.exception.core.ApiRuntimeException;

/**
 * @ClassName ProjectStatusExistException
 * @Description
 * @Auther
 * @Date 2019/12/24 17:39
 **/
public class ProjectStatusNotExistException extends ApiRuntimeException {

    public ProjectStatusNotExistException() {
        super();
    }

    public ProjectStatusNotExistException(String msg) {
        super("状态不存在或已删除: " + msg);
    }
}
