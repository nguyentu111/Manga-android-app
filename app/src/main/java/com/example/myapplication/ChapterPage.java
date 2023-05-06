package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;


import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ChapterPage extends AppCompatActivity {
    Button showbtn;
    LinearLayout mainLayout;
    LinearLayout showRs;
    JSONArray data;
    String mangaId;
    String mangaName;
    String coverUrl;
    ImageView back_to_des ;
    String dataStr;
    static String lastChap;
    public static ArrayList<Truyen> data_Truyen = new ArrayList<>();
    Truyen mTruyen;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_page);
        context = this;
        Bundle extras = getIntent().getExtras();
        mainLayout = findViewById(R.id.main);
        back_to_des = findViewById(R.id.back_to_des);
        dataStr = extras.getString("data");
        mangaId = extras.getString("mangaId");
        mangaName = extras.getString("mangaName");
        coverUrl = extras.getString("imgUrl");
        DescriptionPage descriptionPage = new DescriptionPage();
        mTruyen = (Truyen) extras.getSerializable("truyen");

        try {
            if(dataStr !=null) data = new JSONArray(dataStr);
            loadChapters();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        back_to_des.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void setShowRs(){
        int mesuredHeight = showRs.getMeasuredHeight();
        showbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                int currentHeight = showRs.getMeasuredHeight();
                if(currentHeight == 0 ){
                    ValueAnimator anim = ValueAnimator.ofInt(0,mesuredHeight);
                    anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            int val = (Integer) valueAnimator.getAnimatedValue();
                            Log.v("val",Integer.toString(val));
                            ViewGroup.LayoutParams layoutParams = showRs.getLayoutParams();
                            layoutParams.height = val;
                            showRs.setLayoutParams(layoutParams);
                        }
                    });
                    anim.setDuration(200);
                    anim.start();
                }else{
                    ValueAnimator anim = ValueAnimator.ofInt(mesuredHeight,0);
                    anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            int val = (Integer) valueAnimator.getAnimatedValue();
                            Log.v("val",Integer.toString(val));
                            ViewGroup.LayoutParams layoutParams = showRs.getLayoutParams();
                            layoutParams.height = val;
                            showRs.setLayoutParams(layoutParams);
                        }
                    });
                    anim.setDuration(200);
                    anim.start();
                }


            }
        });
    }
    private void loadChapters() throws JSONException {
        if(data==null){
            callApi();
            return;
        }

        Map<String, Map<String,List<JSONObject>>> grouped_chapters = group_chapters_by_volume_and_chapter();
        List<String> sortedKeys=new ArrayList(grouped_chapters.keySet());
        sort_volume(sortedKeys);
       // sort_volume(v);
        for(String key: sortedKeys){
            LinearLayout header_volume = new LinearLayout(this);
            header_volume.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            header_volume.setLayoutParams(params);
            ////////////////////////////////volume name
            TextView volumeName = new TextView(this);
            if(key.equals("null")){
                volumeName.setText("No volume");
            }else{
                volumeName.setText("Volume "+key);
            }
            volumeName.setTextSize(20);
            volumeName.setTextColor(Color.WHITE);
            LinearLayout.LayoutParams paramsText1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,  1.0f);
            volumeName.setLayoutParams(paramsText1);
            header_volume.addView(volumeName);
            ///////////////////////////////total chap of volume
            TextView totalChap = new TextView(this);
            int total=0;
            for(String v: grouped_chapters.get(key).keySet()){
                total+=  grouped_chapters.get(key).get(v).size();
            }
            totalChap.setText(String.valueOf(total));
            totalChap.setTextSize(20);
            totalChap.setTextColor(Color.WHITE);
            LinearLayout.LayoutParams paramsText2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            totalChap.setLayoutParams(paramsText2);
            header_volume.addView(totalChap);
            /////////////////////////////////
            mainLayout.addView(header_volume);

            ///////////////////////chapter in volume
            LinearLayout chapter_volume = new LinearLayout(this);
            chapter_volume.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            chapter_volume.setLayoutParams(params1);
            List<String> sortedKeysChapter=new ArrayList(grouped_chapters.get(key).keySet());
            sort_chapter(sortedKeysChapter);
            for(String chapterKey: sortedKeysChapter){
                List<JSONObject> chapters = grouped_chapters.get(key).get(chapterKey);
                if(chapters.size()>1){
                    TextView chapterName = new TextView(this);
                    String chapStr = chapters.get(0).getJSONObject("attributes").getString("chapter");
                    if(chapStr.equals("null")){
                        chapterName.setText("One shot");
                    }else{
                        chapterName.setText("Chapter "+chapStr);
                    }
                    chapterName.setTextSize(15);
                    chapterName.setTextColor(Color.WHITE);
                    LinearLayout.LayoutParams paramsText3 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,  1.0f);
                    chapterName.setLayoutParams(paramsText3);
                    chapter_volume.addView(chapterName);
                }
                for(JSONObject chapter: chapters){
                    String title = chapter.getJSONObject("attributes").getString("title");
                    CardView card = new CardView(this);
                    card.setRadius(20);
                    LinearLayout.LayoutParams paramsCard = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    paramsCard.setMargins(0,8,0,8);
                    card.setLayoutParams(paramsCard);
                    LinearLayout one_chap_layout = new LinearLayout(this);
                    one_chap_layout.setOrientation(LinearLayout.VERTICAL);
                    LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    one_chap_layout.setPadding(20,20,20,20);
                    one_chap_layout.setLayoutParams(params2);
                    one_chap_layout.setBackgroundColor(Color.parseColor("#2F3032"));
                    //////  flag :


                    ////////////////
                    ////text chapter
                    TextView chapterName = new TextView(this);
                    String chapStr = chapter.getJSONObject("attributes").getString("chapter");
                    if(title.equals("null") || title.toString().trim().equals("") ){
                        if(chapStr.equals("null")){
                            chapterName.setText("One shot");
                        }else{
                            chapterName.setText("Ch."+chapStr);
                        }
                    }else{
                        if(chapters.size()==1){
                            chapterName.setText("Ch."+chapStr+" - "+title);
                        }else{
                           // chapterName.setText("Ch."+chapStr+" - "+title);
                            chapterName.setText(title);
                        }
                       // chapterName.setText(title);
                    }
                    chapterName.setTextSize(15);
                    chapterName.setTextColor(Color.WHITE);
                    LinearLayout.LayoutParams paramsText3 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,  1.0f);
                    chapterName.setLayoutParams(paramsText3);
                    one_chap_layout.addView(chapterName);
                    String chapterId = chapter.getString("id");
                    String currentLanguage =  chapter.getJSONObject("attributes").getString("translatedLanguage");
                    one_chap_layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            lastChap = chapStr;
                            if (mTruyen.getLastChapter() == null || Float.parseFloat(mTruyen.getLastChapter()) < Float.parseFloat(chapStr)){
                                Log.d("DEBUG_lastChap","-lastChap:"+chapStr+"-mtruyen:"+mTruyen.getLastChapter());
                                mTruyen.setLastChapter(chapStr);
                                mTruyen.setCheck(1);
                            }
                            else{
                                Log.d("DEBUG_lastChap1","-lastChap:"+chapStr+"-mtruyen:"+mTruyen.getLastChapter());
                                mTruyen.setCheck(1);
                            }

                            for (Truyen truyen1 : data_Truyen) {
                                if (truyen1.getMangaId().equals(mangaId)) {
                                    data_Truyen.remove(truyen1);
                                    break;
                                }
                            }
                            data_Truyen.add(0,mTruyen);
                            LichSuFragment.loadTruyen(context);

                            String name= chapterName.getText().toString().trim();
                            Intent i = new Intent(ChapterPage.this, ReadManga.class);
                            i.putExtra("chapterId",chapterId);
                            i.putExtra("chapterName",name);
                            i.putExtra("mangaName",mangaName);
                            i.putExtra("chaptersData",data.toString());
                            i.putExtra("currentLanguage",currentLanguage);
                            i.putExtra("mangaId",mangaId);
                            i.putExtra("coverUrl",coverUrl);
                            i.putExtra("chapStr",chapStr);
                            startActivity(i);
                        }
                    });
                    card.addView(one_chap_layout);
                    ////////
                    chapter_volume.addView(card);
                }

            }
            mainLayout.addView(chapter_volume);
