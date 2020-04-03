package com.blkchainsolutions.common;


/**
 * Created by kimi on 2019/09/21.
 */

public class ResultUtil {

    /**
     * 成功
     * 需要返回对象
     *
     * @return
     */
    public static Result success(Object obj) {
        Result result = new Result();
        result.setData(obj);
        result.setCode(200);
        result.setMsg("SUCCESS");
        result.setStatus("OK");
        return result;
    }

    /**
     * 成功
     * 不需要返回对象
     *
     * @return
     */
    public static Result success() {
        return success(null);
    }

    /**
     * 失败的方法
     *
     * @param code 错误编码
     * @param msg  提示
     * @return
     */
    public static Result error(Integer code, String msg) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        result.setStatus("ERROR");
        return result;
    }
    public static Result error( String msg) {
        Result result = new Result();
        result.setCode(400);
        result.setMsg(msg);
        result.setStatus("ERROR");
        return result;
    }
    /**
     * 失败的方法
     *
     * @return
     */
    public static Result error() {
        Result result = new Result();
        result.setCode(400);
        result.setMsg("Request exception");
        result.setStatus("ERROR");
        return result;
    }
}
