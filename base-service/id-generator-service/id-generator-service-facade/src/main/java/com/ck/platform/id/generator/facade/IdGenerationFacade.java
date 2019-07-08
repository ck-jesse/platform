package com.ck.platform.id.generator.facade;

/**
 * 分布式id生成器
 *
 * @author chenck
 * @date 2019/7/8 9:21
 */
public interface IdGenerationFacade {

    /**
     * 生成分布式ID
     * 注：可作为订单号
     *
     * @return java.lang.Long 分布式ID
     * @author chenck
     * @date 2019/7/8 15:13
     */
    Long generateId();

    /**
     * 生成订单号（生成指定后缀的订单号）
     * 注：后缀可以是具体的业务规则，如用户id的后三位，可用于做订单、用户维度的分库分表的路由规则
     *
     * @param stuffix 后缀
     * @return java.lang.String 订单号
     * @author chenck
     * @date 2019/7/8 15:05
     */
    String generateOrderNo(String stuffix);

    /**
     * 生成订单号（生成指定前缀和后缀的订单号）
     * 注：后缀可以是具体的业务规则，如用户id的后三位，可用于做订单、用户维度的分库分表的路由规则
     *
     * @param prefix  前缀
     * @param stuffix 后缀
     * @return java.lang.String 订单号
     * @author chenck
     * @date 2019/7/8 15:11
     */
    String generateOrderNo(String prefix, String stuffix);
}
