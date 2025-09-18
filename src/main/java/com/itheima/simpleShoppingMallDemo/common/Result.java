package com.itheima.simpleShoppingMallDemo.common;

public class Result<T> {
    private int code;
    private String message;
    private T data;

    public Result() { }

    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // 成功/失败的判断方法
    public boolean isSuccess() {
        return code == 200; // 假设200为成功状态码
    }

    // 成功返回，无数据
    public static <T> Result<T> success() {
        return new Result<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), null);
    }

    // 成功返回，带数据
    public static <T> Result<T> success(T data) {
        return new Result<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), data);
    }

    // 失败返回，自定义错误信息
    public static <T> Result<T> fail(String message) {
        return new Result<>(ResponseCode.FAIL.getCode(), message, null);
    }

    // 失败返回，指定枚举
    public static <T> Result<T> fail(ResponseCode codeEnum) {
        return new Result<>(codeEnum.getCode(), codeEnum.getMessage(), null);
    }

    // getter & setter
    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
}

