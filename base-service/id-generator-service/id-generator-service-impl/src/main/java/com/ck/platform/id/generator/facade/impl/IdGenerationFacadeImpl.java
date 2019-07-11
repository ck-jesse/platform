package com.ck.platform.id.generator.facade.impl;

import com.ck.platform.common.exception.BizException;
import com.ck.platform.common.exception.BizResultCode;
import com.ck.platform.id.generator.consts.RedisKeys;
import com.ck.platform.id.generator.facade.IdGenerationFacade;
import com.ck.platform.id.generator.service.IdGenerationService;
import com.tn.log.access.annotation.LogAccess;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 分布式id生成器
 *
 * @author chenck
 * @date 2019/7/8 9:22
 */
@Service(interfaceClass = IdGenerationFacade.class)
@Component
@LogAccess
public class IdGenerationFacadeImpl implements IdGenerationFacade {

    private static final String EMPTY = "";

    /**
     * 订单号前缀最大长度
     */
    @Value("${order.no.prefix.max.length:3}")
    private Integer orderNoPrefixMaxLength;

    /**
     * 订单号后缀最大长度
     */
    @Value("${order.no.stuffix.max.length:3}")
    private Integer orderNoStuffixMaxLength;

    @Resource
    RedisTemplate<String, Long> redisTemplate;

    @Autowired
    IdGenerationService idGenerationService;

    @Override
    public Long generateId() {
        return idGenerationService.generateId();
    }

    @Override
    public String genChannelId() {
        Long channelId = redisTemplate.opsForValue().increment(RedisKeys.CHANNEL_ID_INCR, 1);
        // 生成渠道id，长度为8位（千万级）
        // 1开头用来标志渠道
        // 渠道号范围 10000000-19999999
        channelId = 10000000 + channelId % 10000000;
        return String.valueOf(channelId);
    }

    @Override
    public String genMchId() {
        return genMchId(null, false);
    }

    @Override
    public String genMchId(String stuffix) {
        return genMchId(stuffix, true);
    }

    @Override
    public String genMchId(String stuffix, Boolean appendStuffix) {
        // 生成id
        Long mchId = genId(RedisKeys.MCH_ID_INCR, stuffix, 3);

        // 生成商户id，长度为8位（千万级）
        // 2开头用来标志商户
        // 商户号范围 20000000-29999999
        mchId = 20000000 + mchId % 10000000;

        String mchIdStr = String.valueOf(mchId);
        if (null != appendStuffix && appendStuffix && StringUtils.isNotBlank(stuffix)) {
            // 给商户号拼接指定后缀，拼接后商户号长度为8到11位
            return mchIdStr + stuffix;
        }
        return mchIdStr;
    }

    /**
     * 基于redis increment 生成分布式全局唯一id
     *
     * @param key
     * @param stuffix
     * @param stuffixMaxLen
     * @return java.lang.Long
     * @author chenck
     * @date 2019/7/11 11:42
     */
    private Long genId(String key, String stuffix, int stuffixMaxLen) {
        // 生成redis key
        if (StringUtils.isNotBlank(stuffix)) {
            stuffix = stuffix.trim();
            if (stuffix.length() > stuffixMaxLen) {
                throw new BizException(BizResultCode.ERR_PARAM, "生成id的后缀长度不能超过" + stuffixMaxLen);
            }
            // 当stuffix不为空时，则可将stuffix看作是一个命名空间
            key = key + ":" + stuffix;
        }
        Long id = redisTemplate.opsForValue().increment(key, 1);
        return id;
    }

    @Override
    public String genUid() {
        return genUid(null);
    }

    @Override
    public String genUid(String stuffix) {
        // 生成id
        Long uid = genId(RedisKeys.UID_INCR, stuffix, 3);

        // 生成用户id，长度为9位（亿级）
        // 3开头用来标志用户
        // 用户id范围 300000000-399999999
        uid = 300000000 + uid % 100000000;

        String uidStr = String.valueOf(uid);
        if (StringUtils.isNotBlank(stuffix)) {
            // 给用户id拼接指定后缀，拼接后长度为9到12位
            return uidStr + stuffix;
        }
        return uidStr;
    }

    @Override
    public String genOrderNo(String stuffix) {
        return genOrderNo(null, stuffix);
    }

    @Override
    public String genOrderNo(String prefix, String stuffix) {
        if (StringUtils.isBlank(prefix)) {
            prefix = EMPTY;
        }
        if (StringUtils.isBlank(stuffix)) {
            stuffix = EMPTY;
        }
        prefix = prefix.trim();
        stuffix = stuffix.trim();

        if (prefix.length() > orderNoPrefixMaxLength) {
            throw new BizException(BizResultCode.ERR_PARAM, "生成订单号时指定的前缀长度不能超过" + orderNoPrefixMaxLength);
        }
        if (stuffix.length() > orderNoStuffixMaxLength) {
            throw new BizException(BizResultCode.ERR_PARAM, "生成订单号时指定的后缀长度不能超过" + orderNoStuffixMaxLength);
        }
        Long id = idGenerationService.generateId();
        // 拼接
        return prefix + String.valueOf(id) + stuffix;
    }
}
