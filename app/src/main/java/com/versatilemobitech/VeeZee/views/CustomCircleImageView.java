package com.versatilemobitech.VeeZee.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.versatilemobitech.VeeZee.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomCircleImageView extends CircleImageView {

    Context context;

    public CustomCircleImageView(Context context) {
        super(context);
        this.context = context;
    }

    public CustomCircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public CustomCircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }


    @Override
    public void onDrawForeground(Canvas canvas) {
        super.onDrawForeground(canvas);

        Paint paint = new Paint();
        paint.setColor(context.getResources().getColor(R.color.colorTransparent));
        paint.setStyle(Paint.Style.FILL);

        float radius = getWidth() / 2f;

        RectF rectF = new RectF();
        rectF.set(getWidth() / 2 - radius,
                getHeight() / 2 - radius,
                getWidth() / 2 + radius,
                getHeight() / 2 + radius);

        canvas.drawArc(rectF, 30f, 120f, false, paint);


    }
}
