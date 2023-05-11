package com.example.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

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
import java.util.LinkedHashSet;

public class KeSachFragment extends Fragment {
    GridView gridView;
    static DescriptionPage descriptionPage = new DescriptionPage();
    static ArrayList<Truyen> data_Truyen = descriptionPage.data_Truyen;
    static AdapterTruyen adapter_Truyen;
    static Context context;
    Dialog dialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ke_sach,container,false);
        context =  view.getContext();
        gridView = view.findViewById(R.id.gvKeSach);

        readKeSach();
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
            LinkedHashSet<Truyen> uniqueTruyens = new LinkedHashSet<>(data_Truyen);
            data_Truyen.clear();
            data_Truyen.addAll(uniqueTruyens);
        }
    }
    public static void loadTruyen(Context context) {
        data_Truyen = descriptionPage.data_Truyen;
        freshDataTruyen(data_Truyen);
        saveKeSach(context);
    }


    public static void readKeSach() {
        try {
            File file = new File(context.getFilesDir(), "dataKeSach.bin");
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

    public static void saveKeSach(Context context) {
        try {
            File file = File.createTempFile("dataKeSach", ".bin", context.getFilesDir());
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(data_Truyen);
            oos.close();
            fos.close();
            File newFile = new File(context.getFilesDir(), "dataKeSach.bin");
            if (!newFile.exists()) {
                newFile.createNewFile();
            }
            file.renameTo(newFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
