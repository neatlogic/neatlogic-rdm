package codedriver.module.rdm.exception.role;

import codedriver.framework.exception.core.ApiRuntimeException;

/**
 * @ClassName ProjectStatusExistException
 * @Description
 * @Auther
 * @Date 2019/12/17 17:39
 **/
public class ActionCheckValueConflictException extends ApiRuntimeException {

    public ActionCheckValueConflictException() {
        super();
    }

    public ActionCheckValueConflictException(String msg) {
        super("操作验证注解键值冲突: " + msg);
    }
}
