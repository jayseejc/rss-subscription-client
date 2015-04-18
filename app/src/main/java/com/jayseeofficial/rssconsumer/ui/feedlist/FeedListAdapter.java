package com.jayseeofficial.rssconsumer.ui.feedlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jayseeofficial.rssconsumer.R;
import com.jayseeofficial.rssconsumer.rest.Feed;
import com.jayseeofficial.rssconsumer.rest.FeedArray;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by jon on 25/03/15.
 */
public class FeedListAdapter extends ArrayAdapter {

    private Context context;
    private List<Feed> feeds;

    private static final int FEED_ITEM_LAYOUT = R.layout.feed_list_item;

    @Override
    public int getCount() {
        return feeds.size();
    }

    @Override
    public Object getItem(int position) {
        return feeds.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(context).inflate(FEED_ITEM_LAYOUT, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        holder.txtTitle.setText(feeds.get(position).getTitle());
        holder.txtDetails.setText(feeds.get(position).getUrl());

        return convertView;
    }

    public FeedListAdapter(Context context, FeedArray feeds) {
        super(context, R.layout.feed_list_item);
        this.context = context;
        this.feeds = feeds.getFeeds();
    }

    public FeedListAdapter(Context context, List<Feed> feeds) {
        super(context, R.layout.feed_list_item);
        this.context = context;
        this.feeds = feeds;
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'feed_list_item.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {
        @InjectView(R.id.txt_title)
        TextView txtTitle;
        @InjectView(R.id.txt_details)
        TextView txtDetails;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
