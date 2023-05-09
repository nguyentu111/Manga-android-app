package com.example.myapplication;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nex3z.flowlayout.FlowLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class DescriptionPage extends AppCompatActivity {
    ImageView imgCoverDes;
    TextView textDes;
    TextView textNameDes;
    LinearLayout altTitles;
    Context context;
    ImageView back_to_discover;
    FlowLayout flowTheLoai;
    FlowLayout flowTrangthai;
    FlowLayout flowDinhdang;
    FlowLayout flowTacgia;
    FlowLayout flowChude;
    LinearLayout chapterBtn, rightButton;
    JSONArray chapterData;
    String finalMangaId, mangaName, imgUrl;
    static Truyen mTruyen = new Truyen();
    public static ArrayList<Truyen> data_Truyen = new ArrayList<>();
    public static boolean savedManga=false;
    private JSONObject lastChap= null;
    JSONObject firstChap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description_page);
        imgCoverDes = findViewById(R.id.imgCoverDes);
        textDes= findViewById(R.id.textDes);
        textNameDes= findViewById(R.id.textNameDes);
        context = getApplicationContext();
        altTitles=findViewById(R.id.altTitles);
        flowTheLoai= findViewById(R.id.flowTheloai);
        flowTrangthai=findViewById(R.id.flowTrangthai);
        flowDinhdang=findViewById(R.id.flowDinhdang);
        flowTacgia=findViewById(R.id.flowTacgia);
        back_to_discover =findViewById(R.id.back_to_discover);
        flowChude=findViewById(R.id.flowChude);
        chapterBtn= findViewById(R.id.chapterBtn);
        rightButton= findViewById(R.id.rightButton);

