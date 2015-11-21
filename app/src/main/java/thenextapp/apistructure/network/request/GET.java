package thenextapp.apistructure.network.request;

import com.squareup.okhttp.Request;

import java.util.Iterator;
import java.util.Map;

import thenextapp.apistructure.network.config.ApiConfig;

/**
 * Created by Sandy on 11/21/15.
 */
public class GET extends Base {

    public enum SEPARE {
        SLASH, NORMAL
    }

    private SEPARE separeType;

    private String separeString = "";

    private String apiUrl = "";

    public GET() {
        super();
    }

    @Override
    public void setParams(Map<String, String> params) {
        StringBuilder stringBuilder = new StringBuilder();

        if (getSepareType() == SEPARE.SLASH) {
            this.separeString = "/";
        } else {
            this.separeString = "&";
        }

        if (params != null && params.size() > 0) {
            Iterator entries = params.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry entry = (Map.Entry) entries.next();
                stringBuilder.append((String) entry.getKey() + this.separeString + (String) entry.getValue());
            }
            apiUrl = ApiConfig.ROOT + stringBuilder.toString();
        }
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setSepareType(SEPARE queryType) {
        separeType = queryType;
    }

    public SEPARE getSepareType() {
        return separeType;
    }
}
