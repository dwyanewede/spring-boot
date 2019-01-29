package com.sxs.web.controller;

import com.sxs.web.domain.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Properties;

/**
 * User {@link RestController}
 *
 * @author sxs
 * @since 2018/5/27
 */
@RestController
public class UserRestController {

    @PostMapping(value = "/echo/user",
            consumes = "application/*;charset=UTF-8",
            produces = "application/json;charset=UTF-8")
    public User user(@RequestBody User user) {
        return user;
    }


    /**
     * 自定义媒体类型text/properties
     * @param properties
     * @return
     */
    @PostMapping(value = "/echo/prop",
            consumes = "text/properties;charset=UTF-8"
           )
    public Properties prop(@RequestBody Properties properties) {
        return properties;
    }

    /**
     * 自定义媒体类型text/user
     * @param user
     * @return
     */
    @PostMapping(value = "/echo/users",
            consumes = "text/user;charset=UTF-8"
           )
    public User users(@RequestBody User user) {
        return user;
    }

}
