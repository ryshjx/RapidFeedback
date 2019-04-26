package com.RapidFeedback;

import java.util.ArrayList;
import java.util.List;

public class ProjectInfo {

    private String projectName;
    private String username;
    private String subjectName;
    private String subjectCode;
    private String description;

    private int durationMin;
    private int durationSec;
    private int warningMin;
    private int warningSec;

    private ArrayList<String> assistantList = new ArrayList<String>();
    private ArrayList<Criteria> criteriaList= new ArrayList<Criteria>();
    private ArrayList<StudentInfo> studentInfoList = new ArrayList<StudentInfo>();


    public void setProjectName(String projectName){

        this.projectName = projectName;

    }

    public void setUsername(String username){

        this.username = username;

    }

    public void setSubjectName(String subjectName){

        this.subjectName = subjectName;

    }

    public void setSubjectCode(String subjectCode){

        this.subjectCode = subjectCode;

    }

    public void setDescription(String description){

        this.description = description;

    }

    public void setAssistant(ArrayList<String> assistantList){

        this.assistantList = assistantList;
    }

    public void setCriteria(ArrayList<Criteria> criteriaList){

        this.criteriaList = criteriaList;

    }

    public void setStudentList(ArrayList<StudentInfo> studentInfoList){

        this.studentInfoList = studentInfoList;
    }

    public void setTimer(int durationMin, int durationSec, int warningMin, int warningSec){

        this.durationMin = durationMin;
        this.durationSec = durationSec;
        this.warningMin = warningMin;
        this.warningSec = warningSec;

    }

    public String getProjectName(){

        return projectName;

    }

    public String getUsername(){

        return username;

    }

    public String getSubjectName(){

        return subjectName;

    }

    public String getSubjectCode(){

        return subjectCode;

    }

    public String getDescription(){

        return description;

    }

    public ArrayList<String> getAssistant(){

        return assistantList;

    }

    public ArrayList<Criteria> getCriteria(){

        return criteriaList;

    }

    public ArrayList<StudentInfo> getStudentInfo(){

        return studentInfoList;

    }

    public int getDurationMin(){

        return durationMin;

    }

    public int getDurationSec(){

        return durationSec;

    }

    public int getWarningMin(){

        return warningMin;

    }

    public int getWarningSec(){

        return warningSec;

    }

}
