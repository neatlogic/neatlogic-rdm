package codedriver.module.rdm.exception.projectiteration;

import codedriver.framework.exception.core.ApiRuntimeException;

/**
 * @ClassName ProjectIterationExistException
 * @Description
 * @Auther
 * @Date 2019/12/23 15:04
 **/
public class ProjectIterationExistException extends ApiRuntimeException {

    public ProjectIterationExistException() {
        super();
    }

    public ProjectIterationExistException(String msg) {
        super("项目迭代名称已存在: " + msg);
    }
}
