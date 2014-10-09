package com.po.parser;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class StoryManager {

	private Map<String, Story> mapStory;
	private Map<String, Integer> mapUserTopics;
	private Map<String, Integer> mapUserTopicsGood;
	private Map<String, Integer> mapUserTopicsBad;
	private Map<String, Integer> mapAuthorRole;
	private Map<String, Set<String>> mapTotalTopicIndex;
	private Map<String, Set<String>> mapGoodTopicIndex;
	private Map<String, Set<String>> mapBadTopicIndex;
	
	private Map<String, Set<String>> mapALLWordsCount;
	
	private Set<String> setGood;
	private Set<String> setBad;
	
	private int nGoodBadCount;
	private int nGoodCount;
	private int nBadCount;
	private int nNotSpecified;
	
	public StoryManager()
	{
		mapStory = new HashMap<String, Story>();
		setGood = new HashSet<String>();
		setBad = new HashSet<String>();
		
		mapUserTopics = new HashMap<String, Integer>();
		mapUserTopicsGood = new HashMap<String, Integer>();
		mapUserTopicsBad = new HashMap<String, Integer>();
		mapAuthorRole = new HashMap<String, Integer>();
		
		mapTotalTopicIndex = new HashMap<String, Set<String>>();
		mapGoodTopicIndex = new HashMap<String, Set<String>>();
		mapBadTopicIndex = new HashMap<String, Set<String>>();
		
		mapALLWordsCount = new HashMap<String, Set<String>>();
		
		nGoodBadCount = 0;
		nGoodCount = 0;
		nBadCount = 0;
		nNotSpecified = 0;
	}
	
	public Map<String, Integer> GetUserTopicTotal() { return mapUserTopics; }
	public Map<String, Integer> GetUserTopicGood() { return mapUserTopicsGood; }
	public Map<String, Integer> GetUserTopicBad() { return mapUserTopicsBad; }

	public Collection<Story> getAllStories() { return mapStory.values(); }
	
	public Set<String> GetIDsByTopic(String strGoodBad, String strTopic)
	{
		if(strGoodBad.equalsIgnoreCase("All"))
		{
			if(strTopic.equalsIgnoreCase("All"))
				return mapStory.keySet();
			else
				return mapTotalTopicIndex.get(strTopic);
		}
		else if(strGoodBad.equalsIgnoreCase("Good"))
		{
			if(strTopic.equalsIgnoreCase("All"))
				return setGood;
			else
				return mapGoodTopicIndex.get(strTopic);
		}
		else if(strGoodBad.equalsIgnoreCase("Bad"))
		{
			if(strTopic.equalsIgnoreCase("All"))
				return setBad;
			else
				return mapBadTopicIndex.get(strTopic);
		}
		else return null;
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
				
				strID = strID.replace("ID:", "").trim();
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
				strRelate = strRelate.replace("Relate:", "").trim();
				
				//Good
				String strGood;
				if((strGood = br.readLine()) == null)
					break;
				
				//Bad
				String strBad;
				if((strBad = br.readLine()) == null)
					break;
				
				String strCountry = "AU";
				Story story = new Story(strID, strTitle, strStory, strTime, strLocation, strAuthor, strRelate, strGood, strBad, strCountry);
				PreProcessStory(story);
				CountAllWords(story);
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
	
	private void CountAllWords(Story story)
	{
		Vector<String> vecStory = story.getStory();
		for(String str : vecStory)
		{
			String[] s1 = str.replaceAll("[^a-zA-Z ]", "").split("\\s+");
			for(String s : s1)
			{
				if(!mapALLWordsCount.containsKey(s))
					mapALLWordsCount.put(s, new HashSet<String>());
				
				mapALLWordsCount.get(s).add(story.GetID());
			}
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
		{
			setGood.add(story.GetID());
			setBad.add(story.GetID());
			nGoodBadCount++;
		}
		else if(story.getGood().size() > 0 && story.getBad().size() <= 0)
		{
			setGood.add(story.GetID());
			nGoodCount++;		
		}
		else if(story.getGood().size() <= 0 && story.getBad().size() > 0)
		{
			setBad.add(story.GetID());
			nBadCount++;
		}
		else if(story.getGood().size() <= 0 && story.getBad().size() <= 0)
			nNotSpecified++;
		
		if(story.getGood().size() > 0)
		{
			for(String value : story.getGood())
				ProcessUserTopic(value, true, story.GetID());
		}
		
		if(story.getBad().size() > 0)
		{
			for(String value : story.getBad())
				ProcessUserTopic(value, false, story.GetID());
		}
	}
	
	private void ProcessUserTopic(String strTopic, boolean bIsGood, String strID)
	{
		if(mapUserTopics.containsKey(strTopic))
		{
			Integer nTopic = mapUserTopics.get(strTopic);
			mapUserTopics.put(strTopic, nTopic + 1);
		}
		else
		{
			mapUserTopics.put(strTopic, 1);
			mapTotalTopicIndex.put(strTopic, new HashSet<String>());
		}
		mapTotalTopicIndex.get(strTopic).add(strID);
		
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
				mapGoodTopicIndex.put(strTopic, new HashSet<String>());
			}
			mapGoodTopicIndex.get(strTopic).add(strID);
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
				mapBadTopicIndex.put(strTopic, new HashSet<String>());
			}
			mapBadTopicIndex.get(strTopic).add(strID);
		}
		
	}
	
	public static Vector<String> SortTopic(Map<String, Integer> mapTopics)
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
		
		if(!mapStory.get(strID).GetState().isEmpty())
			ret += "Location: " + mapStory.get(strID).GetState() + "\n";
		else
			ret += "NO Location Key Word Found! Full Relate Field:\n" + mapStory.get(strID).GetRelate() + "\n";
		
		ret += "=============\n\n";
		
		for(String line : mapStory.get(strID).getStory())
		{
			ret += line + ". ";
		}
		
		return ret;
	}
	
	public Vector<String> GetStoryBodyAsVector(String strID)
	{
		if(!mapStory.containsKey(strID))
			return null;
		
		return mapStory.get(strID).getStory();
	}
	
	public int GetStoryStateFlag(String strID)
	{
		return mapStory.get(strID).GetStateFlag();
	}
	
	public boolean HasTopic(String strGoodBad, String strTopic)
	{
		
		if(strGoodBad.compareToIgnoreCase("GOOD") == 0)
			return mapGoodTopicIndex.keySet().contains(strTopic);
		else if(strGoodBad.compareToIgnoreCase("BAD") == 0)
			return mapBadTopicIndex.keySet().contains(strTopic);
		else
			return mapTotalTopicIndex.keySet().contains(strTopic);
	}
	
	public void LoadWordStats()
	{
		for(Story story : mapStory.values())
		{
			TFIDFWorker.GetInstance().AddWordAsStory(story.GetRawStory(), story.GetID());
		}
		
		TFIDFWorker.GetInstance().PostProcess(mapStory.size());
	}
	
	public Set<String> GetTopicSet(String strTopic)
	{
		if(mapTotalTopicIndex.keySet().contains(strTopic))
		{
			return mapTotalTopicIndex.get(strTopic);
		}
		else
			return null;
	}
	
	public Set<String> GetFullContextTopicSet(String strTopic)
	{
		if(mapALLWordsCount.keySet().contains(strTopic))
			return mapALLWordsCount.get(strTopic);
		else
			return null;
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
