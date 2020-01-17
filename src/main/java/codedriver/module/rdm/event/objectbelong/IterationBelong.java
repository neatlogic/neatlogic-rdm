package codedriver.module.rdm.event.objectbelong;

import codedriver.module.rdm.event.core.Belong;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        return "迭代";
    }

    @Override
    public String getBelongUuid() {
        return UUID.fromString(name()).toString().replaceAll("-", "");
    }

    @Override
    public List<JSONObject> getBelongObjects() {

        List<JSONObject> objectList = new ArrayList<>();
        JSONObject objectData = new JSONObject();
        objectData.put("uuid", UUID.fromString(name()).toString().replaceAll("-", ""));
        objectData.put("name", "迭代");
        objectList.add(objectData);
        return objectList;
    }
}
