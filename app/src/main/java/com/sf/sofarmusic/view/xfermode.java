package com.sf.sofarmusic.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by sufan on 17/9/15.
 */

public class xfermode extends View {

    private Paint mDstPaint,mSrcPaint;

    private Bitmap mDstBitmap,mSrcBitmap;
    private Canvas mDstCanvas,mSrcCanvas;

    public xfermode(Context context) {
        this(context,null);
    }

    public xfermode(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public xfermode(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        mDstPaint = new Paint();
        mDstPaint.setAntiAlias(true);    //抗锯齿
        mDstPaint.setDither(true);       //抗抖动
        mDstPaint.setStyle(Paint.Style.FILL);
        mDstPaint.setColor(Color.RED);

        mSrcPaint = new Paint();
        mSrcPaint.setAntiAlias(true);    //抗锯齿
        mSrcPaint.setDither(true);       //抗抖动
        mSrcPaint.setStyle(Paint.Style.FILL);
        mSrcPaint.setColor(Color.BLUE);

        mDstBitmap =  Bitmap.createBitmap(200,200, Bitmap.Config.ARGB_8888);
        mDstCanvas = new Canvas(mDstBitmap);

        mSrcBitmap =  Bitmap.createBitmap(200,200, Bitmap.Config.ARGB_8888);
        mSrcCanvas = new Canvas(mSrcBitmap);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //dst
        mDstCanvas.drawRect(100,100,300,300,mDstPaint);

        canvas.drawBitmap(mDstBitmap,0,0,mDstPaint);

        //src


        mSrcCanvas.drawCircle(300,300,100,mSrcPaint);

        mSrcPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

        canvas.drawBitmap(mSrcBitmap,0,0,mSrcPaint);
    }
}
