package thenextapp.apistructure;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import thenextapp.apistructure.network.Callback;
import thenextapp.apistructure.network.RestAdapter;

public class MainActivity extends AppCompatActivity {

    private String ROOT = "https://api.github.com/users/google";

    private ImageView ivAvatar;
    private TextView tvName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivAvatar = (ImageView) findViewById(R.id.avatar);
        tvName = (TextView) findViewById(R.id.name);

        RestAdapter restAdapter = new RestAdapter();
        restAdapter
                .setHeader()
                .setRootUrl(ROOT)
                .build();

        ApiGit service = restAdapter.create(ApiGit.class);

        service.getUserInfo(new Callback<User>() {

            @Override
            public void success(User user, Response response) {
                User u =  user;
                Picasso.with(MainActivity.this).load(u.avatar_url).into(ivAvatar);
                tvName.setText(u.name);
            }

            @Override
            public void failure(Exception error) {

            }
        });

    }

}
