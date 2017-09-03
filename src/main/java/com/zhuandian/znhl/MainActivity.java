package com.zhuandian.znhl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.zhuandian.znhl.MyApplication.MyApplication;
import com.zhuandian.znhl.been.WeatherBeen;
import com.zhuandian.znhl.control.CurtainControl;
import com.zhuandian.znhl.control.DoorControl;
import com.zhuandian.znhl.control.LightControl;
import com.zhuandian.znhl.control.VideoControl;
import com.zhuandian.znhl.data.BasicData;
import com.zhuandian.znhl.data.ExtraData;
import com.zhuandian.znhl.data.OnlineNote;
import com.zhuandian.znhl.feedback.FeedBackActivity;
import com.zhuandian.znhl.myUtils.GlobalVariable;
import com.zhuandian.znhl.other.AboutUs;
import com.zhuandian.znhl.other.Music;
import com.zhuandian.znhl.utils.JsonParser;

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
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView aboutUsImageView;
    private Intent myIntent;
    private ImageView curtainImageView;
    private MediaPlayer mediaPlayer;
    private DatagramSocket ds = null;
    private TextView voiceControl;
    private SpeechRecognizer mIat;
    private String TAG = "xiedong";
    private int EXTRAL_DATA_OK = 2;
    private static final int TTS_INFO_OK = 8;
    private static final int DOOR_INFO_OK = 9;
    private static final int LIGHT_INFO_OK = 7;


    private ImageView weatherImageView;
    private TextView weatherTextView;
    private TextView temperatureTextView;
    private TextView locationTextView;

//    public static String TAG = "LocTestDemo";

    private BroadcastReceiver broadcastReceiver;
    public static String LOCATION_BCR = "location_bcr";

