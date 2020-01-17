package codedriver.module.rdm.services;

import codedriver.module.rdm.dto.ProcessAreaVo;

import java.util.List;

/**
 * @ClassName ProcessAreaService
 * @Description
 * @Auther
 * @Date 2019/12/9 11:40
 **/
public interface ProcessAreaService {
    List<ProcessAreaVo> searchProcessArea(ProcessAreaVo processAreaVo);

    String saveProcessArea(ProcessAreaVo processAreaVo);
}
