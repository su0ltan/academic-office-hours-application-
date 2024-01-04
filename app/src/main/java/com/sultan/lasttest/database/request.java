package com.sultan.lasttest.database;

import java.io.Serializable;

public class request implements Serializable {



    public String courseID;
    public String reqID;
    public String studentID;
    public String teacherID;
    public String time;
    public String status;
    public String date;
    public String problem;
    public String reason;


    public request(String courseID, String reqID, String studentID, String teacherID, String time, String status, String Date, String problem, String reason) {
        this.courseID = courseID;
        this.reqID = reqID;
        this.studentID = studentID;
        this.teacherID = teacherID;
        this.time = time;
        this.status = status;
        this.date = Date;
        this.problem = problem;
        this.reason = reason;
    }

    public String ID;



    public request(){}
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        date = date;
    }


    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String CourseID) {
        this.courseID = CourseID;
    }

    public String getReqID() {
        return reqID;
    }

    public void setReqID(String reqID) {
        this.reqID = reqID;
    }

    public request(request r){
        studentID = r.studentID;
        teacherID = r.teacherID;
       time = r.time;
        status = r.status;
        reqID=r.reqID;
        courseID =r.courseID;
        date = r.date;
        problem=r.problem;
    }

    public String getProblem() {
        return problem;
    }

    public String getReason() {
        return reason;
    }

    public String getID() {
        return ID;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        studentID = studentID;
    }

    public String getTeacherID() {
        return teacherID;
    }

    public void setTeacherID(String teacherID) {
        teacherID = teacherID;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
