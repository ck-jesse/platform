package com.ck.platform.common.dto;

import lombok.Data;

/**
 * 分页请求DTO
 * <p>
 * 建议用法：由具体业务实体继承该类
 *
 * @author chenck
 * @date 2019/11/7 10:00
 */
@Data
public class PageInput extends ServiceInput {

    /**
     * 页开始下标 默认1
     */
    private Integer pageNum = 1;

    /**
     * 每页大小 默认20
     */
    private Integer pageSize = 20;

}
