package com.po.parser;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class StoryManager {

	private Map<String, Story> mapStory;
	private Map<String, Integer> mapUserTopics;
	private Map<String, Integer> mapUserTopicsGood;
	private Map<String, Integer> mapUserTopicsBad;
	private Map<String, Integer> mapAuthorRole;
	
	public StoryManager()
	{
		mapStory = new HashMap<String, Story>();
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
				mapStory.put(strID, story);
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
		if(story.getGood().size() > 0)
		{
			for(String value : story.getGood())
				ProcessUserTopic(value, true);
		}
		
		if(story.getBad().size() > 0)
		{
			for(String value : story.getBad())
				ProcessUserTopic(value, false);
		}
	}
	
	private void ProcessUserTopic(String strTopic, boolean bIsGood)
	{
		if(mapUserTopics.containsKey(strTopic))
		{
			Integer nTopic = mapUserTopics.get(strTopic);
			mapUserTopics.put(strTopic, nTopic + 1);
		}
		else
		{
			mapUserTopics.put(strTopic, 1);
		}
		
		if(bIsGood)
		{
			if(mapUserTopicsGood.containsKey(strTopic))
			{
				Integer nTopic = mapUserTopicsGood.get(strTopic);
				mapUserTopicsGood.put(strTopic, nTopic + 1);
			}
			else
			{
				mapUserTopicsGood.put(strTopic, 1);
			}
		}
		else
		{
			if(mapUserTopicsBad.containsKey(strTopic))
			{
				Integer nTopic = mapUserTopicsBad.get(strTopic);
				mapUserTopicsBad.put(strTopic, nTopic + 1);
			}
			else
			{
				mapUserTopicsBad.put(strTopic, 1);
			}
		}
		
	}
	
	private Vector<String> SortTopic(Map<String, Integer> mapTopics)
	{
		Vector<String> ret = new Vector<String>();
		
		for(String key : mapTopics.keySet())
		{
			if(ret.isEmpty())
				ret.add(key);
	
			else
			{
				if(mapTopics.get(key).intValue() <= mapTopics.get(ret.lastElement()).intValue())
				{
					ret.add(key);
					continue;
				}
				
				if(mapTopics.get(key).intValue() >= mapTopics.get(ret.firstElement()).intValue())
				{
					ret.insertElementAt(key, 0);
					continue;
				}
				
				for(int i = 0; i < ret.size(); i++)
				{
					if(mapTopics.get(key).intValue() > mapTopics.get(ret.get(i)).intValue())
					{
						ret.insertElementAt(key, i);
						break;
					}
				}
			}
		}
		
		return ret;
	}
	
	public void PrintStats()
	{
		System.out.println("Story Manager Dump Story Info.....");
		System.out.println("Story Count: " + mapStory.size());
		
		System.out.println("======================================");
		System.out.println("Topic Count: " + mapUserTopics.size());
		Vector<String> vecSort = SortTopic(mapUserTopics);
		for(String key : vecSort)
		{
			System.out.println(key + "," + mapUserTopics.get(key));
		}
			
		System.out.println("======================================");
		
		System.out.println("Good Topic Count: " + mapUserTopicsGood.size());
		vecSort = SortTopic(mapUserTopicsGood);
		for(String key : vecSort)
		{
			System.out.println(key + "," + mapUserTopicsGood.get(key));
		}
		
		
		System.out.println("======================================");
		
		System.out.println("Bad Topic Count: " + mapUserTopicsBad.size());
		Vector<String> vecSort3 = SortTopic(mapUserTopicsBad);
		for(String key : vecSort3)
		{
			System.out.println(key + "," + mapUserTopicsBad.get(key));
		}
	}
}
