package stopword;

import com.saltlux.tms3.util.UserStopWordPattern;

/**
 * ���Ͽ� ���� �ҿ�� üũ
 */
public class SimpleStopWord extends UserStopWordPattern {
	public boolean isPatternStopWord(String term) {
		System.out.println("\t###### " + term);
		// �α��� ���ϴ� �ҿ��
		if (term.length() <= 2) {
			return true;
		}
		return false;
	} 
}
