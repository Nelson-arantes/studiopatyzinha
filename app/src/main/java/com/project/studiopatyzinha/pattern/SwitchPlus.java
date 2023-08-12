package com.project.studiopatyzinha.pattern;


import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.widget.Switch;

public class SwitchPlus extends Switch {

    public SwitchPlus(Context context) {
        super(context);
    }

    public SwitchPlus(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SwitchPlus(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setChecked(boolean checked) {
        super.setChecked(checked);
        changeColor(checked);
    }

    private void changeColor(boolean isChecked) {
        int thumbColor;
        int trackColor;

        if(isChecked) {
            trackColor = Color.argb(255,150,0,0);
            thumbColor = Color.argb(255,150,0,0);
        } else {
            thumbColor = Color.argb(255, 236, 236, 236);
            trackColor = Color.argb(255, 0, 0, 0);
        }

        try {
            getThumbDrawable().setColorFilter(thumbColor, PorterDuff.Mode.MULTIPLY);
            getTrackDrawable().setColorFilter(trackColor, PorterDuff.Mode.MULTIPLY);
        }
        catch (NullPointerException ignored) {
        }
    }
}