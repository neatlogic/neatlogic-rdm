package codedriver.module.rdm.dao.mapper;

import codedriver.module.rdm.dto.FieldVo;
import codedriver.module.rdm.dto.ProcessAreaFieldVo;
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

    void deleteProcessArea(String processAreaUuid);

    ProcessAreaVo getProcessAreaByUuid(String uuid);

    List<FieldVo> getProcessAreaFieldListByUuid(String uuid);

    void deleteAllProcessAreaField(String uuid);

    void updateProcessArea(ProcessAreaVo processAreaVo);

    void insertProcessArea(ProcessAreaVo processAreaVo);

    void insertProcessAreaField(ProcessAreaFieldVo processAreaFieldVo);

    int checkProcessAreaExist(ProcessAreaVo processAreaVo);
}
