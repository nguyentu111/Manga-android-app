package com.example.myapplication;

import static com.example.myapplication.Utils.loadPages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

public class ReadManga extends AppCompatActivity implements ActionBottomDialogFragment.ItemClickListener,ChapterBottomDialogFragment.ItemClickListener {
    LinearLayout pagesLayout;
    LinearLayout topMenu;
    CardView backBtn;
    TextView read_manga_name;
    TextView read_chapter_name;
    CardView menuRead;
    ActionBottomDialogFragment bottomDialogFragment;
    ChapterBottomDialogFragment chapterBottomDialogFragment;
    LinearLayout show_chapter_bottom_btn;
    ScrollView read_scroll_view;
    Context context;
    private float mScale = 1f;
    private ScaleGestureDetector mScaleGestureDetector;
    GestureDetector gestureDetector;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_manga);
        context = this;
        backBtn= findViewById(R.id.back_to_chapter);
        pagesLayout=findViewById(R.id.pagesLayout);
        read_manga_name = findViewById(R.id.read_manga_name);
        read_chapter_name = findViewById(R.id.read_chapter_name);
        menuRead = findViewById(R.id.menuRead);
        topMenu=findViewById(R.id.topMenu);
        read_scroll_view = findViewById(R.id.read_scroll_view);
        show_chapter_bottom_btn = findViewById(R.id.show_chapter_bottom_sheet_btn);
        topMenu.setVisibility(View.GONE);
        Bundle extras = getIntent().getExtras();
        String dataStr = extras.getString("data");
        String chapterId= extras.getString("chapterId");
        loadPages(pagesLayout,chapterId,this);
        ////////
        String chapterName =  extras.getString("chapterName");
        String mangaName = extras.getString("mangaName");
        String mangaId = extras.getString("mangaId");
        String currentLanguage = extras.getString("currentLanguage");
        String coverUrl = extras.getString("coverUrl");


        bottomDialogFragment = ActionBottomDialogFragment.newInstance();
        chapterBottomDialogFragment = new ChapterBottomDialogFragment(currentLanguage,mangaId,coverUrl,mangaName,chapterId,pagesLayout,read_chapter_name,context);
        read_manga_name.setText(mangaName);
        read_chapter_name.setText(chapterName);
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
        show_chapter_bottom_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChapterBottomSheet();
            }
        });

    }



    public void showBottomSheet() {
        if(bottomDialogFragment.isVisible()) {
            return;
        }
            bottomDialogFragment.show(getSupportFragmentManager(),
                    ActionBottomDialogFragment.TAG);
    }
    public void showChapterBottomSheet() {
        if(chapterBottomDialogFragment.isVisible()) {
            return;
        }
        chapterBottomDialogFragment.show(getSupportFragmentManager(),
                ChapterBottomDialogFragment.TAG);
        chapterBottomDialogFragment.scrollToCurrentChapter();
    }
    @Override
    public void onItemClick(String item) {

    }
}