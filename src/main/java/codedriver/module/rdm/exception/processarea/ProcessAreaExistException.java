/*
 * Copyright(c) 2022 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.module.rdm.exception.processarea;

import codedriver.framework.exception.core.ApiRuntimeException;

/**
 * @ClassName ProcessAreaExistException
 * @Description
 * @Auther
 * @Date 2019/12/12 10:04
 **/
public class ProcessAreaExistException extends ApiRuntimeException {
    private static final long serialVersionUID = -7446685746824860958L;

    public ProcessAreaExistException() {
        super();
    }

    public ProcessAreaExistException(String msg) {
        super("过程域名称已存在: " + msg);
    }
}
