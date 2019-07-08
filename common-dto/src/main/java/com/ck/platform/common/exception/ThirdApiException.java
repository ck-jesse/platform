package com.ck.platform.common.exception;

/**
 * @author chenck
 * @date 2019/7/8 21:15
 */
public class ThirdApiException extends BizException {
    public ThirdApiException(ResultCode resultCode) {
        super(resultCode);
    }

    public ThirdApiException(ResultCode resultCode, Throwable cause) {
        super(resultCode, cause);
    }

    public ThirdApiException(ResultCode resultCode, String detailMessage) {
        super(resultCode, detailMessage);
    }

    public ThirdApiException(ResultCode resultCode, String detailMessage, Throwable cause) {
        super(resultCode, detailMessage, cause);
    }

    public ThirdApiException(String code, String msg) {
        super(code, msg);
    }

    public ThirdApiException(String code, String msg, Throwable cause) {
        super(code, msg, cause);
    }

    public ThirdApiException(String code, String msg, String detailMessage) {
        super(code, msg, detailMessage);
    }

    public ThirdApiException(String code, String msg, String detailMessage, Throwable cause) {
        super(code, msg, detailMessage, cause);
    }
}
