package com.ck.platform.common.dto;


import com.alibaba.fastjson.annotation.JSONField;
import com.ck.platform.common.exception.BizResultCode;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页响应DTO，主要用于不同服务之间的数据传递
 * 建议用法：必须指定业务数据T的具体类型，如： PageResult<UserInfoDto>
 *
 * @author chenck
 * @date 2019/10/18 20:07
 */
@Data
@Accessors(chain = true)// 链式调用
public class PageResult<T> implements Serializable {
    /**
     * 页开始下标 默认1
     */
    private Integer pageNum = 1;

    /**
     * 每页大小 默认20
     */
    private Integer pageSize = 10;

    /**
     * 总记录数
     */
    private Integer total = 0;

    /**
     * 总页数
     */
    private Integer pages = 0;

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
    private List<T> dataList = new ArrayList<>();


    // 定义构造函数，方便使用
    public PageResult() {
    }

    public PageResult(String msg) {
        this.msg = msg;
    }

    public PageResult(List<T> dataList) {
        this.dataList = dataList;
    }

    public PageResult(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public PageResult(String code, String msg, Integer pageNum, Integer pageSize, Integer total) {
        this.code = code;
        this.msg = msg;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
    }

    /**
     * 校验是否成功
     */
    @JSONField(serialize = false)
    public boolean isSucc() {
        return BizResultCode.SUCC.getCode().equals(getCode());
    }

    private static <T> PageResult<T> result(List<T> dataList, String code, String msg) {
        // 链式调用
        return new PageResult().setCode(code).setMsg(msg)
                .setDataList(dataList);
    }

    private static <T> PageResult<T> result(List<T> dataList, String code, String msg, Integer pageNum, Integer pageSize, Integer total) {
        // 链式调用
        return new PageResult().setCode(code).setMsg(msg)
                .setDataList(dataList)
                .setPageNum(pageNum)
                .setPageSize(pageSize)
                .setTotal(total);
    }

    /**
     * 成功设置
     */
    public static PageResult succ() {
        return PageResult.result(null, BizResultCode.SUCC.getCode(), BizResultCode.SUCC.getMsg());
    }

    public static PageResult succ(String msg) {
        return PageResult.result(null, BizResultCode.SUCC.getCode(), msg);
    }

    public static <T> PageResult<T> succ(List<T> dataList) {
        return PageResult.result(dataList, BizResultCode.SUCC.getCode(), BizResultCode.SUCC.getMsg());
    }

    public static <T> PageResult<T> succ(List<T> dataList, String msg) {
        return PageResult.result(dataList, BizResultCode.SUCC.getCode(), msg);
    }

    public static <T> PageResult<T> succ(List<T> dataList, String code, String msg) {
        return PageResult.result(dataList, code, msg);
    }

    public static <T> PageResult<T> succ(List<T> dataList, Integer pageNum, Integer pageSize, Integer total) {
        return PageResult.result(dataList, BizResultCode.SUCC.getCode(), BizResultCode.SUCC.getMsg(), pageNum, pageSize, total);
    }

    public static <T> PageResult<T> succ(List<T> dataList, Integer pageNum, Integer pageSize, Integer total, String msg) {
        return PageResult.result(dataList, BizResultCode.SUCC.getCode(), msg, pageNum, pageSize, total);
    }

    /**
     * 失败设置
     */
    public static PageResult error(String msg) {
        return PageResult.result(null, BizResultCode.ERR_SYSTEM.getCode(), msg);
    }

    public static PageResult error(String code, String msg) {
        return PageResult.result(null, code, msg);
    }

    public static <T> PageResult<T> error(List<T> dataList) {
        return PageResult.result(dataList, BizResultCode.ERR_SYSTEM.getCode(), BizResultCode.ERR_SYSTEM.getMsg());
    }

    public static <T> PageResult<T> error(List<T> dataList, String msg) {
        return PageResult.result(dataList, BizResultCode.ERR_SYSTEM.getCode(), msg);
    }

    public static <T> PageResult<T> error(List<T> dataList, String code, String msg) {
        return PageResult.result(dataList, code, msg);
    }

}
