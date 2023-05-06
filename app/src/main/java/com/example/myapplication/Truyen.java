package com.example.myapplication;

import java.io.Serializable;

public class Truyen implements Serializable {
    String mangaId, mangaName, imgUrl, dataStr, lastChapter, finalChapter,lastPage;
    int check;

    public Truyen() {
    }

    public Truyen(String mangaId, String mangaName, String imgUrl, String dataStr, String lastChapter, String finalChapter, int check) {
        this.mangaId = mangaId;
        this.mangaName = mangaName;
        this.imgUrl = imgUrl;
        this.dataStr = dataStr;
        this.lastChapter = lastChapter;
        this.finalChapter = finalChapter;
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

    public String getLastChapter() {
        return lastChapter;
    }

    public void setLastChapter(String lastChapter) {
        this.lastChapter = lastChapter;
    }

    public int getCheck() {
        return check;
    }

    public void setCheck(int check) {
        this.check = check;
    }

    public String getFinalChapter() {
        return finalChapter;
    }

    public void setFinalChapter(String finalChapter) {
        this.finalChapter = finalChapter;
    }
    public void convertTruyen(Truyen truyen){
        this.mangaId = truyen.getMangaId();
        this.mangaName = truyen.getMangaName();
        this.imgUrl = truyen.getImgUrl();
        this.dataStr = truyen.getDataStr();
        this.lastChapter = truyen.getLastChapter();
        this.finalChapter = truyen.getFinalChapter();
        this.check = truyen.getCheck();
    }
}
