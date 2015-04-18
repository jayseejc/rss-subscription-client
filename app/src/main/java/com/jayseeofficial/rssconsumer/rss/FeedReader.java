package com.jayseeofficial.rssconsumer.rss;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.FeedException;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.SyndFeedInput;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.XmlReader;
import com.jayseeofficial.rssconsumer.rest.Feed;

import java.io.IOException;
import java.net.URL;

/**
 * Created by jon on 25/03/15.
 */
public class FeedReader {
    public static SyndFeed readFeed(Feed feed) throws IOException, FeedException {
        SyndFeedInput input = new SyndFeedInput();
        URL url = new URL(feed.getUri());
        XmlReader reader = new XmlReader(url);
        return input.build(reader);
    }
}
