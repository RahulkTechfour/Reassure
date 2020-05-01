package com.luminous.pdi.custom_text_view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class TextViewSemiBold extends AppCompatTextView {


    public TextViewSemiBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public TextViewSemiBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextViewSemiBold(Context context) {
        super(context);
        init();
    }


    private void init() {

        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/Muli-Bold.ttf");
        setTypeface(tf);
    }
}

