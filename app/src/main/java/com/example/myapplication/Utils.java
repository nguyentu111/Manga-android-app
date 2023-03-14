package com.example.myapplication;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.otaliastudios.zoom.ZoomLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Utils {
    public static void loadPages(LinearLayout pagesLayout, String chapterId, Context context) {
        pagesLayout.removeAllViews();
        RequestQueue queue = Volley.newRequestQueue(context);
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
                                    //
//                                   zoomView.
                                   // Log.v(String.valueOf(pagesLayout.getWidth()),String.valueOf(imgView.getHeight()));
//                                    zoomLayout.measure(pagesLayout.getWidth(), imgView.getHeight());
                                    pagesLayout.addView(imgView);
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
                        Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        queue.add(jsonObjectRequest);
    }
}
