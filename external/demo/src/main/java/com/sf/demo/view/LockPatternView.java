package com.sf.demo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.sf.demo.R;
import com.sf.utility.FormatUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sufan on 16/7/27.
 */
public class LockPatternView extends View {

    private Point[][] pointss = new Point[3][3];  //9宫格
    private float mLength;       //九宫格边长

    private Paint mPaint = new Paint();
    private Matrix matrix = new Matrix();

    private Bitmap pointNormalBt, pointPressedBt, pointErrorBt;
    private Bitmap linePressedBt, lineErrorBt;
    private int bitmapR;    //圆点的半径

    private float moveX, moveY;    //手指所在的点的X,Y坐标
    private List<Point> pointList = new ArrayList<>();

    private boolean isInit = true;  //是否需要初始化点
    private static final int minSize = 5;   //密码最小长度

    //监听
    private OnPointChangeListener pointChangeListener;


    public LockPatternView(Context context) {
        super(context);
    }

    public LockPatternView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LockPatternView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isInit) {
            initPoint();
        }
        point2Canvas(canvas);
        if (pointList.size() > 0) {
            Point a = pointList.get(0);
            for (int i = 0; i < pointList.size(); i++) {
                Point b = pointList.get(i);
                line2Canvas(canvas, a, b);
                a = b;
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        moveX = event.getX();
        moveY = event.getY();
        Point point = null;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                resetPoint();
                break;
            case MotionEvent.ACTION_MOVE:
                point = checkPoint();
                if (point != null) {
                    point.state = Point.PRESSED;
                    if (!pointList.contains(point)) {
                        pointList.add(point);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (pointList.size() == 0) {
                    if (pointChangeListener != null) {
                        pointChangeListener.onPointLess(0);
                    }
                } else if (pointList.size() < minSize && pointList.size() > 0) {
                    for (Point errorPoint : pointList) {
                        errorPoint.state = Point.ERROR;
                    }
                    if (pointChangeListener != null) {

                        pointChangeListener.onPointLess(minSize);
                    }
                } else {

                    if (pointChangeListener != null) {
                        String str = "";
                        for (int i = 0; i < pointList.size(); i++) {
                            str += pointList.get(i).index;
                        }
                        pointChangeListener.onPointEnd(str);
                    }
                }
                break;
        }
        postInvalidate();
        return true;
    }


    private void point2Canvas(Canvas canvas) {
        for (Point[] points : pointss) {
            for (Point point : points) {
                if (point.state == Point.NORMAL) {
                    canvas.drawBitmap(pointNormalBt, point.x - bitmapR, point.y - bitmapR, mPaint);
                } else if (point.state == Point.PRESSED) {
                    canvas.drawBitmap(pointPressedBt, point.x - bitmapR, point.y - bitmapR, mPaint);
                } else {
                    canvas.drawBitmap(pointErrorBt, point.x - bitmapR, point.y - bitmapR, mPaint);

                }
            }
        }
    }

    private void line2Canvas(Canvas canvas, Point a, Point b) {
        float lineLength = (float) getDistance(a, b);
        float degree = getDegrees(a, b);
        canvas.rotate(degree, a.x, a.y);
        if (a.state == Point.PRESSED) {
            matrix.setScale(lineLength / lineErrorBt.getWidth(), 1);
            //   matrix.postTranslate(a.x - lineErrorBt.getWidth() / 2, a.y - lineErrorBt.getHeight() / 2);
            matrix.postTranslate(a.x , a.y - lineErrorBt.getHeight() / 2);
            canvas.drawBitmap(linePressedBt, matrix, mPaint);
        } else if (a.state == Point.ERROR) {
            matrix.setScale(lineLength / lineErrorBt.getWidth(), 1);
            // matrix.postTranslate(a.x - lineErrorBt.getWidth() / 2, a.y - lineErrorBt.getHeight() / 2);
            matrix.postTranslate(a.x, a.y - lineErrorBt.getHeight() / 2);
            canvas.drawBitmap(lineErrorBt, matrix, mPaint);
        }
        canvas.rotate(-degree, a.x, a.y);

    }

    // 获取角度
    public float getDegrees(Point pointA, Point pointB) {
        return (float) Math.toDegrees(Math.atan2(pointB.y - pointA.y, pointB.x - pointA.x));
    }


    //检查选中的点
    private Point checkPoint() {
        for (Point[] points : pointss) {
            for (Point point : points) {
                if (point.isOnePoint(point.x, point.y, bitmapR, moveX, moveY)) {
                    return point;
                }
            }
        }
        return null;
    }

    private double getDistance(Point a, Point b) {
        return Math.sqrt(Math.abs(a.x - b.x) * Math.abs(a.x - b.x) + Math.abs(a.y - b.y) * Math.abs(a.y - b.y));
    }


    private void initPoint() {
        //得到view的宽和高
        int width = getWidth();
        int height = getHeight();

        float offsetX = 0;  //view距离X轴的偏移
        float offsetY = 0;  //view距离Y轴的偏移

        //计算偏移量，使得9个点居中
        if (width < height) {
            //竖屏
            offsetY = (height - width) / 2;
            mLength = width;
        } else {
            //横屏
            offsetX = (width - height) / 2;
            mLength = height;
        }


        //初始化图片资源
        pointNormalBt = BitmapFactory.decodeResource(getResources(), R.drawable.demo_lock_point_normal);
        pointPressedBt = BitmapFactory.decodeResource(getResources(), R.drawable.demo_lock_point_pressed);
        pointErrorBt = BitmapFactory.decodeResource(getResources(), R.drawable.demo_lock_point_error);
        //  linePressedBt = BitmapFactory.decodeResource(getResources(), R.drawable.demo_lock_line_pressed);
       //   lineErrorBt = BitmapFactory.decodeResource(getResources(), R.drawable.demo_lock_line_error);

        Drawable drawablePressed = getResources().getDrawable(R.drawable.line_yellow);
        Drawable drawableError = getResources().getDrawable(R.drawable.line_red);
        linePressedBt = FormatUtil.getInstance().Drawable2Bitmap(drawablePressed);
        lineErrorBt = FormatUtil.getInstance().Drawable2Bitmap(drawableError);

        //初始化9个点的位置
        pointss[0][0] = new Point(mLength / 4 + offsetX, mLength / 4 + offsetY);
        pointss[0][1] = new Point(mLength / 2 + offsetX, mLength / 4 + offsetY);
        pointss[0][2] = new Point(mLength / 4 * 3 + offsetX, mLength / 4 + offsetY);

        pointss[1][0] = new Point(mLength / 4 + offsetX, mLength / 2 + offsetY);
        pointss[1][1] = new Point(mLength / 2 + offsetX, mLength / 2 + offsetY);
        pointss[1][2] = new Point(mLength / 4 * 3 + offsetX, mLength / 2 + offsetY);

        pointss[2][0] = new Point(mLength / 4 + offsetX, mLength / 4 * 3 + offsetY);
        pointss[2][1] = new Point(mLength / 2 + offsetX, mLength / 4 * 3 + offsetY);
        pointss[2][2] = new Point(mLength / 4 * 3 + offsetX, mLength / 4 * 3 + offsetY);

        bitmapR = pointNormalBt.getWidth() / 2;

        //设置密码
        int num = 1;
        for (Point[] points : pointss) {
            for (Point point : points) {
                point.index = num;
                num++;
            }
        }

        isInit = false;
    }

    private void resetPoint() {
        pointList.clear();
        for (Point[] points : pointss) {
            for (Point point : points) {
                point.state = Point.NORMAL;
            }
        }
    }


    class Point {
        //点状态
        private static final int NORMAL = 0;
        private static final int PRESSED = 1;
        private static final int ERROR = 2;

        private float x;   //x坐标
        private float y;   //y坐标

        private int state = 0;  //状态
        private int index = 0;  //点所代表的数字

        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }

        //在误差范围内认为是一个点
        public boolean isOnePoint(float x1, float y1, float r, float x2, float y2) {
            return Math.sqrt((y1 - y2) * (y1 - y2) + (x1 - x2) * (x1 - x2)) < r;
        }
    }

    public interface OnPointChangeListener {
//        void onPointStart();

//        void onPointChanging();

        void onPointLess(int size);

        void onPointEnd(String pass);
    }

    public void setOnPointChangeListener(OnPointChangeListener pointChangeListener) {
        this.pointChangeListener = pointChangeListener;
    }
}