//    private  String url = "http://api.map.baidu.com/telematics/v3/weather?location="
//            +"曲阜" + "&output=json&ak=B95329fb7fdda1e32ba3e3a245193146";

    private int[] bitmaps ={R.drawable.background_1,R.drawable.bg_2};     //定义ImageView的背景资源
    private int index; //背景轮询索引

    private MyRunable myRunable = new MyRunable();
    class MyRunable implements Runnable {

        @Override
        public void run() {
            index++;
            index=index%2;
            ((LinearLayout) findViewById(R.id.head_layout)).setBackgroundResource(bitmaps[index]);
            handler.postDelayed(myRunable,5000);

        }
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {




            if (msg.what == TTS_INFO_OK) {
                Log.v(TAG, "handle的消息来啦");
                String info = (String) msg.obj;
                //利用包装类，把字符串转成整形
                int str = Integer.parseInt(info);
                str = str * 10;
                int str1 = str + 3;
                sendInfo("TYPE:3,CP:" + str + ",Sum:" + str1);
            }

            if (msg.what == DOOR_INFO_OK) {

                sendInfo("TRANS:ADMIN");


//                mediaPlayer= MediaPlayer.create(Extral_Data.this, R.raw.door);
//                mediaPlayer.start();

            }

            if (msg.what == LIGHT_INFO_OK) {
                String info = (String) msg.obj;
                int str = Integer.parseInt(info);
                Log.v("xiedong handle灯 亮度", str + "");
                str = str * 10;
                sendInfo("Light:" + str);
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        handler.postDelayed(myRunable,5000);

        SpeechUtility.createUtility(MainActivity.this, "appid=56ab2608");
        mIat = SpeechRecognizer.createRecognizer(this, mInitListener);

        initView();

        initialize();
//
        initializeViews();
//        locInfo = (TextView) findViewById(R.id.info);
//        locInfo.setText("wod ");
//
        MyApplication.getInstance().requestLocationInfo();
//
//		initializeListeners();
//        Log.d("xiedongzhou", GlobalVariable.LocationInfo);
        String location = "";

        try {
            if (GlobalVariable.LocationInfo.contains("省")) {
                String aa[] = GlobalVariable.LocationInfo.split("省");
                String bb[] = aa[1].split("市");
                location = bb[0];
            } else {
                String aa[] = GlobalVariable.LocationInfo.split("市");

                location = aa[0];

            }
//           String  aa[]="济宁市曲阜市".split("省");

        } catch (NullPointerException e) {
            location = "曲阜";
//          new SweetAlertDialog(this ,SweetAlertDialog.ERROR_TYPE)
//                  .setTitleText("确保网络畅通...")
//                  .show();
        }

        String url = "http://api.map.baidu.com/telematics/v3/weather?location="
                + location + "&output=json&ak=B95329fb7fdda1e32ba3e3a245193146";

        new Weather().execute(url);
//        Log.d("xiedongzhou", GlobalVariable.LocationInfo);

        voiceControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(MainActivity.this, "请开始说话...", Toast.LENGTH_SHORT).show();
//                sendInfo("TYPE:3,CP:500,Sum:503");

                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.start);
                mediaPlayer.start();


                setParam();
                int ret = mIat.startListening(mRecognizerListener);
                Log.d(TAG, "startListening" + ret);

            }
        });


    }


    private void initView() {

        //语音控制按钮
        voiceControl = (TextView) findViewById(R.id.voice);


        weatherImageView = (ImageView) findViewById(R.id.weatherimg);
        weatherTextView = (TextView) findViewById(R.id.weather);
        temperatureTextView = (TextView) findViewById(R.id.temperature);
        locationTextView = (TextView) findViewById(R.id.location);


        findViewById(R.id.curtain).setOnClickListener(this);
        findViewById(R.id.door).setOnClickListener(this);
        findViewById(R.id.light).setOnClickListener(this);
        findViewById(R.id.door).setOnClickListener(this);
        findViewById(R.id.basic_data).setOnClickListener(this);
        findViewById(R.id.extra_data).setOnClickListener(this);
        findViewById(R.id.music).setOnClickListener(this);
        findViewById(R.id.aboutus).setOnClickListener(this);
        findViewById(R.id.video).setOnClickListener(this);
        findViewById(R.id.feedback).setOnClickListener(this);
        findViewById(R.id.cloudnote).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.curtain:
                myIntent = new Intent(MainActivity.this, CurtainControl.class);
                startActivity(myIntent);
                break;

            case R.id.light:
                myIntent = new Intent(MainActivity.this, LightControl.class);
                startActivity(myIntent);
                break;

            case R.id.door:
                myIntent = new Intent(MainActivity.this, DoorControl.class);
                startActivity(myIntent);
                break;

            case R.id.basic_data:
                myIntent = new Intent(MainActivity.this, BasicData.class);
                startActivity(myIntent);
                break;

            case R.id.extra_data:
                myIntent = new Intent(MainActivity.this, ExtraData.class);
                startActivity(myIntent);
                break;

            case R.id.music:
                myIntent = new Intent(MainActivity.this, Music.class);
                startActivity(myIntent);
                break;

            case R.id.aboutus:
                myIntent = new Intent(MainActivity.this, AboutUs.class);
                startActivity(myIntent);
                break;

            case R.id.video:
                myIntent = new Intent(MainActivity.this, VideoControl.class);
                startActivity(myIntent);
                break;

            case R.id.feedback:
                myIntent = new Intent(MainActivity.this, FeedBackActivity.class);
                startActivity(myIntent);
                break;
            case R.id.cloudnote:
                myIntent = new Intent(MainActivity.this, OnlineNote.class);
                startActivity(myIntent);
                break;
        }

    }


    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            Log.d(TAG, "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
//                showTip("初始化失败，错误码：" + code);
            }
        }
    };


    public void setParam() {

        //设置业务类型为听写
        mIat.setParameter(SpeechConstant.DOMAIN, "iat");
        // 设置语言  普通话
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        // 设置语言区域  为北京话
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin");

    }


    /**
     * 听写监听器。
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
//            showTip("开始说话");
        }

        @Override
        public void onError(SpeechError error) {
            // Tips：
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            // 如果使用本地功能（语记）需要提示用户开启语记的录音权限。
//            showTip(error.getPlainDescription(true));
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
//            showTip("结束说话");
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            Log.d(TAG, results.getResultString());
            printResult(results);

            if (isLast) {
                // TODO 最后的结果
            }
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
//            showTip("当前正在说话，音量大小：" + volume);
//            Log.d(TAG, "返回音频数据："+data.length);
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };


    /**
     * 对json字符串进行解析
     *
     * @param results
     */
    private void printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());

        Log.v(TAG, text);

        String localInfo = text;

        Log.v("谢栋", localInfo);


