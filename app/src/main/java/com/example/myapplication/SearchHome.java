package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import java.util.Iterator;
import java.util.Locale;

public class SearchHome extends AppCompatActivity {
    TextView tvTimTruyen;
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
        Intent intent = getIntent();
        data_Truyen = (ArrayList<Truyen>) intent.getSerializableExtra("data_Truyen");
        data_temp.addAll(data_Truyen);
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
        if (MainActivityTrangChu.check == 0){
            tvTimTruyen.setText("Tìm truyện (KS)");
        }
        else
            tvTimTruyen.setText("Tìm truyện (LS)");

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
        for (Truyen tr : data_temp){
            if (tr.getMangaName().toLowerCase().contains(title)){
                data_Truyen.add(tr);
            }
        }
        if (data_Truyen.size() > 0){
            tvSearchNotFound.setVisibility(View.GONE);
            adapterTruyen.notifyDataSetChanged();
        }
        else
            tvSearchNotFound.setVisibility(View.VISIBLE);
    }
    private void setControl() {
        searchView = findViewById(R.id.searchView);
        gridView = findViewById(R.id.gridView);
        ivBack = findViewById(R.id.ivBack);
        tvSearchNotFound = findViewById(R.id.tvSearchNotFound);
        tvTimTruyen = findViewById(R.id.tvTimTruyen);
    }
}