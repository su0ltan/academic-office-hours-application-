package com.sultan.lasttest.database;

import java.io.Serializable;
import java.util.List;

public class Course implements Serializable  {
    public String courseCode;
    public String courseName;
   // public String teacherUID;
    public String courseID;
    public String dptID;

    public Course(String courseCode, String courseName, String courseID, List<String> sections ,String dptID) {
        this.courseCode = courseCode;
        this.courseName = courseName;
       // this.teacherUID = teacherUID;
        this.courseID = courseID;
        this.sections = sections;
        this.dptID =dptID;
        //this.studentUID = studentUID;
    }

    public List<String> sections;




   //public List<String> studentUID;
    public Course(){
        courseName = "00";
        courseCode = "00";
      //  teacherUID = "00";
      //  studentUID = null;
        courseID = "00";

    }


    public Course(Course course){
        this.courseCode = course.courseCode;
        this.courseName = course.courseName;
        //this.teacherUID = course.teacherUID;
      //  this.studentUID = course.studentUID;
        this.courseID = course.courseID;
        this.sections = course.sections;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

/*    public void setTeacherUID(String teacherUID) {
        this.teacherUID = teacherUID;
    }*/

/*    public void setStudentUID(List<String> studentUID) {
        this.studentUID = studentUID;
    }*/

    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

/*    public String getTeacherUID() {
        return teacherUID;
    }*/
/*
    public List<String> getStudentUID() {
        return studentUID;
    }*/


}
