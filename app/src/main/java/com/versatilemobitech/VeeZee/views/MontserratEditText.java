package com.versatilemobitech.VeeZee.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;

import com.versatilemobitech.VeeZee.R;


public class MontserratEditText extends AppCompatEditText {
    float mOriginalLeftPadding = -1;
    private KeyImeChange keyImeChangeListener;

    public MontserratEditText(Context context) {
        super(context);
    }

    public MontserratEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs, R.attr.font_type);
    }

    public MontserratEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context, attrs, defStyle);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        calculatePrefix();
    }

    private void initView(Context context, AttributeSet attrs, int defStyle) {
        if (isInEditMode())
            return;
        try {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MontserratTextView);

            String str = a.getString(R.styleable.MontserratTextView_font_type);
            if (str == null)
                str = "1";
            a.recycle();
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void calculatePrefix() {
        if (mOriginalLeftPadding == -1) {
            String prefix = (String) getTag();
            if (prefix != null) {
                float[] widths = new float[prefix.length()];
                getPaint().getTextWidths(prefix, widths);
                float textWidth = 0;
                for (float w : widths) {
                    textWidth += w;
                }
                mOriginalLeftPadding = getCompoundPaddingLeft();
                setPadding((int) (textWidth + mOriginalLeftPadding),
                        getPaddingRight(), getPaddingTop(),
                        getPaddingBottom());
            }

        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        String prefix = (String) getTag();
        if (prefix != null) {
            canvas.drawText(prefix, mOriginalLeftPadding,
                    getLineBounds(0, null), getPaint());
        }

    }

    @Override
    public boolean onKeyPreIme( int keyCode, KeyEvent event) {
    if(keyImeChangeListener!=null){
            keyImeChangeListener.onKeyIme(keyCode,event);
        }
        return false;
    }


    public void setKeyImeChangeListener(KeyImeChange listener){
        keyImeChangeListener = listener;
    }

    public interface KeyImeChange {
        public void onKeyIme( int keyCode, KeyEvent event);
    }
}

