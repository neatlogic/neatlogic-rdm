package codedriver.module.rdm.services;

import codedriver.framework.asynchronization.threadlocal.UserContext;
import codedriver.module.rdm.dao.mapper.TemplateMapper;
import codedriver.module.rdm.dto.TemplateProcessAreaFieldVo;
import codedriver.module.rdm.dto.TemplateProcessAreaVo;
import codedriver.module.rdm.dto.TemplateVo;
import codedriver.module.rdm.util.UuidUtil;
import com.alibaba.fastjson.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: codedriver
 * @description:
 * @create: 2019-12-16 17:08
 **/
@Service
@Transactional
public class ProjectTemplateServiceImpl implements ProjectTemplateService {

    @Autowired
    private TemplateMapper templateMapper;

    @Override
    public String saveTemplate(TemplateVo templateVo) {
        templateVo.setUpdateUser(UserContext.get().getUserId());
        if (StringUtils.isBlank(templateVo.getUuid())){
            templateVo.setUuid(UuidUtil.getUuid());
            templateVo.setCreateUser(UserContext.get().getUserId());
            templateMapper.insertTemplate(templateVo);
        }else {
            templateMapper.updateTemplate(templateVo);
        }
        return templateVo.getUuid();
    }

    @Override
    public void saveTemplateProcessArea(TemplateProcessAreaVo processAreaVo) {
        //删除自定义属性
        templateMapper.deleteTemplateProCustomFieldByTemplateUuidAndAreaUuid(processAreaVo.getTemplateUuid(), processAreaVo.getProcessAreaUuid());
        List<TemplateProcessAreaFieldVo> fieldVoList = processAreaVo.getProcessAreaFieldVoList();
        List<TemplateProcessAreaFieldVo> systemFieldVoList = new ArrayList<>();
        JSONArray sortArray = new JSONArray();
        //只加自定义属性
        for (TemplateProcessAreaFieldVo fieldVo : fieldVoList){
            fieldVo.setFieldUuid(UuidUtil.getUuid());
            if (fieldVo.getIsSystem() == 1){
                systemFieldVoList.add(fieldVo);
            }else {
                templateMapper.insertTemplateProcessAreaField(fieldVo);
            }
            sortArray.add(fieldVo.getId());
        }
        //更新模板过程域
        processAreaVo.setProcessAreaFieldSort(sortArray.toJSONString());
        if (processAreaVo.getId() != null && processAreaVo.getId() != 0L){
            templateMapper.updateTemplateProcessAreaFieldSort(processAreaVo);
        }else {
            //新增过程域
            templateMapper.insertTemplateProcessArea(processAreaVo);
            //新增系统属性
            for (TemplateProcessAreaFieldVo sysTemFieldVo : systemFieldVoList){
                templateMapper.insertTemplateProcessAreaField(sysTemFieldVo);
            }
        }
    }

    @Override
    public void deleteTemplateProcessArea(TemplateProcessAreaVo processAreaVo) {
        templateMapper.deleteTemplateProcessAreaById(processAreaVo.getId());
        templateMapper.deleteTemplateProcessFieldByTemplateUuidAndAreaUuid(processAreaVo.getTemplateUuid(), processAreaVo.getProcessAreaUuid());
    }

    @Override
    public void deleteTemplate(String templateUuid) {
        templateMapper.deleteTemplateByUuid(templateUuid);
        templateMapper.deleteTemplateProcessAreaByTemplateUuid(templateUuid);
        templateMapper.deleteTemplateProcessFieldByTemplateUuid(templateUuid);
    }
}
