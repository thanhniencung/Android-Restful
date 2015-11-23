package thenextapp.apistructure;

import thenextapp.apistructure.network.callback.Callback;

public interface ApiGit {

    void getUserInfo(Callback<User> callback);

    void getStringUserInfo(Callback<String> callback);

}
