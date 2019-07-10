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
     * @author chenck
     * @date 2019/7/8 15:13
     */
    Long genUniqueId();

    /**
     * 生成渠道id
     *
     * @author chenck
     * @date 2019/7/10 17:00
     */
    String genChannelId();

    /**
     * 生成商户id
     * <p>
     * 商户号的范围 20000000-29999999
     *
     * @return java.lang.String 商户id，长度为8
     * @author chenck
     * @date 2019/7/10 19:47
     */
    String genMchId();

    /**
     * 生成商户id
     * <p>
     * 当stuffix不为空时，默认将stuffix追加到商户号上；
     * 商户号的范围 20000000stuffix-29999999stuffix；
     *
     * @param stuffix 后缀，后缀最大长度为3
     * @return java.lang.String 商户id，长度为8到11位
     * @author chenck
     * @date 2019/7/10 17:01
     */
    String genMchId(String stuffix);

    /**
     * 生成商户id
     * <p>
     * 商户号的范围 20000000stuffix-29999999stuffix ，
     * 其中每一个stuffix下商户号的范围都是 20000000-29999999（stuffix可以看作是一个命名空间）
     * <p>
     * 注：
     * 【使用场景举例】：后缀为渠道id的后三位，该后缀可以做为分库分表的业务规则(如：100库10表 / 10库100表），方便路由渠道和商户维度对应的库表做查询
     *
     * @param stuffix       后缀，后缀最大长度为3
     * @param appendStuffix 是否追加后缀
     * @return java.lang.String 商户id，长度为8到11位
     * @author chenck
     * @date 2019/7/10 19:42
     */
    String genMchId(String stuffix, Boolean appendStuffix);

    /**
     * 生成订单号（生成指定后缀的订单号）
     * 注：后缀可以是具体的业务规则，如用户id的后三位，可用于订单、用户维度的分库分表的路由规则
     *
     * @param stuffix 后缀
     * @author chenck
     * @date 2019/7/8 15:05
     */
    String genOrderNo(String stuffix);

    /**
     * 生成订单号（生成指定前缀和后缀的订单号）
     * 注：后缀可以是具体的业务规则，如用户id的后三位，可用于订单、用户维度的分库分表的路由规则
     *
     * @param prefix  前缀
     * @param stuffix 后缀
     * @author chenck
     * @date 2019/7/8 15:11
     */
    String genOrderNo(String prefix, String stuffix);
}
