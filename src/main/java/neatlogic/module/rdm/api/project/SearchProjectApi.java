/*
 * Copyright(c) 2023 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package neatlogic.module.rdm.api.project;

import neatlogic.framework.auth.core.AuthAction;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.common.dto.BasePageVo;
import neatlogic.framework.rdm.auth.label.RDM_BASE;
import neatlogic.framework.rdm.dto.ProjectVo;
import neatlogic.framework.restful.annotation.*;
import neatlogic.framework.restful.constvalue.OperationTypeEnum;
import neatlogic.framework.restful.core.privateapi.PrivateApiComponentBase;
import neatlogic.framework.util.TableResultUtil;
import neatlogic.module.rdm.dao.mapper.ProjectMapper;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@AuthAction(action = RDM_BASE.class)
@OperationType(type = OperationTypeEnum.SEARCH)
public class SearchProjectApi extends PrivateApiComponentBase {

    @Resource
    private ProjectMapper projectMapper;

    @Override
    public String getName() {
        return "查询项目";
    }

    @Override
    public String getConfig() {
        return null;
    }

    @Input({@Param(name = "keyword", type = ApiParamType.STRING, desc = "关键字"),
            @Param(name = "currentPage", type = ApiParamType.INTEGER, desc = "当前页"),
            @Param(name = "pageSize", type = ApiParamType.STRING, desc = "页数")})
    @Output({@Param(explode = BasePageVo.class)})
    @Description(desc = "查询项目接口")
    @Override
    public Object myDoService(JSONObject paramObj) {
        ProjectVo projectVo = JSONObject.toJavaObject(paramObj, ProjectVo.class);
        List<ProjectVo> projectList = projectMapper.searchProject(projectVo);
        if (CollectionUtils.isNotEmpty(projectList)) {
            int rowNum = projectMapper.searchProjectCount(projectVo);
            projectVo.setRowNum(rowNum);
        }
        return TableResultUtil.getResult(projectList, projectVo);
    }

    @Override
    public String getToken() {
        return "/rdm/project/search";
    }
}
