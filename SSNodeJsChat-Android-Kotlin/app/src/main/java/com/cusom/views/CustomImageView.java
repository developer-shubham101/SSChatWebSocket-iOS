package com.cusom.views;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;


import com.bumptech.glide.Glide;



public class CustomImageView extends AppCompatImageView {


    private boolean isOn = false;
    private Context context;

    public CustomImageView(Context context) {

        super(context);
        this.context= context;
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context= context;
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context= context;
    }


    public boolean isOn() {

        return isOn;

    }

/*
    public void setOn(boolean on) {
        isOn = on;
        if(isOn)
        {
            Glide.with(context).load(R.drawable.ic_update_icon).into(this);

        } else{
            Glide.with(context).load(R.drawable.set_edit_icon).into(this);
        }

    }*/
}
