package com.xxx.server.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Keafmd
 *
 * @ClassName: RespBean
 * @Description: 相应返回对象
 * @author: liuchen
 * @date: 2023/3/5 15:12
 * @Blog:
 */

@Data
@NoArgsConstructor // 无参构造
@AllArgsConstructor  //全参构造
public class RespBean {
    private long code;
    private String message;
    private Object obj;

    /**
     * 成功返回结果
     * @param message
     * @return
     */
    public static RespBean success(String message){
        return  new RespBean(200,message,null);
    }

    /**
     * 成功返回结果
     * @param message
     * @param obj
     * @return
     */
    public static RespBean success(String message,Object obj){
        return  new RespBean(200,message,obj);
    }

    /**
     * 失败返回结果
     * @param message
     * @return
     */
    public static RespBean error (String message) {
        return new RespBean(500,message,null);
    }

    /**
     * 失败返回结果
     * @param message
     * @param obj
     * @return
     */
    public static RespBean error (String message,Object obj) {
        return new RespBean(500,message,obj);
    }

}