//            for(String chapter : grouped_chapters.get(volume).keySet()){
//              //  Log.v(volume +" / "+"chapter",grouped_chapters.get(volume).get(chapter).toString());
//            }

        }


    }



    private void callApi(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.mangadex.org/manga/"+mangaId+"/feed?limit=100&order%5Bvolume%5D=desc&order%5Bchapter%5D=desc";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            data = response.getJSONArray("data");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        try {
                            loadChapters();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toast.makeText(ChapterPage.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        queue.add(jsonObjectRequest);
    }
    private void sort_volume(List<String> v) {
        v.sort(new Comparator<String>() {
            @Override
            public int compare(String s, String t1) {

                if(s.equals("null")) return -1;
                else if(t1.equals("null")) return 1;
                float fs = Float.parseFloat(s);
                float ft = Float.parseFloat(t1);
                if(fs>ft) return -1;//ko doi
                else return 1;
            }
        });
    }
    private  void  sort_chapter(List<String> l){
        l.sort(new Comparator<String>() {
            @Override
            public int compare(String s, String t1) {

                if(s.equals("null")) return 1;
                else if(t1.equals("null")) return -1;
                float fs = Float.parseFloat(s);
                float ft = Float.parseFloat(t1);
                if(fs>ft) return -1;//ko doi
                else return 1;
            }
        });

    }
    private  Map<String, Map<String,List<JSONObject>>> group_chapters_by_volume_and_chapter() throws JSONException {
        Map<String, Map<String,List<JSONObject>>> result = new TreeMap<String, Map<String,List<JSONObject>>>();
        for(int i =0;i<data.length();i++){
            String volume = data.getJSONObject(i).getJSONObject("attributes").getString("volume");
            String chapter = data.getJSONObject(i).getJSONObject("attributes").getString("chapter");
            Map<String,List<JSONObject>> items = result.get(volume);
            if(items == null){
                items =  new TreeMap<String,List<JSONObject>>();
                result.put(volume, items);
            }
            List<JSONObject> chapters_in_volume = items.get(chapter);
            if(chapters_in_volume==null){
                chapters_in_volume=  new ArrayList<JSONObject>();
            }
            chapters_in_volume.add( data.getJSONObject(i));
            items.put(data.getJSONObject(i).getJSONObject("attributes").getString("chapter"),chapters_in_volume);
        }
        //sort chapter by descending but remain grouped:
//        for(String volume : result.keySet()) {
//            Log.v("///volume", "");
//
//            for (String chapter : result.get(volume).keySet()) {
//                List<JSONObject> l = result.get(volume).get(chapter);
//                Collections.sort(l, new Comparator<JSONObject>() {
//                    public int compare(JSONObject o1, JSONObject o2) {
//                        // compare two instance of `Score` and return `int` as result.
//                        try {
//                            return o2.getJSONObject("attributes").getString("chapter").compareTo(o1.getJSONObject("attributes").getString("chapter"));
//                        } catch (JSONException e) {
//                            throw new RuntimeException(e);
//                        }
//                    }
//                });
//
//            }
//        }

        return result;
    }

}