package com.ck.platform.id.generator.config;

import com.ck.platform.id.generator.consts.RedisKeys;
import com.ck.platform.id.generator.service.SnowFlakeIdGenerator;
import com.ck.platform.id.generator.util.DockerUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 保证机器ID的全局唯一性
 * 注：因为产号服务必定要上容器，而上容器后，如果发布或重启，则IP会发生变化，所以需要针对这种情况来提供一种方案来保证机器ID全局唯一
 * <p>
 * TODO 暂不放开，后续有需求再放开
 *
 * @author chenck
 * @date 2019/11/20 10:30
 */
@Component
@Slf4j
public class MachineIdGenConfig {

    /**
     * 时间戳占用的位数
     */
    @Value("${snowflake.bits.timestamp:29}")
    private Integer timestampBits;
    /**
     * 机器标识占用的位数
     */
    @Value("${snowflake.bits.machineId:21}")
    private Integer machineIdBits;
    /**
     * 列号占用的位数
     */
    @Value("${snowflake.bits.sequence:13}")
    private Integer sequenceBits;

    @Value("${server.port}")
    private Integer serverPort;

    @Autowired
    RedisTemplate<String, Long> redisTemplate;

    /**
     * 获取ip地址
     */
    private String getIPAddress() throws UnknownHostException {
        InetAddress address = InetAddress.getLocalHost();
        return address.getHostAddress();
    }

    /**
     * 定义雪花算法ID生成器
     * 注：含机器id的自动生成
     */
    @Bean
    public SnowFlakeIdGenerator snowFlake() throws UnknownHostException {
        String host = null;
        int port = 0;

        // 检测是不是docker容器环境
        boolean isDocker = DockerUtils.isDocker();
        if (isDocker) {
            host = DockerUtils.getDockerHost();
            port = Integer.parseInt(DockerUtils.getDockerPort());
        } else {
            host = getIPAddress();
            port = serverPort;
        }

        // 获取机器ID
        Long machineId = getMachineId(isDocker, host, port);

        return new SnowFlakeIdGenerator(timestampBits, machineIdBits, sequenceBits, Math.toIntExact(machineId));
    }

    /**
     * 获取一个机器id
     * 注：ip+port对应唯一一个机器id
     * <p>
     * 基于redis来实现机器id自动化、中心化；
     */
    private Long getMachineId(boolean isDocker, String ip, int port) {
        // 基于redis实现机器id中心化
        String macIdRdsKey = RedisKeys.SNOWFLAKE_MAC_ID + ip + "." + port + "." + (isDocker ? "container" : "actual");
        Long machineId = redisTemplate.opsForValue().get(macIdRdsKey);
        if (machineId != null) {
            log.info("重复使用机器id, 生成 {} = {}", macIdRdsKey, machineId);
            return machineId;
        }
        machineId = redisTemplate.opsForValue().increment(RedisKeys.SNOWFLAKE_MAC_ID_INCR, 1);
        log.info("初始化机器id成功, 生成 {} = {}", macIdRdsKey, machineId);
        redisTemplate.opsForValue().set(macIdRdsKey, machineId);
        return machineId;
    }

}
