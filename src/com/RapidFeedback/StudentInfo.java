package com.RapidFeedback;

public class StudentInfo {

    private String number;
    private String firstName;
    private String middleName;
    private String surname;
    private String email;
    private Double mark;
    private int group;


    public StudentInfo(String number, String firstName, String middleName,
                           String surname, String email){

        this.number = number;
        this.firstName = firstName;
        this.middleName = middleName;
        this.surname = surname;
        this.email = email;

    }

    public void setMark(Double mark){

        this.mark = mark;

    }


    public void setGroup(int group){

        this.group = group;

    }

    public String getNumber(){

        return number;

    }

    public String getFirstName(){

        return firstName;

    }

    public String getMiddleName(){

        return middleName;

    }

    public String getSurname(){

        return surname;

    }

    public String getEmail(){

        return email;

    }

    public Double getMark(){

        return mark;

    }

    public int getGroup(){

        return group;

    }
}