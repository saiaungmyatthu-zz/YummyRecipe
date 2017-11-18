package com.mm.saiaumain.yummyrecipe;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * Created by Sai Aung Myat Thu on 11/11/2017.
 */

public class NewRecipeScreen extends TimePickerPopup implements View.OnClickListener{

    private View view;
    private EditText name, prepareTime, cookingTime, totalTime;
    private ImageView image;

    private static final String TAG = "Yummy-NewRecipe";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (view == null)
            view = inflater.inflate(R.layout.new_recipe_input, container, false);
        initViews();

        prepareTime.setOnClickListener(this);
        cookingTime.setOnClickListener(this);
        totalTime.setOnClickListener(this);
        return view;
    }

    private void initViews(){
        image = (ImageView) view.findViewById(R.id.image);
        name = (EditText) view.findViewById(R.id.recipeName);
        prepareTime = (EditText) view.findViewById(R.id.prepareTime);
        cookingTime = (EditText) view.findViewById(R.id.cookingTime);
        totalTime = (EditText) view.findViewById(R.id.totalTime);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch(id){
            case R.id.prepareTime:
                displayTimerPopup(getString(R.string.add_lbl_prepare_time), prepareTime);
                break;
            case R.id.cookingTime:
                displayTimerPopup(getString(R.string.add_lbl_cooking_time), cookingTime);
                break;
            case R.id.totalTime:
                displayTimerPopup(getString(R.string.add_lbl_total_time), totalTime);
                break;
        }
    }

    private void displayTimerPopup(String title, final EditText editText){
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
