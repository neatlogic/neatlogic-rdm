package codedriver.module.rdm.dao.mapper;

import codedriver.module.rdm.dto.FieldVo;
import codedriver.module.rdm.dto.ProcessAreaVo;

import java.util.List;

/**
 * @ClassName ProcessAreaMapper
 * @Description
 * @Auther
 * @Date 2019/12/4 14:34
 **/
public interface ProcessAreaMapper {
    int searchProcessAreaCount(ProcessAreaVo processAreaVo);

    List<ProcessAreaVo> searchProcessArea(ProcessAreaVo processAreaVo);

    void deleteProcessArea(Long processAreaId);

    ProcessAreaVo getProcessAreaByUuid(String uuid);

    List<FieldVo> getProcessAreaFieldListByUuid(String uuid);
}
