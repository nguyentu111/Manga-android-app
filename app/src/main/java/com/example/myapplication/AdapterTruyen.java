package com.example.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.apache.commons.lang3.math.NumberUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;


public class AdapterTruyen extends ArrayAdapter<Truyen> {
    Context context;
    int resource;
    ArrayList<Truyen> data = new ArrayList<>();
    ArrayList<Truyen> data_temp = new ArrayList<>();
    
    public AdapterTruyen(@NonNull Context context, int resource, @NonNull ArrayList<Truyen> data) {
        super(context, resource, data);
        this.context = context;
        this.resource = resource;
        this.data = data;

        data_temp.addAll(this.data);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        viewHolder viewHolder = null;
        if (convertView==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_truyen,null);
            viewHolder = new viewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (viewHolder) convertView.getTag();
        }
        Truyen truyen = data.get(position);
        if (data.size()>0){
            if (truyen.getCheck() == 1) {
                if (NumberUtils.isCreatable(truyen.getCurrentReadChap())) {

                    viewHolder.tvChapter.setText("Chap " + truyen.getCurrentReadChap());
                } else {
                    loadLastChapter(truyen, truyen.getMangaId(), viewHolder.tvChapter);
                }
            }
            else  {
                loadLastChapter(truyen, truyen.getMangaId(), viewHolder.tvChapter);
            }
            viewHolder.tvTenTruyen.setText(truyen.getMangaName());
            Glide.with(this.context).load(truyen.getImgUrl()).into(viewHolder.ivTruyen);
        }

//        try {
//            JSONObject lastChapter = new JSONObject(truyen.getLastChap());//.getJSONObject("attributes").getString("chapter");
//            JSONObject currentReadChap = new JSONObject(truyen.getCurrentReadChap());
//            String tvChap = lastChapter.getJSONObject("attributes").getString("chapter");
//            String currentRead = currentReadChap.getJSONObject("attributes").getString("chapter");
//            if(lastChapter ==null ) Log.v("DEBUG: ","lastchap null");
//            if(currentReadChap ==null ) Log.v("DEBUG2: ","currentReadChap null");
//
//
////            String currentReadChapter = truyen.getLastChap().getJSONObject("attributes").getString("chapter");
////            Log.v("lastChapter",currentReadChapter.getJSONObject("attributes").getString("chapter"));
////            if(lastChapter.equals("null")) lastChapter="One Shot";
////            else lastChapter = "Chap "+ lastChapter;
////            if (data.size()>0){
//
//            if (truyen.getCheck() == 1 ) {// lich su
//                if(currentReadChap !=null){
//                    String currentReadChapterStr = currentReadChap.getJSONObject("attributes").getString("chapter");
//                    Log.d("DEBUG_tvChapLS", currentRead);
//                    viewHolder.tvChapter.setText(currentReadChapterStr);
//                }
//            }
//            else  {
//                viewHolder.tvChapter.setText(tvChap);
//                Log.d("DEBUG_tvChapKS", tvChap);
//            }
//            viewHolder.tvTenTruyen.setText(truyen.getMangaName());
//            Glide.with(this.context).load(truyen.getImgUrl()).into(viewHolder.ivTruyen);
////            }
//        } catch (Exception e) {
//            //throw new RuntimeException(e);
//            Log.v("error: ",e.getMessage());
//        }


        viewHolder.ivTruyen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), DescriptionPage.class);
                i.putExtra("data", truyen.getDataStr());
                i.putExtra("imgUrl", truyen.getImgUrl());
                parent.getContext().startActivity(i);
            }
        });
        viewHolder.ivTruyen.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                DialogRemove(truyen);
                return false;
            }
        });
        return convertView;
    }

    private void loadLastChapter(Truyen truyen, String mangaId,TextView textView){

        String url = "https://api.mangadex.org/manga/"+mangaId+"/feed";
        RequestQueue queue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            float maxChapter =0;
                            JSONArray datas=   response.getJSONArray("data");

                            for(int i=0;i<datas.length();i++){
                                try{
                                    String f = datas.getJSONObject(i).getJSONObject("attributes").getString("chapter");
                                    if(!f.equals("null")){
                                        float chap = Float.parseFloat(f);
                                        if(maxChapter<chap) maxChapter=chap;
                                    }else  {
                                        textView.setText("Oneshot");
                                        truyen.setLastChap("Oneshot");
                                        return;
                                    };
                                    if(maxChapter==0.0){
                                        textView.setText("Chapter 0");
                                        truyen.setLastChap("Chapter 0");
                                    }
                                    else {
                                        if((int) maxChapter % 1 ==0){
                                            textView.setText("Chap "+Float.toString((int) maxChapter));
                                            truyen.setLastChap("Chap "+Float.toString((int) maxChapter));
                                        }
                                        else {
                                            textView.setText("Chap "+Float.toString(maxChapter));
                                            truyen.setLastChap("Chap "+Float.toString(maxChapter));
                                        }
                                    }
                                }catch (NumberFormatException  e){
                                    Log.v("chapter parse string to float error,string value :",datas.getJSONObject(i).getJSONObject("attributes").getString("chapter"));
                                    String f = datas.getJSONObject(i).getJSONObject("attributes").getString("chapter");

                                    continue;
                                }
                            }
                        } catch (JSONException e) {

                            //throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        queue.add(jsonObjectRequest);

    }

    private void DialogRemove(Truyen truyen) {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_remove);
        dialog.setCanceledOnTouchOutside(false);

        TextView textView = dialog.findViewById(R.id.tvTitle);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        Button btnDelete = dialog.findViewById(R.id.btnDelete);
        textView.setText("Bạn có muốn xóa truyện này không?");
        dialog.show();

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Iterator<Truyen> iterator = data.iterator();
                while (iterator.hasNext()){
                    Truyen tr = iterator.next();
                    if (tr.getMangaId().equals(truyen.getMangaId())){
                        iterator.remove();
                        notifyDataSetChanged();
                        KeSachFragment.saveKeSach(context);
                        LichSuFragment.saveLichSu(context);
                        Toast.makeText(context, "Đã xóa truyện", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
    private class viewHolder{
        ImageView ivTruyen;
        TextView tvTenTruyen;
        TextView tvChapter;
        public viewHolder(View view){
            ivTruyen = view.findViewById(R.id.ivTruyen);
            tvTenTruyen = view.findViewById(R.id.tvTenTruyen);
            tvChapter = view.findViewById(R.id.tvChapter);
        }
    }

}
