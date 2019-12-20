package codedriver.module.rdm.services;

import codedriver.framework.asynchronization.threadlocal.UserContext;
import codedriver.framework.common.util.PageUtil;
import codedriver.module.rdm.constants.ProjectStatusType;
import codedriver.module.rdm.dao.mapper.ProjectMapper;
import codedriver.module.rdm.dao.mapper.TemplateMapper;
import codedriver.module.rdm.dto.*;
import codedriver.module.rdm.util.UuidUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName ProjectServiceImpl
 * @Description
 * @Auther fandong
 * @Date 2019/12/10 17:11
 **/
@Service
@Transactional
public class ProjectServiceImpl implements ProjectService {

    @Resource
    private ProjectMapper projectMapper;

    @Autowired
    private TemplateMapper templateMapper;

    @Override
    public List<ProjectVo> searchProject(ProjectVo projectVo) {
        if (projectVo.getNeedPage()) {
            int rowNum = projectMapper.searchProjectCount(projectVo);
            projectVo.setRowNum(rowNum);
            projectVo.setPageCount(PageUtil.getPageCount(rowNum, projectVo.getPageSize()));
        }
        return projectMapper.searchProject(projectVo);
    }

    @Override
    public String saveProject(ProjectVo projectVo) {
        projectVo.setUpdateUser(UserContext.get().getUserId());
        if (StringUtils.isBlank(projectVo.getUuid())){
            projectVo.setUuid(UuidUtil.getUuid());
            projectVo.setCreateUser(UserContext.get().getUserId());
            projectMapper.insertProject(projectVo);
        }else {
            projectMapper.updateProject(projectVo);
        }
        copyTemplateData(projectVo.getTemplateUuid(), projectVo.getUuid());
        return projectVo.getUuid();
    }

    public void copyTemplateData(String templateUuid, String projectUuid) {
        List<TemplateProcessAreaVo> processAreaVoList = templateMapper.getTemplateProcessAreaListByTemplateUuid(templateUuid);
        for (TemplateProcessAreaVo areaVo : processAreaVoList){
            ProjectProcessAreaVo projectProcessAreaVo = new ProjectProcessAreaVo();
            projectProcessAreaVo.setIsEnable(1);
            projectProcessAreaVo.setProcessAreaName(areaVo.getProcessAreaName());
            projectProcessAreaVo.setProcessAreaFieldSort(areaVo.getProcessAreaFieldSort());
            projectProcessAreaVo.setProjectUuid(projectUuid);
            projectProcessAreaVo.setProcessAreaUuid(areaVo.getProcessAreaUuid());
            projectMapper.insertProjectProcessArea(projectProcessAreaVo);

            List<TemplateProcessAreaFieldVo> areaFieldVoList = areaVo.getProcessAreaFieldVoList();
            for (TemplateProcessAreaFieldVo areaFieldVo : areaFieldVoList){
                ProjectProcessAreaFieldVo fieldVo = new ProjectProcessAreaFieldVo();
                fieldVo.setConfig(areaFieldVo.getConfig());
                fieldVo.setField(areaFieldVo.getField());
                fieldVo.setFieldName(areaFieldVo.getFieldName());
                fieldVo.setFieldType(areaFieldVo.getFieldType());
                fieldVo.setFieldUuid(areaFieldVo.getFieldUuid());
                fieldVo.setIsShow(1);
                fieldVo.setIsSystem(areaFieldVo.getIsSystem());
                fieldVo.setProcessAreaUuid(areaFieldVo.getProcessAreaUuid());
                fieldVo.setProjectUuid(projectUuid);
                projectMapper.insertProjectProcessAreaField(fieldVo);
            }
        }

        List<TemplateProcessAreaTemplateVo> templateVos = templateMapper.getTemplateProcessAreaTemplateListByTemplateUuid(templateUuid);
        if (templateVos != null && templateVos.size() > 0){
            for (TemplateProcessAreaTemplateVo templateVo : templateVos){
                ProjectProcessAreaTemplateVo projectTemplate = new ProjectProcessAreaTemplateVo();
                projectTemplate.setContent(templateVo.getContent());
                projectTemplate.setProcessAreaUuid(templateVo.getProcessAreaUuid());
                projectTemplate.setProjectUuid(projectUuid);
                projectMapper.insertProjectProcessAreaTemplate(projectTemplate);
            }
        }
    }
}
