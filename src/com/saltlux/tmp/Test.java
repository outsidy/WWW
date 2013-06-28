package com.saltlux.tmp;

import stopword.PattenStopWord;

public class Test {
	public static void main(String[] args) {
		boolean result = PattenStopWord.isStopWord("ÃÑ°ýº°");
		if (result) {
			System.out.println(PattenStopWord.getReason());
		}
		System.out.println("Á¾·á");
	}
}
