package com.mm.saiaumain.yummyrecipe;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.gson.Gson;
import com.mm.saiaumain.yummyrecipe.utils.YummyRecipeUtils;
import com.mm.saiaumain.yummyrecipe.vo.Recipe;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView serving, totalTime, prepareTime, cookTime, ingredientText, stepText, title;
    private ImageView share, back, detailImage;
    private ToggleButton shopToggle, favToggle;
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout flexToolbar;

    private static final String TAG = "Yummy-DetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_detail);

        String detailData = getIntent().getExtras().getString("detail");
        if(null != detailData){
            final Recipe item = new Gson().fromJson(detailData, Recipe.class);
            appBarLayout = (AppBarLayout) findViewById(R.id.appBar);
            flexToolbar = (CollapsingToolbarLayout) findViewById(R.id.flexToolBar);
            flexToolbar.setTitle(" ");
            appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                boolean isShow = false;
                int scrollRange = -1;
                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                    if (scrollRange == -1) {
                        scrollRange = appBarLayout.getTotalScrollRange();
                    }
                    if (scrollRange + verticalOffset == 0) {
                        flexToolbar.setTitle(item.getName().toUpperCase());
                        isShow = true;
                    } else if(isShow) {
                        flexToolbar.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
                        isShow = false;
                    }
                }
            });
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            //setTitle(item.getName().toUpperCase());

            title = (TextView) findViewById(R.id.title);
            title.setText(item.getName().toUpperCase());

            share = (ImageView) findViewById(R.id.img_share);
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(DetailActivity.this, "Share!!!", Toast.LENGTH_SHORT).show();
                }
            });

            shopToggle = (ToggleButton) findViewById(R.id.shopToggle);
            shopToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if(isChecked)
                        Toast.makeText(DetailActivity.this, "Add " + item.getName() + "'s ingredients to buy list.", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(DetailActivity.this, "Remove " + item.getName() + "'s ingredients to buy list.", Toast.LENGTH_SHORT).show();
                }
            });

            detailImage = (ImageView) findViewById(R.id.detailImage);
            Log.e(TAG,"*** Single View Background image url >>>>> " + item.getImageInfo().getUrl());
            Drawable background = YummyRecipeUtils.getDrawableFromAsset(this, "img_150x180/" + item.getImageInfo().getUrl());
            detailImage.setImageBitmap(((BitmapDrawable)background).getBitmap());

            favToggle = (ToggleButton) findViewById(R.id.favToggle);
            favToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if(isChecked)
                        Toast.makeText(DetailActivity.this, "Add " + item.getName() + "to favourite list.", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(DetailActivity.this, "Remove " + item.getName() + "from favourite list.", Toast.LENGTH_SHORT).show();
                }
            });

            back = (ImageView) findViewById(R.id.back);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DetailActivity.super.onBackPressed();
                }
            });

            serving = (TextView) findViewById(R.id.servingCount);
            serving.setText("SERVING: " + item.getServingCount());
            totalTime = (TextView) findViewById(R.id.value1);
            totalTime.setText(item.getTotalTime());
            prepareTime = (TextView) findViewById(R.id.value2);
            prepareTime.setText(item.getTotalTime());
            cookTime = (TextView) findViewById(R.id.value3);
            cookTime.setText(item.getTotalTime());

            List<String> ingredientDataList = item.getIngredients();
            if(null != ingredientDataList && !ingredientDataList.isEmpty()){
                StringBuilder ingredientBuilder = new StringBuilder();
                for(int i = 0; i < ingredientDataList.size(); i++){
                    String data = ingredientDataList.get(i);
                    ingredientBuilder.append((i+1)).append(". ");
                    ingredientBuilder.append(data).append("\n");
                }
                ingredientText = (TextView) findViewById(R.id.ingredientText);
                ingredientText.setText(ingredientBuilder.toString());
            }

            List<String> stepsDataList = item.getSteps();
            if(null != stepsDataList && !stepsDataList.isEmpty()){
                StringBuilder stepBuilder = new StringBuilder();
                for(int j = 0; j < stepsDataList.size(); j++){
                    String step = stepsDataList.get(j);
                    stepBuilder.append((j+1)).append(". ");
                    stepBuilder.append(step).append("\n");
                }
                stepText = (TextView) findViewById(R.id.stepText);
                stepText.setText(stepBuilder.toString());
            }
        }
    }
}
