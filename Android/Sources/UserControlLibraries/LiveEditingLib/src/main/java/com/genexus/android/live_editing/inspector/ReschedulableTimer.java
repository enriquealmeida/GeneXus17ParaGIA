package com.genexus.android.live_editing.inspector;

import androidx.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

class ReschedulableTimer extends Timer {
    private final Runnable mTask;
    private final long mDelay;
    private @Nullable TimerTask mTimerTask;

    public ReschedulableTimer(Runnable runnable, long delay) {
        mTask = runnable;
        mDelay = delay;
    }

    public void schedule() {
        mTimerTask = new TimerTask() {

            @Override
            public void run() {
                mTask.run();
            }
        };
        schedule(mTimerTask, mDelay);
    }

    public void reschedule() {
        if (mTimerTask != null && mDelay > 0) {
            mTimerTask.cancel();
            schedule();
        }
    }

    public boolean cancelTask() {
        return mTimerTask != null && mTimerTask.cancel();
    }
}
