package codedriver.module.rdm.exception.projectiteration;

import codedriver.framework.exception.core.ApiRuntimeException;

/**
 * @ClassName ProjectIterationExistException
 * @Description
 * @Auther
 * @Date 2019/12/23 15:04
 **/
public class ProjectIterationNotExistException extends ApiRuntimeException {

    public ProjectIterationNotExistException() {
        super();
    }

    public ProjectIterationNotExistException(String msg) {
        super("项目迭代不存在或已删除: " + msg);
    }
}
