package com.sf.demo.view.linechart;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.sf.utility.DensityUtil;

/**
 * Created by sufan on 17/7/7.
 */

public class MothBillView extends View {

    //折线相关
    private Paint mLinePaint;
    private String mLineColor = "#E56151";
    private Path mPath;
    private int mLineWidth = 2;   //线条粗细 dp

    //空心圆
    private Paint mEmptyCirclePaint;
    private String mCircleColor = "#E56151";
    private int mRadius = 10;   //圆的半径 dp
    private int mCircleWidth = 1;   //圆圈线条的粗细 dp

    private Paint mWhiteCirclePaint;   //是为了把线盖住
    private Paint mAlphaCirclePaint;   //选中的点圆

    //文字
    private Paint mTextPaint;
    private String mTextColor = "#666666";
    private int mTextSize = 12;   //文字大小，sp
    private int mLeft=5;     //第一个数据或者最后一个数据，距离两边的间隔,dp

    //数据
    private List<IncomeExpendInfo> billList;
    public static final int TYPE_INCOME = 0;
    public static final int TYPE_EXPEND = 1;
    private int type = TYPE_INCOME;
    private String[] month;
    private float[] data;     //金额


    //view相关
    private int mWidth;
    private int mHeight;

    //x坐标
    private float mBaseWidth; //基准宽度，即第一个数据点和最后一个数据点，距离左右的距离
    private float mItemWidth;  //相邻两个点的距离

    //y坐标
    private float mMinLineHeight;   //数据点最低y轴坐标
    private float mMaxLineHeight;   //数据点最高y轴坐标
    private float mMaxLineSpace;    //折线最大可占用空间的高度,即折线波峰和波谷的差值,用来控制折线的陡峭程度


    //金额
    private double mMinBillValue;     //已出账单月份中最低消费金额
    private double mMaxBillValue;    //已出账单月份中最高消费金额
    private double mBillRange;   //消费金额的极差


    //点击事件相关
    private int mSelectedIndex = 2;  //选中的点
    private List<Point> mPointList;
    private int mTouchR;    //触摸范围


    public MothBillView(Context context) {
        this(context, null);
    }

