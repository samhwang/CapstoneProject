package com.po.parser;

import java.io.File;

public class ParserMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		StoryParser.getInstance().LoadStopWords();
		StoryParser.getInstance().LoadSentiment();
		StoryParser.getInstance().LoadWordMapping();
		StoryManager sM = new StoryManager();
		sM.LoadStory("data" + File.separator + "auStory.txt");

		sM.PrintStats();
	}

}
