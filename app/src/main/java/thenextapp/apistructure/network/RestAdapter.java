package thenextapp.apistructure.network;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.squareup.okhttp.*;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import thenextapp.apistructure.network.callback.*;
import thenextapp.apistructure.network.callback.Callback;

public class RestAdapter {

    private  Executor httpExecutor = null;
    private  Executor callbackExecutor = null;

    private String apiUrl = "";
    private Class<?> clazz;
    private Object reponseObject;
    private Request.Builder requestBuilder;

    private static OkHttpClient okHttpClient = null;
    private static RestAdapter restAdapter = null;

    public static RestAdapter getInstance() {
        if (restAdapter == null) {
            restAdapter = new RestAdapter();
        }
        return restAdapter;
    }

    public RestAdapter() {
        initExecutor();
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    private String getApiUrl() {
        return this.apiUrl;
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

            Type[] types = method.getGenericParameterTypes();
            ParameterizedType pType = (ParameterizedType) types[0];

            clazz = (Class<?>) pType.getActualTypeArguments()[0];

            Callback<?> callback = (Callback<?>) objects[objects.length - 1];

            httpExecutor.execute(new CallbackRunnable(callback, callbackExecutor) {
                @Override
                public ResponseWrapper obtainResponse() {
                    return (ResponseWrapper) invokeRequest(getApiUrl());
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

    public void setRequestBuilder(Request.Builder requestBuilder) {
        this.requestBuilder = requestBuilder;
    }

    public Object invokeRequest(String url)  {
        try {

            OkHttpClient client = getOkHttpClient();
            Response response = client.newCall(requestBuilder.url(url).build()).execute();

            if (clazz.equals(String.class)) {
                reponseObject = response.body().string();
            } else {
                Gson gson = new GsonBuilder()
                        .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                        .serializeNulls()
                        .create();
                reponseObject = gson.fromJson(response.body().string(), clazz);
            }

            return new ResponseWrapper(response, reponseObject);

        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return null;
    }
}
