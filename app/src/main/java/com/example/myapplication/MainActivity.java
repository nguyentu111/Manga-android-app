package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.gridlayout.widget.GridLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class MainActivity extends AppCompatActivity {
    Button btn ;
    GridLayout gridView;
    Context context ;
    int gridLayoutGap = 16;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        );
        gridView= findViewById(R.id.gridview);
        context = getApplicationContext();
        loadMangas();


    }
    private void loadMangas(){
        String url="https://api.mangadex.org/manga?limit=50&includes[]=author&includes[]=artist&includes[]=cover_art";
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        int imgWidth = (gridView.getMeasuredWidth()-(gridLayoutGap*6))/3;
                        int imgHeight =(int) imgWidth *3 / 2;
                        try {
                            JSONArray datas=   response.getJSONArray("data");
                            for(int i =0;i<datas.length();i++){
                                JSONObject data = datas.getJSONObject(i);
                                LinearLayout manga = new LinearLayout(context);
                                manga.setOrientation(LinearLayout.VERTICAL);
                                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                                params.setMargins(gridLayoutGap,20,gridLayoutGap,20);
                                manga.setLayoutParams(params);

                                ////////// img
                                ImageView imgView = new ImageView(context);
                                imgView.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                CardView cardView = new CardView(context);
                                ImageView imgViewFallback = new ImageView(context);
                                imgViewFallback.setImageResource(R.drawable.fallbackimg);
                                ViewGroup.LayoutParams imgParams = new ViewGroup.LayoutParams(imgWidth,imgHeight);
                                imgViewFallback.setLayoutParams(imgParams);
                                cardView.addView(imgViewFallback);
                                cardView.setMinimumWidth(imgWidth);
                                cardView.setMinimumHeight(imgHeight);
                                cardView.setRadius(20);
                                String mangaId = data.getString("id");
                                JSONArray relationships = data.getJSONArray("relationships");

                                String imgUrl=null;
                                for(int j =0;j<relationships.length();j++){
                                    if(relationships.getJSONObject(j).getString("type").equals("cover_art")){
                                        imgUrl= "https://uploads.mangadex.org/covers/"+mangaId+"/"+relationships.getJSONObject(j).getJSONObject("attributes").getString("fileName");
                                        Picasso.get()
                                                .load(imgUrl)
                                                .resize(imgWidth, imgHeight)
                                                .centerCrop()
                                                .into(imgView);
                                    }
                                }
                                cardView.addView(imgView);

                                manga.addView(cardView);
                                ////////// name and chap
                                LinearLayout info = new LinearLayout(context);
                                info.setOrientation(LinearLayout.VERTICAL);
                                TextView mangaName = new TextView(context);
                                String name="" ;
                                JSONObject titles = data.getJSONObject("attributes").getJSONObject("title");
                                for (final Iterator<String> iter = titles.keys(); iter.hasNext();) {
                                    String key = iter.next();
                                    name= titles.getString(key);
                                }
                                if(name.length()>25) name=name.substring(0,25)+"...";
                                mangaName.setText(name);
                                mangaName.setTextSize(15);
                                mangaName.setGravity(Gravity.CENTER_HORIZONTAL);
                                mangaName.setTextColor(Color.WHITE);
                                mangaName.setWidth((gridView.getMeasuredWidth()-(gridLayoutGap*6))/3);
                                mangaName.setHeight(110);
                                mangaName.setMaxLines(2);
                                TextView mangaLastChap = new TextView(context);
                                mangaLastChap.setTextSize(15);
                                mangaLastChap.setGravity(Gravity.CENTER);
                                mangaLastChap.setTextColor(Color.WHITE);
                                mangaLastChap.setWidth((gridView.getMeasuredWidth()-(gridLayoutGap*6))/3);
                                loadLastChapter(mangaId,mangaLastChap);
                                info.addView(mangaName);
                                info.addView(mangaLastChap);
                                manga.addView(info);
                                String finalImgUrl = imgUrl;
                                manga.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent i = new Intent(MainActivity.this, DescriptionPage.class);
                                    i.putExtra("data",data.toString());
                                    i.putExtra("imgUrl", finalImgUrl);
                                    startActivity(i);

                                }
                            });
                                //////////
                                gridView.addView(manga);
                            }


                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
//                        textView.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                        showbtn.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });
        queue.add(jsonObjectRequest);
    }
    private void loadLastChapter(String mangaId,TextView textView){

        String url = "https://api.mangadex.org/manga/"+mangaId+"/feed";
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            float maxChapter =0;
                            JSONArray datas=   response.getJSONArray("data");

                               for(int i=0;i<datas.length();i++){
                                   try{
                                       String f = datas.getJSONObject(i).getJSONObject("attributes").getString("chapter");
                                       if(!f.equals("null")){
                                           float chap = Float.parseFloat(f);
                                           if(maxChapter<chap) maxChapter=chap;
                                       }else  {
                                           textView.setText("Oneshot");
                                           return;
                                       };
                                       if(maxChapter==0.0)  textView.setText("Chapter 0");
                                       else {
                                           if((int) maxChapter % 1 ==0)  textView.setText("Chap "+Float.toString((int) maxChapter));
                                           else textView.setText("Chap "+Float.toString(maxChapter));
                                       };
                                   }catch (NumberFormatException  e){
                                        Log.v("chapter parse string to float error,string value :",datas.getJSONObject(i).getJSONObject("attributes").getString("chapter"));
                                       String f = datas.getJSONObject(i).getJSONObject("attributes").getString("chapter");

                                        continue;
                                   }
                               }
                        } catch (JSONException e) {

                            //throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        queue.add(jsonObjectRequest);

    }

}