package com.sf.sofarmusic.demo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v7.graphics.Palette;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.sf.sofarmusic.data.LocalData;
import com.sf.utility.DensityUtil;
import com.sf.utility.ResUtil;

import java.util.List;

/**
 * Created by sufan on 17/3/28.
 */

public class ChartView extends View {
    private Context mContext;
    private Paint mPaint;
    private Rect mTextRect;
    private Path mPath;

    private Bitmap mBitmap;
    private Canvas mCanvas;
    private RectF mRectF;
    private boolean isFirst = true;

    private int mWidth, mHeight;
    private int mDx, mDy;   //x,y坐标间隔
    private int mYcount = 5;   //y轴的笑脸个数
    private int mXcount = 10;    //x轴间隔个数

    //笑脸图片和笑脸的颜色
    private Bitmap[] bitmaps = new Bitmap[mYcount];
    private int[] colors = new int[mYcount];
    private int[] ids = new int[mYcount];

    private int mDefaultColor = 0xFF999999;
    private int mLineWidth = 1;   //dp
    private int mTextSize = 16;   //sp

    //x,y坐标轴所代表的数字
    private int mStartX = 1;
    private int mStartY = 0;
    private int mXNum = 3;
    private int mYNum = 50;

    private float mXrate;   //x轴坐标和在图中位置的比例
    private float mYrate;

    private List<Point> mPointList;

    private Bitmap[] mBitmaps = new Bitmap[mYcount];


    public ChartView(Context context) {
        this(context, null);
    }

    public ChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();

        mLineWidth = DensityUtil.dp2px(mContext, mLineWidth);
        mTextSize = DensityUtil.sp2px(mContext, mTextSize);

        mTextRect = new Rect();
        mPath = new Path();
        mRectF = new RectF();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);    //抗锯齿
        mPaint.setDither(true);       //抗抖动
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mDefaultColor);
        mPaint.setStrokeWidth(mLineWidth);
        mPaint.setTextSize(mTextSize);
    }

    private void init() {
        for (int i = 0; i < mYcount; i++) {
            ids[i]= ResUtil.getDrawableId(mContext,"face" + i);
            bitmaps[i] = BitmapFactory.decodeResource(getResources(), ids[i]);
            colors[i] = Palette.from(bitmaps[i]).generate().getDominantColor(mDefaultColor);
        }

        mPointList = LocalData.getChartPoint();


    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //画一个自己的画布，以后画的东西都在这上面画
        canvas.drawColor(Color.RED);
        //不同区域画不同的颜色
        for (int i = 0; i < mYcount; i++) {
            mRectF.left = 0;
            mRectF.top = mDy * i;
            mRectF.right = getWidth();
            mRectF.bottom = mDy*(i+1);
            mBitmaps[i] = Bitmap.createBitmap(mWidth, mDy, Bitmap.Config.ARGB_8888);
            Canvas myCanvas = new Canvas(mBitmaps[i]);
            myCanvas.drawColor(colors[i]);
            canvas.drawBitmap(mBitmaps[i], null, mRectF, null);

        }


        mRectF.left = 0;
        mRectF.top = 0;
        mRectF.right = getWidth();
        mRectF.bottom = getHeight();
        canvas.drawBitmap(mBitmap, null, mRectF, null);


        drawGridLine();
        drawFace();
        drawX();
        drawPath();


    }

    //画网格线
    private void drawGridLine() {
        //初始化画笔
        mPaint.setXfermode(null);
        mLineWidth = DensityUtil.dp2px(mContext, 1);
        mPaint.setStrokeWidth(mLineWidth);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mDefaultColor);

        for (int i = 0; i <= mYcount; i++) {
            mCanvas.drawLine(0, i * mDy + mLineWidth / 2, mWidth, i * mDy + mLineWidth / 2, mPaint);
        }
    }

    //画笑脸
    private void drawFace() {
        //画笑脸
        for (int i = 0; i < mYcount; i++) {
            int bitmapH = bitmaps[i].getWidth();
            int y = (mDy - bitmapH) / 2 + i * mDy;
            mCanvas.drawBitmap(bitmaps[i], mLineWidth / 2, y, mPaint);
        }
    }

    private void drawX() {
        //画x坐标
        mPaint.setTextAlign(Paint.Align.RIGHT);  //对x水平方向起作用
        for (int i = 0; i <= mXcount; i++) {
            String text = (mStartX + i * mXNum) + "";
            mPaint.getTextBounds(text, 0, text.length(), mTextRect);
            int textX = mDy + i * mDx;
            int textY = mDy * mYcount + mTextRect.height() + DensityUtil.dp2px(mContext, 5);
            mCanvas.drawText(text, textX, textY, mPaint);
        }
    }

    private void drawPath() {
        mLineWidth = DensityUtil.dp2px(mContext, 3);
        mPaint.setStrokeWidth(mLineWidth);
        mCanvas.save();
        mCanvas.translate(mDy, mDy * mYcount);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        for (int i = 0; i < mPointList.size(); i++) {
            Point point = mPointList.get(i);
            float x = point.getX() * mXrate;
            float y = point.getY() * mYrate;
            if (i == 0) {
                mPath.moveTo(x, y);
            } else {
                mPath.lineTo(x, y);
            }
        }
        mCanvas.drawPath(mPath, mPaint);
        mCanvas.restore();


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) { // 对应match_parent和确定值
            mWidth = widthSize;
        } else {
            mWidth = 400; // MeasureSpec.AT_MOST对应wrap_content
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
        } else {
            mHeight = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 300, getResources()
                            .getDisplayMetrics());
        }
        setMeasuredDimension(mWidth, mHeight);

        mDy = mHeight / (mYcount + 1);
        mDx = (mWidth - mDy) / mXcount;

        mXrate = mDx * 1.0f / mXNum;
        mYrate = -mDy * 1.0f / mYNum;


        //先自己搞一个画布，然后在这个画布上画，否则在canvas上画，背景始终是黑色的
        //onMeasure会掉用多次，可以设置个标志位，使得mBitmap，mCanvas只创建一次
        if (isFirst) {
            mBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
            mCanvas.drawColor(Color.WHITE);
            isFirst = false;
        }


    }

    public static class Point {
        private float x;
        private float y;

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }
    }

}
