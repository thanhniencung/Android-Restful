package thenextapp.apistructure.network.request;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Sandy on 11/21/15.
 */
public class POST extends Base {

    private String apiUrl = "";

    public POST() {
        super();
    }

    @Override
    protected void setParams(Map<String, String> params) {
        try {
            JSONObject json = new JSONObject();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                try {
                    json.put(key, value);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            RequestBody requestBody = RequestBody.create(super.mediaType, json.toString());
            super.builder.post(requestBody);

        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }

    public String getApiUrl() {
        return this.apiUrl;
    }

    public void setUrl(String url) {
        this.apiUrl = url;
    }
}
