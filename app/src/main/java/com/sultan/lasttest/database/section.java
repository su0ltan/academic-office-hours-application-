package com.sultan.lasttest.database;

import java.io.Serializable;
import java.util.List;

public class section implements Serializable {

    public String ID;

    public List<String>studentUID;
    public String courseID;

    public  String teacherUID;
    public section(){}



    public section(String courseID, List<String> studentUID, String teacherUID) {
        this.courseID = courseID;
        this.studentUID = studentUID;
        this.teacherUID = teacherUID;
    }

;
    public void section(section section){
        this.courseID = section.courseID;
        this.teacherUID = section.teacherUID;
    }

}
