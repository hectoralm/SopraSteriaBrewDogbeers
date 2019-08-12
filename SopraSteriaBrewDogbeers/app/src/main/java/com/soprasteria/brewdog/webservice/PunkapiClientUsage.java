package com.soprasteria.brewdog.webservice;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.soprasteria.brewdog.objects.Beer;
import com.soprasteria.brewdog.MainActivity;
import com.soprasteria.brewdog.utilities.Utils;

import org.json.JSONException;
import java.util.ArrayList;
import cz.msebera.android.httpclient.Header;

public class PunkapiClientUsage{

    public interface OnRestFullCallback {
        void onRestFullResponse(boolean result, ArrayList<Beer> beers);
    }

    public void getBeersByFood (final Context context, final String food, final OnRestFullCallback callback) throws JSONException{
        Log.i(this.getClass().getName(), "Buscando cervezas compatibles con [" + food + "].");

        HttpUtils.get(HttpUtils.GET_BEERS_BY_FOOD + food, new BaseJsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                Log.i(this.getClass().getName(), "OnSuccess status: " + statusCode + " response: " + rawJsonResponse);

                //Almacenar la busqueda en sharedPreferences
                SharedPreferences prefs = context.getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
                prefs.edit().putString(food, rawJsonResponse).commit();

                //Lista actual de cervezas
                if (!rawJsonResponse.equals("")) {
                    prefs.edit().putString(MainActivity.CURRENT_BEER_LIST, rawJsonResponse).commit();
                }

                ArrayList<Beer> arrayBeer = Utils.jsonToBeerArrayList(rawJsonResponse);
                callback.onRestFullResponse(true, arrayBeer);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, Object errorResponse) {
                Log.i(this.getClass().getName(), "onFailure status: " + statusCode + " response: " + rawJsonData);
                callback.onRestFullResponse(false, null);
            }

            @Override
            protected Object parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return null;
            }
        });
    }
}
