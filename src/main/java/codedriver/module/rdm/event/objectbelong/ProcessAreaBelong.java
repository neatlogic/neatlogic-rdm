package codedriver.module.rdm.event.objectbelong;

import codedriver.module.rdm.dao.mapper.ProcessAreaMapper;
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
public class ProcessAreaBelong implements Belong {

    private String processAreaUuid;
    @Resource
    private ProcessAreaMapper processAreaMapper;

    public ProcessAreaBelong() {

    }


    public ProcessAreaBelong(String _processAreaUuid) {
        this.processAreaUuid = _processAreaUuid;
    }

    @Override
    public String getBelongUuid() {
        return processAreaUuid;
    }

    @Override
    public String name() {
        return "process_area";
    }

    @Override
    public String description() {
        return "过程域";
    }

    @Override
    public List<JSONObject> getBelongObjects() {
        List<ProcessAreaVo> processAreaVoList = processAreaMapper.searchProcessArea(new ProcessAreaVo() {{
            setNeedPage(false);
        }});

        List<JSONObject> objectList = new ArrayList<>();
        for (ProcessAreaVo processAreaVo : processAreaVoList) {
            JSONObject objectData = new JSONObject();
            objectData.put("uuid", processAreaVo.getUuid());
            objectData.put("name", processAreaVo.getName());
            objectList.add(objectData);
        }
        return objectList;
    }
}
