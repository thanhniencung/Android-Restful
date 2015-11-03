package thenextapp.apistructure.network;

import com.squareup.okhttp.*;

final class ResponseWrapper {
    final com.squareup.okhttp.Response response;
    final Object responseBody;

    ResponseWrapper(Response response, Object responseBody) {
        this.response = response;
        this.responseBody = responseBody;
    }
}