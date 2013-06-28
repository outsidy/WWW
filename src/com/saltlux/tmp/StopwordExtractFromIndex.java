package com.saltlux.tmp;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.store.FSDirectory;

import stopword.PattenStopWord;

import com.setvect.common.util.LapTimeChecker;

/**
 * ���ο��� �ҿ�� ����
 */
public class StopwordExtractFromIndex {
	private static final int MORE_THEN_DF = 5;
	private static final String INDEX_DIR = "D:\\01.������Ʈ\\21.�Ｚ����\\IN2_DISCOVERY_2_0\\index\\IS_IH";
	private static final String FIELD = "TMS_RAW_STREAM";

	private static final String PATH_CANDIDATE_AUTOWORD = "result/01.�Ϲݴܾ�.txt";
	private static final String PATH_CANDIDATE_STOPWORD = "result/02.�ҿ��.txt";
	private static final String PATH_CANDIDATE_UNDERWORD = "result/03.DF" + MORE_THEN_DF + "_�̸�_�ܾ�.txt";

	/** �ҿ�� �� */
	private static final Set<TermInfo> STOP_WORD_SET = new TreeSet<TermInfo>();
	/** �ҿ� �ƴ� Term �� */
	private static final Set<TermInfo> AUTO_WORD_SET = new TreeSet<TermInfo>();

	/** MORE_THEN_DF ���� DF ���� ���� Term */
	private static final Set<TermInfo> UNDER_WORD_SET = new TreeSet<TermInfo>();

	public static void main(String[] args) throws IOException {
		IndexReader reader = null;
		int count = 0;
		int skip = 0;
		LapTimeChecker ck = new LapTimeChecker("�ҿ�� ���� �ܰ�");
		try {
			reader = IndexReader.open(FSDirectory.getDirectory(INDEX_DIR), true);
			TermEnum tEnum = reader.terms(new Term(FIELD));
			do {
				Term term = tEnum.term();

				// Validate
				if (term == null || !term.field().equals(FIELD)) {
					break;
				}
				count++;

				int docFreq = tEnum.docFreq();
				String text = term.text();
				if (docFreq < MORE_THEN_DF) {
					UNDER_WORD_SET.add(new TermInfo(text, docFreq));
					skip++;
					continue;
				}

				boolean isStop = PattenStopWord.isStopWord(text);

				if (isStop) {
					STOP_WORD_SET.add(new TermInfo(text, docFreq));
				}
				else {
					AUTO_WORD_SET.add(new TermInfo(text, docFreq));
				}
				if (count % 1000 == 0) {
					ck.check(String.format("%,d ����, ���: %,d", count, skip));
				}
			} while (tEnum.next());
			ck.check(String.format("%,d �Ϸ�, ���: %,d", count, skip));
		} catch (Exception e) {
			e.printStackTrace();
		}
		FileUtils.writeLines(new File(PATH_CANDIDATE_AUTOWORD), AUTO_WORD_SET);
		System.out.println(PATH_CANDIDATE_AUTOWORD + "���� �Ϸ�");

		FileUtils.writeLines(new File(PATH_CANDIDATE_STOPWORD), STOP_WORD_SET);
		System.out.println(PATH_CANDIDATE_STOPWORD + "���� �Ϸ�");

		FileUtils.writeLines(new File(PATH_CANDIDATE_UNDERWORD), UNDER_WORD_SET);
		System.out.println(PATH_CANDIDATE_UNDERWORD + "���� �Ϸ�");
	}
}
