package com.she.customview.slidedeletelistview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.ListView;

/**
 * Created by Administrator on 2016/8/24.
 */
public class SlideListView extends ListView {
    private int currentPositon;//获取当前item

    private SlideDeleteView itemViewContent;

    private boolean isScrolling = false;//是否已经侧滑出来了
    private int mPosition = -1;
    private int mX;
    private int mY;

    private int screenWidth;//获取屏幕的宽度
    public SlideListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SlideListView(Context context) {
        super(context);
    }

    public SlideListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        screenWidth = wm.getDefaultDisplay().getWidth();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        return super.dispatchTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mX = x;
                mY = y;
                //根据 x,y 确定点击的那个item
                currentPositon = this.pointToPosition((int) event.getX(), (int) event.getY());
                //未关闭状态进行复原
                if (mPosition != currentPositon) {
                    mPosition = currentPositon;
                    if (itemViewContent != null) {
                        //恢复上个侧滑的状态
                        itemViewContent.reset();
                    }
                    isScrolling = false;
                } else {
                    if (isScrolling) {//如果当前item侧滑打开就关闭
                        itemViewContent.reset();
                    }
                }

                break;
            case MotionEvent.ACTION_UP:
                if (itemViewContent != null) {
                    //手指抬起，去判断自定滑动到起点还是终点
                    itemViewContent.adjustScroller();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mPosition != -1) {
                    float detex = Math.abs(x - mX);
                    float deteY = Math.abs(y - mY);
                    if (event.getRawX()>= screenWidth / 3 && detex > 20) {
                        isScrolling = true;
                        //这里本可以通过position拿到 点击的view，因为listview的item的缓存复用问题的，这里我们只需要考虑屏幕内显示的item
                        int firstPositon = getFirstVisiblePosition();
                        int screenPosition = currentPositon - firstPositon;
                        itemViewContent = (SlideDeleteView) getChildAt(screenPosition);
                        //将事件传递给sliderView去处理
                        itemViewContent.onTouchEvent(event);
                        return false;
                    }
                }
                break;
            default:
                break;
        }
        //注意当我们不主动消耗事件，交给系统去分发接下来的事件
        return super.onTouchEvent(event);

    }
}
