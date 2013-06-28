package com.saltlux.tmp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.store.FSDirectory;

/**
 * ���� ���Ͽ��� Ư�� �ε��� ������ ���� �Ѵ�.
 */
public class TermIndexExtractor {
	private final File indexFile;
	/** �ҿ�� �ܾ� */
	public Set<String> stopWordList = new HashSet<String>();
	/** DF �ּҰ�. DF ���� <tt>minDf</tt> �̸� �̸� ������ ��� ���� ���� */
	private int minDf;
	private boolean usePattenStopWord = false;

	/**
	 * @param index
	 *            �ε��� ���
	 * @param stopWordFile
	 *            �ҿ�� �ܾ� ����
	 */
	public TermIndexExtractor(File index, File stopWordFile) {
		this.indexFile = index;
		loadStopWord(stopWordFile);
	}

	/**
	 * Ư�� �ʵ� Term ����
	 * 
	 * @param field
	 *            Term ���� �ʵ� �̸�
	 * @return ���� ����
	 */
	public Set<TermInfo> extractTerm(String field) {
		Set<TermInfo> result = new TreeSet<TermInfo>();
		IndexReader reader = null;
		try {
			reader = IndexReader.open(FSDirectory.getDirectory(indexFile), true);
			TermEnum tEnum = reader.terms(new Term(field));
			do {
				Term term = tEnum.term();

				// Validate
				if (term == null || !term.field().equals(field)) {
					break;
				}

				String text = term.text();
				if (!isAllowTerm(text)) {
					continue;
				}

				if (tEnum.docFreq() < minDf) {
					continue;
				}
				// '_'�� �������� ġȯ
				text = text.replace("_", " ");
				result.add(new TermInfo(text, tEnum.docFreq()));
			} while (tEnum.next());
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					// ingnore
				}
			}
		}
		return result;
	}

	/**
	 * ���ʿ��� �ܾ �ڵ��ϼ� �������� ���� ��Ű�� ����
	 * 
	 * @param text
	 *            �ܾ�
	 * @return ������ ��� �� �� �ִ� �ܾ� :true, �׷��� ���� ��� false
	 */
	private boolean isAllowTerm(String text) {
		if (stopWordList.contains(text)) {
			return false;
		}

		if (!usePattenStopWord) {
			return true;
		}

		char ch;
		int alpha = 0;
		for (int i = 0; i < text.length(); i++) {
			ch = text.charAt(i);
			if (ch == '"' || ch == ')' || ch == '(' || ch == ',' || ch == '\'' || ch == '&' || ch == ':' || ch == '-'
					|| ch == '.' || ch == '^' || ch == '#' || ch == '/' || ch == '%' || ch == '|' || ch == '��'
					|| ch == '��' || ch == ']' || ch == '[' || ch == '+' || ch == '*' || ch == ',' || ch == ';')
				return false;
			if ((ch >= '0' && ch <= '9') || (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || ch == ' ') {
				alpha++;
			}
		}
		if (alpha == text.length())
			return false;
		return true;
	}

	/**
	 * @return DF �ּҰ�. DF ���� <tt>minDf</tt> �̸� �̸� ������ ��� ���� ����
	 */
	public int getMinDf() {
		return minDf;
	}

	/**
	 * @param minDf
	 *            DF �ּҰ�. DF ���� <tt>minDf</tt> �̸� �̸� ������ ��� ���� ����
	 */
	public void setMinDf(int minDf) {
		this.minDf = minDf;
	}

	/**
	 * ���� ������ �ҿ�� ó��
	 * 
	 * @return
	 */
	public boolean isUsePattenStopWord() {
		return usePattenStopWord;
	}

	/**
	 * ���� ������ �ҿ�� ó��
	 * 
	 * @param usePattenStopWord
	 */
	public void setUsePattenStopWord(boolean usePattenStopWord) {
		this.usePattenStopWord = usePattenStopWord;
	}

	/**
	 * @param StopWordFileName
	 */
	private void loadStopWord(File sfile) {
		stopWordList = new HashSet<String>();
		BufferedReader fis = null;
		try {
			if (sfile == null || !sfile.exists() || !sfile.isFile()) {
				System.out.println("Not used Stopword Dirctionary.");
				return;
			}
			fis = new BufferedReader(new InputStreamReader(new FileInputStream(sfile), "UTF-8"));
			String line = null;

			while ((line = fis.readLine()) != null) {
				if (line.startsWith("#")) {
					continue;
				}
				line = line.toLowerCase();
				line = line.replaceAll("\\_", " ");
				if (!stopWordList.contains(line))
					stopWordList.add(line);
			}
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
			}
		}
		System.out.printf("Stop Word: %,d Loaded\n", stopWordList.size());
	}

}
