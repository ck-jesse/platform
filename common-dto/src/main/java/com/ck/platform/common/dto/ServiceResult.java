package com.ck.platform.common.dto;


import com.ck.platform.common.exception.BizResultCode;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 响应DTO，主要用于不同服务之间的数据传递
 * <p>
 * 建议用法：必须指定业务数据T的具体类型，如： ServiceResult<UserInfoDto>
 *
 * @author chenck
 * @date 2019/10/18 20:07
 */
@Data
@Accessors(chain = true)// 链式调用
public class ServiceResult<T> implements Serializable {

    /**
     * 返回码
     */
    private String code = BizResultCode.SUCC.getCode();

    /**
     * 描述信息
     */
    private String msg = BizResultCode.SUCC.getMsg();

    /**
     * 返回业务数据
     */
    private T data;


    // 定义构造函数，方便使用
    public ServiceResult() {
    }

    public ServiceResult(String msg) {
        this.msg = msg;
    }

    public ServiceResult(T data) {
        this.data = data;
    }

    public ServiceResult(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 校验是否成功
     */
    public boolean isSucc() {
        return BizResultCode.SUCC.getCode().equals(code);
    }

    private static <T> ServiceResult<T> result(T data, String code, String msg) {
        return new ServiceResult().setCode(code).setMsg(msg).setData(data);
    }

    /**
     * 成功设置
     */
    public static ServiceResult succ() {
        return ServiceResult.result(null, BizResultCode.SUCC.getCode(), BizResultCode.SUCC.getMsg());
    }

    public static <T> ServiceResult<T> succ(T data) {
        return ServiceResult.result(data, BizResultCode.SUCC.getCode(), BizResultCode.SUCC.getMsg());
    }

    public static <T> ServiceResult<T> succ(T data, String msg) {
        return ServiceResult.result(data, BizResultCode.SUCC.getCode(), msg);
    }

    public static <T> ServiceResult<T> succ(T data, String code, String msg) {
        return ServiceResult.result(data, code, msg);
    }

    /**
     * 失败设置
     */
    public static ServiceResult error(String msg) {
        return ServiceResult.result(null, BizResultCode.ERR_SYSTEM.getCode(), msg);
    }

    public static ServiceResult error(String code, String msg) {
        return ServiceResult.result(null, code, msg);
    }

    public static <T> ServiceResult<T> error(T data) {
        return ServiceResult.result(data, BizResultCode.ERR_SYSTEM.getCode(), BizResultCode.ERR_SYSTEM.getMsg());
    }

    public static <T> ServiceResult<T> error(T data, String msg) {
        return ServiceResult.result(data, BizResultCode.ERR_SYSTEM.getCode(), msg);
    }

    public static <T> ServiceResult<T> error(T data, String code, String msg) {
        return ServiceResult.result(data, code, msg);
    }
}
