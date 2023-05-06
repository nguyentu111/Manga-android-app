package com.example.myapplication.tabs;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.myapplication.FilterManga;
import com.example.myapplication.R;
import com.nex3z.flowlayout.FlowLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
public class tab4 extends Fragment {

    List<String[]> theme = new ArrayList<String []>(); // aliens
    List<String[]> format = new ArrayList<String []>();;
    List<String[]> genre = new ArrayList<String []>();;
    List<String[]> contentRating = new ArrayList<>();; //safe, erotica, suggestive
    List<String[]> status = new ArrayList<>();;
    List<String[]> magazineDemographic = new ArrayList<>();;
    FlowLayout contentRatingFlow;
    FlowLayout statusFlow;
    FlowLayout magazineDemographicFlow;
    FlowLayout themeFlow;
    FlowLayout formatFlow;
    FlowLayout genreFlow;
    CardView btnFilter;
    Switch filter_tag_mode;
    public tab4() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab4, container, false);
    }
    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        contentRatingFlow = view.findViewById(R.id.contentRating);
        statusFlow = view.findViewById(R.id.status);
        magazineDemographicFlow = view.findViewById(R.id.magazineDemographic);
        themeFlow = view.findViewById(R.id.theme);
        formatFlow = view.findViewById(R.id.format);
        genreFlow = view.findViewById(R.id.genre);
        btnFilter = view.findViewById(R.id.btnFilter);
        filter_tag_mode = view.findViewById(R.id.filter_tag_mode);
        configFilter();
        showFilterToScreen(contentRating,contentRatingFlow);
        showFilterToScreen(status,statusFlow);
        showFilterToScreen(magazineDemographic,magazineDemographicFlow);
        setOnClickSearch();
    }
    private void configFilter(){
        String[]  a = new String[2];
        a[0]= "0";
        a[1] = "safe";
        contentRating.add(a);
        String[] b = new String[2];
        b[0]= "0";
        b[1] = "suggestive";
        contentRating.add(b);
        String[]  c = new String[2];
        c[0]= "0";
        c[1] = "erotica";
        contentRating.add(c);
//        String[]  o = new String[2];
//        o[0]= "0";
//        o[1] = "pornographic";
//        contentRating.add(o);
        String[]  d = new String[2];
        d[0]= "0";
        d[1] = "ongoing";
        status.add(d);
        String[]  e = new String[2];
        e[0]= "0";
        e[1] = "completed";
        status.add(e);
        String[]  f = new String[2];
        f[0]= "0";
        f[1] = "cancelled";
        status.add(f);
        String[]  g = new String[2];
        g[0]= "0";
        g[1] = "hiatus";
        status.add(g);

        String[]  h = new String[2];
        h[0]= "0";
        h[1] = "shounen";
        magazineDemographic.add(h);
        String[]  i = new String[2];
        i[0]= "0";
        i[1] = "shoujo";
        magazineDemographic.add(i);
        String[]  j = new String[2];
        j[0]= "0";
        j[1] = "seinen";
        magazineDemographic.add(j);
        String[]  k = new String[2];
        k[0]= "0";
        k[1] = "josei";
        magazineDemographic.add(k);
        String[]  l = new String[2];
        l[0]= "0";
        l[1] = "none";
        magazineDemographic.add(l);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "https://api.mangadex.org/manga/tag";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                            try {
                                JSONArray datas=   response.getJSONArray("data");
                                for(int i =0;i<datas.length();i++){
                                    String id = datas.getJSONObject(i).getString("id");
                                    String name = datas.getJSONObject(i).getJSONObject("attributes").getJSONObject("name").getString("en");
                                    String group = datas.getJSONObject(i).getJSONObject("attributes").getString("group");
                                    String[] a = new String[3];
                                    a[0] = "0";
                                    a[1] = name;
                                    a[2] = id;
                                    if(group.equals("format")){
                                        format.add(a);
                                    }else if(group.equals("genre")){
                                        genre.add(a);
                                    }else if(group.equals("theme")){
                                        theme.add(a);
                                    }

                                }
                                showFilterToScreen(theme,themeFlow);
                                showFilterToScreen(format,formatFlow);
                                showFilterToScreen(genre,genreFlow);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        queue.add(jsonObjectRequest);
    }
    private void showFilterToScreen(List<String[]> a, FlowLayout flowlayout){
        GradientDrawable gd2 = new GradientDrawable();
        gd2.setColor(Color.parseColor("#FF4D00")); // Changes this drawbale to use a single color instead of a gradient
        gd2.setCornerRadius(10);
        LinearLayout layout2 = new LinearLayout(getContext());
        GridLayout.LayoutParams params2 = new GridLayout.LayoutParams();
        params2.setMargins(25,10,25,10);
        layout2.setLayoutParams(params2);
        layout2.setOrientation(LinearLayout.HORIZONTAL);
        layout2.setBackground(gd2);
        TextView text2 = new TextView(getContext());
        text2.setText("any");
        text2.setTextSize(17);
        text2.setTextColor(Color.WHITE);
        text2.setPadding(12,10,12,10);
        text2.measure(ViewGroup.LayoutParams.WRAP_CONTENT, 110);
        layout2.addView(text2);
        flowlayout.addView(layout2);
        for(int i =0;i< a.size();i++){
            GradientDrawable gd = new GradientDrawable();
            gd.setColor(Color.parseColor("#373737")); // Changes this drawbale to use a single color instead of a gradient
            gd.setCornerRadius(10);
            //gd.setStroke(2, Color.parseColor("#FF4D00"));
            LinearLayout layout = new LinearLayout(getContext());
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.setMargins(25,10,25,10);
            layout.setLayoutParams(params);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            layout.setBackground(gd);
            TextView text = new TextView(getContext());
            text.setText(a.get(i)[1]);
            text.setTextSize(17);
            text.setTextColor(Color.WHITE);
            text.setPadding(12,8,12,8);
            text.measure(ViewGroup.LayoutParams.WRAP_CONTENT, 110);
            layout.addView(text);
            flowlayout.addView(layout);
            int finalI = i;
            ////  //any button

            //////////
            layout.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                    if(a.get(finalI)[0].equals("0")){
                        GradientDrawable gd = new GradientDrawable();
                        gd.setCornerRadius(10);
                        gd.setColor(Color.parseColor("#FF4D00"));
                        layout.setBackground(gd);
                        a.get(finalI)[0] = "1";


                    }else {
                        GradientDrawable gd = new GradientDrawable();
                        gd.setCornerRadius(10);
                        gd.setColor(Color.parseColor("#373737"));
                        layout.setBackground(gd);
                        a.get(finalI)[0] = "0";
                    }

                   if(isAllChoosed(a)) layout2.setBackgroundColor(Color.parseColor("#FF4D00"));
                   else  layout2.setBackgroundColor(Color.parseColor("#373737"));
               }
           });


        }

    }
    private void setOnClickSearch(){
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String main = "https://api.mangadex.org/manga?limit=32&offset=0&includes[]=cover_art&includes[]=artist&includes[]=author&order[latestUploadedChapter]=desc&excludedTags[]=5920b825-4181-4a17-beeb-9918b0ff7a30&excludedTagsMode=AND";
                String content_rating_query = "";
                if(isAllChoosed(contentRating)) content_rating_query = "&contentRating[]=safe&contentRating[]=suggestive&contentRating[]=erotica";
                else {
                    for(int i =0;i< contentRating.size();i++){
                        if(contentRating.get(i)[0].equals("1"))  content_rating_query += "&contentRating[]="+ contentRating.get(i)[1];
                    }
                }
                String status_query = "";
                if(isAllChoosed(status)) status_query = "&status[]=ongoing&status[]=completed&status[]=hiatus&status[]=cancelled";
                else {
                    for(int i =0;i< status.size();i++){
                        if(status.get(i)[0].equals("1"))  status_query += "&status[]="+ status.get(i)[1];
                    }
                }
                String magazine_demographic_query = "";
                if(isAllChoosed(magazineDemographic)) magazine_demographic_query = "&publicationDemographic[]=shounen&publicationDemographic[]=shoujo&publicationDemographic[]=seinen&publicationDemographic[]=josei&publicationDemographic[]=none";
                else {
                    for(int i =0;i< magazineDemographic.size();i++){
                        if(magazineDemographic.get(i)[0].equals("1"))  magazine_demographic_query += "&publicationDemographic[]="+ magazineDemographic.get(i)[1];
                    }
                }
                String includedTags = "";
                for(int i =0;i< format.size();i++){
                    if(format.get(i)[0].equals("1"))  includedTags += "&includedTags[]="+ format.get(i)[2];
                }
                for(int i =0;i< genre.size();i++){
                    if(genre.get(i)[0].equals("1"))  includedTags += "&includedTags[]="+ genre.get(i)[2];
                }
                for(int i =0;i< theme.size();i++){
                    if(theme.get(i)[0].equals("1"))  includedTags += "&includedTags[]="+ theme.get(i)[2];
                }
                String tagmode = filter_tag_mode.isChecked() ? "&includedTagsMode=OR" : "&includedTagsMode=AND";
                String url = main + content_rating_query + status_query + magazine_demographic_query + includedTags + tagmode ;
                Intent i = new Intent(getContext(), FilterManga.class);
                i.putExtra("url",url);
                startActivity(i);
            }
        });
    }
    private boolean isAllChoosed(List<String[]> a){
        boolean markerAllChoosed = true;
        boolean markerAllNotChoosed = true;
        for(int i =0;i< a.size();i++){
            if(a.get(i)[0].equals("0"))  markerAllChoosed = false;
            else markerAllNotChoosed = false;
        }
        return markerAllChoosed || markerAllNotChoosed;
    }


}
