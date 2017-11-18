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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mm.saiaumain.yummyrecipe.R;
import com.mm.saiaumain.yummyrecipe.uicomponents.CardImage;
import com.mm.saiaumain.yummyrecipe.utils.YummyRecipeUtils;
import com.mm.saiaumain.yummyrecipe.vo.Recipe;

import java.util.List;

/**
 * Created by Sai Aung Myat Thu on 10/29/2017.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeHolder>{

    private final static String TAG = "Yummy-RecipeAdapter";

    private Context context;
    private List<Recipe> recipeList;
    private int resourceId;

    public RecipeAdapter(Context context, List<Recipe> recipeList, int resourceId){
        this.context = context;
        this.recipeList = recipeList;
        this.resourceId = resourceId;
    }

    @Override
    public RecipeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View recipeView = LayoutInflater.from(context).inflate(resourceId, parent, false);
        return new RecipeHolder(recipeView);
    }

    @Override
    public void onBindViewHolder(RecipeHolder holder, int position) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(15, 15, 15, 15);
        final Recipe recipe = recipeList.get(position);
        Log.e(TAG, "**** Image Url >>>>> " + recipe.getImageInfo().getUrl());
        Drawable background = YummyRecipeUtils.getDrawableFromAsset(context, "img_150x180/" + recipe.getImageInfo().getUrl());
        Bitmap bitmap = ((BitmapDrawable)background).getBitmap();
        holder.image.setImageBitmap(bitmap);

        String recipeName = recipe.getName().replace("\n", "");
        if(recipeName.length() > 21){
            recipeName = recipeName.substring(0, 19);
            recipeName = recipeName.concat("...");
        }
        holder.name.setText(recipeName);
        holder.time.setText(recipe.getTotalTime());
        holder.card.setLayoutParams(params);

        List<String> ingridentList = recipe.getIngredients();
        if(null != ingridentList && !ingridentList.isEmpty()){
            LinearLayout.LayoutParams itemParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            itemParam.setMargins(5, 5, 5, 5);
            holder.layout.removeAllViewsInLayout();
            for(int i = 0; i < 3; i++){
                String ingrident = ingridentList.get(i);
                if(null != ingrident){
                    TextView item = new TextView(context);
                    item.setText("#" + ingrident);
                    item.setLayoutParams(itemParam);
                    holder.layout.addView(item);
                }
            }
        }

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
        public CardImage image;
        public TextView name;
        public CheckedTextView time;
        public CardView card;
        public LinearLayout layout;

        public RecipeHolder(View view){
            super(view);
            card = (CardView) view.findViewById(R.id.recipeCard);
            image = (CardImage) card.findViewById(R.id.image);
            name = (TextView) card.findViewById(R.id.name);
            time = (CheckedTextView) card.findViewById(R.id.time);
            layout = (LinearLayout) card.findViewById(R.id.cardMain);
        }
    }
}
