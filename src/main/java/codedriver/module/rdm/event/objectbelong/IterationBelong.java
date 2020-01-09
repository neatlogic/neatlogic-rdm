package codedriver.module.rdm.event.objectbelong;

import codedriver.module.rdm.dao.mapper.ProcessAreaMapper;
import codedriver.module.rdm.dao.mapper.TaskMapper;
import codedriver.module.rdm.dto.ProcessAreaVo;
import codedriver.module.rdm.event.core.Belong;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName ProcessAreaObjectBelong
 * @Description
 * @Auther
 * @Date 2020/1/7 15:16
 **/
@Service
public class IterationBelong implements Belong {

    @Override
    public String name() {
        return "iteration";
    }

    @Override
    public String description() {
        return "过程域";
    }

    @Override
    public List<JSONObject> getBelongObjects() {

        List<JSONObject> objectList = new ArrayList<>();
        JSONObject objectData = new JSONObject();
        objectData.put("uuid", "");
        objectData.put("name", "迭代");
        objectList.add(objectData);
        return objectList;
    }
}
