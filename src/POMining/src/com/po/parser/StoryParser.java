package com.po.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class StoryParser {


	public static final int nSentimentModeALL = 1;
	public static final int nSentimentModePositive = 2;
	public static final int nSentimentModeNegative = 3;
	
	private static StoryParser storyParser = null;
	
	private Set<String> setPositive; 
	private Set<String> setNegative;
	private Map<String, String> mapWordMapping;
	private Map<String, String> mapStateMapping;
	
	protected StoryParser()
	{
		setPositive = new HashSet<String>();
		setNegative = new HashSet<String>();
		mapWordMapping = new HashMap<String, String>();
		mapStateMapping = new HashMap<String, String>();
	}
	
	public static StoryParser getInstance()
	{
		if(storyParser == null)
			storyParser = new StoryParser();
		
		return storyParser;
	}
	
	public void LoadStopWords()
	{
		
	}
	public void LoadWordMapping()
	{
		BufferedReader br;
		try {
			//br = new BufferedReader(new FileReader("data" + File.separator + "word-mapping.txt"));
			br = new BufferedReader(new FileReader("word-mapping.txt"));
			String strLine;
			while ((strLine = br.readLine()) != null) {
				String[] arrStr = strLine.split(",");
				mapWordMapping.put(arrStr[0], arrStr[1]);
				}
			br.close();
			
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void LoadSentiment()
	{
		BufferedReader br;
		try {
			//br = new BufferedReader(new FileReader("data" + File.separator + "positive-words.txt"));
			br = new BufferedReader(new FileReader("positive-words.txt"));
			String strWord;
			while ((strWord = br.readLine()) != null) {
				setPositive.add(strWord);
				}
			br.close();
			
			//br = new BufferedReader(new FileReader("data" + File.separator + "negative-words.txt"));
			br = new BufferedReader(new FileReader("negative-words.txt"));
			while ((strWord = br.readLine()) != null) {
				setNegative.add(strWord);
				}
			br.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void LoadStateMapping(String strCountry)
	{
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader("data" + File.separator + "auState.txt"));
			String strLine;
			while ((strLine = br.readLine()) != null) {
				String[] arrStr = strLine.split(",");
				mapStateMapping.put(arrStr[0], arrStr[1]);
				}
			br.close();
			
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void PreProcessStory(Story story)
	{
		
	}
	
	public boolean IsAnySentiment(String strWord)
	{
		if(setPositive.contains(strWord) || setNegative.contains(strWord))
			return true;
		
		return false;
	}
	
	public boolean IsPositive(String strWord)
	{
		if(setPositive.contains(strWord))
			return true;
		
		return false;
	}
	
	public boolean IsNegative(String strWord)
	{
		if(setNegative.contains(strWord))
			return true;
		
		return false;
	}
	
	public String RemoveSentiment(String strLine, String strDelimiter, int nMode)
	{
		String strRet = "";
		String[] arrStr = strLine.split(strDelimiter);
		for(String strTemp : arrStr)
		{
			if(mapWordMapping.containsKey(strTemp.trim()))
				return mapWordMapping.get(strTemp.trim()).trim();
			
			switch(nMode)
			{
				case nSentimentModePositive:
				{
					if(!setPositive.contains(strTemp))
						strRet += strTemp + " ";
					break;
				}
				
				case nSentimentModeNegative:
				{
					if(!setNegative.contains(strTemp))
						strRet += strTemp + " ";
					break;
				}
				
				default:
				{
					if(!setPositive.contains(strTemp) && !setNegative.contains(strTemp))
						strRet += strTemp + " ";
					break;
				}
			}
		}
		return strRet.trim();
	}
	
	public String LookupState(String strKey)
	{
		if(mapStateMapping.containsKey(strKey))
			return mapStateMapping.get(strKey);
		
		return "";
	}
}
