package com.po.main;
import com.po.crawler.Crawler;

public class PoMMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String strRegion = "au";
		int nMode = Crawler.ModeStory;
		int nStartSeq = 0;
		if(args.length >= 2)
		{
			nMode = Integer.valueOf(args[0]).intValue();
			strRegion = args[1];
		}
		if(args.length >= 3)
			nStartSeq = Integer.valueOf(args[2]).intValue();
		
		Crawler crawler = new Crawler();
		crawler.run(nMode, strRegion, nStartSeq);
	}

}
