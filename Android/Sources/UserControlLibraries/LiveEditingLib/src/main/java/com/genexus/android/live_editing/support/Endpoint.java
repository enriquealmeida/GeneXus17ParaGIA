package com.genexus.android.live_editing.support;

import java.util.Locale;

public class Endpoint {
	public final String ip;
	public final int port;

	public Endpoint(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	public Endpoint(String endpoint) {
		String[] tokens = endpoint.split(":", -1);
		this.ip = tokens[0];
		this.port = Integer.parseInt(tokens[1]);
	}

	@Override
	public String toString() {
		return String.format(Locale.ENGLISH, "%s:%d", ip, port);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (!(o instanceof Endpoint))
			return false;

		Endpoint endpoint = (Endpoint) o;

		//noinspection SimplifiableIfStatement
		if (port != endpoint.port) return false;
		return ip != null ? ip.equals(endpoint.ip) : endpoint.ip == null;
	}

	@Override
	public int hashCode() {
		int result = ip != null ? ip.hashCode() : 0;
		result = 31 * result + port;
		return result;
	}
}
