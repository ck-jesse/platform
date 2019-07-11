package com.ck.platform.id.generator.facade;

/**
 * 分布式id生成器
 *
 * <p>
 * 业务规则说明举例：
 * 渠道ID：长度8位，千万级
 * 商户ID：长度8到11位，8位序列号+后缀（渠道ID后3位），每一个后缀可生成的商户ID都是千万级
 * 用户ID：长度9到12位，9位序列号+后缀（商户ID后3位），每一个后缀可生成的用户ID都是亿级
 * 订单ID：
 * 规则1：长度18到21位，18位序列号+后缀（用户ID后3位），其中18位ID基于雪花算法生成(6年内为18位，往后则为19位)，如：333639455124492325+888
 * 规则2：15位时间戳+12位用户ID=27位，如：190710162908995+100000000888
 * <p>
 * 如果按照上面生成ID的定义，可以基于上面ID的 后3位 来做分库分表(如：100库10表 / 10库100表）。
 * 比如：订单表按照订单ID的 后3位 作为分库分表规则，那么渠道、商户、用户、订单这些维度的数据是落在相同的库和表中的，所以基于这些维度查询订单时非常方便。
 * 这也算是一个小技巧，当然业务方也可按自己的规则来定义分库分表，所以上述方案只是一种使用场景，可供参考。
 *
 * <p>
 * 注：每一个stuffix下生成ID的范围都是独立的（stuffix可以看作是一个命名空间）
 *
 * @author chenck
 * @date 2019/7/8 9:21
 */
public interface IdGenerationFacade {

    /**
     * 基于雪花算法 生成分布式ID
     * 注：可作为订单号
     * <p>
     * 方案一：基于雪花算法来生成唯一id
     * <p>
     * 方案二：可基于表（id字段，ip字段）来实现生产分布式id，每个ip地址对应的机器一条数据，天然避免重号的情况。
     * 1、先从表中取出id
     * 2、基于数据库表锁机制来更新id，达到生成唯一id的目的
     * （缺点：因为每个id的生成都基于数据库表，所以效率低）
     * 3、对取出来的id进行设置步长，然后在内存中通过AtomicLong来进行原子操作生成唯一id
     * （优点：基于内存生成效率高；缺点：当机器宕机时，可能会丢失一部分号段，但是相对性能来说丢失一部分号段完全可以接受）
     *
     * @author chenck
     * @date 2019/7/8 15:13
     */
    Long generateId();

    /**
     * 生成渠道id
     * <p>
     * 渠道号范围 10000000-19999999
     * 注：隐性含义 1开头用来标志渠道id
     *
     * @author chenck
     * @date 2019/7/10 17:00
     */
    String genChannelId();

    /**
     * 生成商户id
     * <p>
     * 商户号范围 20000000-29999999
     * 注：隐性含义 2开头用来标志商户id
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
     * 商户号范围 20000000stuffix-29999999stuffix；
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
     * 商户号范围 20000000stuffix-29999999stuffix ，
     * 其中每一个stuffix下商户号的范围都是 20000000-29999999（stuffix可以看作是一个命名空间）
     * <p>
     * 【使用场景举例】：
     * 后缀为渠道id的后三位，该后缀可以做为分库分表的业务规则(如：100库10表 / 10库100表），方便路由渠道和商户维度对应的库表做查询
     *
     * @param stuffix       后缀，后缀最大长度为3
     * @param appendStuffix 是否追加后缀
     * @return java.lang.String 商户id，长度为8到11位
     * @author chenck
     * @date 2019/7/10 19:42
     */
    String genMchId(String stuffix, Boolean appendStuffix);

    /**
     * 生成用户id
     * <p>
     * 用户id范围 300000000-399999999
     * 注：隐性含义 3开头用来标志用户id
     *
     * @return java.lang.String
     * @author chenck
     * @date 2019/7/11 11:23
     */
    String genUid();

    /**
     * 生成用户id
     * <p>
     * 【使用场景举例】：
     * 后缀为商户id的后三位，该后缀可以做为分库分表的业务规则(如：100库10表 / 10库100表），方便路由用户和商户维度对应的库表做查询
     *
     * @param stuffix 后缀，后缀最大长度为3
     * @return java.lang.String
     * @author chenck
     * @date 2019/7/11 11:22
     */
    String genUid(String stuffix);

    /**
     * 基于雪花算法 生成订单号（生成指定后缀的订单号）
     *
     * @param stuffix 后缀
     * @author chenck
     * @date 2019/7/8 15:05
     */
    String genOrderNo(String stuffix);

    /**
     * 基于雪花算法 生成订单号（生成指定前缀和后缀的订单号）
     * <p>
     * 【使用场景举例】：
     * 后缀为用户id的后三位，该后缀可以做为分库分表的业务规则(如：100库10表 / 10库100表），方便路由订单和用户维度对应的库表做查询
     *
     * @param prefix  前缀
     * @param stuffix 后缀
     * @author chenck
     * @date 2019/7/8 15:11
     */
    String genOrderNo(String prefix, String stuffix);
}
