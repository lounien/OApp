package cn.o.app.push;

import cn.o.app.context.IContextProvider;
import cn.o.app.io.INoProguard;

/**
 * Push dispatcher of framework
 */
public interface IPushDispatcher<MESSAGE> extends IContextProvider, INoProguard {

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
	 * @param msg
	 */
	public void onMessage(MESSAGE message);

	/**
	 * Get push manager
	 * 
	 * @return
	 */
	public IPushManager getManager();

	/**
	 * Set push manager
	 * 
	 * @param manger
	 */
	public void setManager(IPushManager manager);

}
