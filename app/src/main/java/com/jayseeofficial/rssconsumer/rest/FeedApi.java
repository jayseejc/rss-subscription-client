package com.jayseeofficial.rssconsumer.rest;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jayseeofficial.rssconsumer.Application;
import com.jayseeofficial.rssconsumer.Constants;
import com.jayseeofficial.rssconsumer.rest.retrofit.RssProviderService;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by jon on 21/03/15.
 */
public class FeedApi {

    public static enum Request {
        NEW_USER,
        GET_FEED,
        GET_ALL_FEEDS,
        NEW_FEED,
        DELETE_FEED
    }

    private RssProviderService service;
    //private Context context;
    private String username = Constants.TEST_USER;
    private String password = Constants.TEST_PASSWORD;

    private List<FeedRequestListener> listeners;

    private FeedApi(Context context) {
        //this.context = context.getApplicationContext();
        service = new RestAdapter.Builder()
                .setEndpoint(Constants.SERVER_ADDRESS)
                .build().create(RssProviderService.class);
        listeners = new ArrayList<>();
    }

    private static FeedApi api = null;

    public static FeedApi getInstance(Context context) {
        if (api == null) api = new FeedApi(context);
        return api;
    }

    public static FeedApi getInstance() {
        return getInstance(Application.getInstance());
    }

    public void registerListener(FeedRequestListener listener) {
        listeners.add(listener);
    }

    public void removeListener(FeedRequestListener listener) {
        listeners.remove(listener);
    }

    public void requestNewUser() {
        service.newUser(username, password, new RssProviderCallback(Request.NEW_FEED));
    }

    public void requestFeed(int feedId) {
        service.getFeed(username, password, feedId, new RssProviderCallback(Request.GET_FEED));
    }

    public void requestAllFeeds() {
        service.getAllFeeds(username, password, new RssProviderCallback(Request.GET_ALL_FEEDS));
    }

    public void requestNewFeed(Feed feed) {
        String title = feed.getTitle();
        String url = feed.getUrl();
        service.newFeed(username, password, title, url, new RssProviderCallback(Request.NEW_FEED));
    }

    public void deleteFeed(Feed feed) {
        int feedId = feed.getId();
        // Feed of -1 means it's local only
        if (feedId != -1)
            service.deleteFeed(username, password, feedId, new RssProviderCallback(Request.DELETE_FEED));
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private class RssProviderCallback implements Callback<JsonElement> {

        private Gson gson = new Gson();

        private Request requestCode;

        public RssProviderCallback(Request requestCode) {
            this.requestCode = requestCode;
        }

        @Override
        public void success(JsonElement e, Response response) {
            JsonObject json = e.getAsJsonObject();
            if (json.has("error")) {
                ServerError error = gson.fromJson(e, ServerError.class);
                error.setErrorcode(response.getStatus());
                for (FeedRequestListener listener : listeners)
                    listener.onRequestError(requestCode, error);
            } else if (json.has("feed")) {
                e = e.getAsJsonObject().get("feed");
                Feed f = gson.fromJson(e, Feed.class);
                Log.d("Feed", f.toString());
                for (FeedRequestListener listener : listeners)
                    listener.onRequestCompleted(requestCode, f);
            } else if (json.has("feeds")) {
                FeedArray feeds = gson.fromJson(e, FeedArray.class);
                for (FeedRequestListener listener : listeners)
                    listener.onRequestCompleted(requestCode, feeds);
            } else {
                // Happens when requesting a new user for instance
                for (FeedRequestListener listener : listeners)
                    listener.onRequestCompleted(requestCode, null);
            }
        }

        @Override
        public void failure(RetrofitError error) {
            ServerError serverError = new ServerError();
            Log.d(getClass().getSimpleName(), error.getBody().toString());
            serverError.setErrorMessage(error.getMessage());
            for (FeedRequestListener listener : listeners)
                listener.onRequestError(requestCode, serverError);
        }
    }

    public interface FeedRequestListener {
        public void onRequestCompleted(Request requestCode, ServerResult result);

        public void onRequestError(Request request, ServerError error);
    }

}
