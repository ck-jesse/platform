package com.ck.platform.id.generator;

import com.tn.log.access.config.EnableLogAccess;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ID生成器 启动类
 *
 * @author chenck
 * @date 2019/7/8 15:37
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.ck.platform.id.generator"})
@RestController
//@EnableCaching
@EnableLogAccess
public class IdGenerationBootstrap {

    /**
     * 定义该方法的目的是为了方便进行http访问测试
     */
    @RequestMapping(value = "/")
    public String index() {
        return "Hi, This is id generator service!";
    }

    public static void main(String[] args) {
        System.setProperty("jute.maxbuffer", 8192 * 1024 + "");
        // 解决启动时报错：No such any registry to refer service in consumer 10.1.6.48 use dubbo version 2.6.4
        System.setProperty("java.net.preferIPv4Stack", "true");
        SpringApplication.run(IdGenerationBootstrap.class, args);
    }

}
