package com.po.data;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import java.util.Vector;

import com.po.parser.StoryManager;
import com.po.parser.StoryParser;

public class RankedLDA {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		RankedLDA rLDA = new RankedLDA();
		System.out.println("Start RankedLDA...");
		rLDA.GenerateMidFile("service");
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
