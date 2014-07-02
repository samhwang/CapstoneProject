package com.po.parser;

public class StoryParser {

	private static StoryParser storyParser = null;
	
	protected StoryParser(){}
	
	public static StoryParser getInstance()
	{
		if(storyParser == null)
			storyParser = new StoryParser();
		
		return storyParser;
	}
	
	public void LoadStopWords()
	{
		
	}
	
	public void LoadSentiment()
	{
		
	}
	
	public void PreProcessStory(Story story)
	{
		
	}
}
