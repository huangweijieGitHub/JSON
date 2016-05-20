package com.example.json;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WeatherActivity extends AppCompatActivity {
    //获取
    private Button btn_get;
    //城市
    private EditText et_city;
    //图片
    private ImageView today_img,tomorrow_img,after_img;
    //日期
    private TextView today_date,tomorrow_date,after_date;
    //天气
    private TextView today_weather,tomorrow_weather,after_weather;
    //温度
    private TextView today_tmp,tomorrow_tmp,after_tmp;

    //日期
    private List<String> dateList = new ArrayList<String>();
    //天气
    private List<String> weatherList = new ArrayList<String>();
    //温度
    private List<String> tmpList = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        //初始图片控件
        today_img =(ImageView)findViewById(R.id.today_img);
        tomorrow_img =(ImageView)findViewById(R.id.tomorrow_img);
        after_img =(ImageView)findViewById(R.id.after_img);

        //初始今天天气内容控件
        today_date =(TextView)findViewById(R.id.tody_date);
        today_tmp=(TextView)findViewById(R.id.today_tmp);
        today_weather=(TextView)findViewById(R.id.today_weather);

        //初始明天天气内容控件
        tomorrow_date =(TextView)findViewById(R.id.tomorrow_date);
        tomorrow_tmp=(TextView)findViewById(R.id.tomorrow_tmp);
        tomorrow_weather=(TextView)findViewById(R.id.tomorrow_weather);

        //初始后天天气内容控件
        after_date=(TextView)findViewById(R.id.after_date);
        after_tmp=(TextView)findViewById(R.id.after_tmp);
        after_weather=(TextView)findViewById(R.id.after_weather);

        //初始城市控件
        et_city=(EditText)findViewById(R.id.et_city);

        findViewById(R.id.btn_get).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(et_city.getText().toString()))
                {
                    getWeather(et_city.getText().toString());
                }
                else
                {
                    Toast.makeText(WeatherActivity.this,"输入框不能为空",Toast.LENGTH_SHORT).show();
                }
            }
            });
    }

    /***
     * 获取天气
     *
     * @param city 城市名
     */
   private  void getWeather(String city)
    {
        String url = "https://api.heweather.com/x3/citylist?search="
                +"allworld"+city+"&key=13c196d784fc4ced8dbe3b5970a6cece";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request =new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    //成功
                    @Override
                    public void onResponse(String json) {
                        Log.i("json",json);
                        Volley_Json(json);
                    }

                }, new Response.ErrorListener() {
            //失败
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("Error",volleyError.toString());
            }
        });
        queue.add(request);
        }

    /***
     * 解析Json
     *
     * @param json
     */
      private  void Volley_Json(String json) {
          try {
              JSONObject jsonObject = new JSONObject(json);
              JSONArray jArray = jsonObject
                      .getJSONArray("HeWeather data service 3.0");
              for (int i = 0; i < jArray.length(); i++) {
                  JSONObject jb1 = (JSONObject) jArray.get(i);
                  JSONArray jr1 = jb1.getJSONArray("daily_forecast");
                  for (int j = 0; j < jr1.length(); j++) {
                      JSONObject jb2 = (JSONObject) jr1.get(j);

                      JSONObject jb3 = jb2.getJSONObject("tmp");

                      dateList.add(jb2.getString("date"));
                      weatherList.add(jb2.getJSONObject("cond")
                              .getString("txt_n"));
                      tmpList.add(jb3.getString("min") + "-"
                              + jb3.getString("max"));
                  }
              }

              // 设置参数
              today_date.setText(dateList.get(0));
              today_weather.setText(weatherList.get(0));
              today_tmp.setText(tmpList.get(0));

              tomorrow_date.setText(dateList.get(1));
              tomorrow_weather.setText(weatherList.get(1));
              tomorrow_tmp.setText(tmpList.get(1));

              after_date.setText(dateList.get(2));
              after_weather.setText(weatherList.get(2));
              after_tmp.setText(tmpList.get(2));

              //设置图片
              if (weatherList.get(0).equals("晴")) {
                  today_img.setImageResource(R.mipmap.sun);
              } else if (weatherList.get(0).equals("多云")) {
                  today_img.setImageResource(R.mipmap.cloudy);
              } else {
                  today_img.setImageResource(R.mipmap.rain);
              }

              if (weatherList.get(1).equals("晴")) {
                  tomorrow_img.setImageResource(R.mipmap.sun);
              } else if (weatherList.get(1).equals("多云")) {
                  tomorrow_img.setImageResource(R.mipmap.cloudy);
              } else {
                  tomorrow_img.setImageResource(R.mipmap.rain);
              }

              if (weatherList.get(2).equals("晴")) {
                  after_img.setImageResource(R.mipmap.sun);
              } else if (weatherList.get(2).equals("多云")) {
                  after_img.setImageResource(R.mipmap.cloudy);
              } else {
                  after_img.setImageResource(R.mipmap.rain);
              }

          } catch (JSONException e) {
              e.printStackTrace();
          }

          //清空数据
          dateList.clear();
          weatherList.clear();
          tmpList.clear();
      }


}
