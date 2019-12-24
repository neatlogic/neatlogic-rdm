package codedriver.module.rdm.services;

import codedriver.framework.asynchronization.threadlocal.UserContext;
import codedriver.module.rdm.dao.mapper.TemplateMapper;
import codedriver.module.rdm.dto.TemplateProcessAreaFieldVo;
import codedriver.module.rdm.dto.TemplateProcessAreaTemplateVo;
import codedriver.module.rdm.dto.TemplateProcessAreaVo;
import codedriver.module.rdm.dto.TemplateVo;
import codedriver.module.rdm.util.UuidUtil;
import com.alibaba.fastjson.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


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
            templateVo.setIsActive(1);
            templateMapper.insertTemplate(templateVo);
        }else {
            templateMapper.updateTemplate(templateVo);
        }
        return templateVo.getUuid();
    }

    @Override
    public void saveTemplateProcessArea(TemplateProcessAreaVo processAreaVo) {
        //删除自定义属性
        TemplateProcessAreaFieldVo fieldParam = new TemplateProcessAreaFieldVo();
        fieldParam.setTemplateUuid(processAreaVo.getTemplateUuid());
        fieldParam.setProcessAreaUuid(processAreaVo.getProcessAreaUuid());
        templateMapper.deleteTemplateProCustomField(fieldParam);
        List<TemplateProcessAreaFieldVo> fieldVoList = processAreaVo.getProcessAreaFieldVoList();
        JSONArray sortArray = new JSONArray();
        //只加自定义属性
        for (TemplateProcessAreaFieldVo fieldVo : fieldVoList){
            fieldVo.setFieldUuid(UuidUtil.getUuid());
            if (processAreaVo.getId() != null && processAreaVo.getId() != 0L){
                if (fieldVo.getIsSystem() != 1){
                    templateMapper.insertTemplateProcessAreaField(fieldVo);
                }
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
        }
    }

    @Override
    public void deleteTemplateProcessArea(TemplateProcessAreaVo processAreaVo) {
        TemplateProcessAreaVo areaVo = new TemplateProcessAreaVo();
        areaVo.setId(processAreaVo.getId());
        templateMapper.deleteTemplateProcessArea(areaVo);
        TemplateProcessAreaFieldVo fieldVo = new TemplateProcessAreaFieldVo();
        fieldVo.setProcessAreaUuid(processAreaVo.getProcessAreaUuid());
        fieldVo.setTemplateUuid(processAreaVo.getTemplateUuid());
        templateMapper.deleteTemplateProcessField(fieldVo);
        TemplateProcessAreaTemplateVo templateVo = new TemplateProcessAreaTemplateVo();
        templateVo.setTemplateUuid(processAreaVo.getTemplateUuid());
        templateVo.setProcessAreaUuid(processAreaVo.getProcessAreaUuid());
        templateMapper.deleteTemplateProcessAreaTemplate(templateVo);
    }

    @Override
    public void deleteTemplate(String templateUuid) {
        templateMapper.deleteTemplateByUuid(templateUuid);
        TemplateProcessAreaVo areaVo = new TemplateProcessAreaVo();
        areaVo.setTemplateUuid(templateUuid);
        templateMapper.deleteTemplateProcessArea(areaVo);
        TemplateProcessAreaFieldVo fieldParam = new TemplateProcessAreaFieldVo();
        fieldParam.setTemplateUuid(templateUuid);
        templateMapper.deleteTemplateProcessField(fieldParam);
        TemplateProcessAreaTemplateVo templateVo = new TemplateProcessAreaTemplateVo();
        templateVo.setTemplateUuid(templateUuid);
        templateMapper.deleteTemplateProcessAreaTemplate(templateVo);
    }

    @Override
    public void saveTemplateProcessAreaField(TemplateProcessAreaFieldVo fieldVo) {
        templateMapper.updateTemplateProcessAreaField(fieldVo);
    }

    @Override
    public void saveTemplateProcessAreaTemplate(TemplateProcessAreaTemplateVo processAreaTemplateVo) {
        if (processAreaTemplateVo.getId() != null && processAreaTemplateVo.getId() != 0L){
            templateMapper.updateTemplateProcessTemplate(processAreaTemplateVo);
        } else {
            templateMapper.insertTemplateProcessTemplate(processAreaTemplateVo);
        }
    }
}
