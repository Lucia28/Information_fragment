package com.example.information_fragment;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import java.util.ArrayList;

public class Adapter extends PagerAdapter {
    public ArrayList<ImageView> viewlist;
    public Adapter(ArrayList<ImageView> viewlist) {
        this.viewlist = viewlist;
    }
    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }
    @Override
    public boolean isViewFromObject (View v, Object o) {
        return v == o;
    }
    @Override
    public void destroyItem (ViewGroup container, int position, Object object) {

    }
    @Override
    public Object instantiateItem (ViewGroup container, int position) {
        position = position % viewlist.size();
        if (position < 0) {
            position = viewlist.size() + position;
        }
        ImageView view = viewlist.get(position);
        ViewParent vp = view.getParent();
        if (vp != null) {
            ViewGroup parent = (ViewGroup) vp;
            parent.removeView(view);
        }
        container.addView(view);
        return view;
    }
}

