package thenextapp.apistructure.network;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.*;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RestAdapter {

    private  Executor httpExecutor = null;
    private  Executor callbackExecutor = null;

    private String rootUrl = "";
    private Type type;
    private Object reponseObject;
    private static OkHttpClient okHttpClient = null;

    public RestAdapter setRootUrl(String url) {
        this.rootUrl = url;
        return this;
    }

    private String getRootUrl() {
        return this.rootUrl;
    }

    public void build() {
        initExecutor();
    }

    public RestAdapter setMethod() {
        // POST - PUT - GET - DELETE
        return this;
    }

    public RestAdapter setHeader() {
        // setup header for request
        return this;
    }

    private void initExecutor() {
        if (httpExecutor == null) {
            httpExecutor =  Executors.newCachedThreadPool();
        }

        if (callbackExecutor == null) {
            callbackExecutor = new MainThreadExecutor();
        }
    }

    private OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }
        return okHttpClient;
    }

    @SuppressWarnings("unchecked")
    public <T> T create(Class<T> service) {
        return (T) Proxy.newProxyInstance(service.getClassLoader(),
                new Class<?>[]{service}, new RestHandler());
    }

    private class RestHandler implements InvocationHandler {

        @Override
        public Object invoke(Object o, final Method method, Object[] objects) throws Throwable {

            type = method.getGenericReturnType();

            thenextapp.apistructure.network.Callback<?> callback = (thenextapp.apistructure.network.Callback<?>) objects[objects.length - 1];

            httpExecutor.execute(new CallbackRunnable(callback, callbackExecutor) {
                @Override
                public ResponseWrapper obtainResponse() {
                    return (ResponseWrapper) invokeRequest(getRootUrl());
                }
            });

            return null;
        }
    }

    static class MainThreadExecutor implements Executor {
        private final Handler handler = new Handler(Looper.getMainLooper());
        @Override public void execute(Runnable r) {
            handler.post(r);
        }
    }

    public Object invokeRequest(String url)  {
        try {
            OkHttpClient client = getOkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();

            if(type.equals(String.class)) {
                reponseObject = response.body().string();
            } else {
                Gson gson = new GsonBuilder()
                        .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                        .serializeNulls()
                        .create();
                reponseObject = gson.fromJson(response.body().string(), type);
            }

            return new ResponseWrapper(response, reponseObject);

        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return null;
    }
}
