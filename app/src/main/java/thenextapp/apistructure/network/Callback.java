package thenextapp.apistructure.network;

import com.squareup.okhttp.*;

public interface Callback<T> {
    void success(T t, Response response);
    void failure(Exception error);
}
