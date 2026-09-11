package com.treasure.basic.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.treasure.basic.R;


/**
 * 加粗字体
 * @author by Zyq
 * @date 2022/3/15.
 */
public class BoldTextView extends AppCompatTextView {
    /**
     * 数值越大，字体越粗，0.0f表示常规画笔的宽度，相当于默认情况
     */
    private float mStrokeWidth = 0.7f;
    public BoldTextView(Context context) {
        this(context,null);
    }

    public BoldTextView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BoldTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取xml定义属性
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.BoldTextView,defStyleAttr,0);
        mStrokeWidth = array.getFloat(R.styleable.BoldTextView_stroke_width,mStrokeWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //获取当前控件的画笔
        TextPaint paint = getPaint();
        //设置画笔的描边宽度值
        paint.setStrokeWidth(mStrokeWidth);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        super.onDraw(canvas);
    }

    public void setStrokeWidth(float mStrokeWidth) {
        this.mStrokeWidth = mStrokeWidth;
        invalidate();
    }

    public float getStrokeWidth() {
        return mStrokeWidth;
    }
}

