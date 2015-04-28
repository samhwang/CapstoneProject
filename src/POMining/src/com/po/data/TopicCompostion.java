package com.po.data;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class TopicCompostion {

	private static TopicCompostion topicCompostion = null;

	protected class TopicStats {
		public String strID;
		public Map<Integer, Float> mapTopicScore;

		public TopicStats(String strID) {
			this.strID = strID;
			mapTopicScore = new HashMap<Integer, Float>();
		}

		public void AddStats(int nTopicIdx, float fTopicScore) {
			mapTopicScore.put(nTopicIdx, fTopicScore);
		}
	}

	private Map<String, TopicStats> mapTopics;

	private TopicCompostion() {
		mapTopics = new HashMap<String, TopicStats>();
	}

	public static TopicCompostion getInstance() {
		if (topicCompostion == null)
			topicCompostion = new TopicCompostion();

		return topicCompostion;
	}

	public void init() {
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader("all_compostion.txt"));
			String strLine;
			// Skip first row
			strLine = br.readLine();

			while ((strLine = br.readLine()) != null) {
				String[] arrLine = strLine.split("\t");
				String strID = arrLine[1];
				TopicStats topicStats = new TopicStats(strID);

				for (int i = 2; i < arrLine.length;) {
					topicStats.AddStats(Integer.valueOf(arrLine[i]),
							Float.valueOf(arrLine[i + 1]));
					i += 2;
				}

				mapTopics.put(strID, topicStats);
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public float GetWeighting(String strDocID, int nTopicIdx) {
		float fRet = 0;

		if (mapTopics.containsKey(strDocID)
				&& mapTopics.get(strDocID).mapTopicScore.containsKey(nTopicIdx))
			fRet = mapTopics.get(strDocID).mapTopicScore.get(nTopicIdx);

		return fRet;
	}

	public float GetWeighting(Vector<String> vecDocID, int nTopicIdx) {
		float fRet = 0;
		Set<String> setIDCheck = new HashSet<String>();
		for (String strID : vecDocID) {
			if (setIDCheck.contains(strID))
				continue;
			fRet += GetWeighting(strID, nTopicIdx);
			setIDCheck.add(strID);
		}

		return fRet;
	}

	public void DumpTopicStats(String strID) {
		if (mapTopics.containsKey(strID)) {
			TopicStats stats = mapTopics.get(strID);
			for (int nIdx : stats.mapTopicScore.keySet()) {
				System.out.println(String.format("[%d] -> [%f]", nIdx,
						stats.mapTopicScore.get(nIdx)));
			}
		} else {
			System.out.println("No Story found!");
		}
	}
}
