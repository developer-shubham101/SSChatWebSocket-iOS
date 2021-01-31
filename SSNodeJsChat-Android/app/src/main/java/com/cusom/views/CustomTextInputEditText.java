package com.cusom.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;


import com.google.android.material.textfield.TextInputEditText;

import in.newdevpoint.ssnodejschat.R;


public class CustomTextInputEditText extends TextInputEditText {

    public CustomTextInputEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        //Typeface.createFromAsset doesn't work in the layout editor. Skipping...
        /*if (isInEditMode()) {
            return;
        }*/

        TypedArray styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.TypeFacedTextView);

//        String fontName = styledAttrs.getString(R.styleable.TypeFacedTextView_typeface);
        int weight = styledAttrs.getInt(R.styleable.TypeFacedTextView_weight,
                0);
        styledAttrs.recycle();

        switch (weight) {
            case 0:
                Typeface typeface = Typeface.createFromAsset(context.getAssets(), "Roboto-Regular.ttf");
                setTypeface(typeface);
                break;
            case 1:
                Typeface typeface1 = Typeface.createFromAsset(context.getAssets(), "Roboto-Bold.ttf");
                setTypeface(typeface1);
                break;
            case 2:
                Typeface typeface2 = Typeface.createFromAsset(context.getAssets(), "Roboto-BoldItalic.ttf");
                setTypeface(typeface2);
                break;
            case 3:
                Typeface typeface3 = Typeface.createFromAsset(context.getAssets(), "Roboto-Italic.ttf");
                setTypeface(typeface3);
                break;
            case 4:
                Typeface typeface4 = Typeface.createFromAsset(context.getAssets(), "Roboto-Light.ttf");
                setTypeface(typeface4);
                break;
            case 5:
                Typeface typeface5 = Typeface.createFromAsset(context.getAssets(), "Roboto-LightItalic.ttf");
                setTypeface(typeface5);
                break;

            case 6:
                Typeface typeface6 = Typeface.createFromAsset(context.getAssets(), "Roboto-Medium.ttf");
                setTypeface(typeface6);
                break;

            case 7:
                Typeface typeface7 = Typeface.createFromAsset(context.getAssets(), "Roboto-MediumItalic.ttf");
                setTypeface(typeface7);
                break;

            case 8:
                Typeface typeface8 = Typeface.createFromAsset(context.getAssets(), "Roboto-Black.ttf");
                setTypeface(typeface8);
                break;

            case 9:
                Typeface typeface9 = Typeface.createFromAsset(context.getAssets(), "Roboto-Thin.ttf");
                setTypeface(typeface9);
                break;

            case 10:
                Typeface typeface10  = Typeface.createFromAsset(context.getAssets(), "Roboto-ThinItalic.ttf");
                setTypeface(typeface10);
                break;

        }
    }

}