package com.yk.onedemo.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by BTFTU on 2018/3/20.
 * 不知道怎么形容 感觉就是观察被依赖的view的状态
 */

public class HomePageBehavior extends CoordinatorLayout.Behavior<RelativeLayout> {

    private int mTotalScrollRange;//全部滑行距离

    private int mAppBarHeight;//appbar高度

    private float mAppBarStartY;// AppBar的起始Y坐标

    private float mPercent;// 滚动执行百分比[0~1]
    private Context mContext;
    private HomePageBehaviorListener mBehaviorListener;

    public HomePageBehavior(Context context) {
        this.mContext = context;
        init();
    }

    public HomePageBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    private void init() {

    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, RelativeLayout child, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, RelativeLayout child, View dependency) {

        initProperties(child, dependency);
        mPercent = (mAppBarStartY - dependency.getY()) * 1.0f / mTotalScrollRange;
//        child.setAlpha(mPercent);
        if (mPercent>1)
            mPercent=1;
        System.out.println("================="+mPercent);
        float alpha = (255 * mPercent);
        child.setBackgroundColor(Color.argb((int) alpha, 0, 0, 0));

        return true;
    }


    private void initProperties(View child, View dependency) {
        if (mAppBarHeight == 0) {
            mAppBarHeight = dependency.getHeight();
            mAppBarStartY = dependency.getY();
        }

        if (mTotalScrollRange == 0) {
            mTotalScrollRange = ((AppBarLayout) dependency).getTotalScrollRange() - 154;
        }
    }



    public interface HomePageBehaviorListener {
        void onDependentViewChanged(float t);
    }
}
