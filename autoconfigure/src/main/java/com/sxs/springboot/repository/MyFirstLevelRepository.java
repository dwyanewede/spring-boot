package com.sxs.springboot.repository;

import com.sxs.springboot.annotation.FirstLevelRepository;
import com.sxs.springboot.annotation.SecondLevelRepository;
import org.springframework.stereotype.Component;

/**
 * 我的 {@link FirstLevelRepository}
 *
 * @author sxs
 * @since 2018/5/14
 */
@SecondLevelRepository(value = "myFirstLevelRepository") // Bean 名称
public class MyFirstLevelRepository {
}
