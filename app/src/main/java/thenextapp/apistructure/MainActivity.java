package thenextapp.apistructure;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import thenextapp.apistructure.network.callback.Callback;
import thenextapp.apistructure.network.RestAdapter;
import thenextapp.apistructure.network.request.GET;

public class MainActivity extends AppCompatActivity {

    private ImageView ivAvatar;
    private TextView tvName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivAvatar = (ImageView) findViewById(R.id.avatar);
        tvName = (TextView) findViewById(R.id.name);


        ////////////////////////////////////////////////
        Map<String, String> map = new HashMap<String, String>();
        map.put("users", "google");

        GET get = new GET();
        get.setSepareType(GET.SEPARE.SLASH);
        get.setParams(map);
        ////////////////////////////////////////////////


        ////////////////////////////////////////////////
        RestAdapter restAdapter = RestAdapter.getInstance();
        restAdapter.setApiUrl(get.getApiUrl());
        ////////////////////////////////////////////////

        ////////////////////////////////////////////////
        restAdapter.setRequestBuilder(get.buildRequest());
        ////////////////////////////////////////////////

        ApiGit service = restAdapter.create(ApiGit.class);

        service.getUserInfo(new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                if (user != null) {
                    Picasso.with(MainActivity.this).load(user.avatar_url).into(ivAvatar);
                    tvName.setText(user.name);
                }
            }

            @Override
            public void failure(Exception error) {
                error.printStackTrace();
            }
        });

    }

}
