package com.mm.saiaumain.yummyrecipe.adapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mm.saiaumain.yummyrecipe.R;
import com.mm.saiaumain.yummyrecipe.vo.Recipe;

import java.util.ArrayList;

/**
 * Created by Sai Aung Myat Thu on 11/13/2017.
 */

public class RecipePagerAdapter extends PagerAdapter {

    private Context context;
    private int[] layouts;

    public RecipePagerAdapter(Context context, int[] layouts){
        this.context = context;
        this.layouts = layouts;
    }

    @Override
    public int getCount() {
        return this.layouts.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = LayoutInflater.from(context).inflate(layouts[position], container, false);
        itemView.setTag("item_" + position);
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
