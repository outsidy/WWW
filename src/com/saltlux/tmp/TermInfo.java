package com.saltlux.tmp;

/**
 * Term 정보. Term, DF 값
 */
public class TermInfo implements Comparable<TermInfo> {
	public final String term;
	public int df;

	/**
	 * @param term
	 *            추출 Term
	 * @param df
	 *            DF
	 */
	public TermInfo(String term, int df) {
		this.term = term;
		this.df = df;
	}

	/**
	 * @return DF 값
	 */
	public int getDf() {
		return df;
	}

	/**
	 * DF 값 증가
	 * 
	 * @param addValue
	 * 
	 */
	public void addDf(int addValue) {
		this.df += addValue;
	}

	/**
	 * @return term 정보
	 */
	public String getTerm() {
		return term;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return term + "\t" + df;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + df;
		result = prime * result + ((term == null) ? 0 : term.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof TermInfo)) {
			return false;
		}
		TermInfo other = (TermInfo) obj;
		if (df != other.df) {
			return false;
		}
		if (term == null) {
			if (other.term != null) {
				return false;
			}
		}
		else if (!term.equals(other.term)) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(TermInfo o) {
		int a = o.df - df;
		if (a != 0) {
			return a;
		}

		return term.compareTo(o.term);
	}

}