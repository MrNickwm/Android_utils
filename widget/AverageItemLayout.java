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
 *
 * <declare-styleable name="AverageItemLayout">
        <attr name="columnCount" format="integer" />
        <attr name="textLength" format="integer" />
        <attr name="itemMargin" format="integer" />
        <attr name="itemGravity" format="integer">
            <enum name="center" value="0"/>
            <enum name="left" value="1"/>
        </attr>
    </declare-styleable>
 */
public class AverageItemLayout extends LinearLayout {
    private int mTextLength = 5;
    private int mColumns = 4;
    private int mWidth;
    private int mHeight;
    public static final int ITEM_GRAVITY_LEFT = 1;
    public static final int ITEM_GRAVITY_CENTER = 0;
    private int mItemGravity = ITEM_GRAVITY_CENTER;


    private float density;
    private int mItemMargin;


    public AverageItemLayout(Context context) {
        this(context, null);
    }

    public AverageItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        density = context.getResources().getDisplayMetrics().density;
        mItemMargin = (int) (12 * density);
        setOrientation(HORIZONTAL);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AverageItemLayout);
        mColumns = ta.getInteger(R.styleable.AverageItemLayout_columnCount, 4);
        mTextLength = ta.getInteger(R.styleable.AverageItemLayout_textLength, 5);
        mItemMargin = ta.getInteger(R.styleable.AverageItemLayout_itemMargin, 12);
        mItemGravity = ta.getInteger(R.styleable.AverageItemLayout_itemGravity, ITEM_GRAVITY_CENTER);
        ta.recycle();
    }

    /**
     * 设置单行的Item数量
     *
     * @param columns
     */
    public void setOneLineChildCount(int columns) {
        this.mColumns = columns;
        invalidate();
    }

    /**
     * 设置一个Item中文字的最大长度
     *
     * @param length
     */
    public void setChildTextLength(int length) {
        this.mTextLength = length;
    }

    /**
     * 设置方向
     * @param gravity
     */
    public void setItemGravity(int gravity) {
        this.mItemGravity = gravity;
        invalidate();
    }

    /**
     * 设置Item的间距
     * @param margin
     */
    public void setItemMargin(int margin) {
        this.mItemMargin = (int) (margin * density);
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
        int line = getChildCount() % mColumns == 0 ? getChildCount() / mColumns : getChildCount() / mColumns + 1;
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
        int topMarging = (int) (12 * density);
        int childWidth = (mWidth - mColumns * mItemMargin) / mColumns;
        int childHeight = getChildAt(0).getMeasuredHeight();

        int leftMargin = 0;
        if (mItemGravity ==ITEM_GRAVITY_CENTER && childCount < mColumns) {
            // 判断居中还是左对齐
            leftMargin = (mColumns - childCount) * (childWidth + mItemMargin) / 2;
        }

        for (int i = 0; i < childCount; i++) {
            int line = i / mColumns;
            int childBottom = (line + 1) * childHeight + line * topMarging;
            int position = i % mColumns;
            int left = position * (childWidth + mItemMargin) + leftMargin;
            int top = line * (childHeight + topMarging);
            int right = (position + 1) * childWidth + (position * mItemMargin) + leftMargin;
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
                if (tv.getText().toString().length() > mTextLength) {
                    tv.setText(tv.getText().toString().substring(0, mTextLength) + "..");
                }
            }
        }
    }
}
