package com.zhuandian.znhl.been;

import android.graphics.Bitmap;

/**
 * Created by 谢栋 on 2016/8/6.
 */
public class WeatherBeen {

    private String temperature="";
    private String currentCity="";
    private String weatherInfo="";
    private Bitmap bitmap=null;

    public String getCurrentCity() {
        return currentCity;
    }

    public void setCurrentCity(String currentCity) {
        this.currentCity = currentCity;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }
    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }



    public String getWeatherInfo() {
        return weatherInfo;
    }

    public void setWeatherInfo(String weatherInfo) {
        this.weatherInfo = weatherInfo;
    }
}
