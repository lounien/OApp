package cn.mutils.app.fir;

import java.io.File;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.callback.RequestCallBack;

import android.view.Gravity;
import cn.mutils.app.AppUtil;
import cn.mutils.app.core.event.listener.VersionUpdateListener;
import cn.mutils.app.core.text.MBFormat;
import cn.mutils.app.core.text.StringUtil;
import cn.mutils.app.fir.FIRUpdateTask.FIRUpdateReq;
import cn.mutils.app.fir.FIRUpdateTask.FIRUpdateRes;
import cn.mutils.app.net.INetTask;
import cn.mutils.app.net.NetTaskListener;
import cn.mutils.app.task.ContextOwnerTask;
import cn.mutils.app.ui.Alert;
import cn.mutils.app.ui.Alert.AlertListener;

/**
 * Fly It Remotely Update Agent
 */
public class FIRUpdateAgent extends ContextOwnerTask {

	protected VersionUpdateListener mVersionUpdateListener;

	protected RequestCallBack<File> mDownloadCallBack;

	protected FIRUpdateTask mUpdateTask;

	public FIRUpdateAgent() {
		FIRUpdateReq req = new FIRUpdateReq();
		mUpdateTask = new FIRUpdateTask();
		mUpdateTask.setRequest(req);
		mUpdateTask.addListener(new FirUpdateTaskListener());
	}

	@Override
	protected void onStart() {
		mUpdateTask.setContext(mContext);
		mUpdateTask.start();
	}

	@Override
	protected void onStop() {
		mUpdateTask.stop();
	}

	public void setListener(VersionUpdateListener listener) {
		mVersionUpdateListener = listener;
	}

	public void setDownloadCallBack(RequestCallBack<File> downloadCallBack) {
		mDownloadCallBack = downloadCallBack;
	}

	public void setBundleId(String bundleId) {
		if (mStarted || mStoped) {
			return;
		}
		mUpdateTask.getRequest().bundle_id = bundleId;
	}

	public void setApiToken(String apiToken) {
		if (mStarted || mStoped) {
			return;
		}
		mUpdateTask.getRequest().api_token = apiToken;
	}

	class FirUpdateTaskListener extends NetTaskListener<FIRUpdateTask.FIRUpdateReq, FIRUpdateTask.FIRUpdateRes> {

		@Override
		public void onException(INetTask<FIRUpdateReq, FIRUpdateRes> task, Exception e) {
			if (mVersionUpdateListener != null) {
				mVersionUpdateListener.onNo();
			}
		}

		@Override
		public void onComplete(INetTask<FIRUpdateReq, FIRUpdateRes> task, FIRUpdateRes response) {
			String version = AppUtil.getAppVersionName(mContext);
			String installUrl = response.installUrl;
			String versionShort = response.versionShort;
			if (StringUtil.compareVersion(version, versionShort) >= 0) {
				if (mVersionUpdateListener != null) {
					mVersionUpdateListener.onNo();
				}
				return;
			}
			if (mVersionUpdateListener != null) {
				if (mVersionUpdateListener.onYes(versionShort)) {
					return;
				}
			}
			StringBuilder sb = new StringBuilder();
			sb.append("最新版本:");
			sb.append(versionShort);
			if (response.binary != null) {
				sb.append("\n更新大小:");
				sb.append(MBFormat.format(response.binary.fsize / MBFormat.MILLION_SIZE));
			}
			sb.append("\n更新内容:\n");
			sb.append(response.changelog);
			FirUpdateAlertListener listener = new FirUpdateAlertListener();
			listener.setVersionShort(versionShort);
			listener.setInstallUrl(installUrl);
			Alert alert = new Alert(mContext);
			alert.setTitle("发现新版本");
			alert.setMessage(sb.toString());
			alert.setOK("立即更新");
			alert.setCancel("以后再说");
			alert.setMessageGravity(Gravity.CENTER_VERTICAL);
			alert.setListener(listener);
			alert.show();
		}
	}

	class FirUpdateAlertListener extends AlertListener {

		protected String mVersionShort;

		protected String mInstallUrl;

		public void setVersionShort(String versionShort) {
			mVersionShort = versionShort;
		}

		public void setInstallUrl(String installUrl) {
			mInstallUrl = installUrl;
		}

		@Override
		public boolean onOK(Alert alert) {
			if (mVersionUpdateListener != null) {
				mVersionUpdateListener.onUpdate(mVersionShort);
			}
			StringBuilder sb = new StringBuilder();
			sb.append(AppUtil.getDiskCacheDir(mContext, "FIR"));
			sb.append("/");
			sb.append(mVersionShort);
			sb.append(".apk");
			new HttpUtils().download(mInstallUrl, sb.toString(), mDownloadCallBack);
			return false;
		}

		@Override
		public boolean onCancel(Alert alert) {
			if (mVersionUpdateListener != null) {
				mVersionUpdateListener.onUpdateCancel(mVersionShort);
			}
			return false;
		}
	}

}