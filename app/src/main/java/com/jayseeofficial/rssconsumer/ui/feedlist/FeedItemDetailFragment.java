package com.jayseeofficial.rssconsumer.ui.feedlist;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.FeedException;
import com.jayseeofficial.rssconsumer.R;
import com.jayseeofficial.rssconsumer.rest.Feed;
import com.jayseeofficial.rssconsumer.rest.FeedApi;
import com.jayseeofficial.rssconsumer.rest.ServerError;
import com.jayseeofficial.rssconsumer.rest.ServerResult;
import com.jayseeofficial.rssconsumer.rss.FeedReader;
import com.jayseeofficial.rssconsumer.rss.SyndFeedAdapter;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * A fragment representing a single FeedItem detail screen.
 * This fragment is either contained in a {@link FeedItemListActivity}
 * in two-pane mode (on tablets) or a {@link FeedItemDetailActivity}
 * on handsets.
 */
public class FeedItemDetailFragment extends Fragment {

    public static final String ARG_ITEM_ID = "item_id";

    private Feed mItem;
    private int feedId = -1;

    View contentView;
    View loadingView;
    View errorView;
    View[] allViews;
    ListView itemList;
    TextView txtError;

    boolean loading = false;
    boolean error = false;

    FeedApi.FeedRequestListener listener = new FeedApi.FeedRequestListener() {
        @Override
        public void onRequestCompleted(FeedApi.Request requestCode, ServerResult result) {
            if (requestCode == FeedApi.Request.GET_FEED) {
                loading = false;
                loadFeed((Feed) result);
            }
        }

        @Override
        public void onRequestError(FeedApi.Request request, ServerError serverError) {
            loading = false;
            error = true;
            txtError.setText(serverError.getErrorMessage());
            showLoadingOrContent();
        }
    };

    public FeedItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            feedId = Integer.parseInt(getArguments().getString(ARG_ITEM_ID));
            loadFeedInfo();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_feeditem_detail, container, false);
        contentView = rootView.findViewById(R.id.layout_content);
        loadingView = rootView.findViewById(R.id.layout_loading);
        errorView = rootView.findViewById(R.id.layout_error);
        allViews = new View[]{contentView, loadingView, errorView};
        itemList = (ListView) rootView.findViewById(R.id.lst_items);
        txtError = (TextView) rootView.findViewById(R.id.txt_error);
        showLoadingOrContent();

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FeedApi.getInstance().registerListener(listener);
    }

    @Override
    public void onStop() {
        super.onStop();
        FeedApi.getInstance().removeListener(listener);
    }

    private void loadFeed(Feed result) {
        new AsyncTask<Feed, Void, Boolean>() {
            private SyndFeed feed;
            private Exception exception;

            @Override
            protected Boolean doInBackground(Feed... feeds) {
                try {
                    feed = FeedReader.readFeed(feeds[0]);
                    return true;
                } catch (IOException | FeedException e) {
                    exception = e;
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean success) {
                if (success) {
                    loading = false;
                    itemList.setAdapter(new SyndFeedAdapter(getActivity(), feed));
                } else {
                    error = true;
                    if(exception instanceof FileNotFoundException){
                        txtError.setText("404: Not found");
                    } else {
                        exception.printStackTrace();
                        txtError.setText(exception.getMessage());
                    }
                }
                showLoadingOrContent();
            }
        }.execute(result);
    }

    private void showLoadingOrContent() {
        for (View view : allViews) {
            view.setVisibility(View.GONE);
        }
        if (error) {
            errorView.setVisibility(View.VISIBLE);
            return;
        }
        if (loading) {
            loadingView.setVisibility(View.VISIBLE);
            return;
        } else {
            contentView.setVisibility(View.VISIBLE);
            return;
        }
    }

    private void loadFeedInfo() {
        loading = true;
        FeedApi.getInstance().requestFeed(feedId);
    }

}