    public MothBillView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MothBillView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);   //抗锯齿
        mLinePaint.setColor(Color.parseColor(mLineColor));
        mLinePaint.setStrokeWidth(DensityUtil.dp2px(context, mLineWidth));
        mLinePaint.setStyle(Paint.Style.STROKE);
        //   mLinePaint.setStyle(Paint.Style.FILL);   //会将点下面的区域填充

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.parseColor(mTextColor));
        mTextPaint.setTextSize(DensityUtil.sp2px(context, mTextSize));
        mTextPaint.setTextAlign(Paint.Align.CENTER);   //水平居中

        mEmptyCirclePaint = new Paint();
        mEmptyCirclePaint.setAntiAlias(true);
        mEmptyCirclePaint.setColor(Color.parseColor(mCircleColor));
        mEmptyCirclePaint.setStyle(Paint.Style.FILL);
        mEmptyCirclePaint.setStrokeWidth(DensityUtil.dp2px(context, mCircleWidth));

        mWhiteCirclePaint = new Paint();
        mWhiteCirclePaint.setAntiAlias(true);
        mWhiteCirclePaint.setColor(Color.parseColor("#FFFFFF"));
        mEmptyCirclePaint.setStyle(Paint.Style.FILL);
        mEmptyCirclePaint.setStrokeWidth(1);

        mAlphaCirclePaint = new Paint();
        mAlphaCirclePaint.setAntiAlias(true);
        mAlphaCirclePaint.setColor(Color.parseColor(mCircleColor));
        mAlphaCirclePaint.setAlpha(125);  //必须放在setColor后面
        mAlphaCirclePaint.setStyle(Paint.Style.FILL);
        mAlphaCirclePaint.setStrokeWidth(1);

        mPath = new Path();
        mPointList = new ArrayList<>();
        mTouchR = DensityUtil.dp2px(context, 8);
        mLeft=DensityUtil.dp2px(context,mLeft);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();
        if (billList != null && billList.size() != 0) {
            drawMonthText(canvas);    //绘制月份
            drawLine(canvas);         //花折线
            drawEmptyCircle(canvas);    //画空心圆
            drawBill(canvas);           //绘制消费金额
        }

    }


    private void drawMonthText(Canvas canvas) {
        mTextPaint.setAlpha(255);
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float fontHeight = fontMetrics.descent - fontMetrics.ascent;
        float dx;
        float dy = mMaxLineHeight + fontHeight * 3 / 2;

        for (int i = 0; i < month.length; i++) {
            dx = mBaseWidth + mItemWidth * i;
            canvas.drawText(month[i], dx, dy, mTextPaint);
        }

    }

    private void drawLine(Canvas canvas) {
        float startY = (float) (mMaxLineHeight - (data[0] - mMinBillValue) / mBillRange * mMaxLineSpace);
        mPath.moveTo(mBaseWidth, startY);

        for (int i = 1; i < billList.size(); i++) {
            float dx = mBaseWidth + mItemWidth * i;
            float dy = (float) (mMaxLineHeight - (data[i] - mMinBillValue) / mBillRange * mMaxLineSpace);
            mPath.lineTo(dx, dy);
        }
        canvas.drawPath(mPath, mLinePaint);
    }

    private void drawEmptyCircle(Canvas canvas) {
        mPointList.clear();
        for (int i = 0; i < billList.size(); i++) {
            float dx = mBaseWidth + mItemWidth * i;
            float dy = (float) (mMaxLineHeight - (data[i] - mMinBillValue) / mBillRange * mMaxLineSpace);

            if (i == mSelectedIndex) {
                canvas.drawCircle(dx, dy, 2 * mRadius, mAlphaCirclePaint);
                canvas.drawCircle(dx, dy, mRadius + 2 * mCircleWidth, mWhiteCirclePaint);
                canvas.drawCircle(dx, dy, mRadius - 2 * mCircleWidth, mEmptyCirclePaint);
            } else {
                canvas.drawCircle(dx, dy, mRadius, mEmptyCirclePaint);
                canvas.drawCircle(dx, dy, mRadius - 2 * mCircleWidth, mWhiteCirclePaint);
            }

            //将点击的点记录下来
            Point point = new Point();
            point.x = dx;
            point.y = dy;
            mPointList.add(point);
        }
    }

    private void drawBill(Canvas canvas) {
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float fontHeight = fontMetrics.descent - fontMetrics.ascent;
        for (int i = 0; i < billList.size(); i++) {
            float dx = mBaseWidth + mItemWidth * i;
            if (i == 0) {
                dx = mBaseWidth+mLeft;
            }
            if (i == billList.size() - 1) {
                dx = mBaseWidth + mItemWidth * i-mLeft;
            }
            float dy = (float) (mMaxLineHeight - (data[i] - mMinBillValue) / mBillRange * mMaxLineSpace);
            if (i == mSelectedIndex) {
                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                canvas.drawText(decimalFormat.format(data[i]), dx, dy - fontHeight, mTextPaint);
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // mWidth为当前整个View的显示宽度
        // mWidth = 2 * mBaseWidth + 6 * mItemWidth = 20 * mBaseWidth;
        mBaseWidth = mWidth / 20f;
        mItemWidth = mBaseWidth * 3;

        mMaxLineSpace = mHeight / 2f;
        mMaxLineHeight = mHeight * 3 / 4f;
        mMinLineHeight = mHeight / 4f;

        mMaxBillValue = BillUtil.getMaxBillValue(data);
        mMinBillValue = BillUtil.getMinBillValue(data);
        mBillRange = mMaxBillValue - mMinBillValue;

        // 避免只有一个月消费金额或者最低最高消费金额相同造成mBillRange当除数为0的情况
        mBillRange = (mBillRange == 0 ? 1 : mBillRange);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float fontHeight = fontMetrics.descent - fontMetrics.ascent;

        mWidth = widthSize;


        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
        } else {
            mHeight = (int) (4 * fontHeight + 4 * fontHeight + getPaddingTop() + getPaddingBottom());
        }

        setMeasuredDimension(mWidth, mHeight);

    }


    public void setData(int type, List<IncomeExpendInfo> infos) {
        this.type = type;
        this.billList = infos;
        initData(type, infos);
        invalidateView();
    }


    private void invalidateView() {
        if (Looper.myLooper() == Looper.getMainLooper())
            invalidate();
        else
            postInvalidate();
    }

    private void initData(int type, List<IncomeExpendInfo> infos) {
        if (infos == null || infos.size() == 0) {
            month = null;
            data = null;
            return;
        }
        month = new String[infos.size()];
        data = new float[infos.size()];
        for (int i = 0; i < infos.size(); i++) {
            IncomeExpendInfo info = infos.get(i);
            month[i] = info.month;
            if (type == TYPE_INCOME) {
                data[i] = Float.parseFloat(info.income);
            } else if (type == TYPE_EXPEND) {
                data[i] = Float.parseFloat(info.expend);
            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.getParent().requestDisallowInterceptTouchEvent(true);
        //一旦底层View收到touch的action后调用这个方法那么父层View就不会再调用onInterceptTouchEvent了，也无法截获以后的action，这个事件被消费了

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP://触摸(ACTION_DOWN操作)，滑动(ACTION_MOVE操作)和抬起(ACTION_UP)
                onActionUpEvent(event);
                this.getParent().requestDisallowInterceptTouchEvent(false);
                break;
            case MotionEvent.ACTION_CANCEL:
                this.getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        return true;
    }

    private void onActionUpEvent(MotionEvent event) {
        boolean isValidTouch = validTouch(event.getX(), event.getY());
        if (isValidTouch) {
            invalidateView();
        }

    }

    private boolean validTouch(float x, float y) {
        //曲线触摸区域
        for (int i = 0; i < mPointList.size(); i++) {
            // dipToPx(8)乘以2为了适当增大触摸面积
            if (x > (mPointList.get(i).x - mTouchR * 2) && x < (mPointList.get(i).x + mTouchR * 2)) {
                if (y > (mPointList.get(i).y - mTouchR * 2) && y < (mPointList.get(i).y + mTouchR * 2)) {
                    mSelectedIndex = i;
                    return true;
                }
            }
        }
        return false;
    }


    //点
    class Point {
        float x;
        float y;
    }
}
