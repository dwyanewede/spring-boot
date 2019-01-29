package com.sxs.web.domain;

import java.io.Serializable;

/**
 *  用户模型
 *
 * @author sxs
 * @since 2018/5/27
 */
public class User implements Serializable {

    private static final long serialVersionUID = 4745971306038921362L;

    private Long id;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
