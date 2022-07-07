package com.genexus.android.live_editing.network;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.genexus.android.live_editing.support.Endpoint;

import static com.google.common.truth.Truth.assertThat;

public class ConnectionAttemptsTest {
	private List<String> mIpList;
	private List<Endpoint> mStoredEndpoints;

	@Before
	public void setUp() {
		mIpList = Arrays.asList("192.168.1.1", "172.16.150.65", "MY-PC-NAME");
		mStoredEndpoints = Arrays.asList(
				new Endpoint("192.168.1.1", 30120),
				new Endpoint("10.0.0.1", 30103)
		);
	}

	@Test
	public void shouldHaveTheCorrectAmountOfAttempts() {
		ConnectionAttempts connectionAttempts = new ConnectionAttempts(mIpList, mStoredEndpoints);

		List<Endpoint> attempts = new ArrayList<>();
		while (connectionAttempts.hasLeft()) {
			attempts.add(connectionAttempts.getNext());
		}

		assertThat(attempts.size())
				.isEqualTo(mIpList.size() * ConnectionAttempts.PORT_RANGE_SIZE + mStoredEndpoints.size());
	}

	@Test
	public void shouldHaveTheSavedEndpointsAsFirstAttempts() {
		ConnectionAttempts connectionAttempts = new ConnectionAttempts(mIpList, mStoredEndpoints);

		List<Endpoint> attempts = new ArrayList<>();
		while (connectionAttempts.hasLeft()) {
			attempts.add(connectionAttempts.getNext());
		}

		assertThat(attempts.get(0)).isEqualTo(mStoredEndpoints.get(0));
		assertThat(attempts.get(1)).isEqualTo(mStoredEndpoints.get(1));
	}
}
