package codedriver.module.rdm.api.processarea;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.restful.annotation.Description;
import codedriver.framework.restful.annotation.Input;
import codedriver.framework.restful.annotation.Output;
import codedriver.framework.restful.annotation.Param;
import codedriver.framework.restful.core.ApiComponentBase;
import codedriver.module.rdm.dto.ProcessAreaVo;
import codedriver.module.rdm.services.ProcessAreaService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName ProcessAreaSearchApi
 * @Description
 * @Auther
 * @Date 2019/12/4 9:52
 **/
@Service
public class ProcessAreaSearchApi extends ApiComponentBase {

    @Resource
    private ProcessAreaService processAreaService;

    @Override
    public String getToken() {
        return "module/rdm/processarea/search";
    }

    @Override
    public String getName() {
        return "查询过程域接口";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "keyword", type = ApiParamType.STRING, desc = "关键字(过程域名称),模糊查询", isRequired = false),
            @Param(name = "needPage", type = ApiParamType.BOOLEAN, desc = "是否需要分页", isRequired = false),
            @Param(name = "currentPage", type = ApiParamType.INTEGER, desc = "当前页数", isRequired = false),
            @Param(name = "pageSize", type = ApiParamType.INTEGER, desc = "每页展示数量 默认10", isRequired = false)})
    @Output({@Param(name = "processAreaList", type = ApiParamType.JSONARRAY, desc = "过程域数据集合", explode = ProcessAreaVo[].class),
            @Param(name = "pageCount", type = ApiParamType.INTEGER, desc = "总页数"),
            @Param(name = "currentPage", type = ApiParamType.INTEGER, desc = "当前页数"),
            @Param(name = "pageSize", type = ApiParamType.INTEGER, desc = "每页展示数量")})
    @Description(desc = "查询过程域接口")
    @Override
    public Object myDoService(JSONObject jsonObj) throws Exception {
        JSONObject result = new JSONObject();
        ProcessAreaVo processAreaVo = new ProcessAreaVo();
        processAreaVo.setKeyword(jsonObj.getString("keyword"));
        if (jsonObj.containsKey("needPage")) {
            processAreaVo.setNeedPage(jsonObj.getBoolean("needPage"));
        }

        if (jsonObj.containsKey("pageSize")) {
            processAreaVo.setPageSize(jsonObj.getInteger("pageSize"));
        }

        if (jsonObj.containsKey("currentPage")) {
            processAreaVo.setCurrentPage(jsonObj.getInteger("currentPage"));
        }

        List<ProcessAreaVo> dataList = processAreaService.searchProcessArea(processAreaVo);
        result.put("processAreaList", dataList);
        if (processAreaVo.getNeedPage()) {
            result.put("pageSize", processAreaVo.getPageSize());
            result.put("currentPage", processAreaVo.getCurrentPage());
            result.put("rowNum", processAreaVo.getRowNum());
            result.put("pageCount", processAreaVo.getPageCount());
        }
        return result;
    }


}
