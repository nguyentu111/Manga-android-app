package com.example.myapplication;

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

import androidx.appcompat.app.AppCompatActivity;

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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

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
    static Truyen mTruyen = ReadManga.mTruyen;
    static Truyen mTruyenKS;
    public static ArrayList<Truyen> data_Truyen = new ArrayList<>();
    static String currentChapJSON;
    public static boolean savedManga;
    private JSONObject lastChap= null;
    JSONObject firstChap;
    LinearLayout fastReadBtn;
    String dataStr;
    boolean check1 = false; // kiểm tra truyện đã lưu chưa
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
        fastReadBtn = findViewById(R.id.fastReadBtn);
        savedManga = false; 
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_LA);
//        getWindow().setStatusBarColor(Color.TRANSPARENT);
//        getWindow().getDecorView().setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
//                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//        );
        if (mTruyen == null){
            ReadManga.mTruyen = new Truyen();
            mTruyen = ReadManga.mTruyen;
        }
        else {
            ReadManga.mTruyen = new Truyen();
            mTruyen = ReadManga.mTruyen;
        }
        Bundle extras = getIntent().getExtras();
        if (extras == null) return ;
        dataStr = extras.getString("data");
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
            loadChapterData(data.getString("id"),null);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        String mangaId = null;
        try {
            mangaId = data.getString("id");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        finalMangaId = mangaId;
        for (Truyen truyenDaLuu : data_Truyen){
            if (truyenDaLuu.getMangaId().equals(finalMangaId)){
               savedManga = true;
               break;
            }
        }
        Truyen truyen = new Truyen();
        truyen.setMangaId(finalMangaId);
        truyen.setMangaName(mangaName);
        truyen.setImgUrl(imgUrl);
        truyen.setDataStr(dataStr);

//        if (LichSuFragment.data_Truyen.size() > 0){
//
//        }
        boolean check = false;
        List<Truyen> truyensToUpdate = new ArrayList<>();
        for (Truyen truyen1 : LichSuFragment.data_Truyen){
            if (truyen1.getMangaId().equals(finalMangaId)){
                truyen.setFirstChap(truyen1.getFirstChap());
                truyen.setLastChap(truyen1.getLastChap());
                truyen.setCurrentReadChapJSONObject(truyen1.getCurrentReadChapJSONObject());
                truyensToUpdate.add(truyen);
                Log.d("DEBUGmTruyencJSONLLS", ""+truyen.getCurrentReadChapJSONObject());
                check = true;
                check1 = true;
            }
        }

        for (Truyen tr : truyensToUpdate){
            if (LichSuFragment.data_Truyen.contains(tr)) {
                LichSuFragment.data_Truyen.set(LichSuFragment.data_Truyen.indexOf(tr), tr);
                LichSuFragment.loadTruyen(context);
            }
        }


        if (check == false){
            truyensToUpdate.clear();
            for (Truyen truyen1 : KeSachFragment.data_Truyen){
                if (truyen1.getMangaId().equals(finalMangaId)){
                    truyen.setFirstChap(truyen1.getFirstChap());
                    truyen.setLastChap(truyen1.getLastChap());
                    truyen.setCurrentReadChapJSONObject(truyen1.getCurrentReadChapJSONObject());
                    truyensToUpdate.add(truyen);
                    Log.d("DEBUGmTruyencJSONKKS", ""+truyen.getCurrentReadChapJSONObject());
                    check1 = true;
                }
            }
            for (Truyen tr : truyensToUpdate){
                if (DescriptionPage.data_Truyen.contains(tr)) {
                    DescriptionPage.data_Truyen.set(DescriptionPage.data_Truyen.indexOf(tr), tr);
                    KeSachFragment.loadTruyen(context);
                }

            }
        }
        mTruyen = truyen;
        Log.d("DEBUGmTruyencJSON", ""+mTruyen.getCurrentReadChapJSONObject());

        chapterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                mTruyenKS = mTruyen;
                mTruyenKS.setCheck(0);
                handleClickSave(true, null);

            }
        });

        back_to_discover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        fastReadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleClickSave(false, new loadChapJSONCallback() {
                    @Override
                    public void onLoadChapJSON(Truyen mTruyen1) {
                        if (check1 == false){  // truyen chưa lưu
                            mTruyen.setFirstChap(mTruyen1.getFirstChap());
                            mTruyen.setLastChap(mTruyen1.getLastChap());
                        }
//                        if (mTruyen.getMangaId().equals(ReadManga.mTruyen.getMangaId())){
//                            mTruyen.setCurrentReadChapJSONObject(ReadManga.currentChapJSON);
//                            Log.d("DEBUG_currentChapJSONtemp", "currentChapJSONtemp");
//                        }
                        try {
                            if(mTruyen.getCurrentReadChapJSONObject() != null){
                                Log.d("DEBUG_currentChapCallback1", mTruyen.getCurrentReadChapJSONObject());
                                loadChapter(new JSONObject(mTruyen.getCurrentReadChapJSONObject()));
                            }
                            else {
                                loadChapter(new JSONObject(mTruyen.getFirstChap()));
                            }
                        } catch (JSONException e) {
                            Toast.makeText(DescriptionPage.this,"Lỗi đọc nhanh truyện",Toast.LENGTH_SHORT);
                            //throw new RuntimeException(e);
                        }

                    }
                });

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
    private void loadChapterData(String mangaId,Runnable callback){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.mangadex.org/manga/"+mangaId+"/feed?limit=200&order%5Bvolume%5D=asc&order%5Bchapter%5D=asc";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            chapterData = response.getJSONArray("data");
                            if(callback !=null) callback.run();
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
    private void loadChapter(JSONObject chap) throws JSONException {

        String chapterName ;
        String chapStr = chap.getJSONObject("attributes").getString("chapter");
        String title = chap.getJSONObject("attributes").getString("title");
        String currentLanguage =  chap.getJSONObject("attributes").getString("translatedLanguage");
        if(title.equals("null") || title.toString().trim().equals("") ){
            if(chapStr.equals("null")){
                chapterName = ("One shot");
            }else{
                chapterName = ("Ch."+chapStr);
            }
        }else {
            if (chapterData.length() == 1) {
                chapterName = ("Ch." + chapStr + " - " + title);
            } else {
                chapterName = (title);
            }
        }
        Intent i = new Intent(DescriptionPage.this, ReadManga.class);

        i.putExtra("chapterId", chap.getString("id"));
        i.putExtra("chapterName",chapterName);
        i.putExtra("mangaName",mangaName);
        i.putExtra("mangaId",mTruyen.getMangaId());
        i.putExtra("currentLanguage",currentLanguage);
        i.putExtra("coverUrl",imgUrl);
        i.putExtra("mTruyen",mTruyen);
        i.putExtra("chapter", String.valueOf(chap));

        startActivity(i);
    }
    private void handleClickSave( boolean check, loadChapJSONCallback callback ){
        if (check == true){ // khi click lưu vào kệ sách
            if(savedManga){// về sau chỉnh lại thành xóa truyện đã lưu
                Toast.makeText(DescriptionPage.this , "Truyện đã nằm trong kệ",Toast.LENGTH_SHORT).show();
                return;
            }
            savedManga = true;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if(chapterData == null) {
                        throw new RuntimeException(new Exception("Lỗi fetch chapterdata in des page"));
                    }

                    try {
                        JSONObject firstChap = chapterData.getJSONObject(0);
                        JSONObject  lastChap1 = chapterData.getJSONObject(chapterData.length()-1);
                        Log.v("DEBUG firsstchap",firstChap.toString());
//                        lastChap = firstChap;
                        mTruyenKS.setFirstChap(firstChap.toString());
                        mTruyenKS.setLastChap(lastChap1.toString());
                        mTruyenKS.setCheck(0);
                    }
                    catch (JSONException e) {
                        Log.v("DEBUG","error save last chap and firstchap");
                        throw new RuntimeException(e);
                    }

                    data_Truyen.add(0,mTruyenKS);
                    freshDataTruyen(data_Truyen);
                    KeSachFragment.loadTruyen(context);
                    Toast.makeText(context, "Lưu thành công",Toast.LENGTH_SHORT).show();
                }
            };
            if(chapterData !=null){
                runnable.run();

            }else{
                loadChapterData(mTruyenKS.getMangaId(),runnable);
            }
        }
        else { // khi click đọc nhanh
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if(chapterData == null) {
                        throw new RuntimeException(new Exception("Lỗi fetch chapterdata in des page"));
                    }

                    try {
                        JSONObject firstChap = chapterData.getJSONObject(0);
                        JSONObject  lastChap1 = chapterData.getJSONObject(chapterData.length()-1);
                        Log.v("DEBUG firsstchap",firstChap.toString());
//                        lastChap = firstChap;
                        mTruyen.setFirstChap(firstChap.toString());
                        mTruyen.setLastChap(lastChap1.toString());
//                        mTruyen.setCheck(1);
                    }
                    catch (JSONException e) {
                        Log.v("DEBUG","error save last chap and firstchap");
                        throw new RuntimeException(e);
                    }
                    callback.onLoadChapJSON(mTruyen);
                }
            };
            if(chapterData !=null){
                runnable.run();

            }else{
                loadChapterData(mTruyen.getMangaId(),runnable);
            }
        }
    }
    public interface loadChapJSONCallback {
        void onLoadChapJSON(Truyen mTruyen);
    }
    private static void freshDataTruyen(ArrayList<Truyen> data_Truyen) {
        if (data_Truyen.size()>1){
            LinkedHashSet<Truyen> uniqueTruyens = new LinkedHashSet<>(data_Truyen);
            data_Truyen.clear();
            data_Truyen.addAll(uniqueTruyens);
        }
    }
}