package cn.bunnytrip.happy.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.bunnytrip.happy.R;

/**
 * 子View宽度平均分配的横向LinearLayout
 * Created by Nickwm on 2016.
 */
public class AverageItemLayout extends LinearLayout {
    private int textLength = 5;
    private int columns = 4;
    private int mWidth;
    private int mHeight;
    private float density;

    public AverageItemLayout(Context context) {
        this(context, null);
    }

    public AverageItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        density = context.getResources().getDisplayMetrics().density;
        setOrientation(HORIZONTAL);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AverageItemLayout);
        columns = ta.getInteger(R.styleable.AverageItemLayout_columnCount, 4);
        textLength = ta.getInteger(R.styleable.AverageItemLayout_textLength, 5);
        ta.recycle();
    }

    /**
     * 设置单行的Item数量
     *
     * @param childCount
     */
    public void setOneLineChildCount(int childCount) {
        this.columns = childCount;
    }

    /**
     * 设置一个Item中文字的最大长度
     *
     * @param length
     */
    public void setChildTextLength(int length) {
        this.textLength = length;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        View childView = getChildAt(0);
        if (childView == null) {
            return;
        }
        measureChild(childView, widthMeasureSpec, heightMeasureSpec);
        int childHeight = childView.getMeasuredHeight();
        int line = getChildCount() % columns == 0 ? getChildCount() / columns : getChildCount() / columns + 1;
        int topMarging = (int) (12 * density);
        mHeight = line * childHeight + (line - 1) * topMarging;
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        int childCount = getChildCount();
        if (childCount <= 0) {
            return;
        }
        int margin = (int) (12 * density);
        int topMarging = (int) (12 * density);
        int childWidth = (mWidth - columns * margin) / columns;
        int childHeight = getChildAt(0).getMeasuredHeight();

        int leftMargin = 0;
        if (childCount < columns) {
            leftMargin = (columns - childCount) * (childWidth + margin) / 2;
        }

        for (int i = 0; i < childCount; i++) {
            int line = i / columns;
            int childBottom = (line + 1) * childHeight + line * topMarging;
            int position = i % columns;
            int left = position * (childWidth + margin) + leftMargin;
            int top = line * (childHeight + topMarging);
            int right = (position + 1) * childWidth + (position * margin) + leftMargin;
            int bottom = childBottom;
            getChildAt(i).layout(left, top, right, bottom);

            int paddingleft = (int) (1 * density);
            int paddingtop = (int) (8 * density);
            getChildAt(i).setPadding(0, paddingtop, 0, paddingtop);
            if (getChildAt(i) instanceof TextView) {
                TextView tv = (TextView) getChildAt(i);
                tv.setIncludeFontPadding(false);
                tv.setSingleLine();
                tv.setGravity(Gravity.CENTER);
                if (tv.getText().toString().length() > textLength) {
                    tv.setText(tv.getText().toString().substring(0, textLength) + "..");
                }
            }
        }
    }
}
