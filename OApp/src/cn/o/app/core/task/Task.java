package cn.o.app.core.task;

import cn.o.app.core.event.Dispatcher;

public class Task extends Dispatcher implements IStopable {

	protected boolean mStarted;

	protected boolean mStoped;

	protected boolean mRunInBackground = true;

	protected boolean mRestartable = false;

	public Task() {

	}

	@Override
	public boolean isRunInBackground() {
		return mRunInBackground;
	}

	@Override
	public void setRunInBackground(boolean runInBackground) {
		if (mStarted || mStoped) {
			return;
		}
		mRunInBackground = runInBackground;
	}

	@Override
	public boolean isStoped() {
		return mStoped;
	}

	@Override
	public boolean stop() {
		if (mStoped) {
			return false;
		}
		mStoped = true;
		onStop();
		if (!mRestartable) {
			removeAllListeners();
		}
		return true;
	}

	protected void onStop() {

	}

	public boolean isStarted() {
		return mStarted;
	}

	public boolean start() {
		if (mStarted || mStoped) {
			return false;
		}
		mStarted = true;
		onStart();
		return true;
	}

	protected void onStart() {

	}

	public boolean isRunning() {
		return mStarted && !mStoped;
	}

}