package com.RapidFeedback;

import java.util.ArrayList;

public class SubSection{

	private String name;
	private ArrayList<ShortText> shortTextList = new ArrayList<ShortText>();
	
	public String getName(){

		return name;

	}

	public void setName(String name) {

		this.name = name;

	}

	public ArrayList<ShortText> getShortTextList() {

		return shortTextList;

	}

	public void setShortTextList(ArrayList<ShortText> shortTextList) {

		this.shortTextList = shortTextList;

	}
}