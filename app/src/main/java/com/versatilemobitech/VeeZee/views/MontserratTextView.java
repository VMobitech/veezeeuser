package com.versatilemobitech.VeeZee.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.versatilemobitech.VeeZee.R;


public class MontserratTextView extends AppCompatTextView {
    private int font;

    public MontserratTextView(Context context) {
        super(context);
    }

    public MontserratTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
//         initFont(context, attrs);
        initView(context, attrs);
    }

    public MontserratTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // initFont(context, attrs);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        try {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MontserratTextView);
            String str = a.getString(R.styleable.MontserratTextView_font_type);
            if (str == null)
                str = "1";
            switch (Integer.parseInt(str)) {
                case 1:
                    str = "Montserrat-Regular.ttf";
                    break;
                case 2:
                    str = "Montserrat-Bold.ttf";
                    break;
                case 5:
                   // str = "Montserrat-Medium.ttf";
                    str = "Montserrat-Regular.ttf";
                    break;
                case 6:
                    str = "Montserrat-ExtraBold.ttf";
                    break;
                case 7:
                    str = "montserrat_semibold.ttf";
                    break;

                default:
                    str = "Montserrat-Regular.ttf";

                    break;
            }
            setTypeface(FontManager.getInstance(getContext()).loadFont(str));
            a.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}