package codedriver.module.rdm.exception.projectiteration;

import codedriver.framework.exception.core.ApiRuntimeException;

/**
 * @ClassName ProjectIterationException
 * @Description
 * @Auther
 * @Date 2019/12/23 15:04
 **/
public class ProjectIterationException extends ApiRuntimeException {

    public ProjectIterationException() {
        super();
    }

    public ProjectIterationException(String msg) {
        super("项目迭代名称已存在: " + msg);
    }
}
