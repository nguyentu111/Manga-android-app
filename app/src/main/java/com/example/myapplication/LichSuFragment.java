package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class LichSuFragment extends Fragment {
    GridView gridView;

    static ChapterPage chapterPage = new ChapterPage();
    static ArrayList<Truyen> data_Truyen = ReadManga.data_Truyen;
    static AdapterTruyen adapter_Truyen;
    static Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lich_su,container,false);
        context = view.getContext();
        gridView = view.findViewById(R.id.gvLichSu);

        readLichSu();

        adapter_Truyen = new AdapterTruyen(getActivity(), R.layout.item_truyen, data_Truyen);
        gridView.setAdapter(adapter_Truyen);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter_Truyen != null) {
            loadTruyen(context);
            adapter_Truyen.notifyDataSetChanged();
        }
    }
    private static void freshDataTruyen(ArrayList<Truyen> data_Truyen) {
        if (data_Truyen.size()>1){
            Set<String> seenMangaIds = new HashSet<>();
            Iterator<Truyen> iterator = data_Truyen.iterator();
            while (iterator.hasNext()) {
                Truyen truyen = iterator.next();
                if (seenMangaIds.contains(truyen.getMangaId())) {
                    iterator.remove();
                } else {
                    seenMangaIds.add(truyen.getMangaId());
                }
            }
        }
    }


    public static void loadTruyen(Context context) {
        data_Truyen = ReadManga.data_Truyen;
        freshDataTruyen(data_Truyen);
        saveLichSu(context);
    }

    public static void readLichSu() {
        try {
            File file = new File(context.getFilesDir(), "dataLichSu.bin");
            if (!file.exists()) {
                return;
            }
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            ArrayList<Truyen> temp = (ArrayList<Truyen>) ois.readObject();
            if (temp.size()>0){
                data_Truyen.addAll(temp);
            }
            ois.close();
            fis.close();

            freshDataTruyen(data_Truyen);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void saveLichSu(Context context) {
        try {

            File file = File.createTempFile("dataLichSu", ".bin", context.getFilesDir());
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(data_Truyen);
            oos.close();
            fos.close();
            File newFile = new File(context.getFilesDir(), "dataLichSu.bin");
            if (!newFile.exists()) {
                newFile.createNewFile();
            }
            file.renameTo(newFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
