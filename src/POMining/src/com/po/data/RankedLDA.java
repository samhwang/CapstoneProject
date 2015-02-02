package com.po.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import com.po.parser.Story;
import com.po.parser.StoryManager;
import com.po.parser.StoryParser;
import com.po.parser.TFIDFWorker;

class ValueComparator implements Comparator<String> {

    Map<String, Double> base;
    public ValueComparator(Map<String, Double> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with equals.    
    public int compare(String a, String b) {
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }
}

public class RankedLDA {

	protected StoryManager sM;
	Vector<Double> vecScoreOnly = new Vector<Double>(); 
	public RankedLDA()
	{
		StoryParser.getInstance().LoadStopWords();
		StoryParser.getInstance().LoadSentiment();
		StoryParser.getInstance().LoadWordMapping();
		sM = new StoryManager();
		sM.LoadStory("auStory.txt");
		sM.LoadWordStats();
		
		//TopicCompostion.getInstance().init();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		RankedLDA rLDA = new RankedLDA();
		System.out.println("Start RankedLDA...");
		
		int nMode = 0;
		if(args.length >= 1 )
			nMode = Integer.valueOf(args[0]);
		
		String strExtraArg = "";
		if(args.length >=2 )
			strExtraArg = args[1];
		
		switch (nMode){
			case 1:
				rLDA.GenerateMidFile(strExtraArg);
				break;
			case 2:
				rLDA.LDAScore();
				break;
			case 3:
				rLDA.CalculateTFIDF(strExtraArg);
				break;
			case 4:
				rLDA.LDAScoreEX();
				break;
			case 5:
				rLDA.RandomTopicLDA();
			case 6:
				rLDA.CalculatePMI();
			case 7:
				rLDA.TopicIDF();
			default:
				rLDA.LDAScoreEX();
				break;
		}
		
		System.out.println("End RankedLDA...");

	}

	private void CalculateTFIDF(String strWord)
	{
		
		TFIDFWorker.GetInstance().PrintWordStats(strWord);
	}
	
