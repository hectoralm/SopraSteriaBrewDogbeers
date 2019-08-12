package com.soprasteria.brewdog.webservice;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class HttpUtils {
    public static final String BASE_URL = "https://api.punkapi.com/v2/";
    public static final String GET_BEERS_BY_FOOD = "beers/?food=";

    private static AsyncHttpClient clientAsync;

    static {
        clientAsync = new AsyncHttpClient();
        clientAsync.setMaxRetriesAndTimeout(3, 1000); // Número de intentos y tiempo para cada uno
    }

    public static void get(String url, AsyncHttpResponseHandler responseHandler) {
        clientAsync.get(getAbsoluteUrl(url), responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
