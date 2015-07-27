package cn.o.app.push;

import java.util.ArrayList;
import java.util.List;

import cn.o.app.context.ContextOwner;
import cn.o.app.json.JsonUtil;

/**
 * Implements {@link IPushManager}
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class PushManager extends ContextOwner implements IPushManager {

	protected List<Class<? extends IPushDispatcher<?>>> mDispatchers;

	@Override
	public void add(Class<? extends IPushDispatcher<?>> dispatcherClass) {
		if (mDispatchers == null) {
			mDispatchers = new ArrayList<Class<? extends IPushDispatcher<?>>>();
		}
		mDispatchers.add(dispatcherClass);
	}

	@Override
	public void onMessage(String message) {
		if (mDispatchers == null) {
			return;
		}
		Object json = null;
		try {
			json = JsonUtil.toJSON(message);
		} catch (Exception e) {
			return;
		}
		for (Class<? extends IPushDispatcher<?>> dispatcherClass : mDispatchers) {
			IPushDispatcher dispatcher = null;
			try {
				dispatcher = JsonUtil.convertFromJson(json, dispatcherClass);
				dispatcher.setManager(this);
				if (!dispatcher.preTranslateMessage()) {
					Object msg = dispatcher.translateMessage();
					if (msg != null) {
						dispatcher.onMessage(msg);
					}
				}
			} catch (Exception e) {

			} finally {
				if (dispatcher != null) {
					dispatcher.setManager(null);
				}
			}
		}
	}

}