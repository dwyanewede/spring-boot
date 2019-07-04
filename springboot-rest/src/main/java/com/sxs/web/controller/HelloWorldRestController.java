package com.sxs.web.controller;

import com.sxs.web.config.TestConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * HelloWorld {@link RestController} 实现
 *
 * @author sxs
 * @since 2018/5/27
 */
@RestController
public class HelloWorldRestController {

    @Autowired
    private TestConfiguration configuration;

    @GetMapping(value = "/hello-world")
    public String helloWorld(@RequestParam(required = false) String message) {

        return "Hello,World! : " + message;
    }

//    @CrossOrigin("*")
    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping(value = "/test-config")
    public String testConfiguration(@RequestParam(required = false) String message) {

        return configuration.toString();
    }
}