//        .表示除\n之外的任意字符  *表示匹配0-无穷 +表示匹配1-无穷
        String info = text.replaceAll(".*([0-9]{2,}).*", "$1");
        Log.v(TAG, info + "正则的结果哇");

        Message msg = new Message();

        if (localInfo.contains("门")) {
            msg.what = DOOR_INFO_OK;
        }
        if (localInfo.contains("窗帘")) {
            msg.what = TTS_INFO_OK;

        }
        if (localInfo.contains("灯")) {
            msg.what = LIGHT_INFO_OK;
        }

//        msg.what=TTS_INFO_OK;
        msg.obj = info;
        if (info.length() > 1) {
            handler.sendMessage(msg);
        }

//         if(text.contains("一半")) {
//
//              Log.v(TAG,"听懂啦");
////              TYPE:3,CP:50,Sum:53
//              sendInfo("TYPE:3,CP:800,Sum:803");
//              Log.v(TAG,"也发出去啦");
//         }

		/*
         * 1.开始的时候忘了在清单文件中注册了，我说为啥老是停止运行
		 * 2.还有一个错误就是设置了activity的跳转，忘了设置startActivity();咯
		 * */
        //这里做相应的，读取到的字符串然后跳转到另一个了类中
//		Log.d(text,"哈哈");
//		int code = mTts.startSpeaking("重中之重", mTtsListener);
        //如果字符串中包含天气字符，则跳转到天气查询的activity中去，做相应的处理

//        speak(text);


