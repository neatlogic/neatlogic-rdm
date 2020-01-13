package codedriver.module.rdm.exception.role;

import codedriver.framework.exception.core.ApiRuntimeException;

/**
 * @ClassName ProjectStatusExistException
 * @Description
 * @Auther
 * @Date 2019/12/17 17:39
 **/
public class ActionCheckParamErrorException extends ApiRuntimeException {

    public ActionCheckParamErrorException() {
        super();
    }

    public ActionCheckParamErrorException(String msg) {
        super("方法入参格式不正确: " + msg);
    }
}
