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

    public ServiceResult(String retcode, String retmsg) {
        this.retcode = retcode;
        this.retmsg = retmsg;
    }

    /**
     * 设置错误描述
     */
    @JSONField(serialize = false)
    public ServiceResult error(String retmsg) {
        this.retcode = BizResultCode.ERR_SYSTEM.getCode();
        this.retmsg = retmsg;
        return this;
    }

    /**
     * 校验是否成功
     */
    @JSONField(serialize = false)
    public boolean isSucc() {
        return BizResultCode.SUCC.getCode().equals(retcode);
    }
}
