package com.sxs.web.mapper;

import com.sxs.web.model.Course;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * @ClassName: CourseMapper
 * @Description: java类作用描述
 * @Author: 尚先生
 * @CreateDate: 2019/5/22 17:24
 * @Version: 1.0
 */
@Mapper
@Repository
public interface CourseMapper {

    @Select("select * from tb_score limit 2 offset 1")
    Collection<Course> findAllCourses();

    @Select("select * from tb_score limit 1 offset 1")
    Course findSingleCourse();
}
