package com.she.customview.slidedeletelistview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * 自定义一个viewgroup来代替listview的item
 * Created by Arron on 2016/8/24.
 */
public class SlideDeleteView extends LinearLayout {

    LinearLayout linerContent;
    private Context context;
    private int slideViewWidth = 100;
    private Scroller scroller;//滑动类
    private float mLastX;
    private float mLastY;


    public SlideDeleteView(Context context) {
        super(context);
        initView();
    }

    public SlideDeleteView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SlideDeleteView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    /**
     * 初始化组件
     */
    private void initView() {
        context = getContext();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        /**
         * 将制定的数值转换为制定变量值 系统提供
         */
        slideViewWidth = Math.round(TypedValue.applyDimension
                (TypedValue.COMPLEX_UNIT_DIP, slideViewWidth, getResources().getDisplayMetrics())
        );
        setOrientation(HORIZONTAL);
        View.inflate(context, R.layout.slide_view_merge, this);
        linerContent = (LinearLayout) findViewById(R.id.view_content);
        scroller = new Scroller(context);
    }

    /**
     * 添加item内容显示的view
     * @param contentView
     * @return
     */
    public View setContentView(View contentView) {
        linerContent.addView(contentView);
        return linerContent;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);//这句话的意思是默认系统返回false向下分发事件
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_MOVE:
                //先获取移动时的x,y坐标
                float x = event.getX();
                float y = event.getY();
                //获取移动的距离差值
                float offsetX = x-mLastX;
                float offsetY = y-mLastY;

                //更新上次的X,Y值
                mLastX = x;
                mLastY = y;

                float newScrollX = getScrollX() - offsetX;//计算距离原点的X偏移量和手指移动X的差值
                if (newScrollX < 0) {
                    newScrollX = 0;
                } else if (newScrollX > slideViewWidth) { //滑动距离为规定的del删除的距离
                    newScrollX = slideViewWidth;
                }
                this.scrollTo((int) newScrollX, 0);
                break;
        }

        return super.onTouchEvent(event);//将事件传递给子类
    }

    /**
     * 平滑滚动到制定的位置
     *
     * @param destX
     * @param destY
     */
    public void smoothScrollTo(int destX, int destY) {
        int scrollX = getScrollX();
        int deltaX = destX - scrollX;
        scroller.startScroll(scrollX, 0, deltaX, 0, 200);
        invalidate();
    }

    /**
     * 重写此方法是为了达到弹性滑动的效果
     */
    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {//判断是否还在继续滑动
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            postInvalidate();
        }
    }

    /**
     * 恢复原来的位置
     */
    public void reset() {
        int offset = getScrollX();
        if (offset == 0) {
            return;
        }
        smoothScrollTo(0, 0);
    }

    /**
     * 判断滚动方向
     */
    public void adjustScroller() {
        int offset = getScrollX();
        if (offset == 0) {
            return;
        }
        if (offset > slideViewWidth / 2) {
            smoothScrollTo(slideViewWidth, 0);
        } else {
            reset();
        }
    }
}
