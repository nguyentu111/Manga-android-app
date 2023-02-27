package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class ReadManga extends AppCompatActivity implements ActionBottomDialogFragment.ItemClickListener {
    LinearLayout pagesLayout;
    LinearLayout topMenu;
    CardView backBtn;
    TextView read_manga_name;
    TextView read_chapter_name;
    CardView menuRead;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_manga);
        backBtn= findViewById(R.id.back_to_chapter);
        pagesLayout=findViewById(R.id.pagesLayout);
        read_manga_name = findViewById(R.id.read_manga_name);
        read_chapter_name = findViewById(R.id.read_chapter_name);
        menuRead = findViewById(R.id.menuRead);
        topMenu=findViewById(R.id.topMenu);
        topMenu.setVisibility(View.GONE);
        Bundle extras = getIntent().getExtras();
        String chapterId= extras.getString("chapterId");
        String chapterName =  extras.getString("chapterName");
        String mangaName = extras.getString("mangaName");
        read_manga_name.setText(mangaName);
        read_chapter_name.setText(chapterName);
        loadPages(chapterId);
        pagesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(topMenu.isShown()){
                    topMenu.setVisibility(View.GONE);
                }else topMenu.setVisibility(View.VISIBLE);
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        menuRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheet();
            }
        });
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
                        // TODO: Handle error

                    }
                });
        queue.add(jsonObjectRequest);
    }

    public void showBottomSheet() {
        ActionBottomDialogFragment addPhotoBottomDialogFragment =
                ActionBottomDialogFragment.newInstance();
        addPhotoBottomDialogFragment.show(getSupportFragmentManager(),
                ActionBottomDialogFragment.TAG);
    }
    @Override
    public void onItemClick(String item) {

    }
}