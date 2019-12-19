package codedriver.module.rdm.dao.mapper;

import codedriver.module.rdm.dto.TemplateProcessAreaFieldVo;
import codedriver.module.rdm.dto.TemplateProcessAreaTemplateVo;
import codedriver.module.rdm.dto.TemplateProcessAreaVo;
import codedriver.module.rdm.dto.TemplateVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TemplateMapper {

    void insertTemplate(TemplateVo templateVo);

    void insertTemplateProcessTemplate(TemplateProcessAreaTemplateVo processAreaTemplateVo);

    void insertTemplateProcessArea(TemplateProcessAreaVo processAreaVo);

    void insertTemplateProcessAreaField(TemplateProcessAreaFieldVo processAreaFieldVo);

    void updateTemplate(TemplateVo templateVo);

    void updateTemplateProcessTemplate(TemplateProcessAreaTemplateVo processAreaTemplateVo);

    void updateTemplateProcessAreaField(TemplateProcessAreaFieldVo fieldVo);

    void deleteTemplateByUuid(String uuid);

    void deleteTemplateProcessAreaById(Long id);

    void deleteTemplateProcessAreaByTemplateUuid(String templateUuid);

    void deleteTemplateProcessFieldByTemplateUuid(String templateUuid);

    void deleteTemplateProcessFieldByTemplateUuidAndAreaUuid(@Param("templateUuid") String templateUuid, @Param("processAreaUuid") String processAreaUuid);

    void deleteTemplateProCustomFieldByTemplateUuidAndAreaUuid(@Param("templateUuid") String templateUuid, @Param("processAreaUuid") String processAreaUuid);

    void updateTemplateProcessAreaFieldSort(TemplateProcessAreaVo processAreaVo);

    List<TemplateProcessAreaVo> getTemplateProcessAreaListByTemplateUuid(String templateUuid);
}
