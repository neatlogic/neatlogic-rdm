package codedriver.module.rdm.services;

import codedriver.framework.common.util.PageUtil;
import codedriver.module.rdm.dao.mapper.ProcessAreaMapper;
import codedriver.module.rdm.dto.ProcessAreaVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

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
}
