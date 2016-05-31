package com.xujun.administrator.customprogressbar;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

public class HorizontalProgressBarWithNumber extends BaseProgressBar {

    public HorizontalProgressBarWithNumber(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec,
                                          int heightMeasureSpec) {

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);
        setMeasuredDimension(width, height);

        mRealWidth = getMeasuredWidth() - getPaddingRight() - getPaddingLeft();
    }

    private int measureHeight(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            float textHeight = (mPaint.descent() - mPaint.ascent());
            result = (int) (getPaddingTop() + getPaddingBottom() + Math.max(
                    Math.max(mReachedProgressBarHeight,
                            mUnReachedProgressBarHeight), Math.abs(textHeight)));
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {

        canvas.save();
        canvas.translate(getPaddingLeft(), getHeight() / 2);

        boolean noNeedBg = false;
        float radio = getProgress() * 1.0f / getMax();
        float progressPosX = (int) (mRealWidth * radio);
        String text = getProgress() + "%";
        // mPaint.getTextBounds(text, 0, text.length(), mTextBound);

        float textWidth = mPaint.measureText(text);
        float textHeight = (mPaint.descent() + mPaint.ascent()) / 2;

        if (progressPosX + textWidth > mRealWidth) {
            progressPosX = mRealWidth - textWidth;
            noNeedBg = true;
        }

        // draw reached bar
        float endX = progressPosX - mTextOffset / 2;
        if (endX > 0) {
            mPaint.setColor(mReachedBarColor);
            mPaint.setStrokeWidth(mReachedProgressBarHeight);
            canvas.drawLine(0, 0, endX, 0, mPaint);
        }
        // draw progress bar
        // measure text bound
        if (mIfDrawText) {
            mPaint.setColor(mTextColor);
            canvas.drawText(text, progressPosX, -textHeight, mPaint);
        }

        // draw unreached bar
        if (!noNeedBg) {
            float start = progressPosX + mTextOffset / 2 + textWidth;
            mPaint.setColor(mUnReachedBarColor);
            mPaint.setStrokeWidth(mUnReachedProgressBarHeight);
            canvas.drawLine(start, 0, mRealWidth, 0, mPaint);
        }

        canvas.restore();

    }

}
