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
        data_temp.addAll(data);
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

        try {
            JSONObject lastChapter = truyen.getLastChap();//.getJSONObject("attributes").getString("chapter");
            JSONObject currentReadChap = truyen.getCurrentReadChap();
            if(lastChapter ==null ) Log.v("DEBUG: ","lastchap null");
            if(currentReadChap ==null ) Log.v("DEBUG2: ","currentReadChap null");


//            String currentReadChapter = truyen.getLastChap().getJSONObject("attributes").getString("chapter");
//            Log.v("lastChapter",currentReadChapter.getJSONObject("attributes").getString("chapter"));
//            if(lastChapter.equals("null")) lastChapter="One Shot";
//            else lastChapter = "Chap "+ lastChapter;
//            if (data.size()>0){

                if (truyen.getCheck() == 1 ) {// lich su
                    if(currentReadChap !=null){
                        String currentReadChapterStr = currentReadChap.getJSONObject("attributes").getString("chapter");
                        viewHolder.tvChapter.setText(currentReadChapterStr);
                    }
                }
                else  {
                    viewHolder.tvChapter.setText(lastChapter.getJSONObject("attributes").getString("chapter"));
                }
                viewHolder.tvTenTruyen.setText(truyen.getMangaName());
                Glide.with(this.context).load(truyen.getImgUrl()).into(viewHolder.ivTruyen);
//            }
        } catch (Exception e) {
            //throw new RuntimeException(e);
            Log.v("error: ",e.getMessage());
        }


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
                        KeSachFragment.saveKeSach();
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
