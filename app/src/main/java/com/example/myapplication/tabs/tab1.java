package com.example.myapplication.tabs;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.gridlayout.widget.GridLayout;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.DescriptionPage;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.components.CustomScrollView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
public class tab1 extends Fragment   {
    GridLayout gridView;
    Context context;
    int gridLayoutGap = 16;
    ScrollView scrollView;
    public tab1() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab1, container, false);
    }
    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gridView= view.findViewById(R.id.gridview);
        scrollView = view.findViewById(R.id.tab1ScrollView);
        context = view.getContext();
        loadMangas(0);
//        scrollView.getViewTreeObserver()
//                .addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
//                    @Override
//                    public void onScrollChanged() {
//                        if (scrollView.getChildAt(0).getBottom()
//                                <= (scrollView.getHeight() + scrollView.getScrollY())) {
//                            loadMangas(1);//
//                        } else {
//                            //scroll view is not at bottom
//                        }
//                    }
//                });
    }
    private void loadMangas(int page){
        Log.v("load","load mange");
        String offset = String.valueOf(page*51);
        String url="https://api.mangadex.org/manga?limit=51&offset="+offset+"&includes[]=author&includes[]=artist&includes[]=cover_art";
        RequestQueue queue = Volley.newRequestQueue(context);

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
                                TextView mangaLastChap = new TextView(getContext());
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
                                        Intent i = new Intent(getContext(), DescriptionPage.class);
                                        i.putExtra("data",data.toString());
                                        i.putExtra("imgUrl", finalImgUrl);
                                        startActivity(i);

                                    }
                                });
                                //////////
                                gridView.addView(manga);

                            }
//                            Button loadmorebtn = new Button(context);
//                            loadmorebtn.setText("Load more");
//                            loadmorebtn.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//
//
//                                }
//                            });
//                            gridView.addView(loadmorebtn);

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
                        //Toast.makeText(tab1.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        queue.add(jsonObjectRequest);
    }
    private void loadLastChapter(String mangaId,TextView textView){

        String url = "https://api.mangadex.org/manga/"+mangaId+"/feed";
        RequestQueue queue = Volley.newRequestQueue(getContext());
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