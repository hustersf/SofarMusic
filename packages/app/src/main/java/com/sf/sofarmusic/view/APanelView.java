package com.sf.sofarmusic.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.sf.sofarmusic.R;
import com.sf.utility.DensityUtil;

public class APanelView extends View {
    private Context mContext;

    private int mCircleWidth = 40;    //刻度盘的宽度 dp
    private int mLineColor = 0xFFFFFFFF;         //线条的颜色
    private int mLineWidth = 3;           //线条宽度dp
    private int mWidth, mHeight;   //宽高

    private Paint mPaint;
    private RectF mOval;
    private Shader mShader;
    private Rect mTextRect;
    private Path mPath;

    //刻度相关属性
    private int mStart = 50;
    private int mEnd = 210;
    private int mBigMaskCount = 8;   //大刻度的个数(间隔为8)
    private int mSpace = 20;   //(50,70...)
    private int mSmallMaskCount = (mBigMaskCount) * 10;
    private int mBigMaskWidth = 2;     //dp    大刻度宽度
    private int mSmallMaskWidth = 1;     //dp   小刻度宽度
    private int mBigMaskLength = 10;     //dp    大刻度长度
    private int mSmallMaskLength = 5;     //dp   小刻度长度
    private int mTextSize = 12;      //字体大小  sp
    private float mTotalDegree = 180;      //刻度盘总度数

    //范围分割线
    private int mLeft = 95;
    private int mRight = 168;

    private float mProgress = (float) (55 * 1.0f * 1.8);
    private int mCenter;         //中心点
    private Canvas mCanvas;

    private static final int[] COLORS = new int[]{0xFFA9A9A9, 0xFF7FFF00, 0xFFFF7F50, 0xFFFF1493};
    // private static final int[] COLORS = new int[]{Color.GRAY,Color.GRAY, Color.GREEN, Color.RED, Color.RED};

    private static final float[] POSITIONS = new float[]{0.0f, 0.3f, 0.6f, 1.0f};


    public APanelView(Context context) {
        super(context, null);
    }

