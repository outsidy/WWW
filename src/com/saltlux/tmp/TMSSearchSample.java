package com.saltlux.tmp;

import java.util.List;

import com.saltlux.dor.api.IN2TMSNewCluster;
import com.saltlux.dor.api.IN2TMSOldOwlimSearch;

public class TMSSearchSample {
	public static void main(String[] args) {

		// IN2StdCommander commander = new IN2StdCommander();
		// commander.setServer("127.0.0.1", 20000);
		// commander.newQuery();
		// boolean t = commander.ReloadClusteringEngine();
		// System.out.println("불용어 사전 다시 읽기: " + t);
		//

		IN2TMSOldOwlimSearch topicRank = new IN2TMSOldOwlimSearch();

		topicRank.setServer("127.0.0.1", 20000);
		topicRank.setSearchCount(100);
		topicRank.setLanguage("KOR");
		topicRank.setContentField("TMS_RAW_STREAM");
		
		topicRank.addIndex("IS_SI");
		topicRank.addQueryEx("태양광", IN2TMSNewCluster.ANALYSIS_KMA_TOPIC, "TMS_RAW_STREAM");

		topicRank.analyzeDocument();

		String topicXml = topicRank.getTopicRank();
		List<TopicItem> topicList = TopicParser.parsing(topicXml);
		for (TopicItem topic : topicList) {
			System.out.println(topic);
		}
		
		System.out.println(topicXml);

		System.out.println("종료");
	}
}
