package com.example.drawingfun;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by hugob on 23/11/2017.
 */

public class EditTextBackEvent extends TextView
{
    public boolean isDragable = false;
    public EditTextBackEvent(Context context) {
        super(context, null, android.R.attr.editTextStyle);
    }

    public EditTextBackEvent(Context context, AttributeSet attrs) {
        super(context, attrs, android.R.attr.editTextStyle);
    }

    public EditTextBackEvent(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK &&
                event.getAction() == KeyEvent.ACTION_UP) {
            if (getText().toString().matches(""))
            {
                setVisibility(GONE);
            }
            setFocusable(false);
            setTextIsSelectable(false);
        }
        return super.dispatchKeyEvent(event);
    }



    @Override
    public boolean performLongClick()
    {
        Log.d("blabla", "onLongClick");
        setBackgroundColor(0x66ffffff);

        Activity a = (Activity) getContext();
        DrawingView d = a.findViewById(R.id.drawing);
        d.setTextPlacement(true);
        return true;
    }
}


