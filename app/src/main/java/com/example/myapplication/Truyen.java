package com.example.myapplication;

import org.json.JSONObject;

import java.io.Serializable;

public class Truyen implements Serializable {
    String mangaId, mangaName, imgUrl, dataStr;
    String currentReadChap, firstChap = null, lastChap = null;
    String currentReadChapJSONObject = null;
    int check;
    public Truyen() {
    }

    public Truyen(String mangaId, String mangaName, String imgUrl, String dataStr, int check) {
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


    public String getCurrentReadChap() {
        return currentReadChap;
    }

    public void setCurrentReadChap(String currentReadChap) {
        this.currentReadChap = currentReadChap;
    }

    public String getFirstChap() {
        return firstChap;
    }

    public void setFirstChap(String firstChap) {
        this.firstChap = firstChap;
    }

    public String getLastChap() {
        return lastChap;
    }

    public void setLastChap(String lastChap) {
        this.lastChap = lastChap;
    }

    public String getCurrentReadChapJSONObject() {
        return currentReadChapJSONObject;
    }

    public void setCurrentReadChapJSONObject(String currentReadChapJSONObject) {
        this.currentReadChapJSONObject = currentReadChapJSONObject;
    }

    public void convertTruyen(Truyen truyen){
        this.mangaId = truyen.getMangaId();
        this.mangaName = truyen.getMangaName();
        this.imgUrl = truyen.getImgUrl();
        this.dataStr = truyen.getDataStr();
        this.check = truyen.getCheck();
    }

}
