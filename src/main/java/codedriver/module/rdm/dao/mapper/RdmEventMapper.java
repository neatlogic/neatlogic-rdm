package codedriver.module.rdm.dao.mapper;

import codedriver.module.rdm.event.dto.EventRuleVo;
import codedriver.module.rdm.event.dto.EventVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName RdmEventMapper
 * @Description
 * @Auther
 * @Date 2020/1/7 16:58
 **/
public interface RdmEventMapper {

    List<EventRuleVo> getAllEventRule();

    EventVo getEvent(EventVo event);

    void insertEvent(EventVo event);

    void deleteEvent(EventVo event);

    EventRuleVo getEventRuleByUuid(@Param("uuid") String uuid);

    void deleteEventById(@Param("id") Long id);
}
