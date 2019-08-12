package com.soprasteria.brewdog.objects;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

public class Beer implements Comparable<Beer>{
    //Campos obligatorios
    String beerName;
    String tagLine;
    String description;
    String image_url;
    Double abv;


    public static Beer convertFromJSONToBeer (JSONObject json) throws JSONException {
        Beer beer = null;
        if (json != null) {
            beer = new Beer();
            beer.setBeerName( json.get("name").toString());
            beer.setTagLine( json.get("tagline").toString());
            beer.setDescription(json.get("description").toString());
            beer.setImage_url(json.get("image_url").toString());
            beer.setAbv(Double.parseDouble(json.get("abv").toString()));
        }
        return beer;
    }

    public Beer() {

    }

    public String getBeerName() {
        return beerName;
    }

    public void setBeerName(String beerName) {
        this.beerName = beerName;
    }

    public String getTagLine() {
        return tagLine;
    }

    public void setTagLine(String tagLine) {
        this.tagLine = tagLine;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public Double getAbv() {
        return abv;
    }

    public void setAbv(Double abv) {
        this.abv = abv;
    }

    @Override
    public String toString() {
        return "Beer{" +
                "beerName='" + beerName + '\'' +
                ", tagLine='" + tagLine + '\'' +
                ", description='" + description + '\'' +
                ", image_url='" + image_url + '\'' +
                ", abv=" + abv +
                '}';
    }

    //Implements de comparable.
    @Override
    public int compareTo(@NonNull Beer beer) {
        return this.getAbv().compareTo(beer.getAbv());
    }

}
