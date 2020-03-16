package com.ck.platform.common.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.ck.platform.common.exception.BizResultCode;
import lombok.Data;

/**
 * 数据响应DTO
 *
 * @author chenck
 * @date 2019/6/28 16:33
 */
@Data
public class DataResult<T> extends ServiceResult {

    /**
     * 返回业务数据
     */
    private T data;

    /**
     * 校验是否成功
     */
    @JSONField(serialize = false)
    public boolean isSucc() {
        return BizResultCode.SUCC.getCode().equals(getCode());
    }

    public DataResult() {
        super();
    }

    public DataResult(String msg) {
        super(msg);
    }

    public DataResult(String code, String msg) {
        super(code, msg);
    }

    public DataResult(T data, String code, String msg) {
        super(code, msg);
        this.data = data;
    }

    public DataResult<T> setData(T data) {
        this.data = data;
        return this;
    }

    /**
     * 设置结果
     */
    public static <T> DataResult<T> result(T data, String code, String msg) {
        DataResult dataResult = new DataResult(code, msg);
        dataResult.setData(data);
        return dataResult;
    }

    /**
     * 设置为成功
     */
    public static DataResult succ() {
        return DataResult.result(null, BizResultCode.SUCC.getCode(), BizResultCode.SUCC.getMsg());
    }

    public static <T> DataResult<T> succ(T data) {
        return DataResult.result(data, BizResultCode.SUCC.getCode(), BizResultCode.SUCC.getMsg());
    }

    public static <T> DataResult<T> succ(T data, String msg) {
        return DataResult.result(data, BizResultCode.SUCC.getCode(), msg);
    }

    public static <T> DataResult<T> succ(T data, String code, String msg) {
        return DataResult.result(data, code, msg);
    }

    public static DataResult succMsg(String msg) {
        return DataResult.result(null, BizResultCode.SUCC.getCode(), msg);
    }


    /**
     * 设置为失败
     */
    public static DataResult error() {
        return DataResult.result(null, BizResultCode.ERR_SYSTEM.getCode(), BizResultCode.ERR_SYSTEM.getMsg());
    }

    public static DataResult error(String msg) {
        return DataResult.result(null, BizResultCode.ERR_SYSTEM.getCode(), msg);
    }

    public static DataResult error(String code, String msg) {
        return DataResult.result(null, code, msg);
    }


    public static <T> DataResult<T> error(T data, String msg) {
        return DataResult.result(data, BizResultCode.ERR_SYSTEM.getCode(), msg);
    }

    public static <T> DataResult<T> error(T data, String code, String msg) {
        return DataResult.result(data, code, msg);
    }

    public static <T> DataResult<T> errorData(T data) {
        return DataResult.result(data, BizResultCode.ERR_SYSTEM.getCode(), BizResultCode.ERR_SYSTEM.getMsg());
    }
}
