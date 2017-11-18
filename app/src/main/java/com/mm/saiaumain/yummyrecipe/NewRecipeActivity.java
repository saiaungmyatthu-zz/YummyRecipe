package com.mm.saiaumain.yummyrecipe;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.mm.saiaumain.yummyrecipe.adapters.RecipePagerAdapter;
import com.mm.saiaumain.yummyrecipe.utils.YummyRecipeUtils;

/**
 * Created by Sai Aung Myat Thu on 11/14/2017.
 */

public class NewRecipeActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener{

    private ImageButton back;
    private Button prev, next;
    private TextView label, totalTime;
    TextInputEditText recipeName, prepareTime, cookingTime;
    private LinearLayout indicatorLayout, total;
    private ViewPager pager;
    protected View view;
    private ImageView[] indicators;
    private int indicatorCount;
    private RecipePagerAdapter adapter;

    private String[] labels = new String[]{"Recipe Name & Time", "Recipe Image", "Serving", "Ingredients", "Directions"};
    private int[] layouts = new int[]{R.layout.input_1, R.layout.input_2, R.layout.input_3, R.layout.input_4, R.layout.input_4};
    private static final String TAG = "Yummy-NewRecipe";

    private void initViews(){
        back = (ImageButton) findViewById(R.id.btnBack);
        prev = (Button) findViewById(R.id.btnPrev);
        next = (Button) findViewById(R.id.btnNxt);
        label = (TextView) findViewById(R.id.label);
        label.setText(labels[0]);
        indicatorLayout = (LinearLayout) findViewById(R.id.indicator);
        pager = (ViewPager) findViewById(R.id.pager);

        adapter = new RecipePagerAdapter(this, labels, layouts);
        pager.setAdapter(adapter);
        pager.setCurrentItem(0);
        pager.setOnPageChangeListener(this);

        next.setOnClickListener(this);
        prev.setOnClickListener(this);
        back.setOnClickListener(this);
        displayIndicators();
    }

    private void displayIndicators(){
        indicatorCount = adapter.getCount();
        indicators = new ImageView[indicatorCount];
        for (int i = 0; i < indicatorCount; i++) {
            indicators[i] = new ImageView(this);
            indicators[i].setImageDrawable(getResources().getDrawable(R.drawable.unselected_item));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(20, 0, 20, 0);
            indicatorLayout.addView(indicators[i], params);
        }
        indicators[0].setImageDrawable(getResources().getDrawable(R.drawable.selected_item));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_recipe_screen);
        initViews();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
        label.setText(labels[position]);
        for (int i = 0; i < indicatorCount; i++) {
            indicators[i].setImageDrawable(getResources().getDrawable(R.drawable.unselected_item));
        }
        indicators[position].setImageDrawable(getResources().getDrawable(R.drawable.selected_item));
        prev.setVisibility(position == 0? View.GONE : View.VISIBLE);
        next.setText(position == indicators.length - 1? R.string.lbl_subm : R.string.lbl_next);

        View itemView = pager.findViewWithTag("item_" + position);
        switch (position){
            case 0:
                doStep1(itemView);
                break;
            case 1:
                doStep2(itemView);
                break;
            case 2:
                doStep3(itemView);
                break;
            case 3:
                doStep4(itemView);
                break;
            case 4:
                doStep5(itemView);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {}

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnNxt:
                pager.setCurrentItem((pager.getCurrentItem() < indicatorCount)
                        ? pager.getCurrentItem() + 1 : 0);
                break;
            case R.id.btnPrev:
                pager.setCurrentItem(pager.getCurrentItem() - 1);
                break;
            case R.id.btnBack:
                Intent intent = new Intent(this, MainActivity.class);
                this.startActivity(intent);
                break;
        }
    }

    private void doStep1(View view){
        recipeName = (TextInputEditText) view.findViewById(R.id.recipeName);
        prepareTime = (TextInputEditText) view.findViewById(R.id.pTime);
        prepareTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayTimerPopup(getString(R.string.add_lbl_prepare_time), prepareTime);
            }
        });
        cookingTime = (TextInputEditText) view.findViewById(R.id.cTime);
        cookingTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayTimerPopup(getString(R.string.add_lbl_cooking_time), cookingTime);
            }
        });
        total = (LinearLayout) view.findViewById(R.id.total);
        totalTime = (TextView) view.findViewById(R.id.totalTime);
    }

    private void doStep2(View view){

    }

    private void doStep3(View view){

    }

    private void doStep4(View view){

    }

    private void doStep5(View view){

    }

    private void displayTimerPopup(String title, final TextInputEditText editText){
        TimePickerPopup popup = new TimePickerPopup().createInstance(title, new TimePickerPopup.Callback() {
            @Override
            public void onTimeSelectResult(String value) {
                Log.e(TAG, "Selected value from callback is >>>>>> " + value);
                editText.setText(value);
            }
        });
        popup.setCancelable(false);
        popup.show(getFragmentManager(), "dialog");
    }
}
