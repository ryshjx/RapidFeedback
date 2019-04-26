package com.RapidFeedback;

import java.util.ArrayList;

public class ShortText{
	
	private String name;
	private int grade;
	private ArrayList<String> longtext = new ArrayList<String>();
	
	public String getName(){

		return name;

	}

	public void setName(String name){

		this.name = name;

	}

	public int getGrade() {

		return grade;

	}

	public void setGrade(int grade){

		this.grade = grade;

	}

	public ArrayList<String> getLongtext(){

		return longtext;

	}

	public void setLongtext(ArrayList<String> longtext){

		this.longtext = longtext;

	}

}
