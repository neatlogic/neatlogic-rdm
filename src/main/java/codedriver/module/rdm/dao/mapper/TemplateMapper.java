package codedriver.module.rdm.dao.mapper;

import codedriver.module.rdm.dto.TemplateProcessAreaFieldVo;
import codedriver.module.rdm.dto.TemplateProcessAreaTemplateVo;
import codedriver.module.rdm.dto.TemplateProcessAreaVo;
import codedriver.module.rdm.dto.TemplateVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TemplateMapper {

    void insertTemplate(TemplateVo templateVo);

    void insertTemplateProcessArea(TemplateProcessAreaVo processAreaVo);

    void insertTemplateProcessTemplate(TemplateProcessAreaTemplateVo processAreaTemplateVo);

    void insertTemplateProcessAreaField(TemplateProcessAreaFieldVo processAreaFieldVo);

    void updateTemplate(TemplateVo templateVo);

    void updateTemplateProcessTemplate(TemplateProcessAreaTemplateVo processAreaTemplateVo);

    void updateTemplateProcessAreaField(TemplateProcessAreaFieldVo fieldVo);

    void deleteTemplateByUuid(String uuid);

    void deleteTemplateProcessArea(TemplateProcessAreaVo areaVo);

    void deleteTemplateProcessField(TemplateProcessAreaFieldVo fieldVo);

    void deleteTemplateProCustomField(TemplateProcessAreaFieldVo fieldVo);

    void deleteTemplateProcessAreaTemplate(TemplateProcessAreaTemplateVo templateVo);

    void updateTemplateProcessAreaFieldSort(TemplateProcessAreaVo processAreaVo);

    List<TemplateProcessAreaVo> getTemplateProcessAreaListByTemplateUuid(String templateUuid);

    List<TemplateProcessAreaTemplateVo> getTemplateProcessAreaTemplateListByTemplateUuid(String templateUuid);
}
