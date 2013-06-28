package com.saltlux.tmp;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.setvect.common.util.StringUtilAd;

/**
 * 토픽 XML String => List<TopicItem>으로 변경
 */
public class TopicParser {
	private static final int MAX_NODE = 20;

	public static List<TopicItem> parsing(String xml) {
		try {
			if (StringUtilAd.isEmpty(xml)) {
				return Collections.emptyList();
			}
			InputStream istream = new ByteArrayInputStream(xml.getBytes("utf-8"));
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(istream);
			Element docElement = doc.getDocumentElement();
			NodeList nodeList = docElement.getElementsByTagName("Node");

			List<TopicItem> result = new ArrayList<TopicItem>();

			for (int i = 0; i < nodeList.getLength(); i++) {
				if (i >= MAX_NODE) {
					break;
				}
				Element item = (Element) nodeList.item(i);
				TopicItem topic = new TopicItem();
				topic.setId(item.getAttribute("id"));
				topic.setName(item.getAttribute("name"));
				topic.setScore(item.getAttribute("score"));
				result.add(topic);
			}
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
