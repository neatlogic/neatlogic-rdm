package codedriver.module.rdm.api.projectiteration;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.restful.annotation.Description;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Output;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.ApiComponentBase;
import codedriver.module.rdm.dto.ProjectIterationVo;
import codedriver.module.rdm.dto.ProjectPriorityVo;
import codedriver.module.rdm.services.ProjectIterationService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName ProjectIterationSearchApi
 * @Description 查询项目迭代接口
 * @Auther
 * @Date 2019/12/4 9:52
 **/
@Service
public class ProjectIterationSearchApi extends ApiComponentBase {

    @Resource
    private ProjectIterationService projectIterationService;

    @Override
    public String getToken() {
        return "module/rdm/projectiteration/search";
    }

    @Override
    public String getName() {
        return "查询项目迭代接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({
            @Param(name = "projectUuid", type = ApiParamType.STRING, desc = "项目uuid", isRequired = true),
            @Param(name = "keyword", type = ApiParamType.STRING, desc = "查询关键字", isRequired = true),
    })
    @Output({@Param(name = "iterationList", type = ApiParamType.JSONARRAY, desc = "项目迭代列表", explode = ProjectPriorityVo[].class)})
    @Description(desc = "查询项目迭代接口")
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        JSONObject result = new JSONObject();
        ProjectIterationVo projectIterationVo = new ProjectIterationVo();
        String projectUuid = jsonObj.getString("projectUuid");
        String keyword = jsonObj.getString("keyword");
        projectIterationVo.setProjectUuid(projectUuid);
        projectIterationVo.setKeyword(keyword);

        List<ProjectIterationVo> iterationList = projectIterationService.searchProjectIteration(projectIterationVo);
        result.put("iterationList", iterationList);
        if (projectIterationVo.getNeedPage()) {
            result.put("pageSize", projectIterationVo.getPageSize());
            result.put("currentPage", projectIterationVo.getCurrentPage());
            result.put("rowNum", projectIterationVo.getRowNum());
            result.put("pageCount", projectIterationVo.getPageCount());
        }
        return result;
    }


}
