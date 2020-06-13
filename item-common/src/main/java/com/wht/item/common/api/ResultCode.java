package com.wht.item.common.api;

/**
 * 枚举了一些常用API操作码
 * Created by wht on 2019/4/19.
 */
public enum ResultCode implements IErrorCode {
    SUCCESS(200, "操作成功"),
    FAILED(500, "操作失败"),
    VALIDATE_FAILED(412, "参数检验失败"),
    UNAUTHORIZED(401, "暂未登录或token已经过期"),
    FORBIDDEN(403, "没有相关权限"),

    /**
     * 运行时异常
     */
    RUNTIME(1000, "RuntimeException"),
    /**
     * 空指针异常
     */
    NULL_POINTER(1001, "NullPointerException "),
    /**
     * 类型住转换异常
     */
    CLASS_CAST(1002, "ClassCastException"),
    /**
     * IO异常
     */
    IO(1003, "IOException"),
    /**
     * 找不到方法异常
     */
    NO_SUCH_METHOD(1004, "NoSuchMethodException"),
    /**
     * 数组越界异常
     */
    INDEX_OUTOF_BOUNDS(1005, "IndexOutOfBoundsException"),
    /**
     * 400异常
     */
    BAD_REQUEST(400, "Bad Request"),
    /**
     * 404异常
     */
    NOT_FOUND(404, "Not Found"),
    /**
     * 方法不允许异常
     */
    METHOD_BOT_ALLOWED(405, "Method Not Allowed"),
    /**
     * 不可到达异常
     */
    NOT_ACCEPTABLE(406, "Not Acceptable"),
    /**
     * 500异常
     */
    INTERNAL_SERVER_ERROR(500, "Internal Server Error");


    private int code;
    private String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
