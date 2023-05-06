package com.example.myapplication.tabs;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.gridlayout.widget.GridLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.DescriptionPage;
import com.example.myapplication.FilterManga;
import com.example.myapplication.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class tab3 extends Fragment {
    GridLayout gridView;
    Context context;
    int gridLayoutGap = 30;
    ScrollView scrollView;
    public tab3() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab3, container, false);
    }
    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gridView= view.findViewById(R.id.gridview);
        scrollView = view.findViewById(R.id.tab3ScrollView);
        context = view.getContext();
        loadTags();
    }
    private void loadTags(){
        String url="https://api.mangadex.org/manga/tag";
        RequestQueue queue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        int imgWidth = (gridView.getMeasuredWidth()-(gridLayoutGap*4))/2;
                        int imgHeight =(int) imgWidth *2 / 3;
                        try {
                            JSONArray datas=   response.getJSONArray("data");
                            for(int i =0;i<datas.length();i++){
                                JSONObject data = datas.getJSONObject(i);
                                String tag = data.getJSONObject("attributes").getJSONObject("name").getString("en");
                                String tagId = data.getString("id");
                                CardView cardView = new CardView(context);
                                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                                params.setMargins(gridLayoutGap,20,gridLayoutGap,20);
                                cardView.setLayoutParams(params);
                                cardView.setRadius(40);
                                cardView.setBackgroundColor(Color.parseColor("#373737"));
                                TextView tagName = new TextView(context);
                                tagName.setText(tag);
                                tagName.setTextSize(15);
                                tagName.setGravity(Gravity.CENTER);
                                tagName.setTextColor(Color.WHITE);
                                tagName.setWidth((gridView.getMeasuredWidth()-(gridLayoutGap*4))/2);
                                tagName.setHeight(180);
                                cardView.addView(tagName);
                                cardView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent i = new Intent(getContext(), FilterManga.class);
                                        i.putExtra("url","https://api.mangadex.org/manga?limit=51&includedTags%5B%5D="+tagId+"&includedTagsMode=OR&includes[]=author&includes[]=artist&includes[]=cover_art&excludedTags[]=5920b825-4181-4a17-beeb-9918b0ff7a30&excludedTagsMode=AND");
                                        startActivity(i);
                                    }
                                });
                                //////////
                                gridView.addView(cardView);

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
}