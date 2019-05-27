package com.sxs.web.controller;

import com.alibaba.fastjson.JSON;
import com.sxs.web.mapper.CourseMapper;
import com.sxs.web.model.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Collection;

/**
 * @ClassName: CourseController
 * @Description: Course数据测试
 * @Author: 尚先生
 * @CreateDate: 2019/5/22 17:29
 * @Version: 1.0
 */
@RestController
public class CourseController {

    @Autowired
    private CourseMapper courseMapper;

    @GetMapping(value = "/find-all-courses",produces = {"application/json;charset=UTF-8"})
    public Collection<Course> findAllCourses(){

        Collection<Course> courses1 = courseMapper.findAllCourses();

        for (Course course : courses1){
            course.setCurrentThreadName(Thread.currentThread().getName());
        }
        return courses1;
    }

    @GetMapping(value = "/find-single-course",produces = {"application/json;charset=UTF-8"})
    public String findSingleCourse(){
        Course course = courseMapper.findSingleCourse();
        course.setCurrentThreadName(Thread.currentThread().getName());
        return JSON.toJSONString(course);
    }

}
