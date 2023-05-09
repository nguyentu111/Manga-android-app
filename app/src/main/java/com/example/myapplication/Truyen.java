package com.example.myapplication;

import org.json.JSONObject;

import java.io.Serializable;

public class Truyen implements Serializable {
    String mangaId, mangaName, imgUrl, dataStr;
    JSONObject currentReadChap=null, firstChap = null, lastChap =null;
    int check;
    public Truyen() {
    }

    public Truyen(String mangaId, String mangaName, String imgUrl, String dataStr, String lastChapter, int check) {
        this.mangaId = mangaId;
        this.mangaName = mangaName;
        this.imgUrl = imgUrl;
        this.dataStr = dataStr;
        this.check = check;
    }

    public String getMangaId() {
        return mangaId;
    }

    public void setMangaId(String mangaId) {
        this.mangaId = mangaId;
    }

    public String getMangaName() {
        return mangaName;
    }

    public void setMangaName(String mangaName) {
        this.mangaName = mangaName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDataStr() {
        return dataStr;
    }

    public void setDataStr(String dataStr) {
        this.dataStr = dataStr;
    }


    public int getCheck() {
        return check;
    }

    public void setCheck(int check) {
        this.check = check;
    }


    public JSONObject getFirstChap() {
        return firstChap;
    }

    public void setFirstChap(JSONObject fistChap) {
        this.firstChap = fistChap;
    }

    public JSONObject getCurrentReadChap() {
        return currentReadChap;
    }

    public void setCurrentReadChap(JSONObject currentReadChap) {
        this.currentReadChap = currentReadChap;
    }

    public JSONObject getLastChap() {
        return lastChap;
    }

    public void setLastChap(JSONObject lastChap) {
        this.lastChap = lastChap;
    }

    public void convertTruyen(Truyen truyen){
        this.mangaId = truyen.getMangaId();
        this.mangaName = truyen.getMangaName();
        this.imgUrl = truyen.getImgUrl();
        this.dataStr = truyen.getDataStr();
        this.check = truyen.getCheck();
    }
}
