package cn.o.app.ui.core;

import android.support.v4.app.FragmentManager;
import cn.o.app.core.task.ILockable;

public interface IFragment extends IStateView, ILockable {

	public FragmentManager getParentSupportFragmentManager();

	public boolean isFragmentVisible();

	public void setFragmentVisible(boolean visible);

}
