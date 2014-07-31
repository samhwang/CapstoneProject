package com.po.crawler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler {

	public static final int ModeID = 1;
	public static final int ModeStory = 2;
	public static final int ModeSinglePageForID = 3;
	
	private final String strIDFileSuffix = "IDs.txt";
	private final String strStoryFileSuffix = "Story.txt";
	private final String strBaseURL = "https://www.patientopinion.org.";
	private final String strPageNumber = "/opinions/searchresults?page=";
	private final String strOpinions = "/opinions/";
	
	private final int SleepTime = 60000;
	
	private String strEntryURL;
	private String strOutFile;
//	private String strRegion;
	private Set<String> setOpinionIDs;
	
	public void run(int nMode, String strRegion, int nStartSeq)
	{
	    System.getProperties().put("proxySet", "true");
	    System.getProperties().put("proxyHost", "webironport2");
	    System.getProperties().put("proxyPort", "80");
	    
		System.out.println("Crawler Start..." + "Mode:" + nMode + " Region: " + strRegion);
		if(!strRegion.equals("au") && !strRegion.equals("uk"))
		{
			System.out.println("Region: " + strRegion + " Not supported");
			return;
		}
		strEntryURL = strBaseURL+strRegion+"/opinions";
		
		switch(nMode)
		{
		case ModeID:
			strOutFile = strRegion+strIDFileSuffix;
			GetIDs(strEntryURL, strRegion, nStartSeq);
			break;
		case ModeStory:
			setOpinionIDs = new TreeSet<String>();
			LoadOpinionIDs(strRegion+strIDFileSuffix);
			strOutFile = strRegion+strStoryFileSuffix;
			ProcessOpinions(nStartSeq);
			break;
		case ModeSinglePageForID:
			strOutFile = strRegion+strIDFileSuffix;
			GetSinglePageID(strEntryURL, strRegion, nStartSeq);
		default:
			System.out.println("Mode: " + nMode + " Not supported");
			break;
		}
		
		System.out.println("Crawler End..." + "Mode:" + nMode + " Region: " + strRegion);
	}
	
	private void GetSinglePageID(String strURL, String strRegion, int nPageNumber)
	{
		String strPageURL = strBaseURL+strRegion+"/opinions?page=" + nPageNumber;
		ParseAndStoreIDs(getHTML(strPageURL));
	}
	
	private void GetIDs(String strURL, String strRegion, int nStartSeq)
	{
		String strHTML = getHTML(strURL);
		Document doc = Jsoup.parse(strHTML);
		Elements links = doc.select("a[href]");
		int nMaxPage = 0;
		for(int i = 0; i < links.size(); i++)
		{
			Element link = links.get(i);
			String strHref = link.attr("href");
			if(strHref.indexOf(strPageNumber) >= 0)
			{
				int nIdx = strHref.indexOf(strPageNumber);
				int nPage = Integer.valueOf(strHref.substring(nIdx + strPageNumber.length(), strHref.length())).intValue();
				if(nPage > nMaxPage)
					nMaxPage = nPage;
			}
		}
		
		System.out.println("Max page: " + nMaxPage);
		try {
			Thread.sleep(SleepTime);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(int i = 1; i <= nMaxPage; i++)
		{
			if(i <= nStartSeq)
				continue;
			String strPageURL = strBaseURL+strRegion+"/opinions?page=" + i;
			ParseAndStoreIDs(getHTML(strPageURL));
			System.out.println("Page[" + i + "] Done!");
			try {
				Thread.sleep(SleepTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void ParseAndStoreIDs(String strHTML)
	{
		Document doc = Jsoup.parse(strHTML);
		Elements links = doc.select("a[href]");
		for(int i = 0; i < links.size(); i++)
		{
			Element link = links.get(i);
			String strHref = link.attr("href");
			if(strHref.indexOf(strOpinions) >= 0 && link.parent().tagName().equals("blockquote"))
			{
				
				String strTempID = strHref.substring(strHref.indexOf(strOpinions)+strOpinions.length(), strHref.length());
				PrintWriter out = null;
				try
				{
				    out = new PrintWriter(new BufferedWriter(new FileWriter(strOutFile, true)));
				    out.println(strTempID);
				    System.out.print(strTempID + ";");
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
		System.out.println();
	}
	
	private void LoadOpinionIDs(String strFile)
	{
		BufferedReader br;
		String line;
		try {
			br = new BufferedReader(new FileReader(strFile));
			while ((line = br.readLine()) != null) {
				   if(!line.isEmpty())
				   {
					   setOpinionIDs.add(line);
				   }
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
	
	private void ProcessOpinions(int nStartSeq)
	{
		int i = 0;
		for(String strID : setOpinionIDs)
		{
			i++;
			if(Integer.valueOf(strID).intValue() <= nStartSeq)
				continue;
			String strURL = strEntryURL + "/" + strID;
			
			ParseAndStoreOpinion(getHTML(strURL), strID);
			System.out.println("[" + i + "] Opinion - " + strID + " Done!");
			try {
				Thread.sleep(SleepTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void ParseAndStoreOpinion(String strHTML, String strID)
	{
		String strTitle = "";
		String strAuthorRole = "";
		String strStory = "";
		String strTime = "";
		Vector<String> vecLocations = new Vector<String>();
		Vector<String> vecRelated = new Vector<String>();
		Vector<String> vecGood = new Vector<String>();
		Vector<String> vecBad = new Vector<String>();
		Document doc = Jsoup.parse(strHTML);
		Elements titles = doc.select("span[id]");
		for(Element elem : titles)
		{
			if(elem.attr("id").equals("opinion_title"))
			{
				strTitle = elem.text();
			}
			else if(elem.attr("id").equals("opinion_author_role"))
			{
				strAuthorRole = elem.text();
			}
		}
		
		Elements story = doc.select("blockquote[id]");
		for(Element elem : story)
		{
			if(elem.attr("id").equals("opinion_body"))
			{
				strStory += elem.text();
			}
		}
		
		Elements timeElem = doc.select("time[itemprop]");
		if(timeElem != null && timeElem.size() > 0)
		{
			strTime = timeElem.get(0).attr("datetime");
		}
		
		Elements locationElem = doc.select("a[data-po-nacs]");
		int nCount = locationElem.size();
		if(nCount > 0)
		{
			for(Element elem : locationElem)
			{
				vecLocations.add(elem.text());
			}
		}
		
		Elements related = doc.select("div[class=related clearfix]");
		if(related != null && related.first() != null)
		{
			Elements rel = related.first().getElementsByTag("a");
			if(rel != null)
			{
				for(Element el : rel)
				{
					vecRelated.add(el.text());
				}
			}
		}
		
		Elements summary = doc.select("div[class=module standard_module]#saying");
		if(summary != null)
		{
			Elements ul = summary.select("ul[class]");
			for(Element el : ul)
			{
				boolean bIsGood = true;
				if(el.className().equals("left"))
				{
					bIsGood = true;
				}
				else if(el.className().equals("right"))
				{
					bIsGood = false;
				}
				else
				{
					continue;
				}
				
				Elements opinions = el.getElementsByTag("a");
				for(Element opEl : opinions)
				{
					if(bIsGood)
						vecGood.add(opEl.text());
					else
						vecBad.add(opEl.text());
				}
			}
		}
		

		PrintWriter out = null;
		try
		{
		    out = new PrintWriter(new BufferedWriter(new FileWriter(strOutFile, true)));
		    out.println("ID: " + strID);
			out.println("Title: " + strTitle);
			out.println("Time: " + strTime);
			out.print("Location: ");
			for(String strLocation : vecLocations)
			{
				out.print(strLocation + ";");
			}
			out.println();
			
			out.println("Author: " + strAuthorRole);
			out.println("Story: " + strStory);
			out.print("Relate: ");
			for(String strRelate : vecRelated)
			{
				out.print(strRelate + ";");
			}
			out.println();
			
			out.print("Good: ");
			for(String strGood : vecGood)
			{
				out.print(strGood + ";");
			}
			out.println();
			
			out.print("Bad: ");
			for(String strBad : vecBad)
			{
				out.print(strBad + ";");
			}
			out.println();
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
	
	private String getHTML(String urlToRead) {
	      URL url;
	      HttpURLConnection conn;
	      BufferedReader rd;
	      String line;
	      String result = "";
	      boolean bProcessed = true;
	      try {
	         url = new URL(urlToRead);
	         conn = (HttpURLConnection) url.openConnection();
	         conn.setRequestMethod("GET");
	         rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	         while ((line = rd.readLine()) != null) {
	            result += line;
	         }
	         rd.close();
	      } catch (IOException e) {
	         e.printStackTrace();
	         bProcessed = false;
	      } catch (Exception e) {
	         e.printStackTrace();
	         bProcessed = false;
	      }
	      
	      finally{
		      if(!bProcessed)
		      {
				try {
					Thread.sleep(SleepTime+SleepTime);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	return getHTML(urlToRead);
		      }
		      //return result;
	      }

	      return result;
	   }
}
