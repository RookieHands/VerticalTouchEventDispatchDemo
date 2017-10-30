package com.hudson.worktest;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Hudson on 2017/10/29.
 * 经过测试，还有一些小问题，就是listView滑动到顶部，但是listView并没有回调onScroll方法.
 * 不过这是listView的问题，如果有同学有好的解决方法可以告诉我
 */

public class MyVerticalScrollView extends LinearLayout {

    private int mHeadViewHeight;

    public MyVerticalScrollView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        final View childAt = getChildAt(0);
        childAt.post(new Runnable() {//获取头布局的高度
            @Override
            public void run() {
                mHeadViewHeight = childAt.getHeight();
            }
        });
        super.onSizeChanged(w, h, oldw, oldh);
    }

    boolean tmp = false;
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean intercept = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = event.getY();
                if(mIsTop){
                    mIsTop = false;
                    tmp = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                //不拦截的情况：scrollY已经是mHeadHeight了，这个时候滑动ListView。
                //但是有一种特殊情况，listView到了顶部了，再往下拉，这时应该拦截
                if(getScrollY()>=mHeadViewHeight){
                    if(event.getY() - mLastY>0&&tmp){
                        intercept = true;
                    }else{
                        intercept = false;
                    }
                }else{
                    intercept = true;
                }
                tmp = false;
                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return intercept;
    }

    private float mLastY;
    private boolean start = false;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //避免由于listView一开始初始化时会回调onScroll方法导致mIsTop为true，
        // 由于一开始listView肯定没有滑上来，所以必须等本view滑动了才去检测listView是否top
        if(!start){
            start = true;
        }
        float y = event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:
                int destY = getScrollY() + (int) (mLastY - y);
                //防止滑出边界
                destY = (destY<0)?0:destY;
                destY = (destY>mHeadViewHeight)?mHeadViewHeight:destY;
                //listView处特殊处理，如果滑动的目的地接近mHeadViewHeight，那么直接指定为mHeadViewHeight
                if(Math.abs(destY - mHeadViewHeight)<30){
                    destY = mHeadViewHeight;
                }
                scrollTo(0, destY);
                break;
            case MotionEvent.ACTION_UP:

                break;
            default:
                break;
        }
//        if(getScrollY()>=mHeadViewHeight){
//            event.setAction(MotionEvent.ACTION_DOWN);
//            dispatchTouchEvent(event);
//        }
        mLastY = y;
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(mIsTop){//listView已经滑动到了顶部，需要重新决定是否拦截事件了
            ev.setAction(MotionEvent.ACTION_DOWN);
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean mIsTop = false;
    public void setListViewScrollTop(boolean isTop){//由listView通知本view，listView已经到顶部了
        mIsTop = isTop&&start;
    }

}
