package com.po.parser;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class StoryManager {

	private Map<String, Story> mapStory;
	private Map<String, Integer> mapUserTopics;
	private Map<String, Integer> mapUserTopicsGood;
	private Map<String, Integer> mapUserTopicsBad;
	private Map<String, Integer> mapAuthorRole;
	
	private int nGoodBadCount;
	private int nGoodCount;
	private int nBadCount;
	private int nNotSpecified;
	
	public StoryManager()
	{
		mapStory = new HashMap<String, Story>();
		mapUserTopics = new HashMap<String, Integer>();
		mapUserTopicsGood = new HashMap<String, Integer>();
		mapUserTopicsBad = new HashMap<String, Integer>();
		mapAuthorRole = new HashMap<String, Integer>();
		
		nGoodBadCount = 0;
		nGoodCount = 0;
		nBadCount = 0;
		nNotSpecified = 0;
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
		
		if(mapAuthorRole.containsKey(story.GetAuthor()))
		{
			Integer nCount = mapAuthorRole.get(story.GetAuthor());
			mapAuthorRole.put(story.GetAuthor(), nCount + 1);
		}
		else
		{
			mapAuthorRole.put(story.GetAuthor(), 1);
		}
		
		if(story.getGood().size() > 0 && story.getBad().size() > 0)
			nGoodBadCount++;		
		else if(story.getGood().size() > 0 && story.getBad().size() <= 0)
			nGoodCount++;		
		else if(story.getGood().size() <= 0 && story.getBad().size() > 0)
			nBadCount++;
		else if(story.getGood().size() <= 0 && story.getBad().size() <= 0)
			nNotSpecified++;
		
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
	
	public Set<String> GetStoryIDs()
	{
		return mapStory.keySet();
	}
	
	public String GetStoryBody(String strID)
	{
		String ret = "";
		if(!mapStory.containsKey(strID))
			return "";
		
		 for(String line : mapStory.get(strID).getStory())
		 {
			 ret += line;
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
		vecSort = SortTopic(mapUserTopicsBad);
		for(String key : vecSort)
		{
			System.out.println(key + "," + mapUserTopicsBad.get(key));
		}
		
		System.out.println("======================================");
		
		System.out.println("Author Count: " + mapAuthorRole.size());
		vecSort = SortTopic(mapAuthorRole);
		for(String key : vecSort)
		{
			System.out.println(key + "," + mapAuthorRole.get(key));
		}
		
		System.out.println("======================================");
		System.out.println("Good/Bad Stories," + nGoodBadCount);
		System.out.println("Good	 Stories," + nGoodCount);
		System.out.println("Bad	 Stories," + nBadCount);
		System.out.println("NotSpecified Stories," + nNotSpecified);
	}
}
