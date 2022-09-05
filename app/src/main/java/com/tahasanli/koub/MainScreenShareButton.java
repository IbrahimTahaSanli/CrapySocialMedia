package com.tahasanli.koub;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

public class MainScreenShareButton extends Button {
    public MainScreenShareButton(Context context, AttributeSet attrs){
        super(context, attrs);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(width, width);
    }
}
