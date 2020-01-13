package codedriver.module.rdm.exception.role;

import codedriver.framework.exception.core.ApiRuntimeException;

/**
 * @ClassName ProjectStatusExistException
 * @Description
 * @Auther
 * @Date 2019/12/17 17:39
 **/
public class ActionCheckFailedException extends ApiRuntimeException {

    public ActionCheckFailedException() {
        super();
    }

    public ActionCheckFailedException(String msg) {
        super("验证失败，当前用户无权限: " + msg);
    }
}
