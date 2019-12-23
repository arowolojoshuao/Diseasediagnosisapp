package com.ecosa.diseasediagnosisapp.async;

/**
 * Created by Lekan Adigun on 1/10/2018.
 */

public abstract class Task<T> implements Runnable {

    private CallBack mCallBack;
    public Task(CallBack callBack) {
        mCallBack = callBack;
    }
    @Override
    public void run() {
        T result = work();
        if (mCallBack != null) mCallBack.done(result);
    }

    protected abstract T work();

    public interface CallBack<T> {

        void done(T data);
        void error(Throwable throwable);
    }
}
