package com.sxs.web.model;

import java.io.Serializable;

/**
 * @ClassName: Course
 * @Description: 课程实体
 * @Author: 尚先生
 * @CreateDate: 2019/5/22 17:22
 * @Version: 1.0
 */

public class Course implements Serializable {

    private static final long serialVersionUID = 1848918888438459623L;
    private String course_name;
    private String stu_name;
    private double score;
    private String currentThreadName;

    @Override
    public String toString() {
        return "Course{" +
                "course_name='" + course_name + '\'' +
                ", stu_name='" + stu_name + '\'' +
                ", score=" + score +
                ", currentThreadName='" + currentThreadName + '\'' +
                '}';
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getStu_name() {
        return stu_name;
    }

    public void setStu_name(String stu_name) {
        this.stu_name = stu_name;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getCurrentThreadName() {
        return currentThreadName;
    }

    public void setCurrentThreadName(String currentThreadName) {
        this.currentThreadName = currentThreadName;
    }
}
