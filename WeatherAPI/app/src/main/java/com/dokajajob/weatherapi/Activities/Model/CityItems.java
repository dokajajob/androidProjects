package com.dokajajob.weatherapi.Activities.Model;

import android.media.Image;
import android.widget.ImageView;

public class CityItems {

    private String CityName;
    private String CityTemperature;
    private ImageView imageView;


    public CityItems() {
    }

    public CityItems(String cityName, String cityTemperature, ImageView imageView) {
        CityName = cityName;
        CityTemperature = cityTemperature;
        this.imageView = imageView;
    }


    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    public String getCityTemperature() {
        return CityTemperature;
    }

    public void setCityTemperature(String cityTemperature) {
        CityTemperature = cityTemperature;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }


}
