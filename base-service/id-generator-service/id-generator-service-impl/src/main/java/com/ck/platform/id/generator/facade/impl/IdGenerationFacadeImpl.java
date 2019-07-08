package com.ck.platform.id.generator.facade.impl;

import com.ck.platform.common.exception.BizException;
import com.ck.platform.common.exception.BizResultCode;
import com.ck.platform.id.generator.facade.IdGenerationFacade;
import com.ck.platform.id.generator.service.IdGenerationService;
import com.tn.log.access.annotation.LogAccess;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
    private Integer prefixMaxLength;

    /**
     * 订单号后缀最大长度
     */
    @Value("${order.no.stuffix.max.length:3}")
    private Integer stuffixMaxLength;

    @Autowired
    IdGenerationService idGenerationService;

    @Override
    public Long generateId() {
        return idGenerationService.generateId();
    }

    @Override
    public String generateOrderNo(String stuffix) {
        return generateOrderNo(null, stuffix);
    }

    @Override
    public String generateOrderNo(String prefix, String stuffix) {
        if (StringUtils.isBlank(prefix)) {
            prefix = EMPTY;
        }
        if (StringUtils.isBlank(stuffix)) {
            stuffix = EMPTY;
        }
        prefix = prefix.trim();
        stuffix = stuffix.trim();

        if (prefix.length() > prefixMaxLength) {
            throw new BizException(BizResultCode.ERR_PARAM, "生成订单号时指定的前缀长度不能超过" + prefixMaxLength);
        }
        if (stuffix.length() > stuffixMaxLength) {
            throw new BizException(BizResultCode.ERR_PARAM, "生成订单号时指定的后缀长度不能超过" + stuffixMaxLength);
        }
        Long id = idGenerationService.generateId();
        // 拼接
        return prefix + String.valueOf(id) + stuffix;
    }
}
