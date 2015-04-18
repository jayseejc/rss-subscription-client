package com.jayseeofficial.rssconsumer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.jayseeofficial.rssconsumer.R;
import com.jayseeofficial.rssconsumer.rest.Feed;
import com.jayseeofficial.rssconsumer.rest.FeedApi;
import com.jayseeofficial.rssconsumer.rest.FeedArray;
import com.jayseeofficial.rssconsumer.rest.ServerError;
import com.jayseeofficial.rssconsumer.rest.ServerResult;
import com.jayseeofficial.rssconsumer.ui.feedlist.FeedItemListActivity;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class DebugActivity extends ActionBarActivity {

    private FeedApi.FeedRequestListener listener = new FeedApi.FeedRequestListener() {
        @Override
        public void onRequestCompleted(FeedApi.Request requestCode, ServerResult result) {
            if (result instanceof FeedArray) {
                List<Feed> feeds = ((FeedArray) result).getFeeds();
                for (Feed feed : feeds) {
                    txtDump.append(feed.getTitle() + "\n");
                }
            }
        }

        @Override
        public void onRequestError(FeedApi.Request request, ServerError error) {
            txtDump.append(request.name() + ": " + error.getErrorMessage());
        }
    };

    @InjectView(R.id.txt_debug_dump)
    TextView txtDump;

    @OnClick(R.id.btn_debug_new_user)
    void newUser() {
        startActivity(new Intent(this, NewUserActivity.class));
    }

    @OnClick(R.id.btn_debug_get_all_feeds)
    void getAllFeeds() {
        FeedApi.getInstance(this).requestAllFeeds();
    }

    @OnClick(R.id.btn_debug_add_feed)
    void addFeed() {
    }

    @OnClick(R.id.btn_debug_show_feeds_list)
    void showFeedsList() {
        startActivity(new Intent(this, FeedItemListActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);
        ButterKnife.inject(this);
        FeedApi.getInstance(this).registerListener(listener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
