package com.atguigu.ssyx.common.result;

import lombok.Data;

/**
 * @BelongsProject: guigu-ssyx-parent
 * @BelongsPackage: com.atguigu.ssyx.common.result
 * @Author: zt
 * @CreateTime: 2023-06-06  22:59
 * @Description:
 */

@Data
public class Result<T> {

    //状态码
    private Integer code;

    //信息
    private String message;

    //数据
    private T data;

    //构造私有化
    private Result() {

    }

    //设置数据的方法
    public static<T> Result<T> build(T data, ResultCodeEnum resultCodeEnum) {
        //创建Result对象，设置值，返回对象
        Result<T> result = new Result<>();
        //判断返回结果是否需要数据
        if(data != null){
            //设置数据到Result对象
            result.setData(data);
        }
        //设置其他值
        result.setCode(resultCodeEnum.getCode());
        result.setMessage(resultCodeEnum.getMessage());
        //返回设置值之后的对象
        return result;
    }

    //成功的方法
    public static<T> Result<T> ok(T data) {
        Result<T> result = build(data, ResultCodeEnum.SUCCESS);
        return result;
    }

    //失败的方法
    public static<T> Result<T> fail(T data) {
        Result<T> result = build(data, ResultCodeEnum.FAIL);
        return result;
    }

}
