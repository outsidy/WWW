package com.saltlux.tmp;

/**
 * 토픽 항목 설정
 */
public class TopicItem {
	/** 아이디 */
	private String id;
	/** 이름(연관어) */
	private String name;
	/** 연관도(1~3) 숫자가 낮을 수록 연관도 높음 */
	private String score;

	/**
	 * @return 아이디
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            아이디
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return 이름(연관어)
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            이름(연관어)
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return 연관도(1~3) 숫자가 낮을 수록 연관도 높음
	 */
	public String getScore() {
		return score;
	}

	/**
	 * @param score
	 *            연관도(1~3) 숫자가 낮을 수록 연관도 높음
	 */
	public void setScore(String score) {
		this.score = score;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TopicItem [id=" + id + ", name=" + name + ", score=" + score + "]";
	}

}
