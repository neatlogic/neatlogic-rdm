package codedriver.module.rdm.exception.processarea;

import codedriver.framework.exception.core.ApiRuntimeException;

/**
 * @ClassName ProcessAreaExistException
 * @Description
 * @Auther
 * @Date 2019/12/12 10:04
 **/
public class ProcessAreaExistException extends ApiRuntimeException {

    public ProcessAreaExistException() {
        super();
    }

    public ProcessAreaExistException(String msg) {
        super("过程域名称已存在: " + msg);
    }
}