//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_LA);
//        getWindow().setStatusBarColor(Color.TRANSPARENT);
//        getWindow().getDecorView().setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
//                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//        );
        Bundle extras = getIntent().getExtras();
        if (extras == null) return ;
        String dataStr = extras.getString("data");
        JSONObject data = null;
        try {
            data = new JSONObject(dataStr);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        imgUrl =  extras.getString("imgUrl");
        try {
            loadInfo(data,imgUrl);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        try {
            loadChapterData(data.getString("id"));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        String mangaId = null;
        try {
            mangaId = data.getString("id");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        for (Truyen truyenDaLuu : KeSachFragment.data_Truyen){
            if (truyenDaLuu.getMangaId().equals(finalMangaId)){
                changeRightButtonToContinuteRead();
                savedManga = true;

                break;
            }
        }
        Truyen truyen = new Truyen();
        truyen.setMangaId(mangaId);
        truyen.setMangaName(mangaName);
        truyen.setImgUrl(imgUrl);
        truyen.setDataStr(dataStr);
        for (Truyen truyen1 : LichSuFragment.data_Truyen){
            if (truyen1.getMangaId().equals(finalMangaId)){
                truyen.setFirstChap(truyen1.getFirstChap());
                truyen.setLastChap(truyen1.getLastChap());
            }
        }
        mTruyen = truyen;
        chapterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Truyen truyen = new Truyen();
//                truyen.setMangaId(finalMangaId);
//                truyen.setMangaName(mangaName);
//                truyen.setImgUrl(imgUrl);
//                truyen.setDataStr(dataStr);
//                for (Truyen truyen1 : LichSuFragment.data_Truyen){
//                    if (truyen1.getMangaId().equals(finalMangaId)){
//                        truyen.setLastChapter(truyen1.getLastChapter());
//                    }
//                }
//                mTruyen = truyen;
//                mTruyen.convertTruyen(truyen);

                Intent i = new Intent(DescriptionPage.this, ChapterPage.class);
                if(chapterData !=null) i.putExtra("data",chapterData.toString());
                i.putExtra("mangaId", finalMangaId);
                i.putExtra("mangaName",mangaName);
                i.putExtra("imgUrl",imgUrl);
                i.putExtra("truyen",mTruyen);
                startActivity(i);
            }
        });
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(savedManga ){
                    // mở chap đã đọc gần nhất , ko có thì load chap đầu
                    try {
                        if(mTruyen.getCurrentReadChap() !=null){
                            loadChapter(mTruyen.getCurrentReadChap());
                        }else  loadChapter(mTruyen.getFirstChap());
                    } catch (JSONException e) {
                        Toast.makeText(DescriptionPage.this,"Lỗi load truyện",Toast.LENGTH_SHORT);
                        throw new RuntimeException(e);

                    }
                    return;
                }
                changeRightButtonToContinuteRead();
                savedManga = true;
                Truyen truyen = new Truyen();
                truyen.setMangaId(finalMangaId);
                truyen.setMangaName(mangaName);
                truyen.setImgUrl(imgUrl);
                truyen.setDataStr(dataStr);
                truyen.setCheck(0);
                mTruyen = truyen;

                int a = 0;
                for (Truyen truyen1 : data_Truyen) {
                    if (truyen1.getMangaId().equals(finalMangaId)) {
                        Toast.makeText(context, "Truyện đã được lưu", Toast.LENGTH_SHORT).show();
                        a = 1;
                        break;
                    }
                }
                if (a == 0){
                    data_Truyen.add(0,truyen);
                    KeSachFragment.loadTruyen();
                    Toast.makeText(context, "Đã lưu vào kệ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        back_to_discover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void loadInfo(JSONObject data, String imgUrl) throws JSONException {
        JSONArray relationships = data.getJSONArray("relationships");
        //artist
        for(int j =0;j<relationships.length();j++){
            if(relationships.getJSONObject(j).getString("type").equals("artist")){
                TextView tagView = new TextView(context);
                tagView.setTextColor(Color.WHITE);
                tagView.setTextSize(13);
                tagView.setText(relationships.getJSONObject(j).getJSONObject("attributes").getString("name"));
                tagView.setBackgroundColor(Color.parseColor("#373737"));
                tagView.setPadding(8,6,8,6);
                flowTacgia.addView(tagView);
            }

        }
        int imgWidth =120;
        int imgHeight =(int) imgWidth *3 / 2;
        if(imgUrl!=null){
            Picasso.get()
                    .load(imgUrl)
                    .resize(328, 492)
                    //.centerCrop()
                    .into(imgCoverDes);
        }
        String name="" ;
        JSONObject titles = data.getJSONObject("attributes").getJSONObject("title");
        for (final Iterator<String> iter = titles.keys(); iter.hasNext();) {
            String key = iter.next();
            mangaName= titles.getString(key);
        }
        textNameDes.setText(mangaName);
        JSONArray alt_titles = data.getJSONObject("attributes").getJSONArray("altTitles");

        for(int j =0;j<alt_titles.length();j++){
            JSONObject objTT = alt_titles.getJSONObject(j);
            for (final Iterator<String> iter = objTT.keys(); iter.hasNext();) {
                String key = iter.next();
                String name_alt= objTT.getString(key);
                TextView alt_title_tv = new TextView(context);
                alt_title_tv.setTextColor(Color.WHITE);
                alt_title_tv.setTextSize(13);
                alt_title_tv.setText(name_alt);
                altTitles.addView(alt_title_tv);
            }
        }
        /////////load middle:
        JSONArray tags = data.getJSONObject("attributes").getJSONArray("tags");
        for(int j =0;j<tags.length();j++){
            JSONObject tagName = tags.getJSONObject(j).getJSONObject("attributes").getJSONObject("name");
            String tagGroup = tags.getJSONObject(j).getJSONObject("attributes").getString("group");
            if (tagName.keys().hasNext()) {
                String key = tagName.keys().next();
                TextView tagView = new TextView(context);
                tagView.setTextColor(Color.WHITE);
                tagView.setTextSize(13);
                tagView.setText(tagName.getString(key).toUpperCase());
                tagView.setBackgroundColor(Color.parseColor("#373737"));
                tagView.setPadding(8,6,8,6);
                if(tagGroup.equals("genre")) flowTheLoai.addView(tagView);
                else if(tagGroup.equals("format")) flowDinhdang.addView(tagView);
                else if (tagGroup.equals("theme")) flowChude.addView(tagView);
            }
        }
        String status = data.getJSONObject("attributes").getString("status");
        TextView tagView = new TextView(context);
        tagView.setTextColor(Color.WHITE);
        tagView.setTextSize(13);
        tagView.setText(status);
        tagView.setBackgroundColor(Color.parseColor("#373737"));
        tagView.setPadding(8,6,8,6);
        flowTrangthai.addView(tagView);
        ////description:
        JSONObject desObj = data.getJSONObject("attributes").getJSONObject("description");
        try {
            String en =  desObj.getString("en");
            textDes.setText(en);
        }catch(Exception e){
            for (final Iterator<String> iter = desObj.keys(); iter.hasNext();) {
                String key = iter.next();
                String des =  desObj.getString(key);
                textDes.setText(des);

            }
        }


        /////////////////////


    }
    private void loadChapterData(String mangaId){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.mangadex.org/manga/"+mangaId+"/feed?limit=200&order%5Bvolume%5D=asc&order%5Bchapter%5D=asc";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            chapterData = response.getJSONArray("data");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toast.makeText(DescriptionPage.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        queue.add(jsonObjectRequest);
    }
    public void changeRightButtonToContinuteRead(){
        savedManga = true;
        ImageView imvBottomRight = findViewById(R.id.imvBottomRight);
        TextView tvBottomRight = findViewById(R.id.tvBottomRight);
        imvBottomRight.setBackgroundResource(R.drawable.baseline_electric_bolt_24);
        tvBottomRight.setText("Đọc nhanh");
    }
    private void loadChapter(JSONObject chap) throws JSONException {


        String chapterName ;
        String chapStr = chap.getJSONObject("attributes").getString("chapter");
        String title = chap.getJSONObject("attributes").getString("title");
        if(title.equals("null") || title.toString().trim().equals("") ){
            if(chapStr.equals("null")){
                chapterName = ("One shot");
            }else{
                chapterName = ("Ch."+chapStr);
            }
        }else{
            if(chapterData.length()==1){
                chapterName = ("Ch."+chapStr+" - "+title);
            }else{
                chapterName = (title);
            }
        }
        Toast.makeText(DescriptionPage.this,"chay toi load chapter "+chapterName,Toast.LENGTH_SHORT);
//        Intent i = new Intent(DescriptionPage.this, ReadManga.class);

//        i.putExtra("chapterId", lastChap.getString("id"));
//        i.putExtra("mangaName",mangaName);
//        i.putExtra("mangaId",mTruyen.getMangaId());
//        i.putExtra("chapterName",chapterName);
//        i.putExtra("currentLanguage",imgUrl);
//        i.putExtra("coverUrl",imgUrl);
//
//        startActivity(i);
    }

}