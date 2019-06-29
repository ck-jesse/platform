package com.ck.platform.common.dto;

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

    public DataResult<T> setData(T data) {
        this.data = data;
        return this;
    }
}
