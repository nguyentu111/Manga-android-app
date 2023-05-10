package com.example.myapplication;

import static com.example.myapplication.Utils.loadPages;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ChapterBottomDialogFragment extends BottomSheetDialogFragment
        implements View.OnClickListener {
    public static final String TAG = "ChapterBottomDialog";
    String currentLanguage;
    String mangaId;
    String coverUrl;
    String mangaName;
    String currentChapterId;
    LinearLayout pagesLayout;
    TextView read_chapter_name;
    private ItemClickListener mListener;
    TextView current_reading_chapter_textview;
    NestedScrollView scrollView;
    LinearLayout bottom_sheet_chapters;
    static float chap = 0;
    Context context;
//    public static ChapterBottomDialogFragment newInstance(JSONArray chapters) {
//        return new ChapterBottomDialogFragment(chapters);
//    }

    public ChapterBottomDialogFragment(String currentLanguage , String mangaId, String coverUrl, String mangaName
            , String currentChapterId, LinearLayout pagesLayout, TextView read_chapter_name, Context context){
        super();
        this.currentLanguage = currentLanguage;
        this.mangaId = mangaId;
        this.coverUrl=coverUrl;
        this.mangaName=mangaName;
        this.currentChapterId = currentChapterId;
        this.pagesLayout =  pagesLayout;
        this.read_chapter_name =read_chapter_name;
        this.context = context;
    }
    private int getScreenHeight(){
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
    public void scrollToCurrentChapter(){
        if(bottom_sheet_chapters !=null){
            scrollView.scrollTo(0, ((int) (current_reading_chapter_textview.getTop() )));
        }
    }
    @NonNull @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;

                FrameLayout bottomSheet = (FrameLayout) d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                BottomSheetBehavior.from(bottomSheet).setMaxHeight((int) (getScreenHeight()/1.5));

//                if(  current_reading_chapter_textview !=null){
//
//                }
//                Point childOffset = new Point();
//                getDeepChildOffset(scrollViewParent, view.getParent(), view, childOffset);
                // Scroll to child.
//                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
//                    // In landscape
//
//                } else {
//                    // In portrait
//                }

            }
        });

        // Do something with your dialog like setContentView() or whatever
        return dialog;
    }
    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.chapter_bottom_sheet, container, false);
    }
    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        scrollView = view.findViewById(R.id.bottom_sheet_chapters_scroolview);
        bottom_sheet_chapters = view.findViewById(R.id.bottom_sheet_chapters);
        TextView bottom_sheet_manga_name = view.findViewById(R.id.bottom_sheet_manga_name);
        bottom_sheet_manga_name.setText(mangaName);
        ImageView bottom_sheet_img_cover = view.findViewById(R.id.bottom_sheet_img_cover);

        Picasso.get()
                .load(coverUrl)
//                .resize(30 ,45)
//                .centerCrop()
                .into(bottom_sheet_img_cover);
        fetchApi(view);
    }
//    public class ChapUtil {
//        public static float chap = 0;
//    }

    private void fetchApi(View view) {
        RequestQueue queue = Volley.newRequestQueue(view.getContext());
        String url="https://api.mangadex.org/manga/"+mangaId+"/feed?limit=500&translatedLanguage%5B%5D="+currentLanguage+"&order%5Bvolume%5D=desc&order%5Bchapter%5D=desc";
        LinearLayout bottom_sheet_chapters = view.findViewById(R.id.bottom_sheet_chapters);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray datas=   response.getJSONArray("data");

                            for(int i =0;i<datas.length();i++){

                                TextView textView = new TextView(view.getContext());
                                textView.setTextColor(Color.WHITE);
                                LinearLayout.LayoutParams paramsText3 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,  1.0f);
                                textView.setPadding(45,45,15,45);
                                textView.setLayoutParams(paramsText3);
                                JSONObject chapter = datas.getJSONObject(i);
                                String chapterId = chapter.getString("id");
                                if( chapterId.equals(currentChapterId)) {
                                    textView.setBackgroundColor(Color.GRAY);
                                    current_reading_chapter_textview = textView;
                                }
                                String title = chapter.getJSONObject("attributes").getString("title");
                                String chapStr = chapter.getJSONObject("attributes").getString("chapter");
                                String calculatedName ;
                                if(title.equals("null") || title.toString().trim().equals("") ){
                                    if(chapStr.equals("null")){
                                        calculatedName="One shot";
                                    }else{
                                        calculatedName= "Chap "+chapStr;
                                    }
                                }else{
                                    calculatedName="Chap "+chapStr+": "+title;
                                }
                                textView.setText(calculatedName);
                                textView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        chap = Float.parseFloat(chapStr);
                                        for (Truyen tr : LichSuFragment.data_Truyen){
                                            if (tr.getMangaId().equals(mangaId)
                                               ){
                                                tr.setCurrentReadChap(chapter.toString());
                                                LichSuFragment.saveLichSu(context);
                                            }
                                        }

                                        read_chapter_name.setText(calculatedName);

                                        if(chapterId.equals(currentChapterId)) {
                                            dismiss();
                                            return;
                                        }
                                        currentChapterId = chapterId;
                                        final int childCount = pagesLayout.getChildCount();
                                        for (int i = 0; i < childCount; i++) {
                                            View v = pagesLayout.getChildAt(i);
                                            v.setBackgroundColor(Color.TRANSPARENT);
                                        }
                                        current_reading_chapter_textview = textView;
                                        textView.setBackgroundColor(Color.GRAY);
                                        dismiss();

                                        loadPages(pagesLayout,chapterId,view.getContext());
                                    }
                                });
                                bottom_sheet_chapters.addView(textView);
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toast.makeText(view.getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        queue.add(jsonObjectRequest);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ItemClickListener) {
            mListener = (ItemClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ItemClickListener");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    @Override public void onClick(View view) {
        TextView tvSelected = (TextView) view;
        mListener.onItemClick(tvSelected.getText().toString());
        tvSelected.setBackgroundColor(Color.GREEN);
        dismiss();
    }
    public interface ItemClickListener {
        void onItemClick(String item);
    }
}