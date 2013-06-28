package stopword;

import com.saltlux.tms3.util.UserStopWordPattern;

/**
 * 패턴에 의한 불용어 체크
 */
public class SimpleStopWord extends UserStopWordPattern {
	public boolean isPatternStopWord(String term) {
		System.out.println("\t###### " + term);
		// 두글자 이하는 불용어
		if (term.length() <= 2) {
			return true;
		}
		return false;
	} 
}
