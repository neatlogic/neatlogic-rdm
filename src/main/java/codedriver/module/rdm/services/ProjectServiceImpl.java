package codedriver.module.rdm.services;

import codedriver.framework.asynchronization.threadlocal.UserContext;
import codedriver.framework.common.util.PageUtil;
import codedriver.module.rdm.dao.mapper.ProjectMapper;
import codedriver.module.rdm.dao.mapper.ProjectPriorityMapper;
import codedriver.module.rdm.dao.mapper.ProjectWorkflowMapper;
import codedriver.module.rdm.dao.mapper.TemplateMapper;
import codedriver.module.rdm.dto.*;
import codedriver.module.rdm.util.UuidUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private ProjectPriorityMapper priorityMapper;

    @Autowired
    private ProjectWorkflowMapper workflowMapper;

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
                fieldVo.setIsRequired(areaFieldVo.getIsRequired());
                fieldVo.setProcessAreaUuid(areaFieldVo.getProcessAreaUuid());
                fieldVo.setProjectUuid(projectUuid);
                insertProjectPriority(fieldVo);
                insertProjectStatus(fieldVo);
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

    @Override
    public List<ProjectProcessAreaVo> searchProjectProcessArea(ProjectProcessAreaVo processAreaVo) {
        List<ProjectProcessAreaVo> areaVoList = projectMapper.getProjectProcessArea(processAreaVo);
        for (ProjectProcessAreaVo areaVo : areaVoList){
            String fieldSort = areaVo.getProcessAreaFieldSort();
            JSONArray sortArray = JSON.parseArray(fieldSort);
            List<ProjectProcessAreaFieldVo> fieldVoList = areaVo.getFieldList();
            List<ProjectProcessAreaFieldVo> sortFieldList = new ArrayList<>();
            for (int i = 0; i < sortArray.size(); i ++){
                for (ProjectProcessAreaFieldVo fieldVo : fieldVoList){
                    if (sortArray.getLong(i) == fieldVo.getId()){
                        sortFieldList.add(fieldVo);
                        break;
                    }
                }
            }
            areaVo.setFieldList(sortFieldList);
        }
        return projectMapper.getProjectProcessArea(processAreaVo);
    }

    @Override
    public void saveProjectProcessArea(ProjectProcessAreaVo processAreaVo) {
        //删除自定义属性
        ProjectProcessAreaFieldVo fieldVo = new ProjectProcessAreaFieldVo();
        fieldVo.setProcessAreaUuid(processAreaVo.getProcessAreaUuid());
        fieldVo.setProjectUuid(processAreaVo.getProjectUuid());
        projectMapper.deleteProjectProcessAreaCustomFiled(fieldVo);
        List<ProjectProcessAreaFieldVo> fieldVoList = processAreaVo.getFieldList();
        List<ProjectProcessAreaFieldVo> systemFieldList = new ArrayList<>();
        JSONArray sortArray = new JSONArray();
        for (ProjectProcessAreaFieldVo f : fieldVoList){

            if (f.getId() != null && f.getId() != 0L){
                if (f.getIsSystem() != 1){
                    f.setFieldUuid(UuidUtil.getUuid());
                    projectMapper.insertProjectProcessAreaField(f);
                }else {
                    systemFieldList.add(f);
                }
            }else {
                f.setFieldUuid(UuidUtil.getUuid());
                projectMapper.insertProjectProcessAreaField(f);
            }
            sortArray.add(f.getId());
        }

        for (ProjectProcessAreaFieldVo field : systemFieldList){
            projectMapper.updateProjectProcessAreaFieldRequired(field);
        }

        //更新过程域
        processAreaVo.setProcessAreaFieldSort(sortArray.toJSONString());
        projectMapper.updateProjectProcessArea(processAreaVo);
    }

    @Override
    public void saveProjectProcessAreaTemplate(ProjectProcessAreaTemplateVo templateVo) {
        if (templateVo.getId() != null && templateVo.getId() != 0L){
            projectMapper.updteProjectProcessAreaTemplate(templateVo);
        }else {
            projectMapper.insertProjectProcessAreaTemplate(templateVo);
        }
    }

    @Override
    public void saveProjectProcessFieldConfig(ProjectProcessAreaFieldVo fieldVo) {
        projectMapper.updateProjectProcessAreaFieldConfig(fieldVo);
    }

    public void insertProjectPriority(ProjectProcessAreaFieldVo fieldVo){
        if(fieldVo.getField().equals("priority")){
            String config = fieldVo.getConfig();
            if (JSONArray.isValid(config)){
                JSONArray configArray = JSONArray.parseArray(config);
                for (int i = 0 ; i < configArray.size() ; i++){
                    JSONObject priority = configArray.getJSONObject(i);
                    ProjectPriorityVo priorityVo = new ProjectPriorityVo();
                    priorityVo.setColor(priority.getString("color"));
                    priorityVo.setName(priority.getString("name"));
                    if (priority.getBooleanValue("isdefault")){
                        priorityVo.setIsDefault(1);
                    }else {
                        priorityVo.setIsDefault(0);
                    }
                    priorityVo.setProcessAreaUuid(fieldVo.getProcessAreaUuid());
                    priorityVo.setProjectUuid(fieldVo.getProjectUuid());
                    priorityVo.setUuid(UuidUtil.getUuid());
                    priorityVo.setCreateUser(UserContext.get().getUserId());
                    priorityVo.setUpdateUser(UserContext.get().getUserId());
                    priorityMapper.insertProjectPriority(priorityVo);
                }
            }
        }
    }

    public void insertProjectStatus(ProjectProcessAreaFieldVo fieldVo){
        if (fieldVo.getField().equals("status")){
            String config = fieldVo.getConfig();
            if (JSONArray.isValid(config)){
                Map<String, JSONArray> transferArray = new HashMap<>();
                Map<String, String> statusMap = new HashMap<>();
                JSONArray statusArray = JSONArray.parseArray(config);
                //录入流转状态
                for (int i = 0; i < statusArray.size(); i++){
                    JSONObject obj = statusArray.getJSONObject(i);
                    ProjectWorkFlowStatusVo statusVo = new ProjectWorkFlowStatusVo();
                    statusVo.setProcessAreaUuid(fieldVo.getProcessAreaUuid());
                    statusVo.setProjectUuid(fieldVo.getProjectUuid());
                    statusVo.setUuid(UuidUtil.getUuid());
                    statusVo.setName(obj.getString("name"));
                    statusVo.setColor(obj.getString("color"));
                    statusVo.setType(obj.getString("type"));
                    workflowMapper.insertProjectWorkflowStatus(statusVo);
                    JSONArray array = obj.getJSONArray("to");
                    statusMap.put(statusVo.getName(), statusVo.getUuid());
                    transferArray.put(statusVo.getUuid(), array);
                }
                //录入流转关联状态
                for (Map.Entry<String, JSONArray> entry : transferArray.entrySet()){
                    String uuid = entry.getKey();
                    JSONArray transfers = entry.getValue();
                    for (int i = 0; i < transfers.size(); i++){
                        String transferUuid =statusMap.get(transfers.getString(i));
                        workflowMapper.insertProjectWorkflowStatusTransfer(uuid, transferUuid);
                    }
                }

            }
        }
    }
}
