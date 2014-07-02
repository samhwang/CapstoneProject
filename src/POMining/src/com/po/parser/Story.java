package com.po.parser;

import java.util.Vector;

public class Story {

	private String strID;
	private String strTitle;
	private String strTime;
	private String strAuthor;
	private Vector<String> vecGood;
	private Vector<String> vecBad;
	private Vector<String> vecStory;
	
	public Story(String strID, String strTitle, String strStory, String strTime, 
			String strLocation, String strAuthor, String strRelate, String strGood, String strBad)
	{
		this.strID = strID.substring(3);
		this.strTitle = strTitle.substring(6);
		this.strTime = strTime.substring(5);
		this.strAuthor = strAuthor.substring(7);
		vecGood = new Vector<String>();
		vecBad = new Vector<String>();
	}
	
	public String GetID() {return strID;}
	public String GetTitle() {return strTitle;}
	public String GetTime() {return strTime;}
	public String GetAuthor() {return strAuthor;}
	public Vector<String> getGood() {return vecGood;}
	public Vector<String> getBad() {return vecBad;}
	public Vector<String> getStory() {return vecStory;}
}
