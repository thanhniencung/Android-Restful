package thenextapp.apistructure;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Sandy on 11/2/15.
 */
public class User {
    @Expose
    @SerializedName("email")
    public String email;

    @Expose
    @SerializedName("avatar_url")
    public String avatar_url;

    @Expose
    @SerializedName("name")
    public String name;
}
