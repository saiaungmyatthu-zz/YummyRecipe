package com.mm.saiaumain.yummyrecipe.utils;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mm.saiaumain.yummyrecipe.R;
import com.mm.saiaumain.yummyrecipe.RecipeListScreen;
import com.mm.saiaumain.yummyrecipe.adapters.GalleryAdapter;
import com.mm.saiaumain.yummyrecipe.adapters.RecipeAdapter;
import com.mm.saiaumain.yummyrecipe.vo.Recipe;
import com.mm.saiaumain.yummyrecipe.vo.RecipeItem;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Sai Aung Myat Thu on 11/5/2017.
 */

public class UIComponentUtils {

    private static final String TAG = "Yummy-UIComponentUtils";
    private static final int MAX_COUNT = 5;

    public static View getSingleView(Context context, final FragmentManager fragManager, LayoutInflater inflater, ViewGroup container, final RecipeItem item){
        Log.e(TAG, "***** doSingleView *****");
        Log.e(TAG,"*** Single View Background image url >>>>> " + item.getBackground().getUrl());
        Drawable background = YummyRecipeUtils.getDrawableFromAsset(context, "img_400x200/" + item.getBackground().getUrl());
        int height = ((BitmapDrawable)background).getBitmap().getHeight();
        height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, context.getResources().getDisplayMetrics());

        View singleView = inflater.inflate(R.layout.single_layout, container, false);
        ConstraintLayout.LayoutParams param = (ConstraintLayout.LayoutParams)singleView.getLayoutParams();
        param.height = height;
        singleView.requestLayout();
        singleView.setLayoutParams(param);

        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN)
            singleView.setBackgroundDrawable(background);
        else
            singleView.setBackground(background);

        TextView label = (TextView) singleView.findViewById(R.id.recipeLabel);
        String labelText = item.getLabel().replace("@@", "");
        label.setText(labelText);

        singleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doRecipeListAction(fragManager, item);
            }
        });
        return singleView;
    }

    public static View getGalleryView(final Context context, final FragmentManager fragManager,
                                  LayoutInflater inflater, ViewGroup container, final RecipeItem item){
        Log.e(TAG, "***** doGalleryView *****");
        List<Recipe> recipeList = item.getRecipeList();
        Log.e(TAG, "Recipe List Count >>>>> " + recipeList.size());
        View galleryView = inflater.inflate(R.layout.recipe_gallery, container, false);
        Drawable background = YummyRecipeUtils.getDrawableFromAsset(context, "img_400x200/" + item.getBackground().getUrl());
        int height = ((BitmapDrawable)background).getBitmap().getHeight();
        height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, context.getResources().getDisplayMetrics());
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) galleryView.getLayoutParams();
        params.height = height + (new BigDecimal(height * 0.6)).intValue();
        galleryView.setLayoutParams(params);
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN)
            galleryView.setBackgroundDrawable(background);
        else
            galleryView.setBackground(background);

        HorizontalScrollView list = (HorizontalScrollView) galleryView.findViewById(R.id.galleryScroll);
        if(recipeList != null && !recipeList.isEmpty()){
            TextView recipeCategory = (TextView) galleryView.findViewById(R.id.recipeCategory);
            String labelText = item.getLabel().replace("@@", "");
            labelText = labelText.replaceAll("\\s+", " ");
            recipeCategory.setText(labelText);
            TextView recipeCount = (TextView) galleryView.findViewById(R.id.recipeCount);
            recipeCount.setText(recipeList.size() + " Recipes");

            TextView viewMore = (TextView) galleryView.findViewById(R.id.viewMore);
            viewMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UIComponentUtils.doRecipeListAction(fragManager, item);
                }
            });

            LinearLayout listLayout = (LinearLayout) list.getChildAt(0);
            for(int i = 0; i < MAX_COUNT; i++){
                final Recipe recipe = recipeList.get(i);
                View recipeItem = YummyRecipeUtils.generateRecipeItem(context, inflater, container,
                        R.layout.gallery_item, recipe);
                //calcualteCardViewParams(context, galleryView, recipeItem, height);
                recipeItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.e(TAG, "Select Recipe Item " + recipe.getName());
                        YummyRecipeUtils.doDetailData(context, recipe);
                    }
                });
                listLayout.addView(recipeItem);
            }
        }
        return galleryView;
    }

    public static View getScrollView(final Context context, final FragmentManager fragManager,
                              LayoutInflater inflater, ViewGroup container, final RecipeItem item){
        Log.e(TAG, "***** doScrollView *****");
        List<Recipe> recipeList = item.getRecipeList();
        Log.e(TAG, "Recipe List Count >>>>> " + recipeList.size());
        HorizontalScrollView list = new HorizontalScrollView(context);
        if(recipeList != null && !recipeList.isEmpty()){
            LinearLayout scrollView = new LinearLayout(context);

            YummyRecipeUtils.setupHorizontalList(context, item, scrollView);

            for(int i = 0; i < MAX_COUNT; i++){
                final Recipe recipe = recipeList.get(i);
                CardView recipeItem = YummyRecipeUtils.generateRecipeCardItem(context, inflater, container,
                        R.layout.recipe_card, recipe);
                recipeItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.e(TAG, "Select Recipe Item " + recipe.getName());
                        YummyRecipeUtils.doDetailData(context, recipe);
                    }
                });
                scrollView.addView(recipeItem);
            }

            ImageView moreImage = new ImageView(context);
            moreImage.setImageResource(R.drawable.ic_more);
            LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParam.setMargins(30, 50, 30, 50);
            moreImage.setLayoutParams(layoutParam);
            moreImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UIComponentUtils.doRecipeListAction(fragManager, item);
                }
            });

            scrollView.addView(moreImage);
            list.addView(scrollView);
            Drawable background = YummyRecipeUtils.getDrawableFromAsset(context, "img_400x200/" + item.getBackground().getUrl());
            if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN)
                list.setBackgroundDrawable(background);
            else
                list.setBackground(background);
        }
        return list;
    }

    public static void doRecipeListAction(FragmentManager fragManager, RecipeItem item){
        RecipeListScreen fragment = new RecipeListScreen();
        Bundle extra = new Bundle();
        String json = new Gson().toJson(item);
        extra.putString("item", json);
        fragment.setArguments(extra);

        FragmentTransaction transaction = fragManager.beginTransaction();
        transaction.replace(R.id.frame, fragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
