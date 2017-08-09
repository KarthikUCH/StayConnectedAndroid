package com.stay.connected.widget;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stay.connected.R;

import java.lang.reflect.Field;

/**
 * Created by karthikeyan on 8/8/17.
 *
 * @see <a href "https://stackoverflow.com/a/42578667/2790197">Reference</a>
 */

public class AppTextInputLayout extends TextInputLayout {
    public AppTextInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setErrorEnabled(boolean enabled) {
        super.setErrorEnabled(enabled);

        if (!enabled) {
            return;
        }

        try {
            Field errorViewField = TextInputLayout.class.getDeclaredField("mErrorView");
            errorViewField.setAccessible(true);
            TextView errorView = (TextView) errorViewField.get(this);
            if (errorView != null) {
                errorView.setTextColor(ContextCompat.getColor(getContext(), R.color.color_accent));
                errorView.setGravity(Gravity.RIGHT);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.gravity = Gravity.END;
                errorView.setLayoutParams(params);
            }
        } catch (Exception e) {
            // At least log what went wrong
            e.printStackTrace();
        }
    }
}
