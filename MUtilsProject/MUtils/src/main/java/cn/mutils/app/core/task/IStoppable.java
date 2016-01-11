package cn.mutils.app.core.task;

@SuppressWarnings("unused")
public interface IStoppable {

    boolean isRunInBackground();

    void setRunInBackground(boolean runInBackground);

    boolean isStopped();

    boolean stop();

}
