package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;
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

public class DescriptionPage extends AppCompatActivity {
    LinearLayout topBanner;
    ImageView imgCoverDes;
    TextView textDes;
    TextView textNameDes;
    LinearLayout altTitles;
    Context context ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description_page);
        imgCoverDes = findViewById(R.id.imgCoverDes);
        textDes= findViewById(R.id.textDes);
        textNameDes= findViewById(R.id.textNameDes);
        context = getApplicationContext();
        loadInfo();
    }
    private void loadInfo(){
        Bundle extras = getIntent().getExtras();
        if (extras == null) return ;
        String id = extras.getString("mangaId");
        String url= "https://api.mangadex.org/manga/"+id;
        RequestQueue queue = Volley.newRequestQueue(this);

        TextView textView = findViewById(R.id.textview);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String des ="";
                        String title = "";
                        try {


                            JSONObject data=   response.getJSONObject("data");
                            String mangaId = data.getString("id");
                            JSONObject description=   data.getJSONObject("attributes").getJSONObject("description");
                            JSONObject titleObject=   data.getJSONObject("attributes").getJSONObject("title");
                            String imgId="";

                            JSONArray relationships = data.getJSONArray("relationships");
                            for(int j =0;j<relationships.length();j++){
                                if(relationships.getJSONObject(j).getString("type").equals("cover_art")){
                                    imgId=relationships.getJSONObject(j).getString("id");
                                }
                            }

                            loadImg(mangaId,imgId,imgCoverDes);
                            for (final Iterator<String> iter1 = description.keys(); iter1.hasNext();) {
                                String key1 = iter1.next();
                                des= description.getString(key1);
                            }
                            Log.v("daasd",titleObject.toString());
                            for (final Iterator<String> iter2 = titleObject.keys(); iter2.hasNext();) {
                                String key2 = iter2.next();
                                title= titleObject.getString(key2);
                            }
                            textDes.setText("\t\t\t"+des);
                            textNameDes.setText(title);
                        } catch (JSONException e) {
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
    private void loadImg(String mangaId, String imgId, ImageView img){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.mangadex.org/cover/" +imgId;
        TextView textView = findViewById(R.id.textview);
        int imgWidth =120;
        int imgHeight =(int) imgWidth *3 / 2;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject data=   response.getJSONObject("data");
                            String imgUrl = "https://uploads.mangadex.org/covers/"+mangaId+"/"+data.getJSONObject("attributes").getString("fileName");
                            Picasso.get()
                                    .load(imgUrl)
                                    .resize(imgWidth, imgHeight)
                                    .centerCrop()
                                    .into(img);

                        } catch (JSONException e) {
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