package com.ziroom.ziroomcustomer.findhouse.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.freelxl.baselibrary.util.ToastUtil;
import com.ziroom.ziroomcustomer.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 密码框输入的自定义View
 * Created by Nickwm on 2017/9/19.
<declare-styleable name="PasswordInputView">
        <attr name="passwordLenth" format="integer" />
        <attr name="passwordTextSize" format="dimension" />
        <attr name="passwordTextColor" format="color|reference" />
        <attr name="passwordBackgroud" format="reference" />
        <attr name="passwordBackgroudColor" format="color|reference" />
        <attr name="passwordDividerColor" format="color|reference" />
        <attr name="passwordDividerWidth" format="dimension" />
        <attr name="isDividerShow" format="boolean" />
    </declare-styleable>
 */

public class PasswordInputView extends RelativeLayout {
    private Context mContext;
    private float density;
    private List<TextView> mPasswordList = new ArrayList<>();
    private StringBuilder mBuilder = new StringBuilder();

    private int mPasswordLenth = 6;
    private int mPasswordTextSize;
    private int mPasswordTextColor;
    private int mPasswordBackgroud = R.drawable.shape_rectagle_border_gray2;
    private int mPasswordBackgroudColor;
    private int mPasswordDividerColor;
    private int mPasswordDividerWidth;
    private boolean mIsDividerShow = true;


    public PasswordInputView(Context context) {
        this(context, null);
    }

    public PasswordInputView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PasswordInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initAttrs(attrs);
        init();
    }

    private void init() {
        density = mContext.getResources().getDisplayMetrics().density;
        setBackgroundColor(mPasswordBackgroudColor);
        addVisibleText();
        addHintEditText();
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray ta = null;
        try {
            ta = mContext.obtainStyledAttributes(attrs, R.styleable.PasswordInputView);
            mPasswordLenth = ta.getInt(R.styleable.PasswordInputView_passwordLenth, 6);
            mPasswordTextSize = ta.getDimensionPixelSize(R.styleable.PasswordInputView_passwordTextSize
                    , (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 18, mContext.getResources().getDisplayMetrics()));
            mPasswordTextColor = ta.getColor(R.styleable.PasswordInputView_passwordTextColor
                    , mContext.getResources().getColor(R.color.house_detail_text_black));
            mPasswordBackgroud = ta.getResourceId(R.styleable.PasswordInputView_passwordBackgroud
                    , R.drawable.shape_rectagle_border_gray2);
            mPasswordBackgroudColor = ta.getColor(R.styleable.PasswordInputView_passwordBackgroudColor
                    , mContext.getResources().getColor(R.color.white));
            mPasswordDividerColor = ta.getColor(R.styleable.PasswordInputView_passwordDividerColor
                    , mContext.getResources().getColor(R.color.colorGray_dddddd));
            mPasswordDividerWidth = ta.getDimensionPixelSize(R.styleable.PasswordInputView_passwordDividerWidth
                    , (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, mContext.getResources().getDisplayMetrics()));
            mIsDividerShow = ta.getBoolean(R.styleable.PasswordInputView_isDividerShow, true);
        }finally {
            if (ta!=null) {
                ta.recycle();
            }
        }
    }

    private void addHintEditText() {
        final EditText et = new EditText(mContext);
        et.setCursorVisible(false);
        et.setTextSize(1);
        et.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mPasswordLenth)});
        int transparentColor = mContext.getResources().getColor(R.color.transparent,null);
        et.setTextColor(transparentColor);
        et.setBackgroundColor(transparentColor);
        et.setLayoutParams(getMatchParentParams());
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()>mPasswordLenth) {
                    et.setText(s.subSequence(0,mPasswordLenth));
                    return;
                }
                mBuilder.delete(0,mBuilder.length());
                mBuilder.append(s.toString());
                showText();
            }
        });

        addView(et);
    }

    private void showText() {
        for(int i=0; i<mPasswordLenth; i++) {
            String str = null;
            if (mBuilder.length()>i) {
                str = mBuilder.charAt(i) + "";
            }else {
                str = "";
            }
            String curStr = mPasswordList.get(i).toString();
            if (!str.equals(curStr)) {
                mPasswordList.get(i).setText(str);
            }
        }
    }

    private void addVisibleText() {
        LinearLayout ll = new LinearLayout(mContext);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setGravity(Gravity.CENTER);
        ll.setLayoutParams(getMatchParentParams());
        mPasswordList.clear();
        ll.setBackgroundResource(mPasswordBackgroud);
        for(int i=0; i<mPasswordLenth; i++) {
            TextView tv = getPasswordTextView();
            ll.addView(tv);
            mPasswordList.add(tv);

            if (mIsDividerShow && i!=mPasswordLenth-1) {
                ll.addView(getDivider());
            }
        }

        addView(ll);
    }

    private TextView getPasswordTextView() {
        TextView tv = new TextView(mContext);
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(mPasswordTextColor);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX,mPasswordTextSize);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.weight = 1;
        tv.setLayoutParams(lp);
        return tv;
    }

    private View getDivider() {
        View view = new View(mContext);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(mPasswordDividerWidth, ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(lp);
        view.setBackgroundColor(mPasswordDividerColor);
        return view;
    }

    private RelativeLayout.LayoutParams getMatchParentParams() {
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return lp;
    }


}
