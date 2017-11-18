package com.mm.saiaumain.yummyrecipe.uicomponents;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * Created by Sai Aung Myat Thu on 10/8/2017.
 */

public class CardImage extends AppCompatImageView {
    public CardImage(Context context) {
        super(context);
    }

    public CardImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CardImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Drawable image = getDrawable();
        if(image != null){
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = width * image.getIntrinsicHeight() / image.getIntrinsicWidth();
            setMeasuredDimension(width, height);
        }else{
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
