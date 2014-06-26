package com.po.parser;

import java.util.Vector;

public class Story {

	private String strID;
	private String strTitle;
	private String strStory;
	private Vector<String> vecPositiveFeature;
	private Vector<String> vecNegativeFeature;
	
	public Story(String strID, String strTitle, String strStory)
	{
		this.strID = strID;
		this.strTitle = strTitle;
		this.strStory = strStory;
		vecPositiveFeature = new Vector<String>();
		vecNegativeFeature = new Vector<String>();
	}
	
	String GetID() {return strID;}
	String GetTitle() {return strTitle;}
	String GetStory() {return strStory;}
}
