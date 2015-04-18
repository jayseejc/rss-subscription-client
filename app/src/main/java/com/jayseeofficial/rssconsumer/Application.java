package com.jayseeofficial.rssconsumer;

/**
 * Created by jon on 25/03/15.
 */
public class Application extends android.app.Application {

    private static Application instance;

    @Override
    public void onCreate() {
        instance = this;
    }

    public static Application getInstance() {
        return instance;
    }

}
