package com.ck.platform.id.generator.service.impl;

import com.ck.platform.id.generator.service.IdGenerationService;
import com.ck.platform.id.generator.service.SnowFlakeIdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author chenck
 * @date 2019/7/8 14:26
 */
@Component
public class IdGenerationServiceImpl implements IdGenerationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IdGenerationServiceImpl.class);
    @Autowired
    SnowFlakeIdGenerator snowFlake;

    @Override
    public Long generateId() {
        return snowFlake.nextId();
    }
}
