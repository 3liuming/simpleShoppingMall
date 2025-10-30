package com.itheima.simpleShoppingMallDemo.common;

import java.util.List;

public class Result<T> {
    private int code;
    private String message;
    private T data;
    private Long total; // 用于分页场景,记录总数

    public Result() { }

    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Result(int code, String message, T data, Long total) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.total = total;
    }

    // 成功/失败的判断方法
    public boolean isSuccess() {
        return code == 200; // 假设200为成功状态码
    }

    // 成功返回,无数据
    public static <T> Result<T> success() {
        return new Result<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), null);
    }

    // 成功返回,带数据
    public static <T> Result<T> success(T data) {
        return new Result<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), data);
    }

    // 成功返回,带自定义消息和数据
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(ResponseCode.SUCCESS.getCode(), message, data);
    }

    // ========== 带总数的成功返回(常用于分页) ==========

    // 成功返回带总数
    public static <T> Result<T> success(T data, Long total) {
        return new Result<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), data, total);
    }

    // 成功返回带自定义消息和总数
    public static <T> Result<T> success(String message, T data, Long total) {
        return new Result<>(ResponseCode.SUCCESS.getCode(), message, data, total);
    }

    // ========== 失败返回方法 ==========

    // 失败返回,自定义错误信息
    public static <T> Result<T> fail(String message) {
        return new Result<>(ResponseCode.FAIL.getCode(), message, null);
    }

    // 失败返回,指定枚举
    public static <T> Result<T> fail(ResponseCode codeEnum) {
        return new Result<>(codeEnum.getCode(), codeEnum.getMessage(), null);
    }

    // 失败返回,指定状态码和消息
    public static <T> Result<T> fail(int code, String message) {
        return new Result<>(code, message, null);
    }

    // 失败返回List类型
    public static <T> Result<List<T>> failList(String message) {
        return new Result<>(ResponseCode.FAIL.getCode(), message, null);
    }

    // 失败返回List类型,指定枚举
    public static <T> Result<List<T>> failList(ResponseCode codeEnum) {
        return new Result<>(codeEnum.getCode(), codeEnum.getMessage(), null);
    }

    // ========== 工具方法 ==========

    // 链式调用设置total
    public Result<T> total(Long total) {
        this.total = total;
        return this;
    }

    // 链式调用设置message
    public Result<T> message(String message) {
        this.message = message;
        return this;
    }

    // getter & setter
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", total=" + total +
                '}';
    }
}