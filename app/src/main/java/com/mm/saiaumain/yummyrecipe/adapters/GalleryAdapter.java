package com.mm.saiaumain.yummyrecipe.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mm.saiaumain.yummyrecipe.R;
import com.mm.saiaumain.yummyrecipe.uicomponents.CardImage;
import com.mm.saiaumain.yummyrecipe.utils.YummyRecipeUtils;
import com.mm.saiaumain.yummyrecipe.vo.Recipe;

import java.util.List;

/**
 * Created by Sai Aung Myat Thu on 11/5/2017.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.RecipeHolder> {

    private final static String TAG = "Yummy-GalleryAdapter";
    private Context context;
    private List<Recipe> recipeList;
    private int resourceId;

    public GalleryAdapter(Context context, List<Recipe> recipeList, int resourceId){
        this.context = context;
        this.recipeList = recipeList;
        this.resourceId = resourceId;
    }

    @Override
    public GalleryAdapter.RecipeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View recipeView = LayoutInflater.from(context).inflate(resourceId, parent, false);
        return new GalleryAdapter.RecipeHolder(recipeView);
    }

    @Override
    public void onBindViewHolder(GalleryAdapter.RecipeHolder holder, int position) {
        final Recipe recipe = recipeList.get(position);
        Log.e(TAG, "**** Image Url >>>>> " + recipe.getImageInfo().getUrl());
        Drawable background = YummyRecipeUtils.getDrawableFromAsset(context, "img_150x180/" + recipe.getImageInfo().getUrl());
        holder.image.setImageDrawable(background);

        String recipeName = recipe.getName().replace("\n", "");
        if(recipeName.length() > 21){
            recipeName = recipeName.substring(0, 19);
            recipeName = recipeName.concat("...");
        }
        holder.name.setText(recipeName);
        holder.time.setText(recipe.getTotalTime());
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Log.e(TAG, "Select Recipe Item " + recipe.getName());
            YummyRecipeUtils.doDetailData(context, recipe);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public class RecipeHolder extends RecyclerView.ViewHolder{
        public ImageView image;
        public TextView name;
        public CheckedTextView time;
        public CardView card;

        public RecipeHolder(View view){
            super(view);
            card = (CardView) view.findViewById(R.id.recipeCard);
            image = (ImageView) card.findViewById(R.id.image);
            name = (TextView) card.findViewById(R.id.name);
            time = (CheckedTextView) card.findViewById(R.id.time);
        }
    }
}
