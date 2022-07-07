package com.genexus.android.uitesting.utils;

import org.hamcrest.Matcher;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToCompressingWhiteSpace;

public class Misc {

	public static Matcher<String> getTextMatcher(String text) {
		return text.trim().isEmpty() ? equalTo(text) : equalToCompressingWhiteSpace(text);
	}

}
