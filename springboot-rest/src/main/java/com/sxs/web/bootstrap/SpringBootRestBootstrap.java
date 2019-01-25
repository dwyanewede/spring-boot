package com.sxs.web.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot Rest 引导类
 *
 * @author sxs
 * @since 2018/5/27
 */
@SpringBootApplication(scanBasePackages = {
        "com.sxs.web.controller",
        "com.sxs.web.config"
})
public class SpringBootRestBootstrap {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootRestBootstrap.class, args);
    }
}
