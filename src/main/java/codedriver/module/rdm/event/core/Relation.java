package codedriver.module.rdm.event.core;

import codedriver.module.rdm.event.dto.RelationVo;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @ClassName Relation
 * @Description
 * @Auther
 * @Date 2020/1/8 17:07
 **/
public interface Relation {

    String getName();

    List<RelationVo> getRelationList();

    Integer countRelation(String relation, String selectKey, String objectUuid, String targetObjectUuid);

    /*
     * 用于支持除了关联管理之外的校验参数
     */
    JSONObject getRelationParam(String relation, String selectKey, String objectUuid, String targetObjectUuid);
}
