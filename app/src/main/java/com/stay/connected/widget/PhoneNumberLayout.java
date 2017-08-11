package com.stay.connected.widget;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.lamudi.phonefield.PhoneInputLayout;
import com.stay.connected.R;

/**
 * Created by karthikeyan on 11/8/17.
 */

public class PhoneNumberLayout extends PhoneInputLayout {

    public PhoneNumberLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PhoneNumberLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public PhoneNumberLayout(Context context, String countryCode) {
        super(context);
        init();
    }

    private void init() {
        setTextColor(ContextCompat.getColor(getContext(), R.color.color_white));
    }

}
