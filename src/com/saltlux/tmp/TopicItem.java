package com.saltlux.tmp;

/**
 * ���� �׸� ����
 */
public class TopicItem {
	/** ���̵� */
	private String id;
	/** �̸�(������) */
	private String name;
	/** ������(1~3) ���ڰ� ���� ���� ������ ���� */
	private String score;

	/**
	 * @return ���̵�
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            ���̵�
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return �̸�(������)
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            �̸�(������)
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return ������(1~3) ���ڰ� ���� ���� ������ ����
	 */
	public String getScore() {
		return score;
	}

	/**
	 * @param score
	 *            ������(1~3) ���ڰ� ���� ���� ������ ����
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
