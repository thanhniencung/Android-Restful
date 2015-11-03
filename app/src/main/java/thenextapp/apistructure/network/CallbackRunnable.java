package thenextapp.apistructure.network;

import java.util.concurrent.Executor;

abstract class CallbackRunnable<T> implements Runnable {
    private final Callback<T> callback;
    private final Executor callbackExecutor;

    CallbackRunnable(Callback<T> callback, Executor callbackExecutor) {
        this.callback = callback;
        this.callbackExecutor = callbackExecutor;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final void run() {
        try {
            final ResponseWrapper wrapper = obtainResponse();
            callbackExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    callback.success(
                            (T) wrapper.responseBody, wrapper.response);
                }
            });
        } catch (final Exception e) {
            callbackExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    callback.failure(e);
                }
            });
        }
    }

    public abstract ResponseWrapper obtainResponse();
}
