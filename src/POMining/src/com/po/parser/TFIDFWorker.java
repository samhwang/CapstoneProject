package com.po.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class TFIDFWorker {

	protected class WordStats {
		public Vector<Integer> vecCountInDoc;
		public Vector<String> vecDocID;
		public double dIDF;

		public WordStats() {
			vecCountInDoc = new Vector<Integer>();
			vecDocID = new Vector<String>();
			dIDF = 0;
		}
	}

	private static TFIDFWorker worker = null;
	private Map<String, WordStats> mapWordStats;

	protected TFIDFWorker() {
		mapWordStats = new HashMap<String, WordStats>();
	}

	public static TFIDFWorker GetInstance() {
		if (worker == null)
			worker = new TFIDFWorker();

		return worker;
	}

	public void AddWord(String strWord, String strDocID) {
		if (!mapWordStats.containsKey(strWord)) {
			WordStats stats = new WordStats();
			stats.vecCountInDoc.add(1);
			stats.vecDocID.add(strDocID);
			mapWordStats.put(strWord, stats);
		} else {
			WordStats stats = mapWordStats.get(strWord);
			if (!stats.vecDocID.contains(strDocID)) {
				stats.vecCountInDoc.add(1);
				stats.vecDocID.add(strDocID);
			} else {
				int nIdx = stats.vecDocID.indexOf(strDocID);
				int nCount = stats.vecCountInDoc.get(nIdx) + 1;
				stats.vecCountInDoc.set(nIdx, nCount);
			}
		}
	}

	public void AddWordAsStory(String strStory, String strDocID) {

		String[] arrStory = strStory.substring(6).split(" ");
		for (String strTemp : arrStory) {
			// String tempOut =
			// StoryParser.getInstance().RemoveStopWord(strTemp);
			// if(!tempOut.isEmpty())
			AddWord(strTemp, strDocID);
		}
	}

	public void PostProcess(int nTotalNumberOfStory) {
		CalculateAllIDF(nTotalNumberOfStory);
	}

	private void CalculateAllIDF(int nTotalNumberOfStory) {
		for (WordStats stats : mapWordStats.values()) {
			// stats.dIDF = Math.log(nTotalNumberOfStory/stats.vecDocID.size());
			stats.dIDF = Math
					.log10(nTotalNumberOfStory / stats.vecDocID.size());
		}
	}

	public double GetTFIDF(String strWord, Vector<String> docIDs) {
		double dRet = 0.0;
		WordStats stats = mapWordStats.get(strWord);
		if (stats == null)
			return 0;
		for (String docID : docIDs) {
			if (stats == null || stats.vecDocID == null)
				continue;
			if (stats.vecDocID.contains(docID))
				dRet += stats.vecCountInDoc.get(stats.vecDocID.indexOf(docID));
		}

		return dRet * stats.dIDF;
	}

	public void PrintWordStats(String strWord) {
		if (!mapWordStats.containsKey(strWord)) {
			System.out.println("Word: [" + strWord + "] not in the documents");
			return;
		}

		WordStats stats = mapWordStats.get(strWord);
		System.out.println("Stats for word [" + strWord + "]: " + stats.dIDF);
		// for(int i = 0; i < stats.vecCountInDoc.size(); i++)
		// {
		// System.out.println("DocID: [" + stats.vecDocID.get(i) + "]" +
		// " Count: [" + stats.vecCountInDoc.get(i) + "]");
		// }
	}
}
