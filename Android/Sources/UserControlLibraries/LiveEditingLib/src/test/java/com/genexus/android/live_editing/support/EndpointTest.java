package com.genexus.android.live_editing.support;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class EndpointTest {

	@Test
	public void shouldConcatenateValuesWithTwoDots() {
		Endpoint e = new Endpoint("192.168.1.1", 30100);
		assertThat(e.toString()).isEqualTo("192.168.1.1:30100");
	}

	@Test
	public void shouldReturnTrueIfTwoEndpointsHaveSameValues() {
		Endpoint e1 = new Endpoint("192.168.1.1", 30150);
		Endpoint e2 = new Endpoint("192.168.1.1", 30150);
		assertThat(e1).isEqualTo(e2);
	}

	@Test
	public void shouldReturnFalseIfTwoEndpointsHaveDifferentValues() {
		Endpoint e1 = new Endpoint("192.168.1.1", 30150);
		Endpoint e2 = new Endpoint("192.168.1.1", 30151);
		assertThat(e1).isNotEqualTo(e2);

		Endpoint e3 = new Endpoint("192.168.1.1", 30150);
		Endpoint e4 = new Endpoint("192.168.1.2", 30150);
		assertThat(e3).isNotEqualTo(e4);
	}
}