	private void GenerateMidFile(String strTopic)
	{

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
	
	private void TopicIDF()
	{
		BufferedReader br;
		try {
			Map<String, Double> mapTerms = new HashMap<String, Double>();
			ValueComparator bvc =  new ValueComparator(mapTerms);
			TreeMap<String,Double> sorted_map = new TreeMap<String,Double>(bvc);

			//br = new BufferedReader(new FileReader("data" + File.separator + "word-mapping.txt"));
			br = new BufferedReader(new FileReader("all_keys100.txt"));
			String strLine;
			while ((strLine = br.readLine()) != null) 
			{
				String[] arrWords = (strLine.split("\t")[2]).split(" ");
				for(String strWord : arrWords)
				{
					if(mapTerms.containsKey(strWord))
					{
						double count = mapTerms.get(strWord) + 1;
						mapTerms.put(strWord, count);
					}
					else
						mapTerms.put(strWord, 1.0);
				}
			}
			br.close();
			
			System.out.println(mapTerms.size());
			sorted_map.putAll(mapTerms);
			for(String key : sorted_map.keySet())
			{
				double fValue = (double)100/mapTerms.get(key);
				System.out.println(String.format("%s,%f,%f", key, mapTerms.get(key), Math.log10(fValue)));
			}
			
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void CalculatePMI()
	{
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader("all_keys100.txt"));
			String strLine;
			int n = 0;
			while ((strLine = br.readLine()) != null) 
			{
				String[] arrWords = (strLine.split("\t")[2]).split(" ");
				Vector<String> vecWord = new Vector<String>();
				Vector<String> vecAllWord = new Vector<String>();
				for(String strWord : arrWords)
				{
					if(sM.HasTopic("ALL", strWord))
					{
						vecWord.add(strWord);
					}
					vecAllWord.add(strWord);
				}
				
				System.out.println(String.format("%d, %f, %f", n, GetPMIScore(vecAllWord), GetPMIScore(vecWord)));
				n++;
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
	
	private float GetPMIScore(Vector<String> vecStr)
	{
		float fRet = 0;
		
		int i = 1;
		for(String str : vecStr)
		{
			for(int n = i; n < vecStr.size(); n++)
			{
				if(sM.getDocFrequency(str) == 0 || sM.getDocFrequency(vecStr.get(n)) == 0)
					continue;
				fRet += Math.log10(((float)sM.getDocCoFrequency(str, vecStr.get(n))+1)/((float)(sM.getDocFrequency(str)*(float)(sM.getDocFrequency(vecStr.get(n))))));
			}
			i++;
		}
		
		fRet = fRet/(float)(vecStr.size()*(vecStr.size() -1)/2);
		return Math.abs(fRet);
	}
	
	private void LDAScoreEX()
	{
		BufferedReader br;
		
		try {
			Vector<Vector<String>> vecTopic = new Vector<Vector<String>>();
			Vector<Vector<String>> vecUnsortedTopic = new Vector<Vector<String>>();
			Vector<Vector<String>> vecTopicToDocIDs = new Vector<Vector<String>>();
			Vector<Vector<String>> vecTopicTFIDF = new Vector<Vector<String>>();
			Vector<String> vecUnTopic = new Vector<String>();
			Vector<Float> vecDocWeighting = new Vector<Float>();
			br = new BufferedReader(new FileReader("all_keys100.txt"));
			String strLine;
			while ((strLine = br.readLine()) != null) 
			{
				String strTopicID = strLine.split("\t")[0];
				String[] arrWords = (strLine.split("\t")[2]).split(" ");
				Vector<String> vecWord = new Vector<String>();
				Vector<String> vecDocIDs = new Vector<String>();
				Vector<String> vecAllWord = new Vector<String>();
				Vector<String> vecExcluded = new Vector<String>();
				
				for(String strWord : arrWords)
				{
					if(sM.HasTopic("ALL", strWord))
					{
						//strWord += ":"+sM.GetUserTopicTotal().get(strWord);
						vecWord.add(strWord);
						Set<String> ids = sM.GetFullContextTopicSet(strWord);
						if(ids != null)
						{
							for(String id : ids)
							{
								if(!vecDocIDs.contains(id))
									vecDocIDs.add(id);
							}
						}
					}
					else
						vecExcluded.add(strWord);
				}
				
				double dTotalScore = 0;
				Vector<String> vecStr = new Vector<String>();
				for(String strWord : arrWords)
				{
					Set<String> setStr = sM.GetFullContextTopicSet(strWord);
					if(setStr!=null)
					{
						for(String s : setStr)
						{
							if(!vecStr.contains(s))
								vecStr.add(s);
						}
					}
					else
					{
						System.out.println("NULL topic set for :" + strWord);
					}
					vecAllWord.add(strWord);
				}
				
				CalculateTopicCoherence(vecAllWord);
				dTotalScore = TopicCompostion.getInstance().GetWeighting(vecStr, Integer.valueOf(strTopicID));
				vecAllWord.add(0, String.format("%f", dTotalScore));
				vecTopicTFIDF.add(vecAllWord);
		
				vecTopicToDocIDs.add(vecDocIDs);
				vecDocWeighting.add(TopicCompostion.getInstance().GetWeighting(vecDocIDs, Integer.valueOf(strTopicID)));
				
				if(!vecWord.isEmpty())
				{
					CalculateTopicCoherence(vecWord);
					vecWord.add(0, strTopicID);
//					double dTFIDF = 0;
//					for(String str : vecWord)
//					{
//						dTFIDF += TFIDFWorker.GetInstance().GetTFIDF(str, vecDocIDs);
//					}
//					vecNewTFIDF.add(dTFIDF);
					vecUnsortedTopic.add(vecWord);
					int i = 0;
					boolean bFound = false;
					for(Vector<String> vec : vecTopic)
					{
						if(vec.size()>=vecWord.size())
						{
							vecTopic.insertElementAt(vecWord, i);
							bFound = true;
							break;
						}
						i++;
					}
					
					if(!bFound)
						vecTopic.add(vecWord);
				}
				else
					vecUnTopic.add(strTopicID);
				
				CalculateTopicCoherence(vecExcluded);
			}
			br.close();
			
			System.out.println("TF-IDF:");
			System.out.println("*******************************************");
			int nCount = 0;
			for(Vector<String> vecStr : vecTopicTFIDF)
			{
				System.out.print(nCount + "  ");
				for(String str : vecStr)
				{
					System.out.print(str + "; ");
				}
				System.out.println();
				nCount++;
			}
			
			System.out.println("*******************************************");
			
			System.out.println("Un-matched topics:");
			System.out.println("*******************************************");
			for(String strID : vecUnTopic)
			{
				System.out.print(strID + "; ");
			}
			System.out.println();
			System.out.println("*******************************************");
			
			System.out.println("Matched Toics:");
			System.out.println("*******************************************");
			for(Vector<String> vec : vecTopic)
			{
				String strOut = "";
				for(String strWord : vec)
				{
					strOut += strWord + "; ";
				}
				System.out.println(strOut.trim());
			}
			System.out.println("*******************************************");
			
			int i = 0;
			System.out.println("Unsorted Toics:");
			System.out.println("*******************************************");
			for(Vector<String> vec : vecUnsortedTopic)
			{
				System.out.print(String.format("%f; ", vecDocWeighting.get(i)));
				i++;
				String strOut = "";
				for(String strWord : vec)
				{
					strOut += strWord + "; ";
				}
				System.out.println(strOut.trim());
			}
			System.out.println("*******************************************");
			
			System.out.println("#################################");
			for(int k = 0; k < vecScoreOnly.size(); k++)
			{
				System.out.println(String.format("%f,%f,%f", vecScoreOnly.get(k), vecScoreOnly.get(k+1), vecScoreOnly.get(k+2)));
				k+=2;
			}
			
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private void RandomTopicLDA()
	{
		BufferedReader br;
		
		try {
			Vector<Vector<String>> vecTopic = new Vector<Vector<String>>();
			Vector<Vector<String>> vecUnsortedTopic = new Vector<Vector<String>>();
			Vector<Vector<String>> vecTopicToDocIDs = new Vector<Vector<String>>();
			Vector<Vector<String>> vecTopicTFIDF = new Vector<Vector<String>>();
			Vector<String> vecUnTopic = new Vector<String>();
			Vector<Float> vecDocWeighting = new Vector<Float>();
			br = new BufferedReader(new FileReader("all_keys100.txt"));
			String strLine;
			while ((strLine = br.readLine()) != null) 
			{
				String strTopicID = strLine.split("\t")[0];
				String[] arrWords = (strLine.split("\t")[2]).split(" ");
				Vector<String> vecWord = new Vector<String>();
				Vector<String> vecDocIDs = new Vector<String>();
				Vector<String> vecAllWord = new Vector<String>();
				Vector<String> vecExcluded = new Vector<String>();
				
				int k = 0;
				for(String strWord : arrWords)
				{
					//vecAllWord.add(strWord);
					if(k < 4)
					{
						//strWord += ":"+sM.GetUserTopicTotal().get(strWord);
						vecWord.add(strWord);
						Set<String> ids = sM.GetFullContextTopicSet(strWord);
						if(ids != null)
						{
							for(String id : ids)
							{
								if(!vecDocIDs.contains(id))
									vecDocIDs.add(id);
							}
						}
					}
					else
						vecExcluded.add(strWord);
					
					k++;
				}
				
				double dTotalScore = 0;
				Vector<String> vecStr = new Vector<String>();
				for(String strWord : arrWords)
				{
					Set<String> setStr = sM.GetFullContextTopicSet(strWord);
					if(setStr!=null)
					{
						for(String s : setStr)
						{
							if(!vecStr.contains(s))
								vecStr.add(s);
						}
					}
					else
					{
						System.out.println("NULL topic set for :" + strWord);
					}
					vecAllWord.add(strWord);
				}
				
				CalculateTopicCoherence(vecAllWord);
				dTotalScore = TopicCompostion.getInstance().GetWeighting(vecStr, Integer.valueOf(strTopicID));
				vecAllWord.add(0, String.format("%f", dTotalScore));
				vecTopicTFIDF.add(vecAllWord);
		
				vecTopicToDocIDs.add(vecDocIDs);
				vecDocWeighting.add(TopicCompostion.getInstance().GetWeighting(vecDocIDs, Integer.valueOf(strTopicID)));
				
				if(!vecWord.isEmpty())
				{
					CalculateTopicCoherence(vecWord);
					vecWord.add(0, strTopicID);
//					double dTFIDF = 0;
//					for(String str : vecWord)
//					{
//						dTFIDF += TFIDFWorker.GetInstance().GetTFIDF(str, vecDocIDs);
//					}
//					vecNewTFIDF.add(dTFIDF);
					vecUnsortedTopic.add(vecWord);
					int i = 0;
					boolean bFound = false;
					for(Vector<String> vec : vecTopic)
					{
						if(vec.size()>=vecWord.size())
						{
							vecTopic.insertElementAt(vecWord, i);
							bFound = true;
							break;
						}
						i++;
					}
					
					if(!bFound)
						vecTopic.add(vecWord);
				}
				else
					vecUnTopic.add(strTopicID);
				
				CalculateTopicCoherence(vecExcluded);
			}
			br.close();
			
			System.out.println("#################################");
			for(int k = 0; k < vecScoreOnly.size(); k++)
			{
				//System.out.println(String.format("%f,%f,%f", vecScoreOnly.get(k), vecScoreOnly.get(k+1), vecScoreOnly.get(k+2)));
				
				System.out.println(String.format("%f", vecScoreOnly.get(k+1)));
				
				k+=2;
			}
			
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	private void CalculateTopicCoherence(Vector<String> vecWords)
	{
		Vector<String> vecTempString = new Vector<String>();
		for(String s : vecWords)
		{
			if(vecTempString.isEmpty())
				vecTempString.add(s);
			else
			{
				boolean bFound = false;
				for(String s1 : vecTempString)
				{
					if(sM.getDocFrequency(s) >= sM.getDocFrequency(s1))
					{
						vecTempString.add(vecTempString.indexOf(s1), s);
						bFound = true;
						break;
					}
				}
				
				if(!bFound)
					vecTempString.add(s);
			}
		}
		
		double dTopicCoherence = 0;
		//System.out.println();
		for(int i = 0; i < vecTempString.size() - 1; i++)
		{
			//System.out.print(vecTempString.get(i) + " " + sM.getDocFrequency(vecTempString.get(i)) + " ");
			
			for(int j = i + 1; j < vecTempString.size(); j++)
			{
				dTopicCoherence += Math.log10(((double)sM.getDocCoFrequency(vecTempString.get(i), vecTempString.get(j)) + 1)/((double)sM.getDocFrequency(vecTempString.get(i))*(double)sM.getDocFrequency(vecTempString.get(j))));
			}
		}
		//System.out.println();
		vecWords.add(0, String.format("[%f : %f]", dTopicCoherence, dTopicCoherence/((float)vecWords.size()*((float)vecWords.size()-1)/2)));
		vecScoreOnly.add(dTopicCoherence/((float)vecWords.size()*((float)vecWords.size()-1)/2));
	}
	
	private void LDAScore()
	{
		
		BufferedReader br;
		try {
			Vector<Vector<String>> vecTopic = new Vector<Vector<String>>();
			br = new BufferedReader(new FileReader("all_keys100.txt"));
			String strLine;
			while ((strLine = br.readLine()) != null) 
			{
				String[] arrWords = (strLine.split("\t")[2]).split(" ");
				Vector<String> vecWord = new Vector<String>();
				for(String strWord : arrWords)
				{
					if(sM.HasTopic("ALL", strWord))
					{
						strWord += ":"+sM.GetUserTopicTotal().get(strWord);
						vecWord.add(strWord);
					}
				}
				if(!vecWord.isEmpty())
				{
					int i = 0;
					boolean bFound = false;
					for(Vector<String> vec : vecTopic)
					{
						if(vec.size()>=vecWord.size())
						{
							vecTopic.insertElementAt(vecWord, i);
							bFound = true;
							break;
						}
						i++;
					}
					
					if(!bFound)
						vecTopic.add(vecWord);
				}
			}
			br.close();
			
			for(Vector<String> vec : vecTopic)
			{
				String strOut = "";
				for(String strWord : vec)
				{
					strOut += strWord + " ";
				}
				System.out.println(strOut.trim());
			}
			
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
