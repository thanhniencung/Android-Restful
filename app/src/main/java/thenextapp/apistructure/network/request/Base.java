package thenextapp.apistructure.network.request;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by Sandy on 11/21/15.
 */
public abstract class Base {

    protected static final MediaType APPLICATION_JSON = MediaType.parse("application/json; charset=utf-8");

    protected Request.Builder builder;
    protected MediaType mediaType;

    public Base() {
        this.builder = new Request.Builder();
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public void header(String name, String value) {
        this.builder.header(name, value);
    }

    public void addHeaders(Map<String, String> headers) {
        if (headers != null) {
            Iterator entries = headers.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry entry = (Map.Entry) entries.next();
                this.builder.addHeader((String) entry.getKey(), (String) entry.getValue());
            }
        }
    }

    public Request.Builder buildRequest() {
        return builder;
    }

    protected abstract void setParams(Map<String, String> params);
}
