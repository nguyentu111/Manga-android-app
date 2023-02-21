package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.os.Bundle;


import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ChapterPage extends AppCompatActivity {
    Button showbtn;
    LinearLayout showRs;
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_page);
        showbtn = findViewById(R.id.showbtn);
        showRs = findViewById(R.id.showRs);
        showRs.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setShowRs();

        url="https://api.mangadex.org/manga";

        RequestQueue queue = Volley.newRequestQueue(this);

        TextView textView = findViewById(R.id.textview);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray datas=   response.getJSONArray("data");
                            for(int i =0;i<datas.length();i++){
                                JSONObject data = datas.getJSONObject(i);
                                Log.v("data: ",data.getString("id"));
                            }
                            textView.setText("Response is: " + response.getString("data"));
                            Log.v("id",response.getString("data"));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        textView.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        showbtn.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });
        queue.add(jsonObjectRequest);
// Access the RequestQueue through your singleton class.
      //  MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
            loadImg();
    }
    private void setShowRs(){
        int mesuredHeight = showRs.getMeasuredHeight();
        showbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                int currentHeight = showRs.getMeasuredHeight();
                if(currentHeight == 0 ){
                    ValueAnimator anim = ValueAnimator.ofInt(0,mesuredHeight);
                    anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            int val = (Integer) valueAnimator.getAnimatedValue();
                            Log.v("val",Integer.toString(val));
                            ViewGroup.LayoutParams layoutParams = showRs.getLayoutParams();
                            layoutParams.height = val;
                            showRs.setLayoutParams(layoutParams);
                        }
                    });
                    anim.setDuration(200);
                    anim.start();
                }else{
                    ValueAnimator anim = ValueAnimator.ofInt(mesuredHeight,0);
                    anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            int val = (Integer) valueAnimator.getAnimatedValue();
                            Log.v("val",Integer.toString(val));
                            ViewGroup.LayoutParams layoutParams = showRs.getLayoutParams();
                            layoutParams.height = val;
                            showRs.setLayoutParams(layoutParams);
                        }
                    });
                    anim.setDuration(200);
                    anim.start();
                }


            }
        });
    }
    private void loadImg(){
        ImageView imageView = findViewById(R.id.testImgView);
        Picasso.get()
                .load("https://mangadex.org/covers/2db4e1e8-8776-4596-9d90-70291f2545a1/c7896b32-3cc8-47eb-9f7d-4442d9455cbd.png")


                .into(imageView);
    }

}