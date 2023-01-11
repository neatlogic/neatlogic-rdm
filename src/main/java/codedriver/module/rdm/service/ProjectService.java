/*
 * Copyright(c) 2023 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.module.rdm.service;

import codedriver.framework.rdm.dto.ObjectVo;
import codedriver.framework.transaction.core.EscapeTransactionJob;

public interface ProjectService {
    EscapeTransactionJob.State dropObjectSchema(ObjectVo objectVo);

    EscapeTransactionJob.State buildObjectSchema(ObjectVo objectVo);

}
