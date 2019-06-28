package com.ck.platform.common.dto;

import lombok.Data;

/**
 * 分页请求DTO
 *
 * @author chenck
 * @date 2019/6/28 16:30
 */
@Data
public class ServicePageInput extends ServiceInput {

    /**
     * 每页大小 默认20
     */
    private Integer limit = 20;

    /**
     * 开始下标 默认0
     */
    private Integer offset = 0;

}
