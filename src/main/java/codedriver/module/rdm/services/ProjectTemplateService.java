package codedriver.module.rdm.services;

import codedriver.module.rdm.dto.TemplateProcessAreaFieldVo;
import codedriver.module.rdm.dto.TemplateProcessAreaTemplateVo;
import codedriver.module.rdm.dto.TemplateProcessAreaVo;
import codedriver.module.rdm.dto.TemplateVo;

/**
 * @program: codedriver
 * @description:
 * @create: 2019-12-16 17:07
 **/
public interface ProjectTemplateService {

    String saveTemplate(TemplateVo templateVo);

    void saveTemplateProcessArea(TemplateProcessAreaVo processAreaVo);

    void saveTemplateProcessAreaTemplate(TemplateProcessAreaTemplateVo processAreaTemplateVo);

    void deleteTemplateProcessArea(TemplateProcessAreaVo processAreaVo);

    void deleteTemplate(String templateUuid);

    void saveTemplateProcessAreaFieldConfig(TemplateProcessAreaFieldVo fieldVo);
}
