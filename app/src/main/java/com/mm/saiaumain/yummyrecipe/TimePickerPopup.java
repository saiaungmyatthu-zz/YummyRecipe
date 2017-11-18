package com.mm.saiaumain.yummyrecipe;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

/**
 * Created by Sai Aung Myat Thu on 11/11/2017.
 */

public class TimePickerPopup extends DialogFragment {

    private TimePickerPopup popup;
    private NumberPicker hrPicker, minPicker;
    private Button btnOk, btnCancel;
    private TextView dialogTitle;
    private View view;

    private static final String TAG = "Yummy-TimePickerPopup";
    private Callback callback;
    private String selectedValue;

    public interface Callback{
       void onTimeSelectResult(String value);
    }

    public TimePickerPopup createInstance(String title, Callback callback) {
        Log.e(TAG, "Call back inside createInstance is >>>> " + callback);
        popup = new TimePickerPopup();
        popup.callback = callback;
        Bundle args = new Bundle();
        args.putString("title", title);
        popup.setArguments(args);
        return popup;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        Log.e(TAG, "Title value is >>>>> " + title);
        if(view == null)
            view = inflater.inflate(R.layout.time_picker, container, false);

        initViews();
        dialogTitle.setText(title);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder result = new StringBuilder();
                result.append(hrPicker.getValue()).append("Hr ").append(minPicker.getValue()).append("Min");
                Log.e(TAG, "***** result is >>>> " + result.toString());
                selectedValue = result.toString();
                callback.onTimeSelectResult(selectedValue);
                dismiss();
            }
        });
        return view;
    }

    private void initViews(){
        hrPicker = (NumberPicker) view.findViewById(R.id.hourPicker);
        hrPicker.setMinValue(0);
        hrPicker.setMaxValue(24);
        minPicker = (NumberPicker) view.findViewById(R.id.minutePicker);
        minPicker.setMinValue(0);
        minPicker.setMaxValue(59);
        btnOk = (Button) view.findViewById(R.id.btnOk);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        dialogTitle = (TextView) view.findViewById(R.id.title);
    }
}
