package com.ck.platform.id.generator.service.impl;

import com.ck.platform.id.generator.consts.RedisKeys;
import com.ck.platform.id.generator.service.IdGenerationService;
import com.ck.platform.id.generator.service.SnowFlake;
import com.ck.platform.id.generator.service.SnowFlakeTwitter;
import org.apache.dubbo.common.utils.NetUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author chenck
 * @date 2019/7/8 14:26
 */
@Component
public class IdGenerationServiceImpl implements IdGenerationService, InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(IdGenerationServiceImpl.class);

    /**
     * 默认的数据中心id
     */
    private static final long DEFAULT_DATA_CENTER_ID = 1;

    @Resource
    private RedisTemplate<String, Integer> redisTemplate;

    private SnowFlakeTwitter snowFlake;

    @Override
    public void afterPropertiesSet() {
        // init mac id

        String localIpAddress = NetUtils.getLocalAddress().getHostAddress();
        if (localIpAddress.equals("127.0.0.1") || localIpAddress.equals("localhost")) {
            throw new RuntimeException("snowflake can not use localhost as it's mac identity,init fail! ");
        }

        // 基于redis实现机器id中心化
        String macIdRdsKey = RedisKeys.SNOWFLAKE_MAC_ID + localIpAddress;
        Integer macId = redisTemplate.opsForValue().get(macIdRdsKey);
        if (macId == null) {
            macId = Math.toIntExact(redisTemplate.opsForValue().increment(RedisKeys.SNOWFLAKE_MAC_ID_INCR, 1));
            if (macId >= SnowFlakeTwitter.MAX_MACHINE_NUM) {
                throw new RuntimeException("snowflake mac larger than " + SnowFlake.MAX_MACHINE_NUM + " ,mac id exhausted! init fail!");
            }
            LOGGER.info("ID生成器启动初始化：生成macId={}", macId);
            redisTemplate.opsForValue().set(macIdRdsKey, macId);
        } else {
            LOGGER.info("ID生成器启动初始化：已有macId={}", macId);
        }

        // 初始化ID生成器
        snowFlake = new SnowFlakeTwitter(DEFAULT_DATA_CENTER_ID, macId);
    }

    @Override
    public Long generateId() {
        return snowFlake.nextId();
    }
}
