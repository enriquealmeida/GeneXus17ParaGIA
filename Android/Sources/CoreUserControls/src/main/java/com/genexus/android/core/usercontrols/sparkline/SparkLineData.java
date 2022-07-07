package com.genexus.android.core.usercontrols.sparkline;

import java.util.ArrayList;
import java.util.Arrays;

public class SparkLineData extends ArrayList<Float> {
	private static final long serialVersionUID = 1L;
	private Float mMinimum;
	private Float mMaximum;
	private boolean mWasAnalized;

	public Float getMinimum() {
		ensureAnalysis();
		return mMinimum;
	}

	public Float getMaximum() {
		ensureAnalysis();
		return mMaximum;
	}

	private void ensureAnalysis() {
		if (this.size() > 0 && !mWasAnalized) {
			Float [] values = this.toArray(new Float[this.size()]);
			Arrays.sort(values);
			mMinimum = values[0];
			mMaximum = values[values.length - 1];
			mWasAnalized = true;
		}
	}

	public Float getCurrentValue() {
		return this.get(this.size() - 1);
	}
}