    public APanelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public APanelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.APanelView, defStyleAttr, 0);

        mCircleWidth = a.getDimensionPixelSize(R.styleable.APanelView_APcircleWidth,
                DensityUtil.dp2px(mContext, mCircleWidth));
        mLineColor = a.getColor(R.styleable.APanelView_APlineColor,
                mLineColor);
        mLineWidth = a.getDimensionPixelSize(R.styleable.APanelView_APlineWidth,
                DensityUtil.dp2px(mContext, mLineWidth));

        a.recycle();
        init();
    }

    private void init() {
        mPaint = new Paint();
        mOval = new RectF();
        mTextRect = new Rect();
        mPath = new Path();

        mPaint.setAntiAlias(true);    //抗锯齿
        mPaint.setDither(true);       //抗抖动
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mLineColor);
        mPaint.setStrokeWidth(mLineWidth);

        mBigMaskWidth = DensityUtil.dp2px(mContext, mBigMaskWidth);
        mSmallMaskWidth = DensityUtil.dp2px(mContext, mSmallMaskWidth);
        mBigMaskLength = DensityUtil.dp2px(mContext, mBigMaskLength);
        mSmallMaskLength = DensityUtil.dp2px(mContext, mSmallMaskLength);
        mTextSize = DensityUtil.sp2px(mContext, mTextSize);
        mPaint.setTextSize(mTextSize);

        //  mShader = new SweepGradient(mWidth / 2, mWidth / 2, COLORS, null);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // int center = getWidth() / 2;
        mCenter = mWidth / 2; // 两者都可以
        mCanvas = canvas;

        //初始化一下,因为在drawNeedle中改变了这两个值
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mLineColor);
        drawArc();
        drawOutArc();
        drawInArc();
        drawBigMask();
        drawSmallMask();
        drawValue();
        drawSplitLine();
        drawNeedle();
    }

    //仪表盘的圆弧
    private void drawArc() {
        mPaint.setStrokeWidth(mCircleWidth);
        int radius = mCenter - mCircleWidth / 2;
        mOval.left = mCenter - radius;
        mOval.top = mCenter - radius;
        mOval.right = mCenter + radius;
        mOval.bottom = mCenter + radius;
        mShader = new LinearGradient(mOval.left, mOval.top, mOval.right, mOval.bottom, COLORS, null, Shader.TileMode.MIRROR);
        mPaint.setShader(mShader);
        mCanvas.drawArc(mOval, 180, 180, false, mPaint); //顺时针

    }

    //画外层圆弧
    private void drawOutArc() {
        mPaint.setStrokeWidth(mLineWidth);
        mPaint.setShader(null);
        mOval.left = mLineWidth / 2;
        mOval.top = mLineWidth / 2;
        mOval.right = 2 * mCenter - mLineWidth / 2;
        mOval.bottom = 2 * mCenter - mLineWidth / 2;
        mCanvas.drawArc(mOval, 180, 180, false, mPaint); //顺时针
    }

    //画内层圆弧
    private void drawInArc() {
        mPaint.setStrokeWidth(mLineWidth);
        mPaint.setShader(null);
        mOval.left = mLineWidth / 2 + mCircleWidth;
        mOval.top = mLineWidth / 2 + mCircleWidth;
        mOval.right = 2 * mCenter - mLineWidth / 2 - mCircleWidth;
        mOval.bottom = 2 * mCenter - mLineWidth / 2 - mCircleWidth;
        mCanvas.drawArc(mOval, 180, 180, false, mPaint); //顺时针

    }

    //画大刻度
    private void drawBigMask() {
        float bigDegree = (mTotalDegree) / mBigMaskCount;
        mPaint.setStrokeWidth(mBigMaskWidth);
        //第一个不用旋转
        mCanvas.drawLine(mCenter, 0, mCenter, mBigMaskLength, mPaint);
        //右侧
        mCanvas.save();
        for (int i = 0; i < mBigMaskCount / 2; i++) {
            mCanvas.rotate(bigDegree, mCenter, mCenter);
            mCanvas.drawLine(mCenter, 0, mCenter, mBigMaskLength, mPaint);
        }
        mCanvas.restore();
        //左侧
        mCanvas.save();
        for (int i = 0; i < mBigMaskCount / 2; i++) {
            mCanvas.rotate(-bigDegree, mCenter, mCenter);
            mCanvas.drawLine(mCenter, 0, mCenter, mBigMaskLength, mPaint);
        }
        mCanvas.restore();
    }

    //画小刻度
    private void drawSmallMask() {
        //画小刻度
        mCanvas.save();
        mPaint.setStrokeWidth(mSmallMaskWidth);
        //第一个不用旋转
        mCanvas.drawLine(mLineWidth / 2, mCenter, mLineWidth / 2 + mSmallMaskLength, mCenter, mPaint);
        float smallDegree = mTotalDegree / mSmallMaskCount;
        for (int i = 0; i < mSmallMaskCount; i++) {
            mCanvas.rotate(smallDegree, mCenter, mCenter);
            mCanvas.drawLine(mLineWidth / 2, mCenter, mLineWidth / 2 + mSmallMaskLength, mCenter, mPaint);
        }
        mCanvas.restore();
    }

    //画指针
    private void drawNeedle() {
        //画指针
        mPaint.setStyle(Paint.Style.FILL);
        int radius = DensityUtil.dp2px(mContext, 4);
        mCanvas.save();
        mCanvas.rotate(180 + mProgress, mCenter, mCenter);
        //  mCanvas.drawLine(mCenter, mCenter, 2 * mCenter - mBigMaskLength * 2, mCenter, mPaint);
        mPath.moveTo(2 * mCenter - mBigMaskLength * 2, mCenter);
        mPath.lineTo(mCenter, mCenter - radius);
        mPath.lineTo(mCenter, mCenter + radius);
        mPath.close();
        mCanvas.drawPath(mPath, mPaint);
        mCanvas.restore();

        mPaint.setColor(Color.RED);
        mCanvas.drawCircle(mCenter, mCenter, DensityUtil.dp2px(mContext, 10), mPaint);

        mPaint.setColor(mLineColor);
        mCanvas.drawCircle(mCenter, mCenter, DensityUtil.dp2px(mContext, 4), mPaint);
    }


    //画分割线
    private void drawSplitLine() {
        //左分割线
        float dd = mTotalDegree / (mEnd - mStart);
        float left = (mLeft - mStart) * dd;
        mCanvas.save();
        mCanvas.rotate(left, mCenter, mCenter);
        mCanvas.drawLine(mLineWidth / 2, mCenter, mLineWidth / 2 + mCircleWidth, mCenter, mPaint);
        mCanvas.restore();
        //右分割线
        mCanvas.save();
        float rigth = (mRight - mStart) * dd;
        mCanvas.rotate(rigth, mCenter, mCenter);
        mCanvas.drawLine(mLineWidth / 2, mCenter, mLineWidth / 2 + mCircleWidth, mCenter, mPaint);
        mCanvas.restore();

    }


    public void setProgress(float progress) {
        mProgress = progress * 1.8f;
        // postInvalidate();
        invalidate(); // 两者均可以
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        // 该重写没有用到
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) { // 对应match_parent和确定值
            mWidth = widthSize;
        } else {
            mWidth = DensityUtil.dp2px(mContext, 250);// MeasureSpec.AT_MOST对应wrap_content
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
        } else {
            mHeight = DensityUtil.dp2px(mContext, 250);
        }
        setMeasuredDimension(mWidth, mHeight);
    }

    //画刻度值
    private void drawValue() {

        float bigDegree = (mTotalDegree) / mBigMaskCount;
        //画刻度值1      由中间向两侧旋转
//        mCanvas.save();
//        String text = String.valueOf(mStart);
//        mPaint.getTextBounds(text, 0, text.length(), mTextRect);   //计算text高度用
//        mCanvas.drawText(text, mBigMaskLength + mLineWidth / 2, mCenter, mPaint);
//        int textX = mBigMaskLength + mLineWidth / 2;
//        int textY = mCenter + mTextRect.height() / 2;
//        for (int i = 0; i < mBigMaskCount; i++) {
//            int dd = mStart + (i + 1) * mSpace;
//            if (dd >= mEnd) {
//                dd = mEnd;
//                textY = mCenter + mTextRect.height();
//            }
//            mCanvas.rotate(bigDegree, mCenter, mCenter);
//            mCanvas.drawText(String.valueOf(dd), textX, textY, mPaint);
//        }
//        mCanvas.restore();

        //画刻度值2    //由最左端开始旋转
//        String text = String.valueOf((mStart+mEnd)/2);
//        mPaint.getTextBounds(text, 0, text.length(), mTextRect);   //计算text高度用
//        int textX = (int) (mCenter - mPaint.measureText(text) / 2);
//        int textY =  mBigMaskLength + mLineWidth / 2 +mTextRect.height();
//        mCanvas.drawText(text, textX, textY, mPaint);
//        //右侧
//        mCanvas.save();
//        for (int i = 0; i < mBigMaskCount / 2; i++) {
//            String value = String.valueOf(130 + (i + 1) * mSpace);
//            textX = (int) (mCenter - mPaint.measureText(value) / 2);
//            if (i == mBigMaskCount / 2 - 1) {
//                textX = (int) (mCenter - mPaint.measureText(value));
//            }
//            mCanvas.rotate(bigDegree, mCenter, mCenter);
//            mCanvas.drawText(String.valueOf(value), textX, textY, mPaint);
//        }
//        mCanvas.restore();
//        //左侧
//        mCanvas.save();
//        for (int i = 0; i < mBigMaskCount / 2; i++) {
//            String value = String.valueOf(130 - (i + 1) * mSpace);
//            textX = (int) (mCenter - mPaint.measureText(value) / 2);
//            if (i == mBigMaskCount / 2 - 1) {
//                textX = mCenter;
//            }
//            mCanvas.rotate(-bigDegree, mCenter, mCenter);
//            mCanvas.drawText(String.valueOf(value), textX, textY, mPaint);
//        }
//        mCanvas.restore();

        //画刻度值3     text始终水平
        mCanvas.save();
        mCanvas.translate(mCenter, mCenter);    //将中心作为坐标原点进行计算
        mPaint.setTextAlign(Paint.Align.CENTER);  //很有用
        float perAngle = mTotalDegree / (mEnd - mStart);    //每刻度所代表的角度
        float perAngleRadian = (float) (Math.PI / 180 * perAngle);  //角度所对应的弧度
        int radius = mCenter - mBigMaskLength - mLineWidth / 2;
        String text = String.valueOf(mStart);
        mPaint.getTextBounds(text, 0, text.length(), mTextRect);   //计算text高度用
        for (int i = 0; i <= mBigMaskCount; i++) {
            text = String.valueOf(mEnd - i * mSpace);
            int trueRadius = (int) (radius - mPaint.measureText(text)/2);
            int textX = (int) (trueRadius * Math.cos(mSpace * i * perAngleRadian));
            int textY = (int) (trueRadius * Math.sin(mSpace * i * perAngleRadian));
            mCanvas.drawText(text, textX, -textY, mPaint);
        }
        mCanvas.restore();

    }
}
