package com.po.parser;

import java.util.Vector;

public class Story {

	private String strID;
	private String strTitle;
	private String strTime;
	private String strAuthor;
	private String strState;
	private String strCountry;
	private String strRelate;
	private String strRawStory;
	private int nStateFlag;
	private Vector<String> vecGood;
	private Vector<String> vecBad;
	private Vector<String> vecStory;
	
	public static final int AU_STATE_FLAG_NSW = 0x01;
	public static final int AU_STATE_FLAG_VIC = 0x02;
	public static final int AU_STATE_FLAG_ACT = 0x04;
	public static final int AU_STATE_FLAG_TAS = 0x08;
	public static final int AU_STATE_FLAG_QLD = 0x10;
	public static final int AU_STATE_FLAG_SA = 0x20;
	public static final int AU_STATE_FLAG_NT = 0x40;
	public static final int AU_STATE_FLAG_WA = 0x80;
	
	private final String AU_STATE_NSW = "NSW";
	private final String AU_STATE_VIC = "VIC";
	private final String AU_STATE_ACT = "ACT";
	private final String AU_STATE_TAS = "TAS";
	private final String AU_STATE_QLD = "QLD";
	private final String AU_STATE_SA = "SA";
	private final String AU_STATE_NT = "NT";
	private final String AU_STATE_WA = "WA";
	
	public Story(String strID, String strTitle, String strStory, String strTime, 
			String strLocation, String strAuthor, String strRelate, String strGood, String strBad, String strCountry)
	{
		strState = "";
		this.strID = strID;
		this.strCountry = strCountry;
		this.strRelate = strRelate;
		this.strTitle = strTitle.substring(6);
		this.strTime = strTime.substring(5);
		this.strAuthor = strAuthor.substring(7);
		vecGood = new Vector<String>();
		vecBad = new Vector<String>();
		vecStory = new Vector<String>();
		
		String[] arrGood = strGood.substring(5).split(";");
		for(String str : arrGood)
		{
			if(!str.trim().isEmpty())
			{
				String newString = StoryParser.getInstance().RemoveSentiment(str.trim().toLowerCase(), " ", StoryParser.nSentimentModeALL);
				if(!newString.trim().isEmpty())
					vecGood.add(newString);
			}
		}
		
		String[] arrBad = strBad.substring(4).split(";");
		for(String str : arrBad)
		{
			if(!str.trim().isEmpty())
			{
				String newString = StoryParser.getInstance().RemoveSentiment(str.trim().toLowerCase(), " ", StoryParser.nSentimentModeALL);
				if(!newString.trim().isEmpty())
					vecBad.add(newString);
			}
		}
		
		strRawStory = strStory;
		String[] arrStory = strStory.substring(6).split("\\. ");
		for(String str : arrStory)
		{
			if(!str.trim().isEmpty())
			{
				vecStory.add(str.trim());
			}
		}
		
		ParseState(strRelate);
		SetStateFlag();
	}
	
	private void ParseState(String strRelate)
	{
		if(strRelate.isEmpty())
			return;
		if(strCountry.equalsIgnoreCase("AU"))
		{
			String[] strArr = strRelate.split(";");
			for(String str : strArr)
			{
				if(str.equalsIgnoreCase(AU_STATE_NSW)
					||str.equalsIgnoreCase(AU_STATE_VIC)
					||str.equalsIgnoreCase(AU_STATE_ACT)
					||str.equalsIgnoreCase(AU_STATE_TAS)
					||str.equalsIgnoreCase(AU_STATE_QLD)
					||str.equalsIgnoreCase(AU_STATE_SA)
					||str.equalsIgnoreCase(AU_STATE_NT)
					||str.equalsIgnoreCase(AU_STATE_WA))
				{
					strState = str.toUpperCase();
					return;
				}
			}
		}
	}
	
	private void SetStateFlag()
	{
		if(strState.isEmpty())
			return;
		
		switch(strState)
		{
		case AU_STATE_NSW:
			nStateFlag = AU_STATE_FLAG_NSW;
			break;
		case AU_STATE_VIC:
			nStateFlag = AU_STATE_FLAG_VIC;
			break;
		case AU_STATE_ACT:
			nStateFlag = AU_STATE_FLAG_ACT;
			break;
		case AU_STATE_TAS:
			nStateFlag = AU_STATE_FLAG_TAS;
			break;
		case AU_STATE_QLD:
			nStateFlag = AU_STATE_FLAG_QLD;
			break;
		case AU_STATE_SA:
			nStateFlag = AU_STATE_FLAG_SA;
			break;
		case AU_STATE_NT:
			nStateFlag = AU_STATE_FLAG_NT;
			break;
		case AU_STATE_WA:
			nStateFlag = AU_STATE_FLAG_WA;
			break;
		default:
			nStateFlag = 0;
		}
	}
	
	public String GetID() 		{ return strID; }
	public String GetTitle() 	{ return strTitle; }
	public String GetTime() 	{ return strTime; }
	public String GetAuthor() 	{ return strAuthor; }
	public String GetCountry() 	{ return strCountry; }
	public String GetState() 	{ return strState; }
	public String GetRelate() 	{ return strRelate; }
	public String GetRawStory()	{ return strRawStory; }
	public Vector<String> getGood() {return vecGood;}
	public Vector<String> getBad() 	{return vecBad;}
	public Vector<String> getStory() {return vecStory;}
	public int GetStateFlag() 	{ return nStateFlag; }
}
