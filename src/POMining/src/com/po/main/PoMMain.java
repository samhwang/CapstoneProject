package com.po.main;
import com.po.crawler.Crawler;

public class PoMMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String strRegion = "au";
		int nMode = Crawler.ModeStory;
		if(args.length >= 2)
		{
			nMode = Integer.valueOf(args[0]).intValue();
			strRegion = args[1];
		}
		Crawler crawler = new Crawler();
		crawler.run(nMode, strRegion);
	}

}
