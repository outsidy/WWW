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
 * 색인 파일에서 특정 인덱스 정보를 추출 한다.
 */
public class TermIndexExtractor {
	private final File indexFile;
	/** 불용어 단어 */
	public Set<String> stopWordList = new HashSet<String>();
	/** DF 최소값. DF 값이 <tt>minDf</tt> 미만 이면 사전에 등록 하지 않음 */
	private int minDf;
	private boolean usePattenStopWord = false;

	/**
	 * @param index
	 *            인덱스 경로
	 * @param stopWordFile
	 *            불용어 단어 파일
	 */
	public TermIndexExtractor(File index, File stopWordFile) {
		this.indexFile = index;
		loadStopWord(stopWordFile);
	}

	/**
	 * 특정 필드 Term 추출
	 * 
	 * @param field
	 *            Term 추출 필드 이름
	 * @return 색인 추출
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
				// '_'을 공백으로 치환
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
	 * 불필요한 단어를 자동완성 사전에서 제외 시키기 위함
	 * 
	 * @param text
	 *            단어
	 * @return 사전에 등록 할 수 있는 단어 :true, 그렇지 않은 경우 false
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
					|| ch == '.' || ch == '^' || ch == '#' || ch == '/' || ch == '%' || ch == '|' || ch == '→'
					|| ch == '―' || ch == ']' || ch == '[' || ch == '+' || ch == '*' || ch == ',' || ch == ';')
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
	 * @return DF 최소값. DF 값이 <tt>minDf</tt> 미만 이면 사전에 등록 하지 않음
	 */
	public int getMinDf() {
		return minDf;
	}

	/**
	 * @param minDf
	 *            DF 최소값. DF 값이 <tt>minDf</tt> 미만 이면 사전에 등록 하지 않음
	 */
	public void setMinDf(int minDf) {
		this.minDf = minDf;
	}

	/**
	 * 패턴 형태의 불용어 처리
	 * 
	 * @return
	 */
	public boolean isUsePattenStopWord() {
		return usePattenStopWord;
	}

	/**
	 * 패턴 형태의 불용어 처리
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
