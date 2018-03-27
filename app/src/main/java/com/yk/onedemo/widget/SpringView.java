package com.yk.onedemo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


import com.yk.onedemo.utils.MathUtils;
import com.yk.onedemo.utils.UIUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yk on 2017/7/17.
 * 暂时不知道写成什么样
 */

public class SpringView extends View {

    private Paint mSpringPaint;
    private Paint mBallPaint;
    private Paint mBallPathPaint;
    private PointF mTouchPoint;
    private PointF mContactLeftPoint;
    private PointF mContactRightPoint;
    private PointF mStartPoint;
    private PointF mEndPoint;
    private Context mContext;
    private float mTotalHeight;
    private float mTotalWidth;
    private Path springPath;
    private int mBallRadius;
    private PointF mBallStablePosition;
    private Path mBallPath;
    private float mBallPathK;
    private float mSpringLength;
    private float mSpringStrain = 0.1f;//张力 n/px 单位是我自己瞎写的
    private float mGravitational = 10;//重力加速度 m/s^2
    private float mBallQuality = 1;//小球重量 kg


    public SpringView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public SpringView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    private void init() {
        mSpringPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSpringPaint.setColor(Color.parseColor("#F96363"));
        mSpringPaint.setStyle(Paint.Style.STROKE);
        mSpringPaint.setStrokeWidth(2);
        mTotalHeight = UIUtil.getScreenHeight(mContext);
        mTotalWidth = UIUtil.getScreenWidth(mContext);
        mStartPoint = new PointF(150, mTotalHeight / 2);
        mEndPoint = new PointF(mTotalWidth - 150, mTotalHeight / 2);
        mBallPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBallPaint.setStyle(Paint.Style.FILL);
        mBallRadius = 30;
        mBallStablePosition = new PointF(mTotalWidth / 2, mTotalHeight / 2 + 100);
        mTouchPoint = new PointF(mBallStablePosition.x, mBallStablePosition.y);
        mContactLeftPoint = math(mBallStablePosition.x, mBallStablePosition.y, mStartPoint.x, mStartPoint.y, mBallRadius, true);
        mContactRightPoint = math(mBallStablePosition.x, mBallStablePosition.y, mEndPoint.x, mEndPoint.y, mBallRadius, false);
        preBall();
    }

    private void preBall() {
        mBallPathPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBallPathPaint.setColor(Color.GRAY);
        mBallPathPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        PathEffect pathEffect = new DashPathEffect(new float[]{4, 8, 4, 8}, 1);
        mBallPathPaint.setPathEffect(pathEffect);
        mBallPathPaint.setStrokeWidth(1);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        drawSpring(canvas);
        drawBall(canvas);
        drawBallPath(canvas);
    }

    private void drawBallPath(Canvas canvas) {
        mBallPath = new Path();
        PointF point1 = getFinalPoint(mBallPathK, mTouchPoint.x, mTouchPoint.y, 50);
        PointF point2 = getFinalPoint(mBallPathK, point1.x, point1.y, 500);

        mBallPath.moveTo(point1.x, point1.y);
        mBallPath.lineTo(point2.x, point2.y);

        canvas.drawPath(mBallPath, mBallPathPaint);

    }

    private void drawBall(Canvas canvas) {
        canvas.drawCircle(mTouchPoint.x, mTouchPoint.y, mBallRadius, mBallPaint);
    }

