package codedriver.module.rdm.util;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @ClassName PageUtil
 * @Description
 * @Auther
 * @Date 2019/12/9 11:02
 **/
public class JSONUtil {

    /*
      * @Description: 页面数据统一处理
      * @param dataList 数据集合
      * @param rownum  总条数
      * @param pageCount 页数
      * @param pageSize  每页数量
      * @param currentPage 当前页
      * @return: com.alibaba.fastjson.JSONObject
      * @Date 11:12 2019/12/9
      */
    public static JSONObject parsePage(List<?> dataList, Integer rownum, Integer pageCount, Integer pageSize, Integer currentPage) {
        JSONObject data = new JSONObject();
        data.put("dataList", dataList);
        data.put("rownum", rownum);
        data.put("pageCount", pageCount);
        data.put("pageSize", pageSize);
        data.put("currentPage", currentPage);
        return data;
    }
}
