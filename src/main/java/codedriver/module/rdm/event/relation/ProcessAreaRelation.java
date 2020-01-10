package codedriver.module.rdm.event.relation;

import codedriver.module.rdm.dao.mapper.TaskMapper;
import codedriver.module.rdm.event.core.Relation;
import codedriver.module.rdm.event.dto.RelationVo;
import codedriver.module.rdm.event.objectbelong.IterationBelong;
import codedriver.module.rdm.event.objectbelong.ProcessAreaBelong;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName ProcessAreaRelation
 * @Description
 * @Auther
 * @Date 2020/1/8 17:06
 **/
@Service
public class ProcessAreaRelation implements Relation {

    @Resource
    private TaskMapper taskMapper;

    @Override
    public String getName() {
        return "process_area";
    }

    @Override
    public List<RelationVo> getRelationList() {

        return new ArrayList<RelationVo>() {{
            //过程域之间关联
            add(new RelationVo(new ProcessAreaBelong(), new ProcessAreaBelong()));

            //过程域与迭代之间关联
            add(new RelationVo(new ProcessAreaBelong(), new IterationBelong()));

        }};
    }


    @Override
    public Integer countRelation(String relation, String selectKey, String objectUuid, String targetObjectUuid) {
        Integer count = 0;
        if (relation.equalsIgnoreCase(new ProcessAreaBelong().name() + "||" + new ProcessAreaBelong().name())) {
            count = taskMapper.getTaskAssociationCount(selectKey, targetObjectUuid);
        } else if (relation.equalsIgnoreCase(new ProcessAreaBelong().name() + "||" + new IterationBelong().name())) {
            String iterationUuid = taskMapper.getTaskIterationCount(selectKey);
            if (StringUtils.isNotBlank(iterationUuid)) {
                count = 1;
            }
        }
        return count;
    }

    @Override
    public JSONObject getRelationParam(String relation, String selectKey, String objectUuid, String targetObjectUuid) {

        return null;
    }

}
