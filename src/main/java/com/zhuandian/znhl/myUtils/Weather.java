package com.zhuandian.znhl.myUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.zhuandian.znhl.been.WeatherBeen;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by 谢栋 on 2016/8/6.
 */
public class Weather extends AsyncTask<String ,Void ,WeatherBeen>{

    String url = "http://api.map.baidu.com/telematics/v3/weather?location="
            +"菏泽" + "&output=json&ak=B95329fb7fdda1e32ba3e3a245193146";


//    public  WeatherBeen getWeatherJsonStr(String url)
//    {
//        String jsonStr="";
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }).start();
//        try {
//            //得到url对象
//            URL urlWeather=new URL(url);
//
//            try {
//               //对Url进行操作，并且打开连接
//                HttpURLConnection connection = (HttpURLConnection) urlWeather.openConnection();
//
//                //得到网络输入流
//                InputStream is=connection.getInputStream();
//
//                //用字符流包装下输入流
//                InputStreamReader isr =new InputStreamReader(is);
//
//                //建立缓冲区
//                BufferedReader br =new BufferedReader(isr);
//
//                String s="";
//
//                while((s=br.readLine())!=null){
//                    jsonStr+=s;
//                }
//                return getWeatherBean(jsonStr);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }

    /**
     * 从天气json字符串中解析出相应数据
     * @param jsonStr  json字符串
     * @return         WeatherBeen对象
     */
    private  WeatherBeen getWeatherBean(String jsonStr) {

        WeatherBeen weatherBeen = new WeatherBeen();
        Log.i("zhou",jsonStr+".............");
        try {
            JSONObject obj = new JSONObject(jsonStr);

            JSONArray array = obj.getJSONArray("results");

            String currentCity = array.getJSONObject(0).getString("currentCity");
            Log.i("zhou",currentCity+"");

            //首先得到一个json对象中的节点为resultsjson数组，然后得到第一个object对象，再得到第一个对象中的节点为weather_data的数组，
            JSONArray todayWeather=obj.getJSONArray("results").getJSONObject(0).getJSONArray("weather_data");

           //对todayWeather数组中的数据进行提取
            String temperature= todayWeather.getJSONObject(0).getString("temperature");
            String weather =todayWeather.getJSONObject(0).getString("weather");

            String dayPictureUrl = todayWeather.getJSONObject(0).getString("dayPictureUrl");

            Bitmap bitmap = getBitmapFromUrl(dayPictureUrl);

            weatherBeen.setBitmap(bitmap);
            weatherBeen.setWeatherInfo(weather);
            weatherBeen.setTemperature(temperature);
//            Log.i("zhou",temperature+weather+dayPictureUrl+"");
            return weatherBeen;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Bitmap getBitmapFromUrl(String dayPictureUrl) {

        Bitmap bitmap = null;

        try {
            URL mUrl = new URL(dayPictureUrl);

            HttpURLConnection connection = (HttpURLConnection) mUrl.openConnection();
            InputStream is = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected WeatherBeen doInBackground(String... params) {
        Log.i("zhou","aaaaa.............");
        String jsonStr = "";

            //得到url对象
           HttpClient client =new DefaultHttpClient();
            HttpGet get=new HttpGet(params[0]);

        try {
            HttpResponse response =client.execute(get);

            if(response.getStatusLine().getStatusCode()==200){
                //得到网络输入流
                InputStream is = response.getEntity().getContent();

                //用字符流包装下输入流
                InputStreamReader isr = new InputStreamReader(is);
                Log.i("zhou","bbbbb.............");
                //建立缓冲区
                BufferedReader br = new BufferedReader(isr);

                String s = "";

                while ((s = br.readLine()) != null) {
                    jsonStr += s;
                }
                Log.i("zhou","ccccc.............");
                is.close();
                br.close();
                Log.i("zhou","bbbbb"+jsonStr);
                return getWeatherBean(jsonStr);


            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        //对Url进行操作，并且打开连接



        return null;

    }
}
