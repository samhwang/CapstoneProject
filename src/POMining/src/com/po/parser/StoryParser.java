package com.po.parser;

public class StoryParser {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		StoryParser sP = new StoryParser();
		StoryManager sM = new StoryManager();
		sP.LoadStopWords();
		sP.LoadSentiment();
		sM.LoadStory("auStory.txt");
	}

	void LoadStopWords()
	{
		
	}
	
	void LoadSentiment()
	{
		
	}
}
