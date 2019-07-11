package com.ck.platform.id.generator.consts;

/**
 * @author chenck
 * @date 2019/7/8 14:36
 */
public class RedisKeys {

    /**
     * ID generation
     */
    public final static String ID_GENERATION_PREFIX = "id:generator:";

    /**
     * 雪花算法 用于记录当前已经分配的机器id值，新的机器进来时的id为该值+1
     */
    public static String SNOWFLAKE_MAC_ID_INCR = ID_GENERATION_PREFIX + "snowflake:mac_num_incr";

    /**
     * 雪花算法 存放服务所在机器对应的id
     */
    public static String SNOWFLAKE_MAC_ID = ID_GENERATION_PREFIX + "snowflake:ip.";

    /**
     * 渠道id自增key
     */
    public static String CHANNEL_ID_INCR = ID_GENERATION_PREFIX + "channelid:incr";

    /**
     * 商户id自增key
     */
    public static String MCH_ID_INCR = ID_GENERATION_PREFIX + "mchid:incr";

    /**
     * 用户id自增key
     */
    public static String UID_INCR = ID_GENERATION_PREFIX + "uid:incr";

}
