package com.example.json;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;

/**
 * Created by 伟捷。 on 2016/5/20.
 */
public class SingleActivity extends Activity {

    private Button btn_call;
    private TextView tv_content;
    private EditText et_number;
    private  String myphone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);

        et_number=(EditText)findViewById(R.id.phone);
        tv_content=(TextView)findViewById(R.id.tv_content);

        findViewById(R.id.btn_call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myphone=et_number.getText().toString().trim();
                if (et_number==null)
                {
                    Toast.makeText(SingleActivity.this,"号码不能为空"
                    ,Toast.LENGTH_SHORT);
                }
                else
                {
                    Volley_Get();
                }
            }
        });

    }

    /***
     * 解析接口
     */
    private void Volley_Get() {

        String url ="http://apis.juhe.cn/mobile/get?phone="+
                myphone+"&key=22a6ba14995ce26dd0002216be51dabb";


        RequestQueue queue =Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Method.PUBLIC, url,
                new Response.Listener<String>() {
                    //成功
                    @Override
                    public void onResponse(String json) {
                        Log.i("Json", json);
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
     * @param json
     */
    private void Volley_Json(String json) {

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject object =jsonObject.getJSONObject("result");
            tv_content.setText("归属地:"+object.getString("province")
                    +"-"+object.getString("city")+"\n"+"区号:"
                    +object.getString("areacode")+"\n"+"运营商:"+
            object.getString("company")+"\n"+"用户类型:"
                    +object.getString("card"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
