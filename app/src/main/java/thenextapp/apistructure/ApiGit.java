package thenextapp.apistructure;

import thenextapp.apistructure.network.callback.Callback;

public interface ApiGit {

    User getUserInfo(Callback<User> callback);

    String getStringUserInfo(Callback<String> callback);

}
