package codedriver.module.rdm.api.projectpriority;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.restful.annotation.Description;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Output;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.ApiComponentBase;
import codedriver.module.rdm.dto.ProjectPriorityVo;
import codedriver.module.rdm.services.ProjectPriorityService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName ProjectStatusSearchApi
 * @Description 查询项目优先级接口
 * @Auther
 * @Date 2019/12/4 9:52
 **/
@Service
public class ProjectPrioritySearchApi extends ApiComponentBase {

    @Resource
    private ProjectPriorityService projectPriorityService;

    @Override
    public String getToken() {
        return "module/rdm/projectpriority/search";
    }

    @Override
    public String getName() {
        return "查询项目优先级接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({
            @Param(name = "projectUuid", type = ApiParamType.STRING, desc = "项目uuid", isRequired = true),
            @Param(name = "processAreaUuid", type = ApiParamType.STRING, desc = "过程域uuid", isRequired = true)
    })
    @Output({@Param(name = "priorityList", type = ApiParamType.JSONARRAY, desc = "项目优先级列表", explode = ProjectPriorityVo[].class)})
    @Description(desc = "查询项目优先级集合接口")
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        JSONObject result = new JSONObject();
        ProjectPriorityVo projectPriorityVo = new ProjectPriorityVo();
        String projectUuid = jsonObj.getString("projectUuid");
        String processAreaUuid = jsonObj.getString("processAreaUuid");
        projectPriorityVo.setProjectUuid(projectUuid);
        projectPriorityVo.setProcessAreaUuid(processAreaUuid);

        List<ProjectPriorityVo> dataList = projectPriorityService.searchProjectPriority(projectPriorityVo);
        result.put("priorityList", dataList);
        if (projectPriorityVo.getNeedPage()) {
            result.put("pageSize", projectPriorityVo.getPageSize());
            result.put("currentPage", projectPriorityVo.getCurrentPage());
            result.put("rowNum", projectPriorityVo.getRowNum());
            result.put("pageCount", projectPriorityVo.getPageCount());
        }
        return result;
    }


}
