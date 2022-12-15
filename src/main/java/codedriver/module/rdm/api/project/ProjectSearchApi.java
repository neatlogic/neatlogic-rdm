/*
 * Copyright(c) 2022 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.module.rdm.api.project;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import codedriver.framework.common.constvalue.ApiParamType;
import codedriver.framework.restful.annotation.Description;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Output;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.privateapi.PrivateApiComponentBase;
import codedriver.module.rdm.dto.ProjectVo;
import codedriver.module.rdm.services.ProjectService;

/**
 * @ClassName ProjectSearchApi
 * @Description
 * @Auther
 * @Date 2019/12/4 9:52
 **/
@Service
public class ProjectSearchApi extends PrivateApiComponentBase {

    @Resource
    private ProjectService projectService;

    @Override
    public String getToken() {
        return "module/rdm/project/search";
    }

    @Override
    public String getName() {
        return "查询项目接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "keyword", type = ApiParamType.STRING, desc = "关键字(项目名称),模糊查询", isRequired = false),
            @Param(name = "needPage", type = ApiParamType.BOOLEAN, desc = "是否需要分页", isRequired = false),
            @Param(name = "currentPage", type = ApiParamType.INTEGER, desc = "当前页数", isRequired = false),
            @Param(name = "pageSize", type = ApiParamType.INTEGER, desc = "每页展示数量 默认10", isRequired = false)})
    @Output({@Param(name = "projectList", type = ApiParamType.JSONARRAY, desc = "项目数据集合", explode = ProjectVo[].class),
            @Param(name = "pageCount", type = ApiParamType.INTEGER, desc = "总页数"),
            @Param(name = "currentPage", type = ApiParamType.INTEGER, desc = "当前页数"),
            @Param(name = "pageSize", type = ApiParamType.INTEGER, desc = "每页展示数量")})
    @Description(desc = "查询项目接口")
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        JSONObject result = new JSONObject();
        ProjectVo projectVo = new ProjectVo();

        projectVo.setKeyword(jsonObj.getString("keyword"));
        if (jsonObj.containsKey("needPage")) {
            projectVo.setNeedPage(jsonObj.getBoolean("needPage"));
        }

        if (jsonObj.containsKey("pageSize")) {
            projectVo.setPageSize(jsonObj.getInteger("pageSize"));
        }

        if (jsonObj.containsKey("currentPage")) {
            projectVo.setCurrentPage(jsonObj.getInteger("currentPage"));
        }

        List<ProjectVo> dataList = projectService.searchProject(projectVo);
        result.put("projectList", dataList);
        if (projectVo.getNeedPage()) {
            result.put("pageSize", projectVo.getPageSize());
            result.put("currentPage", projectVo.getCurrentPage());
            result.put("rowNum", projectVo.getRowNum());
            result.put("pageCount", projectVo.getPageCount());
        }
        return result;
    }


}
