package com.sxs.springboot.configuration;

import com.sxs.springboot.annotation.EnableHelloWorld;
import com.sxs.springboot.condition.ConditionalOnSystemProperty;
import org.springframework.context.annotation.Configuration;

/**
 * HelloWorld 自动装配
 *
 * @author sxs
 * @since 2018/5/15
 */
@Configuration // Spring 模式注解装配
@EnableHelloWorld // Spring @Enable 模块装配
@ConditionalOnSystemProperty(name = "user.name", value = "尚先生") // 条件装配
public class HelloWorldAutoConfiguration {
}
