package com.sultan.lasttest.database;

import java.io.Serializable;
import java.util.List;

public class Student implements Serializable {
    public String email,name,lastName,studentID , deptID ,  token;

    public List<String> course;

    public Student(String email, String name, String lastName, String studentID, List<String> course, List<String> section ,String deptID , String token) {
        this.email = email;
        this.name = name;
        this.lastName = lastName;
        this.studentID = studentID;
        this.course = course;
        this.section = section;
        this.token = token;
        this.deptID=deptID;
    }

    public List<String> section;





    public void Student(Student student){
        this.email=student.email;
        this.name=student.name;
        this.lastName=student.lastName;
        this.studentID=student.studentID;
        this.course= student.course;
        this.section = student.section;
        this.deptID = student.deptID;
        this.token = student.token;


    }

    public Student(){}


}
