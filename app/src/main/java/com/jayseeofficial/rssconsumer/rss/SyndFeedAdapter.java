package com.jayseeofficial.rssconsumer.rss;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEntry;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;
import com.jayseeofficial.rssconsumer.R;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by jon on 25/03/15.
 */
public class SyndFeedAdapter extends ArrayAdapter {

    List<SyndEntry> entries;
    Context context;

    private static final int ITEM_LAYOUT = R.layout.feed_list_item;

    public SyndFeedAdapter(Context context, SyndFeed feed) {
        super(context, ITEM_LAYOUT);
        entries = feed.getEntries();
        this.context = context;
    }

    @Override
    public int getCount() {
        return entries.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(context).inflate(ITEM_LAYOUT, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        holder.txtTitle.setText(entries.get(position).getTitle());
        holder.txtDetails.setText(entries.get(position).getLink());

        return convertView;
    }

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