    private void drawSpring(Canvas canvas) {
        springPath = new Path();
        springPath.moveTo(mStartPoint.x, mStartPoint.y);
        springPath.lineTo(mContactLeftPoint.x, mContactLeftPoint.y);

        springPath.moveTo(mContactRightPoint.x, mContactRightPoint.y);
        springPath.lineTo(mEndPoint.x, mEndPoint.y);

        double up = 2 * (mBallRadius * mBallRadius) -
                Math.pow(mContactLeftPoint.x - mContactRightPoint.x, 2) -
                Math.pow(mContactLeftPoint.y - mContactRightPoint.y, 2);

        double cosC = up / (2 * mBallRadius * mBallRadius);//相切两点与圆心形成角度的cos值
        double arc = Math.acos(cosC);//弧度
        float angle = (float) (arc * 180 / Math.PI);//角度

        double cosStart = (mContactRightPoint.x - mTouchPoint.x) / mBallRadius;
        float angleStart = (float) (Math.acos(cosStart) * 180 / Math.PI);

        if (mTouchPoint.y < mStartPoint.y) {
            if (mTouchPoint.x < mTotalWidth / 2) {
                angleStart += 90;
            } else {
                angleStart += 270;
            }
        }
        float springLength1 = (float) Math.sqrt(Math.pow(mContactLeftPoint.x - mStartPoint.x, 2)
                + Math.pow(mContactLeftPoint.y - mStartPoint.y, 2));
        float springLength2 = (float) Math.sqrt(Math.pow(mContactRightPoint.x - mEndPoint.x, 2)
                + Math.pow(mContactRightPoint.y - mEndPoint.y, 2));
        float springLength3 = (float) (mBallRadius * arc);

        mSpringLength = springLength1 + springLength2 + springLength3;
        RectF rectF = new RectF(mTouchPoint.x - mBallRadius,
                mTouchPoint.y - mBallRadius,
                mTouchPoint.x + mBallRadius,
                mTouchPoint.y + mBallRadius);
        canvas.drawArc(rectF, angleStart, angle, false, mSpringPaint);
        canvas.drawPath(springPath, mSpringPaint);
    }

    private boolean allowMove = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (x < mTouchPoint.x + 2 * mBallRadius && x > mTouchPoint.x - 2 * mBallRadius &&
                        y < mTouchPoint.y + 2 * mBallRadius && y > mTouchPoint.y - 2 * mBallRadius) {
                    allowMove = true;
                } else {
                    allowMove = false;
                }
                break;
            case MotionEvent.ACTION_UP:

                break;
            case MotionEvent.ACTION_MOVE:
                if (allowMove) {
                    mTouchPoint.x = x;
                    mTouchPoint.y = y;
                    mContactLeftPoint = math(x, y, mStartPoint.x, mStartPoint.y, mBallRadius, true);
                    mContactRightPoint = math(x, y, mEndPoint.x, mEndPoint.y, mBallRadius, false);
                    mBallPathK = -(mContactLeftPoint.x - mContactRightPoint.x) / (mContactLeftPoint.y - mContactRightPoint.y);
                    invalidate();
                }
                break;
        }
        return true;
    }


    private PointF math(float Xo, float Yo, float Xa, float Ya, float r, boolean isLeft) {
        float k = MathUtils.getK(Xa, Ya, Xo, Yo);
        float d = MathUtils.getD(Xa, Ya, Xo, Yo, r);
        float a = MathUtils.getA(k);
        float b = MathUtils.getB(k, d, Xo, Yo);
        float c = MathUtils.getC(d, Xo, Yo, r);
        float x1 = MathUtils.getX1(a, b, c);
        float x2 = MathUtils.getX2(a, b, c);

//        double hu = 60 * Math.PI / 180;
//        double jiao = hu * 180 / Math.PI;
//        System.out.println("=====Math.acos(0.5);======" + Math.acos(1 / Math.sqrt(2)) * 180 / Math.PI);
//        System.out.println("=====Math.cos(60);======" + Math.cos(hu));

        if (isLeft) {
            return new PointF(Math.min(x1, x2), MathUtils.getY(k, Math.min(x1, x2), d));
        } else {
            return new PointF(Math.max(x1, x2), MathUtils.getY(k, Math.max(x1, x2), d));
        }
    }

    private PointF getFinalPoint(float k, float xo, float yo, float l) {
        float d = yo - (k * xo);
        float a = MathUtils.getA(k);
        float b = MathUtils.getB(k, d, xo, yo);
        float c = MathUtils.getC(d, xo, yo, l);
        float x1 = MathUtils.getX1(a, b, c);
        float y1 = MathUtils.getY(k, x1, d);
        float x2 = MathUtils.getX2(a, b, c);
        float y2 = MathUtils.getY(k, x2, d);

        if (y1 < y2) {
            return new PointF(x1, y1);
        } else {
            return new PointF(x2, y2);
        }

    }

}
