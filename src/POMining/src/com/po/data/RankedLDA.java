package com.po.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import java.util.Vector;

import com.po.parser.Story;
import com.po.parser.StoryManager;
import com.po.parser.StoryParser;

public class RankedLDA {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		RankedLDA rLDA = new RankedLDA();
		System.out.println("Start RankedLDA...");
		//rLDA.GenerateMidFile("");
		rLDA.LDAScore();
		System.out.println("End RankedLDA...");

	}

	void GenerateMidFile(String strTopic)
	{
		StoryParser.getInstance().LoadStopWords();
		StoryParser.getInstance().LoadSentiment();
		StoryParser.getInstance().LoadWordMapping();
		StoryManager sM = new StoryManager();
		//sM.LoadStory("data" + File.separator + "auStory.txt");
		sM.LoadStory("auStory.txt");
		
		if(strTopic.isEmpty())
		{
			int nLabel = 0;
			for(Story story : sM.getAllStories())
			{
				String strStory = "";
				String[] arrStory = story.GetRawStory().substring(6).split(" ");
				for(String strTemp : arrStory)
				{
					String tempOut = StoryParser.getInstance().RemoveStopWord(strTemp);
					if(!tempOut.isEmpty() && !tempOut.equalsIgnoreCase(strTopic))
						strStory += tempOut + " ";
				}
				
				String strOut = story.GetID() + " " + "L"+nLabel + " " + strStory;
				PrintWriter out = null;
				try
				{
				    out = new PrintWriter(new BufferedWriter(new FileWriter("storyOutAll", true)));
				    out.println(strOut);
				}
				catch(IOException ioe)
				{
				    System.err.println("IOException: " + ioe.getMessage());
				}
				finally{
				    if(out != null){
				        out.close();
				    }
				}
				nLabel++;
			}
		}
		else
		{
			Set<String> storyIDs = sM.GetIDsByTopic("ALL", strTopic);
			for(String id : storyIDs)
			{
				Vector<String> vecStoryBody = sM.GetStoryBodyAsVector(id);
				for(String strLine : vecStoryBody)
				{
					String strOut = "";
					String[] arrStr = strLine.split(" ");
					for(String tempStr : arrStr)
					{
						String tempOut = StoryParser.getInstance().RemoveStopWord(tempStr);
						if(!tempOut.isEmpty() && !tempOut.equalsIgnoreCase(strTopic))
							strOut += tempOut + " ";
					}
					
					PrintWriter out = null;
					try
					{
					    out = new PrintWriter(new BufferedWriter(new FileWriter("storyOut_"+strTopic, true)));
					    out.println(strOut);
					}
					catch(IOException ioe)
					{
					    System.err.println("IOException: " + ioe.getMessage());
					}
					finally{
					    if(out != null){
					        out.close();
					    }
					}
					
				}
			}
		}

	}
	
	void LDAScore()
	{
		BufferedReader br;
		try {
			//br = new BufferedReader(new FileReader("data" + File.separator + "word-mapping.txt"));
			br = new BufferedReader(new FileReader("all_keys100.txt"));
			String strLine;
			while ((strLine = br.readLine()) != null) {
				System.out.println(strLine);
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
