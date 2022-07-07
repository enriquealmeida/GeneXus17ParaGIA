package com.genexus.android.live_editing.network;

import java.util.Arrays;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class ConnectionStringInfoTest {
	private static final String CONNECTION_STRING = "http://192.168.56.1:30101/" +
			"gxlivepreviewservice/?01569389-0ee8-42ac-98ed-696c402bd48a:AndroidBasicTest" +
			":[192.168.56.1,172.16.150.65,MY-PC-NAME]";

	@Test
	public void shouldParseParametersCorrectly() {
		ConnectionStringInfo connStringInfo = ConnectionStringInfo.parse(CONNECTION_STRING);

		assertThat(connStringInfo.kbUUID).isEqualTo("01569389-0ee8-42ac-98ed-696c402bd48a");
		assertThat(connStringInfo.kbName).isEqualTo("AndroidBasicTest");
		assertThat(connStringInfo.ipList).isEqualTo(Arrays.asList("192.168.56.1", "172.16.150.65", "MY-PC-NAME"));
	}
}
