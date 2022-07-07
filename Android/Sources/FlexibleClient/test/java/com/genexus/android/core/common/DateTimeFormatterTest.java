package com.genexus.android.core.common;

import com.genexus.android.core.common.DateTimeFormatter;

import org.junit.Test;


import static com.google.common.truth.Truth.assertThat;

public class DateTimeFormatterTest {

	@Test
	public void getTimePattern_HourOnly() {
		assertThat(DateTimeFormatter.getTimePattern("99", false)).isEqualTo("hh a");
		assertThat(DateTimeFormatter.getTimePattern("99", true)).isEqualTo("HH");
	}

	@Test
	public void getTimePattern_HourAndMins() {
		assertThat(DateTimeFormatter.getTimePattern("99:99", false)).isEqualTo("hh:mm a");
		assertThat(DateTimeFormatter.getTimePattern("99:99", true)).isEqualTo("HH:mm");
	}

	@Test
	public void getTimePattern_HourMinsAndSecs() {
		assertThat(DateTimeFormatter.getTimePattern("99:99:99", false)).isEqualTo("hh:mm:ss a");
		assertThat(DateTimeFormatter.getTimePattern("99:99:99", true)).isEqualTo("HH:mm:ss");
	}

	@Test
	public void getDatePattern_NotSpecified() {
		assertThat(DateTimeFormatter.getDatePattern("", "M/d/y")).isEqualTo("MM/dd/y");
		assertThat(DateTimeFormatter.getDatePattern("", "d/M/y")).isEqualTo("dd/MM/y");
		assertThat(DateTimeFormatter.getDatePattern("", "y/M/d")).isEqualTo("y/MM/dd");
	}

	@Test
	public void getDatePattern_TwoDigitsYear() {
		assertThat(DateTimeFormatter.getDatePattern("99/99/99", "M/d/y")).isEqualTo("MM/dd/yy");
		assertThat(DateTimeFormatter.getDatePattern("99/99/99", "d/M/y")).isEqualTo("dd/MM/yy");
		assertThat(DateTimeFormatter.getDatePattern("99/99/99", "y/M/d")).isEqualTo("yy/MM/dd");

		assertThat(DateTimeFormatter.getDatePattern("99/99/99", "M/d/yy")).isEqualTo("MM/dd/yy");
		assertThat(DateTimeFormatter.getDatePattern("99/99/99", "d/M/yy")).isEqualTo("dd/MM/yy");
		assertThat(DateTimeFormatter.getDatePattern("99/99/99", "yy/M/d")).isEqualTo("yy/MM/dd");
	}

	@Test
	public void getDatePattern_FourDigitsYear() {
		assertThat(DateTimeFormatter.getDatePattern("99/99/9999", "M/d/y")).isEqualTo("MM/dd/yyyy");
		assertThat(DateTimeFormatter.getDatePattern("99/99/9999", "d/M/y")).isEqualTo("dd/MM/yyyy");
		assertThat(DateTimeFormatter.getDatePattern("99/99/9999", "y/M/d")).isEqualTo("yyyy/MM/dd");

		assertThat(DateTimeFormatter.getDatePattern("99/99/9999", "M/d/yy")).isEqualTo("MM/dd/yyyy");
		assertThat(DateTimeFormatter.getDatePattern("99/99/9999", "d/M/yy")).isEqualTo("dd/MM/yyyy");
		assertThat(DateTimeFormatter.getDatePattern("99/99/9999", "yy/M/d")).isEqualTo("yyyy/MM/dd");
	}
}