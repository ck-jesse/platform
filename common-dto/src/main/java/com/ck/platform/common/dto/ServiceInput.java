package com.ck.platform.common.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 请求DTO
 *
 * @author chenck
 * @date 2019/6/28 16:36
 */
@Data
public class ServiceInput implements Serializable {

    /**
     * 追踪id，可用于服务链路追踪
     */
    private String trace_id;

}
