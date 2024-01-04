package com.sultan.lasttest.database;

import java.io.Serializable;
import java.util.List;

public class Teacher implements Serializable {
    public String email , token;
    public String name;
    public String lastName;
    public String  floorNO;
    public String buildingNO;
    public String officeNO, deptID;;
    private String password ;
    public List<String> course ;
    public List<Integer> timeAvailable;
    public String ID;

   // public List<String> section;

    public Teacher(String email, String name, String lastName, String floorNO, String buildingNO, String officeNO, String password, List<String> course, List<Integer> timeAvailable , String deptID , String token) {
        this.email = email;
        this.name = name;
        this.lastName = lastName;
        this.floorNO = floorNO;
        this.buildingNO = buildingNO;
        this.officeNO = officeNO;
        this.password = password;
        this.course = course;
        this.timeAvailable = timeAvailable;
        this.deptID = deptID;
        this.token = token;

    }

    public Teacher() {
    }


    public List<Integer> getTimeAvailable() {
        return timeAvailable;
    }

    public void setTimeAvailable(List<Integer> timeAvailable) {
        this.timeAvailable = timeAvailable;
    }


    public void Teacher(Teacher teacher){
        this.email=teacher.email;
        this.name=teacher.name;
        this.lastName=teacher.lastName;
        this.course= teacher.course;
        this.timeAvailable = teacher.timeAvailable;

    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setCourse(List<String> course) {
        this.course = course;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public List<String> getCourse() {
        return course;
    }
}
