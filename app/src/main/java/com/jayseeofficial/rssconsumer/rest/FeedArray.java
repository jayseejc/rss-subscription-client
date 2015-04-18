package com.jayseeofficial.rssconsumer.rest;

import java.util.List;

/**
 * Created by jon on 21/03/15.
 */
public class FeedArray implements ServerResult {
    List<Feed> feeds;

    public List<Feed> getFeeds() {
        return feeds;
    }
}
