package com.sxs.spring.boot.web.servlet;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * {@link SpringBootServletInitializer} 默认实现
 *
 * @author sxs
 * @since 2018/6/5
 */
public class DefaultSpringBootServletInitializer extends SpringBootServletInitializer {

    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        builder.sources(SpringBootServletBootstrap.class);
        return builder;
    }

}
