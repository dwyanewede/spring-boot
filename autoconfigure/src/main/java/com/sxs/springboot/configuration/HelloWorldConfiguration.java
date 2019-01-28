package com.sxs.springboot.configuration;

import com.sxs.springboot.annotation.EnableHelloWorld;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * HelloWorld 配置
 *
 * @author sxs
 * @since 2018/5/14
 */
public class HelloWorldConfiguration {

    @Bean
    public String helloWorld() { // 方法名即 Bean 名称
        return "Hello,World sxs";
    }

}
