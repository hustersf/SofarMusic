package com.sf.demo.view.chainheadview;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.sf.demo.R;
import com.sf.utility.BitmapUtil;
import com.sf.utility.DensityUtil;

/**
 * Created by sufan on 2017/10/24.
 */

public class ChainHeadView extends View {

    private Context mContext;
    private Paint mPaint;

    private int mRealWidth;

    private List<HeadInfo> mDatas;
    private int mColumn;

    private int spaceText;   //头像和文字的间距
    private int spaceArrow;   //头像和箭头的间距

    private Bitmap arrowBt;
    private int arrowW;     //箭头宽度
    private int arrowH;     //箭头高度


    public ChainHeadView(Context context) {
        this(context, null);
    }

    public ChainHeadView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChainHeadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setTextSize(DensityUtil.sp2px(context, 14));
        mPaint.setColor(Color.parseColor("#333333"));
        mPaint.setTextAlign(Paint.Align.CENTER);  //居中
    }

    public void init(int column, List<HeadInfo> datas) {
        mColumn = column;
        mDatas = datas;

        spaceText = DensityUtil.dp2px(mContext, 3);
        spaceArrow = DensityUtil.dp2px(mContext, 3);
        arrowBt = BitmapFactory.decodeResource(getResources(), R.drawable.demo_chain_arrow);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mRealWidth = getWidth() - getPaddingLeft() - getPaddingRight();

        int bitmapW = (int) ((mRealWidth * 1.0f) / (2 * mColumn - 1));

        arrowW = bitmapW - 2 * spaceArrow;
        float ratio=arrowBt.getWidth()*1.0f/arrowBt.getHeight();
        arrowH= (int) (arrowW/ratio);

        arrowBt = BitmapUtil.resizeBitmap(arrowBt, arrowW, arrowH);

        Bitmap bt1 = arrowBt;   //正常图片
        Bitmap bt2 = BitmapUtil.rotateBitmap(arrowBt, 90);            //旋转90度
        Bitmap bt3 = BitmapUtil.rotateBitmap(arrowBt, 180);            //旋转180度

        for (int i = 0; i < mDatas.size(); i++) {
            Bitmap srcBt = mDatas.get(i).headBt;
            Bitmap destBt = BitmapUtil.getOvalBitmap(BitmapUtil.resizeBitmap(srcBt, bitmapW, bitmapW));

            //计算第i个图形所在的行数和列数,从0开始计数
            int l = i / mColumn;   //行数
            int c = 0;            //列数
            if (l % 2 == 0) {
                c = i % mColumn;
            } else {
                c = (mColumn - 1) - (i % mColumn);
            }

            //计算出文字高度
            Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
            float fontHeight = fontMetrics.descent - fontMetrics.ascent;
            float textHeight = fontHeight + spaceText;

            //不管如何设置padding,我们都认为左右是对称的，上下是对称的
            int padding1 = (getPaddingLeft() + getPaddingRight()) / 2;
            int padding2 = (getPaddingTop() + getPaddingBottom()) / 2;


            //画头像
            float left = 2 * c * bitmapW + padding1;
            float top = 2 * l * bitmapW + l * textHeight + padding2;
            canvas.drawBitmap(destBt, left, top, mPaint);


            //画文字
            float x = 2 * c * bitmapW + bitmapW / 2 + padding1;
            float y = (2 * l + 1) * bitmapW + textHeight * (l + 1) + padding2;
            canvas.drawText(mDatas.get(i).headName, x, y, mPaint);


            //画箭头
            if(i==mDatas.size()-1){
                return;     //最后一个不画
            }
            if ((i + 1) % mColumn == 0) {
                float arrowTop = y + spaceArrow;
                canvas.drawBitmap(bt2, left+(bitmapW-arrowH)/2, arrowTop, mPaint);
            } else {
                if (l % 2 == 0) {
                    canvas.drawBitmap(bt1, left + bitmapW + spaceArrow, top+(bitmapW-arrowH)/2, mPaint);
                } else {
                    canvas.drawBitmap(bt3, left - bitmapW + spaceArrow, top+(bitmapW-arrowH)/2, mPaint);
                }
            }


        }


    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthValue = MeasureSpec.getSize(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);

        setMeasuredDimension(widthValue, height);
    }

    private int measureHeight(int heightMeasureSpec) {

        int result = 0;
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);

        //计算出文字高度
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        float fontHeight = fontMetrics.descent - fontMetrics.ascent;
        float textHeight = fontHeight + spaceText;

        //计算出头像的高度
        float width = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        float bitmapH = (int) ((width * 1.0f) / (2 * mColumn - 1));

        //计算有多少行
        int line = (int) Math.ceil(mDatas.size() * 1.0f / mColumn);


        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            int viewH = (int) ((bitmapH + textHeight) * line + (line - 1) * bitmapH);
            int h = getPaddingTop() + getPaddingBottom() + viewH;
            result = h;
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(size, h);
            }
        }
        return result;
    }
}
