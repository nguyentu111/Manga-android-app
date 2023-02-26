package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.jsibbold.zoomage.ZoomageView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ReadManga extends AppCompatActivity {
    LinearLayout pagesLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_manga);
        pagesLayout=findViewById(R.id.pagesLayout);
        Bundle extras = getIntent().getExtras();
        String chapterId= extras.getString("chapterId");
        loadPages(chapterId);
    }

    private void loadPages(String chapterId) {
        Context context= this;
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.mangadex.org/at-home/server/"+chapterId;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                       try{
                           try {
                               JSONArray data= response.getJSONObject("chapter").getJSONArray("data");
                               String hash = response.getJSONObject("chapter").getString("hash");
                               for(int i = 0; i < data.length(); i++){
                                   String pageUrl = "https://uploads.mangadex.org/data/"+hash+"/"+ data.getString(i);
                                   ImageView imgView = new ImageView(context);
                                   imgView.setAdjustViewBounds(true);
                                   imgView.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                   Picasso.get()
                                           .load(pageUrl)
//                                        .resize(imgWidth, imgHeight)
//                                        .centerCrop()
                                           .into(imgView);
//                                   ZoomageView zoomView = new ZoomageView(context);
//                                   zoomView.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                                   zoomView.
//                                   pagesLayout.addView(imgView);
                               }
                           } catch (JSONException e) {
                               throw new RuntimeException(e);
                           }
                       }catch (Exception e){//handle Canvas: trying to draw too large(168140800bytes) bitmap.
                           throw new RuntimeException(e);
                       }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });
        queue.add(jsonObjectRequest);
    }
}