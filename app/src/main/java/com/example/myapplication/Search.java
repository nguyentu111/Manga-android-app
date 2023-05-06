package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.gridlayout.widget.GridLayout;

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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class Search extends AppCompatActivity {
    SearchView searchView;
    GridView gridView;
    ImageView ivBack;
    TextView tvSearchNotFound;
    ArrayList<Truyen> data_Truyen = new ArrayList<>();
    ArrayList<Truyen> data_temp = new ArrayList<>();
    AdapterTruyen adapterTruyen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setControl();
        setEvent();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setEvent() {
        adapterTruyen = new AdapterTruyen(this, R.layout.item_truyen, data_Truyen);
        gridView.setAdapter(adapterTruyen);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                loadSearch(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
    }

    private void loadSearch(String newText) {
        data_Truyen.clear();
        String searchText = newText.trim().toLowerCase(Locale.ROOT);
        loadMangas(searchText);

    }
    public void loadMangas(String title){
        RequestQueue queue = Volley.newRequestQueue(getBaseContext());
        String url="https://api.mangadex.org/manga?title="+title+"&limit=51&offset=0&includes[]=author&includes[]=artist&includes[]=cover_art&order%5BcreatedAt%5D=desc&hasAvailableChapters=true&excludedTags[]=5920b825-4181-4a17-beeb-9918b0ff7a30&excludedTagsMode=AND";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray datass = response.getJSONArray("data");
                            if(datass.length()==0){
                                tvSearchNotFound.setVisibility(View.VISIBLE);
                                return;
                            }
                            tvSearchNotFound.setVisibility(View.GONE);
                            for(int i =0;i<datass.length();i++){
                                Truyen tr = new Truyen();
                                JSONObject data = datass.getJSONObject(i);

                                String mangaId = data.getString("id");
                                String name="" ;
                                JSONObject titles = data.getJSONObject("attributes").getJSONObject("title");
                                for (final Iterator<String> iter = titles.keys(); iter.hasNext();) {
                                    String key = iter.next();
                                    name= titles.getString(key);
                                }
                                JSONArray relationships = data.getJSONArray("relationships");
                                String imgUrl=null;
                                for(int j =0;j<relationships.length();j++){
                                    if(relationships.getJSONObject(j).getString("type").equals("cover_art")){
                                        imgUrl= "https://uploads.mangadex.org/covers/"+mangaId+"/"+relationships.getJSONObject(j).getJSONObject("attributes").getString("fileName");
                                    }
                                }

                                tr.setMangaId(mangaId);
                                tr.setMangaName(name);
                                tr.setImgUrl(imgUrl);
                                tr.setCheck(0);
                                tr.setDataStr(String.valueOf(data));

                                if (data_Truyen.size() < 30){
                                    if (data_Truyen.size() > 0) {
                                        Iterator<Truyen> iterator = data_Truyen.iterator();
                                        while (iterator.hasNext()) {
                                            Truyen truyen = iterator.next();
                                            if (truyen.getMangaName().equals(name)) {
                                                iterator.remove();
                                            }
                                        }
                                    }
                                    data_Truyen.add(tr);
                                    adapterTruyen.notifyDataSetChanged();
                                }
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        //Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        queue.add(jsonObjectRequest);
    }
    private void setControl() {
        searchView = findViewById(R.id.searchView);
        gridView = findViewById(R.id.gridView);
        ivBack = findViewById(R.id.ivBack);
        tvSearchNotFound = findViewById(R.id.tvSearchNotFound);
    }
}