package thenextapp.apistructure.network;


import com.squareup.okhttp.Response;

public class ResponseWrapper {
    public Response response;
    public Object responseBody;

    ResponseWrapper(Response response, Object responseBody) {
        this.response = response;
        this.responseBody = responseBody;
    }
}