//
//        String sn = null;
//        // 读取json结果中的sn字段
//        try {
//            JSONObject resultJson = new JSONObject(results.getResultString());
//            sn = resultJson.optString("sn");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        mIatResults.put(sn, text);
//
//        StringBuffer resultBuffer = new StringBuffer();
//        for (String key : mIatResults.keySet()) {
//            resultBuffer.append(mIatResults.get(key));
//        }
//
//        mResultText.setText(resultBuffer.toString());
//        mResultText.setSelection(mResultText.length());
    }


    private void sendInfo(final String info) {

        Log.v(TAG, "info来啦" + info);
        new Thread() {

            @Override
            public void run() {


                try {
                    ds = new DatagramSocket(8086);


                    //建立字符输入流
                    BufferedReader bufr = new BufferedReader(new InputStreamReader(System.in));
                    String line = null;


                    Thread.sleep(500);
//                            if (flag == 1) {
//
//                                flag = 2;
                    byte buf[] = info.getBytes();

                    //把得到的数据封装进数据包中
                    DatagramPacket dp =
                            new DatagramPacket(buf, buf.length, InetAddress.getByName("192.168.1.2"), 8086);
                    //通过socket发送出去
                    Log.v("aaaa", "进来啦");
                    ds.send(dp);

                    //从服务端接收先关闭掉，socket为阻塞式操作，给服务端发送了数据，但是一直得不到服务端回馈的数据，所以该方法一直
                    //处于阻塞状态，所以利用语音发送的数据一直得不到响应

//                        Log.v("aaaa", "发出去啦");
//
//
//                        byte buf2[] = new byte[1024];
//
//                        //建立接受端的数据包，并且指定接收端的数据包
//                        DatagramPacket dp1 = new DatagramPacket(buf2, buf2.length);
//
//                        //利用socket的receive方法，接受数据
//                        ds.receive(dp1);
//
//                        str = new String(dp1.getData(), 0, dp1.getLength());
//                        Log.v("info2", str);


//                        //利用Handler把消息发送到主线程
//                        Message msg = new Message();
//                        msg.what = GET_INFO_TYPE0;
//                        msg.obj = str;
//                        handler.sendMessage(msg);
                    ds.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }.start();
    }

    private void initialize() {
        registerBroadCastReceiver();
    }

    private void initializeViews() {
//		locBtn = (Button) findViewById(R.id.location);
//        locInfo = (TextView) findViewById(R.id.info);
    }

    private void initializeListeners()//手势动作监听
    {
//        locBtn.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
////                locInfo.setText("定位中...");
//
//                MyApplication.getInstance().requestLocationInfo();
//            }
//        });
    }

    /**
     * 注册一个广播，监听定位结果
     */
    private void registerBroadCastReceiver() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String address = intent.getStringExtra("address");
                //toast中的第一个参数是设置要显示在哪个界面的上下文中
                Toast toast = Toast.makeText(MainActivity.this, address, Toast.LENGTH_LONG);
                toast.show();
                GlobalVariable.LocationInfo = address;
                Log.d("xiedong", GlobalVariable.LocationInfo + "  ");
                //	locInfo.setText(address+"啊哈哈，这里就是地址的字符串");
            }
        };
        IntentFilter intentToReceiveFilter = new IntentFilter();
        intentToReceiveFilter.addAction(LOCATION_BCR);
        registerReceiver(broadcastReceiver, intentToReceiveFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }


    class Weather extends AsyncTask<String, Void, WeatherBeen> {

//        String url = "http://api.map.baidu.com/telematics/v3/weather?location="
//                +"菏泽" + "&output=json&ak=B95329fb7fdda1e32ba3e3a245193146";

        WeatherBeen weatherBeen = new WeatherBeen();

        @Override
        protected void onPostExecute(WeatherBeen weatherBeen) {


            //防止程序进来没有加载到数据空指针造成闪退，捕获异常
            try {
                weatherImageView.setImageBitmap(weatherBeen.getBitmap());
                weatherTextView.setText(weatherBeen.getWeatherInfo());
                temperatureTextView.setText(weatherBeen.getTemperature());
                locationTextView.setText(weatherBeen.getCurrentCity());
            } catch (NullPointerException e) {
                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("请确保网络畅通")
                        .show();
            }
        }

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
         *
         * @param jsonStr json字符串
         * @return WeatherBeen对象
         */
        private WeatherBeen getWeatherBean(String jsonStr) {


            Log.i("zhou", jsonStr + ".............");
            try {
                JSONObject obj = new JSONObject(jsonStr);

                JSONArray array = obj.getJSONArray("results");

                String currentCity = array.getJSONObject(0).getString("currentCity");
                Log.i("zhou", currentCity + "");

                //首先得到一个json对象中的节点为resultsjson数组，然后得到第一个object对象，再得到第一个对象中的节点为weather_data的数组，
                JSONArray todayWeather = obj.getJSONArray("results").getJSONObject(0).getJSONArray("weather_data");

                //对todayWeather数组中的数据进行提取
                String temperature = todayWeather.getJSONObject(0).getString("temperature");
                String weather = todayWeather.getJSONObject(0).getString("weather");

                String dayPictureUrl = todayWeather.getJSONObject(0).getString("dayPictureUrl");

                Bitmap bitmap = getBitmapFromUrl(dayPictureUrl);

                weatherBeen.setBitmap(bitmap);
                weatherBeen.setWeatherInfo(weather);
                weatherBeen.setTemperature(temperature);
                weatherBeen.setCurrentCity(currentCity);
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
            Log.i("zhou", "aaaaa.............");
            String jsonStr = "";

            //得到url对象
            HttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet(params[0]);

            try {
                HttpResponse response = client.execute(get);

                if (response.getStatusLine().getStatusCode() == 200) {
                    //得到网络输入流
                    InputStream is = response.getEntity().getContent();

                    //用字符流包装下输入流
                    InputStreamReader isr = new InputStreamReader(is);
                    Log.i("zhou", "bbbbb.............");
                    //建立缓冲区
                    BufferedReader br = new BufferedReader(isr);

                    String s = "";

                    while ((s = br.readLine()) != null) {
                        jsonStr += s;
                    }
                    Log.i("zhou", "ccccc.............");
                    is.close();
                    br.close();
                    Log.i("zhou", "bbbbb" + jsonStr);
                    return getWeatherBean(jsonStr);


                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            //对Url进行操作，并且打开连接


            return null;

        }
    }


    /**
     * activity退出时关闭datagramsocket
     */
    @Override
    public void finish() {
//        ds.close();
        super.finish();
    }

    // 当用户在首Activity点击返回键时，提示用户是否退出
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("确定离开 ？")
                    .setContentText("您真的忍心离开 e 家系统吗 ？")
                    .setCancelText("继续使用 ")
                    .setConfirmText("残忍离开")
                    .showCancelButton(true)
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.cancel();
                        }
                    })
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            finish();
                        }
                    })
                    .show();
//            new AlertDialog.Builder(MainActivity.this)
//                    .setTitle("退出")
//                    .setMessage("您确认要退出吗？")
//                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
//
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            // TODO Auto-generated method stub
//                            finish();
//                        }
//                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
//
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    // TODO Auto-generated method stub
//                    dialog.dismiss();
//                }
//            }).show();
        }
        return super.onKeyDown(keyCode, event);
    }
}
