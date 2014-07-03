package com.po.parser;

public class ParserMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		StoryParser.getInstance().LoadStopWords();
		StoryParser.getInstance().LoadSentiment();
		
		StoryManager sM = new StoryManager();
		sM.LoadStory("data\\auStory.txt");
		
		sM.PrintStats();
	}

}
