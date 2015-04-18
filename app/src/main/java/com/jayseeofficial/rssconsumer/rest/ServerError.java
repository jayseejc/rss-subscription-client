package com.jayseeofficial.rssconsumer.rest;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jon on 21/03/15.
 */
public class ServerError {
    private int errorcode = 200;
    @SerializedName("error")
    private
    String errorMessage;

    public int getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(int errorcode) {
        this.errorcode = errorcode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
