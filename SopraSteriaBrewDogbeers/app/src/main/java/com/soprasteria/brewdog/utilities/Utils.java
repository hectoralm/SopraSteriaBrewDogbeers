package com.soprasteria.brewdog.utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.soprasteria.brewdog.objects.Beer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collections;

public class Utils {

    public static Bitmap getBitmapFromURL(String src) {
        try {
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    //TODO que pasa si devuelve null
    public static ArrayList<Beer> jsonToBeerArrayList(String json) {
        JSONArray jsonArray = null;
        ArrayList<Beer> arrayBeer = null;
        try {
            jsonArray = new JSONArray(json);
            arrayBeer = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject tmp_jsonObject = jsonArray.getJSONObject(i);
                Beer beer = Beer.convertFromJSONToBeer(tmp_jsonObject);
                arrayBeer.add(beer);
            }
            //Ordenar de forma ascendente por el valor 'abv'.
            Collections.sort(arrayBeer);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayBeer;
    }
}
