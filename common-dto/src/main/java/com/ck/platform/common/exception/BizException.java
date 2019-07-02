package com.ck.platform.common.exception;

/**
 * 业务异常
 *
 * @author chenck
 * @date 2019/6/26 14:18
 */
public class BizException extends RuntimeException implements ResultCode {

    /**
     * 结果码
     */
    private String code;

    /**
     * 结果码信息
     */
    private String msg;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }


    /**
     * 构造函数
     *
     * @param resultCode
     */
    public BizException(ResultCode resultCode) {
        super(resultCode.getMsg());
        this.code = resultCode.getCode();
        this.msg = resultCode.getMsg();
    }

    /**
     * 构造函数
     *
     * @param resultCode
     * @param cause
     */
    public BizException(ResultCode resultCode, Throwable cause) {
        super(cause);
        this.code = resultCode.getCode();
        this.msg = resultCode.getMsg();
    }

    /**
     * 构造函数
     *
     * @param resultCode
     * @param detailMessage
     */
    public BizException(ResultCode resultCode, String detailMessage) {
        super(detailMessage);
        this.code = resultCode.getCode();
        this.msg = resultCode.getMsg();
    }

    /**
     * 构造函数
     *
     * @param resultCode
     * @param detailMessage
     * @param cause
     */
    public BizException(ResultCode resultCode, String detailMessage, Throwable cause) {
        super(detailMessage, cause);
        this.code = resultCode.getCode();
        this.msg = resultCode.getMsg();
    }

    /**
     * 构造函数
     *
     * @param code
     * @param msg
     */
    public BizException(String code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    /**
     * 构造函数
     *
     * @param code
     * @param msg
     * @param cause
     */
    public BizException(String code, String msg, Throwable cause) {
        super(cause);
        this.code = code;
        this.msg = msg;
    }

    /**
     * 构造函数
     *
     * @param code
     * @param msg
     * @param detailMessage
     */
    public BizException(String code, String msg, String detailMessage) {
        super(detailMessage);
        this.code = code;
        this.msg = msg;
    }

    /**
     * 构造函数
     *
     * @param code
     * @param msg
     * @param detailMessage
     * @param cause
     */
    public BizException(String code, String msg, String detailMessage, Throwable cause) {
        super(detailMessage, cause);
        this.code = code;
        this.msg = msg;
    }

}
