package com.saltlux.tmp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.io.FileUtils;

import stopword.PattenStopWord;

import com.setvect.common.util.LapTimeChecker;

/**
 * Term 리스트 파일에서 불용어 추출
 */
public class StopwordExtractFromFile {
	private static final String PATH_DUMP_TERM = "ALL_TERM.txt";
	private static final String PATH_CANDIDATE_STOPWORD = "candidate_stopword.txt";
	private static final String PATH_CANDIDATE_AUTOWORD = "candidate_autoword.txt";

	/** 불용어 셋 */
	private static final Set<String> STOP_WORD_SET = new TreeSet<String>();
	/** 불용어가 아닌 Term 셋 */
	private static final Set<String> AUTO_WORD_SET = new TreeSet<String>();

	public static void main(String[] args) throws IOException {

		String term;
		BufferedReader reader = null;
		int count = 0;
		LapTimeChecker ck = new LapTimeChecker("불용어 정제 단계");
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(PATH_DUMP_TERM), "UTF-8"));
			String line = null;

			while ((line = reader.readLine()) != null) {
				term = line.trim().split("\t")[0];

				boolean isStop = PattenStopWord.isStopWord(term);

				if (isStop) {
					STOP_WORD_SET.add(term);
				}
				else {
					AUTO_WORD_SET.add(term);
				}
				count++;
				if (count % 1000 == 0) {
					ck.check(String.format("%,d 진행", count));
				}
			}
			ck.check(String.format("%,d 완료", count));
		} catch (Exception e) {
			e.printStackTrace();
		}

		FileUtils.writeLines(new File(PATH_CANDIDATE_STOPWORD), STOP_WORD_SET);
		System.out.println(PATH_CANDIDATE_STOPWORD + "저장 완료");
		FileUtils.writeLines(new File(PATH_CANDIDATE_AUTOWORD), AUTO_WORD_SET);
		System.out.println(PATH_CANDIDATE_AUTOWORD + "저장 완료");
	}

}
