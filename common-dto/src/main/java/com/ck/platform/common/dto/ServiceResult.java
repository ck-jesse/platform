package com.ck.platform.common.dto;


import com.alibaba.fastjson.annotation.JSONField;
import com.ck.platform.common.exception.BizResultCode;
import lombok.Data;

import java.io.Serializable;

/**
 * 响应DTO，主要用于不同服务之间的数据传递
 *
 * @author chenck
 * @date 2019/6/28 16:15
 */
@Data
public class ServiceResult implements Serializable {

    /**
     * 返回码
     */
    private String retcode = BizResultCode.SUCC.getCode();

    /**
     * 描述信息
     */
    private String retmsg = BizResultCode.SUCC.getMsg();

    // 定义构造函数，方便使用
    public ServiceResult() {
    }

    public ServiceResult(String retmsg) {
        this.retmsg = retmsg;
    }

    public ServiceResult(String retcode, String retmsg) {
        this.retcode = retcode;
        this.retmsg = retmsg;
    }

    /**
     * 校验是否成功
     */
    @JSONField(serialize = false)
    public boolean isSucc() {
        return BizResultCode.SUCC.getCode().equals(retcode);
    }

    /**
     * 设置结果
     */
    public static ServiceResult result(String code, String msg) {
        ServiceResult ServiceResult = new ServiceResult(code, msg);
        return ServiceResult;
    }

    /**
     * 设置为成功
     */
    public static ServiceResult succ() {
        return ServiceResult.result(BizResultCode.SUCC.getCode(), BizResultCode.SUCC.getMsg());
    }

    public static ServiceResult succ(String msg) {
        return ServiceResult.result(BizResultCode.SUCC.getCode(), msg);
    }

    /**
     * 设置为失败
     */
    public static ServiceResult error() {
        return ServiceResult.result(BizResultCode.ERR_SYSTEM.getCode(), BizResultCode.ERR_SYSTEM.getMsg());
    }

    public static ServiceResult error(String msg) {
        return ServiceResult.result(BizResultCode.ERR_SYSTEM.getCode(), msg);
    }

}
