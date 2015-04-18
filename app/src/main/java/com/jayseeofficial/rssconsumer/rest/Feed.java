package com.jayseeofficial.rssconsumer.rest;

/**
 * Created by jon on 21/03/15.
 */
public class Feed implements ServerResult {
    private String title;
    private String uri;
    private int id = -1;
    private String url;

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public Feed(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public String getUri() {
        return uri;
    }

    @Override
    public String toString() {
        return "{\"id\":\"" + id + "\",\"title\":\"" + title + "\",\"uri\":\"" + uri + "\",\"url\":\"" + url + "\"}";
    }
}
