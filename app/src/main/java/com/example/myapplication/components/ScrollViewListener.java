package com.example.myapplication.components;

public interface ScrollViewListener {
    void onScrollChanged(CustomScrollView scrollView,
                         int x, int y, int oldx, int oldy);
}