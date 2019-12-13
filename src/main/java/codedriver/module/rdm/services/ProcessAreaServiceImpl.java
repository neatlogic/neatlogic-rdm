package codedriver.module.rdm.services;

import codedriver.framework.common.util.PageUtil;
import codedriver.framework.exception.core.ApiRuntimeException;
import codedriver.module.rdm.dao.mapper.ProcessAreaMapper;
import codedriver.module.rdm.dto.FieldVo;
import codedriver.module.rdm.dto.ProcessAreaFieldVo;
import codedriver.module.rdm.dto.ProcessAreaVo;
import codedriver.module.rdm.exception.processarea.ProcessAreaExistException;
import codedriver.module.rdm.util.UuidUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

/**
 * @ClassName ProcessAreaServiceImpl
 * @Description
 * @Auther
 * @Date 2019/12/9 11:42
 **/

@Service
@Transactional
public class ProcessAreaServiceImpl implements ProcessAreaService {

    @Resource
    private ProcessAreaMapper processAreaMapper;

    @Override
    public List<ProcessAreaVo> searchProcessArea(ProcessAreaVo processAreaVo) {
        if (processAreaVo.getNeedPage()) {
            int rowNum = processAreaMapper.searchProcessAreaCount(processAreaVo);
            processAreaVo.setRowNum(rowNum);
            processAreaVo.setPageCount(PageUtil.getPageCount(rowNum, processAreaVo.getPageSize()));
        }
        return processAreaMapper.searchProcessArea(processAreaVo);
    }

    @Override
    public String saveProcessArea(ProcessAreaVo processAreaVo) {
        String uuid;

        int count = processAreaMapper.checkProcessAreaExist(processAreaVo);
        if(count >= 1){
            throw new ProcessAreaExistException(processAreaVo.getName());
        }

        if(StringUtils.isNotBlank(processAreaVo.getUuid())){
            uuid = processAreaVo.getUuid();
            processAreaMapper.updateProcessArea(processAreaVo);
            processAreaMapper.deleteAllProcessAreaField(processAreaVo.getUuid());
        }else{
            uuid = UuidUtil.getUuid();
            processAreaVo.setUuid(uuid);
            processAreaMapper.insertProcessArea(processAreaVo);
        }
        List <FieldVo> filedList = processAreaVo.getFieldList();
        if(filedList != null && filedList.size() > 0 ){
            for(FieldVo fieldVo : filedList){
                ProcessAreaFieldVo processAreaFieldVo = new ProcessAreaFieldVo(fieldVo);
                processAreaFieldVo.setProcessAreaUuid(uuid);
                processAreaMapper.insertProcessAreaField(processAreaFieldVo);
            }
        }

        return uuid;
    }
}
