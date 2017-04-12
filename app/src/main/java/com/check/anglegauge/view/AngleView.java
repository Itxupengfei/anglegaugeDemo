package com.check.anglegauge.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * @author 许鹏飞
 * @Desc:
 * @time 2017/4/11 0011
 */
public class AngleView extends View {

    private int mMeasuredHeight;
    private int mMeasuredWidth;
    private Paint mPaint;

    public AngleView(Context context) {
        this(context,null);
    }

    public AngleView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public AngleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    /**
     * 初始化方法
     */
    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.parseColor("#ffffff"));
        mPaint.setStrokeWidth(5);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(getMySize(widthSize,widthMode), getMySize(heightSize,heightMode));
    }
    private int getMySize(int defaultSize, int measureSpec) {
        int mySize = defaultSize;

        int mode = measureSpec;
        int size = defaultSize;

        switch (mode) {
            case MeasureSpec.UNSPECIFIED: {//如果没有指定大小，就设置为默认大小
                mySize = defaultSize;
                break;
            }
            case MeasureSpec.AT_MOST: {//如果测量模式是最大取值为size
                //我们将大小取最大值,你也可以取其他值
                mySize = size;
                break;
            }
            case MeasureSpec.EXACTLY: {//如果是固定的大小，那就不要去改变它
                mySize = size;
                break;
            }
        }
        return mySize;
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mMeasuredHeight = getMeasuredHeight();
        mMeasuredWidth = getMeasuredWidth();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(0f,0f,mAngle,mMeasuredHeight,mPaint);
    }
    float mAngle=0;
    public void setAngle(float angle) {
            //尼玛坑爹,tan居然用的是弧度
            double tan = Math.tan(Math.toRadians(angle));
            mAngle =Math.abs ((float) (mMeasuredHeight *tan));
            Log.e("AngleView", "mAngle:" + angle+","+tan+","+mAngle);
            invalidate();


    }
}
