package codedriver.module.rdm.util;

import java.util.UUID;

/**
 * @ClassName UuidUtil
 * @Description  Uuid工具类
 * @Auther
 * @Date 2019/12/13 16:32
 **/
public class UuidUtil {
    public static String getUuid(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }
}
