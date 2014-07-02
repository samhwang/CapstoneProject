package com.po.parser;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class StoryManager {

	private Vector<Story> vecStory;
	private Map<String, Integer> mapUserTopics;
	private Map<String, Integer> mapUserTopicsGood;
	private Map<String, Integer> mapUserTopicsBad;
	private Map<String, Integer> mapAuthorRole;
	
	public StoryManager()
	{
		vecStory = new Vector<Story>();
		mapUserTopics = new HashMap<String, Integer>();
		mapUserTopicsGood = new HashMap<String, Integer>();
		mapUserTopicsBad = new HashMap<String, Integer>();
		mapAuthorRole = new HashMap<String, Integer>();
	}
	
	public void LoadStory(String strFilePath)
	{
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(strFilePath));
			while (true) {
				
				//ID
				String strID; 
				if((strID = br.readLine()) == null)
					break;
				
				//Title
				String strTitle;
				if((strTitle = br.readLine()) == null)
					break;
				
				//Time
				String strTime;
				if((strTime = br.readLine()) == null)
					break;
				
				//Location
				String strLocation;
				if((strLocation = br.readLine()) == null)
					break;
				
				//Author
				String strAuthor;
				if((strAuthor = br.readLine()) == null)
					break;
				
				//Story
				String strStory;
				if((strStory = br.readLine()) == null)
					break;
				
				//Relate
				String strRelate;
				if((strRelate = br.readLine()) == null)
					break;
				
				//Good
				String strGood;
				if((strGood = br.readLine()) == null)
					break;
				
				//Bad
				String strBad;
				if((strBad = br.readLine()) == null)
					break;
				
				Story story = new Story(strID, strTitle, strStory, strTime, strLocation, strAuthor, strRelate, strGood, strBad);
				PreProcessStory(story);
				StoryParser.getInstance().PreProcessStory(story);
				vecStory.add(story);
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
	
	private void PreProcessStory(Story story)
	{
		
	}
	
	public void PrintStats()
	{
		System.out.println("Story Manager Dump Story Info.....");
		System.out.println("Story Count: " + vecStory.size());
	}
}
