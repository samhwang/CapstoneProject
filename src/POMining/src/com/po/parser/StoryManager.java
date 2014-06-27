package com.po.parser;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

public class StoryManager {

	private Vector<Story> vecStory;
	
	public StoryManager()
	{
		vecStory = new Vector<Story>();
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
}
