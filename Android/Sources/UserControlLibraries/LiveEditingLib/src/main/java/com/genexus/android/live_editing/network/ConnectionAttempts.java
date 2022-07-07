package com.genexus.android.live_editing.network;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import androidx.annotation.NonNull;

import com.genexus.android.live_editing.support.Endpoint;

class ConnectionAttempts {
	private static final int MIN_PORT_NUMBER = 30100;
	private static final int MAX_PORT_NUMBER = 30150;
	public static final int PORT_RANGE_SIZE = MAX_PORT_NUMBER - MIN_PORT_NUMBER + 1;

	private final List<Endpoint> mAttemptsList;
	private ListIterator<Endpoint> mAttemptsIterator;

	public ConnectionAttempts(List<String> ipList, List<Endpoint> savedEndpoints) {
		mAttemptsList = createList(ipList, savedEndpoints);
		mAttemptsIterator = mAttemptsList.listIterator();
	}

	/**
	 * We add the stored endpoints as the first attempts to try. Then, for each IP in ipList from
	 * port 30100 thorough 30150, we add an attempt.
 	 */
	private @NonNull List<Endpoint> createList(List<String> ipList,
											   List<Endpoint> savedEndpoints) {
		int listSize = ipList.size() * PORT_RANGE_SIZE + savedEndpoints.size();
		List<Endpoint> attempts = new ArrayList<>(listSize);

		for (Endpoint e : savedEndpoints) {
			attempts.add(e);
		}

		for (String ip : ipList) {
			for (int port = MIN_PORT_NUMBER; port <= MAX_PORT_NUMBER; port++) {
				attempts.add(new Endpoint(ip, port));
			}
		}

		return Collections.unmodifiableList(attempts);
	}

	public void reset() {
		mAttemptsIterator = mAttemptsList.listIterator();
	}

	public boolean hasLeft() {
		return mAttemptsIterator.hasNext();
	}

	public Endpoint getCurrent() {
		return mAttemptsList.get(mAttemptsIterator.previousIndex());
	}

	public Endpoint getNext() {
		return mAttemptsIterator.next();
	}
}
