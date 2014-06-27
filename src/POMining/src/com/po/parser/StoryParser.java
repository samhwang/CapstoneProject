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
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		StoryParser.getInstance().LoadStopWords();
		StoryParser.getInstance().LoadSentiment();
		
		StoryManager sM = new StoryManager();
		sM.LoadStory("auStory.txt");
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
