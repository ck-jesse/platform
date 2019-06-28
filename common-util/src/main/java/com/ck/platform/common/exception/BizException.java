package com.ck.platform.common.exception;

/**
 * 业务异常
 *
 * @author chenck
 * @date 2019/6/26 14:18
 */
public class BizException extends RuntimeException {

    /**
     * 结果码
     */
    private ResultCode resultCode;

    /**
     * 构造函数
     *
     * @param resultCode
     */
    public BizException(ResultCode resultCode) {
        super(resultCode.getMsg());
        this.resultCode = new ResultCodeImpl(resultCode);
    }

    /**
     * 构造函数
     *
     * @param resultCode
     * @param cause
     */
    public BizException(ResultCode resultCode, Throwable cause) {
        super(cause);
        this.resultCode = new ResultCodeImpl(resultCode);
    }

    /**
     * 构造函数
     *
     * @param resultCode
     * @param detailMessage
     */
    public BizException(ResultCode resultCode, String detailMessage) {
        super(detailMessage);
        this.resultCode = new ResultCodeImpl(resultCode);
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
        this.resultCode = new ResultCodeImpl(resultCode);
    }

    /**
     * 构造函数
     *
     * @param code
     * @param msg
     */
    public BizException(String code, String msg) {
        this(new ResultCodeImpl(code, msg));
    }

    /**
     * 构造函数
     *
     * @param code
     * @param msg
     * @param cause
     */
    public BizException(String code, String msg, Throwable cause) {
        this(new ResultCodeImpl(code, msg), cause);
    }

    /**
     * 构造函数
     *
     * @param code
     * @param msg
     * @param detailMessage
     */
    public BizException(String code, String msg, String detailMessage) {
        this(new ResultCodeImpl(code, msg), detailMessage);
    }

    /**
     * Getter method for property <tt>resultCode</tt>.
     *
     * @return property value of resultCode
     */
    public ResultCode getResultCode() {
        return resultCode;
    }

    public static class ResultCodeImpl implements ResultCode {

        private String code;

        private String msg;

        public ResultCodeImpl(ResultCode resultCode) {
            this.code = resultCode.getCode();
            this.msg = resultCode.getMsg();
        }

        public ResultCodeImpl(String code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        /**
         * @see ResultCode#getCode()
         */
        @Override
        public String getCode() {
            return code;
        }

        /**
         * @see ResultCode#getMsg()
         */
        @Override
        public String getMsg() {
            return msg;
        }

    }

}
