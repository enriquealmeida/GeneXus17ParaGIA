package com.genexus.android.live_editing.network;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;

import okhttp3.HttpUrl;

public class ConnectionStringInfo {
	public final String kbUUID;
	public final String kbName;
	public final List<String> ipList;

	private ConnectionStringInfo(String kbUUID, String kbName, List<String> ipList) {
		this.kbUUID = kbUUID;
		this.kbName = kbName;
		this.ipList = ipList;
	}

	/**
	 * Expected format: http://&lt;ip&gt;:&lt;port&gt;/?&lt;KB-UUID&gt;:&lt;KB-NAME&gt;:[&lt;Comma-separated IP list&gt;]
	 */
	public static ConnectionStringInfo parse(@NonNull String connectionString) {
		if (connectionString.length() == 0) {
			throw new IllegalArgumentException("Empty connection string.");
		}

		HttpUrl url = HttpUrl.parse(connectionString);
		if (url == null) {
			throw new IllegalArgumentException("Empty or malformed URL.");
		}

		String query = url.query();
		if (query == null) {
			throw new IllegalArgumentException("No query found.");
		}

		String[] params = query.split(":", -1);
		if (params.length != 3) {
			throw new IllegalArgumentException("Incorrect number of parameters found.");
		}

		String kbUUID = params[0];
		String kbName = params[1];
		String ipArray = params[2];

		if (!ipArray.startsWith("[") || !ipArray.endsWith("]")) {
			throw new IllegalArgumentException("Invalid format for the IP list.");
		}

		ipArray = ipArray.substring(1, ipArray.length() - 1);
		List<String> ipList = Arrays.asList(ipArray.split(","));

		return new ConnectionStringInfo(kbUUID, kbName, ipList);
	}
}
