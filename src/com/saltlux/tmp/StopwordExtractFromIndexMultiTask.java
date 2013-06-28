package com.saltlux.tmp;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
public class StopwordExtractFromIndexMultiTask {
	private static final String INDEX_DIR = "D:\\01.������Ʈ\\21.�Ｚ����\\IN2_DISCOVERY_2_0\\index\\IS_SI";
	private static final String FIELD = "TMS_RAW_STREAM";
	private static final String PATH_CANDIDATE_STOPWORD = "candidate_stopword.txt";
	private static final String PATH_CANDIDATE_AUTOWORD = "candidate_autoword.txt";

	/** �ҿ�� �� */
	private static final Set<String> STOP_WORD_SET = new TreeSet<String>();
	/** �ҿ� �ƴ� Term �� */
	private static final Set<String> AUTO_WORD_SET = new TreeSet<String>();

	private static final int TASK_THREAD_COUNT = 5;
	private static LinkedBlockingQueue<Runnable> workQueue;
	private static ThreadPoolExecutor taskExecutor;

	public static void main(String[] args) throws IOException {
		IndexReader reader = null;
		int count = 0;
		LapTimeChecker ck = new LapTimeChecker("�ҿ�� ���� �ܰ�");

		workQueue = new LinkedBlockingQueue<Runnable>(TASK_THREAD_COUNT);
		taskExecutor = new ThreadPoolExecutor(TASK_THREAD_COUNT, TASK_THREAD_COUNT, 0L, TimeUnit.MILLISECONDS,
				workQueue);

		try {
			reader = IndexReader.open(FSDirectory.getDirectory(INDEX_DIR), true);
			TermEnum tEnum = reader.terms(new Term(FIELD));
			do {
				Term term = tEnum.term();

				// Validate
				if (term == null || !term.field().equals(FIELD)) {
					break;
				}

				synchronized (workQueue) {
					while (workQueue.size() >= TASK_THREAD_COUNT) {
						try {
							workQueue.wait(1);
						} catch (InterruptedException e) {
							throw new RuntimeException(e);
						}
						continue;
					}
					StopWordFilter command = new StopWordFilter(term);
					taskExecutor.execute(command);
				}
				count++;
				if (count % 1000 == 0) {
					ck.check(String.format("%,d ����", count));
				}
			} while (tEnum.next());
			ck.check(String.format("%,d �Ϸ�", count));
		} catch (Exception e) {
			e.printStackTrace();
		}

		FileUtils.writeLines(new File(PATH_CANDIDATE_STOPWORD), STOP_WORD_SET);
		System.out.println(PATH_CANDIDATE_STOPWORD + "���� �Ϸ�");
		FileUtils.writeLines(new File(PATH_CANDIDATE_AUTOWORD), AUTO_WORD_SET);
		System.out.println(PATH_CANDIDATE_AUTOWORD + "���� �Ϸ�");
	}

	static class StopWordFilter implements Runnable {
		private String text;

		private StopWordFilter(Term term) {
			text = term.text();
		}

		@Override
		public void run() {
			boolean isStop = PattenStopWord.isStopWord(text);
			synchronized (AUTO_WORD_SET) {
				if (isStop) {
					STOP_WORD_SET.add(text);
				}
				else {
					AUTO_WORD_SET.add(text);
				}
			}
		}
	}

}
