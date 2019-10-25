package com.ck.platform.common.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.ck.platform.common.exception.BizResultCode;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页数据响应DTO
 *
 * @author chenck
 * @date 2019/6/28 16:22
 */
@Data
public class ServicePageResult<T> extends ServiceResult {

    /**
     * 每页大小
     */
    private Integer limit = 20;
    /**
     * 开始下标
     */
    private Integer offset = 0;
    /**
     * 总记录数
     */
    private Integer total = 0;

    /**
     * 分页数据
     */
    private List<T> dataList = new ArrayList<T>();

    // 定义构造函数，方便使用
    public ServicePageResult() {
        super();
    }

    public ServicePageResult(String retcode, String retmsg) {
        super(retcode, retmsg);
    }

    public ServicePageResult(ServicePageInput input) {
        super();
        init(input);
    }

    /**
     * 根据输入Dto初始化输出Dto
     */
    @JSONField(serialize = false)
    public void init(ServicePageInput pageInput) {
        this.setLimit(pageInput.getLimit());
        this.setOffset(pageInput.getOffset());
    }

    /**
     * 设置结果
     */
    public static <T> ServicePageResult<T> result(List<T> dataList, String code, String msg) {
        ServicePageResult dataResult = new ServicePageResult(code, msg);
        dataResult.setDataList(dataList);
        return dataResult;
    }

    /**
     * 设置为成功
     */
    public static ServicePageResult succ() {
        return ServicePageResult.result(null, BizResultCode.SUCC.getCode(), BizResultCode.SUCC.getMsg());
    }

    public static ServicePageResult succ(String msg) {
        return ServicePageResult.result(null, BizResultCode.SUCC.getCode(), msg);
    }

    public static <T> ServicePageResult<T> succ(List<T> dataList) {
        return ServicePageResult.result(dataList, BizResultCode.SUCC.getCode(), BizResultCode.SUCC.getMsg());
    }

    /**
     * 设置为失败
     */
    public static ServicePageResult error() {
        return ServicePageResult.result(null, BizResultCode.ERR_SYSTEM.getCode(), BizResultCode.ERR_SYSTEM.getMsg());
    }

    public static ServicePageResult error(String msg) {
        return ServicePageResult.result(null, BizResultCode.ERR_SYSTEM.getCode(), msg);
    }

    public static <T> ServicePageResult<T> error(List<T> dataList) {
        return ServicePageResult.result(dataList, BizResultCode.ERR_SYSTEM.getCode(), BizResultCode.ERR_SYSTEM.getMsg());
    }
}
