package cn.mutils.app.ui.web;

import cn.mutils.app.core.INoProguard;
import cn.mutils.app.os.IContextProvider;

/**
 * Web message dispatcher of framework
 *
 * @param <MESSAGE>
 */
public interface IWebMessageDispatcher<MESSAGE> extends IContextProvider, INoProguard {

	/**
	 * Whether to intercept translate message
	 * 
	 * @return Return true to intercept dispatching
	 */
	public boolean preTranslateMessage();

	/**
	 * Translate dispatcher to MESSAGE
	 * 
	 * @return
	 */
	public MESSAGE translateMessage();

	/**
	 * Dispatch message
	 * 
	 * @param message
	 */
	public void onMessage(MESSAGE message);

	/**
	 * Give result of dispatched message to web message manager
	 * 
	 * @param message
	 */
	public void notifyManager(MESSAGE message);

	/**
	 * Get web message manager
	 * 
	 * @return
	 */
	public IWebMessageManager getManager();

	/**
	 * Set web message manager
	 * 
	 * @param manager
	 */
	public void setManager(IWebMessageManager manager);

}