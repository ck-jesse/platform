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

    public static String SNOWFLAKE_MAC_ID_INCR = ID_GENERATION_PREFIX + "mac_num_incr";

    public static String SNOWFLAKE_MAC_ID = ID_GENERATION_PREFIX + "ip.";

    /**
     * 渠道id自增key
     */
    public static String CHANNEL_ID_INCR = ID_GENERATION_PREFIX + "channel:incr";

    /**
     * 商户id自增key
     */
    public static String MCH_ID_INCR = ID_GENERATION_PREFIX + "mch:incr";
}
