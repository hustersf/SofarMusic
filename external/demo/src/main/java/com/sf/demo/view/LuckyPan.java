package com.sf.demo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.sf.demo.R;

/**
 * Created by sufan on 16/8/8.
 */
public class LuckyPan extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private SurfaceHolder mHolder;
    private Canvas mCanvas;
    private Thread mThread;
    private boolean isRunning;

    private int mItemCount = 6;      //奖项的个数
    private RectF mRange;            //转盘绘制范围
    private int mDiameter;           //转盘直径
    private int mCenter;

    //转盘总体属性设置
    private Bitmap mBitmapBg;        //转盘背景图片
    private Paint mArcPaint;
    private int mPadding;
    private float mSpeed = 0;              //转盘旋转速度
    private float mStartAngle = 0;        //决定了转盘最终停留在什么位置

    //每个盘块的颜色
    private int[] mColors = new int[]{0xFFFFC300, 0xFFF17E01, 0xFFFFC300,
            0xFFF17E01, 0xFFFFC300, 0xFFF17E01};

    //奖项的文字以及和文字有关的属性设置
    private String[] mTexts = new String[]{"单反相机", "IPAD", "恭喜发财", "IPHONE", "妹子一只", "恭喜发财"};
    private int mTextColor = 0xFFFFFFFF;   //字体颜色
    private int mTextSize = 16;           //字体大小，SP
    private Paint mTextPaint;

    //奖项的图片设置
    private int[] mImgs = new int[]{R.drawable.demo_lucky_pan_danfan, R.drawable.demo_lucky_pan_ipad,
            R.drawable.demo_lucky_pan_f040, R.drawable.demo_lucky_pan_iphone,
            R.drawable.demo_lucky_pan_meizi, R.drawable.demo_lucky_pan_f040};
    private Bitmap[] mBitmaps;


    //控制转盘的转动
    private boolean isEnd = false;   //是否点击了停止按钮
    private boolean isCnnEnd = true;    //是否能够再次点击停止按钮


    public LuckyPan(Context context) {
        this(context, null);
    }

    public LuckyPan(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LuckyPan(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mHolder = getHolder();       //拿到surfaceview的持有者
        mHolder.addCallback(this);   //添加回调

//         setZOrderOnTop(true);// 设置画布 背景透明
//         mHolder.setFormat(PixelFormat.TRANSLUCENT);

        //设置可获得焦点
        setFocusable(true);
        setFocusableInTouchMode(true);

        //设置屏幕常亮
        setKeepScreenOn(true);

        mTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mTextSize, getResources().getDisplayMetrics());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = Math.min(getMeasuredWidth(), getMeasuredHeight());
        setMeasuredDimension(width, width);

        mDiameter = width - getPaddingLeft() - getPaddingRight();
        mPadding = getPaddingLeft();
        mRange = new RectF(mPadding, mPadding, mPadding + mDiameter, mPadding + mDiameter);
        mCenter = width / 2;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        //扇形的画笔设置
        mArcPaint = new Paint();
        mArcPaint.setDither(true);
        mArcPaint.setAntiAlias(true);

        //文字画笔的设置
        mTextPaint = new Paint();
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);

        mBitmapBg = BitmapFactory.decodeResource(getResources(), R.drawable.demo_lucky_pan_bg);
        mBitmaps = new Bitmap[mItemCount];
        for (int i = 0; i < mItemCount; i++) {
            mBitmaps[i] = BitmapFactory.decodeResource(getResources(), mImgs[i]);
        }

        //开启一个线程
        isRunning = true;
        mThread = new Thread(this);
        mThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //关闭线程
        isRunning = false;
    }

    @Override
    public void run() {
        while (isRunning) {

            //每50ms秒绘制一次
            long start = System.currentTimeMillis();
            draw();
            long end = System.currentTimeMillis();
            try {
                if (end - start < 50) {
                    Thread.sleep(50 - (end - start));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void draw() {
        try {
            //获得画布
            mCanvas = mHolder.lockCanvas();
            if (mCanvas != null) {
                //draw背景
                drawBg();

                //draw扇形区域
                float tmpAngle = mStartAngle;
                float sweepAngle = 360 / mItemCount;

                for (int i = 0; i < mItemCount; i++) {
                    //draw块块
                    mArcPaint.setColor(mColors[i]);
                    mCanvas.drawArc(mRange, tmpAngle, sweepAngle, true, mArcPaint);

                    //draw文字
                    drawText(tmpAngle, sweepAngle, mTexts[i]);

                    //draw Icon
                    drawIcon(i, tmpAngle);

                    tmpAngle += sweepAngle;
                }

                mStartAngle += mSpeed;
                if (isEnd) {
                    mSpeed--;
                    if (mSpeed < 0) {
                        mSpeed = 0;
                    }
                    if (mSpeed == 0) {
                        isEnd = false;
                        Log.i("TAG", ":" + mStartAngle);
                    }
                }
            }
        } catch (Exception e) {

        } finally {
            if (mCanvas != null) {
                mHolder.unlockCanvasAndPost(mCanvas);
            }
        }

    }

    private void drawIcon(int i, float startAngle) {
        int imgWidth = mDiameter / 8;
        int r = mDiameter / 4;
        float angle = (float) ((startAngle + 360 / mItemCount / 2) * Math.PI / 180);  //弧度
        int x = (int) (mCenter + r * Math.cos(angle));
        int y = (int) (mCenter + r * Math.sin(angle));
        Rect dst = new Rect(x - imgWidth / 2, y - imgWidth / 2, x + imgWidth / 2, y + imgWidth / 2);
        mCanvas.drawBitmap(mBitmaps[i], null, dst, null);
    }


    private void drawText(float startAngle, float sweepAngle, String str) {
        Path textPath = new Path();
        textPath.addArc(mRange, startAngle, sweepAngle);
        int vOffset = mDiameter / 10;
        float arcL = (float) (Math.PI * mDiameter / mItemCount);
        int textWidth = (int) mTextPaint.measureText(str);
        int hOffset = (int) ((arcL - textWidth) / 2);
        mCanvas.drawTextOnPath(str, textPath, hOffset, vOffset, mTextPaint);
    }


    private void drawBg() {
        mCanvas.drawColor(0xFFFFFFFF);
        Rect dstR = new Rect(mPadding / 2, mPadding / 2, getWidth() - mPadding / 2, getHeight() - mPadding / 2);
        mCanvas.drawBitmap(mBitmapBg, null, dstR, null);
    }


    //让转盘开始转动起来,index 代表奖项的下标
    public void start(int index) {
        float angle = 360 / mItemCount;   //每一项的角度
        float moreAngle = 4 * 360;

        float fromAngle = 270 - (index + 1) * angle + moreAngle;
        float toAngle = fromAngle + angle;

        fromAngle = fromAngle + 3;  //防止出现在中间位置

        float v1 = 0;
        //  v1+v1*(v1-1)/2=fromAngle;
        v1 = (float) (Math.sqrt(1 + 8 * fromAngle) - 1) / 2;
        float v2 = 0;
        //  v2+v2*(v2-1)/2=toAngle;
        v2 = (float) (Math.sqrt(1 + 8 * toAngle) - 1) / 2;

        //讲mSpeed从自身到0累加起来算出来的值刚好等于转盘转动的角度  v1+v1*(v1-1)/2=fromAngle//等差数列
        mSpeed = (float) (Math.random() * (v2 - v1) + v1);
    }

    //让转盘停止转动
    public void stop() {
        if (isCnnEnd) {
            mStartAngle = 0;
            isEnd = true;
            isCnnEnd = false;    //停止按钮只能点一次
        }
    }

    //转盘是否停止
    public boolean isStop() {
        if (mSpeed == 0) {
            isCnnEnd = true;
            return true;
        }
        return false;
    }
}
