package com.wxy.miuitoast;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class MiUI8Toast {
    private static boolean mIsShow = false;
    private View mView;
    private Toast mToast;
    private Method hide;
    private Method show;
    private Object mTN;

    @SuppressLint("ShowToast")
    private MiUI8Toast(Context context) {
        if (mToast == null) {
            mToast = Toast.makeText(context, null, Toast.LENGTH_SHORT);
        }
        mView = LayoutInflater.from(context).inflate(R.layout.park_pay_notifi, null);
    }

    static MiUI8Toast makeText(Context context) {
        if (mIsShow) {
            return null;
        }
        return new MiUI8Toast(context);
    }

    void show() {
        if (mIsShow) return;
        initView(mView);
        mToast.setView(mView);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        initTN();
        try {
            show.invoke(mTN);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        mIsShow = true;
    }

    private void initView(View mToastView) {
        Button cancel = (Button) mToastView.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
                mIsShow = false;
            }
        });
    }

    private void hide() {
        if (!mIsShow) return;
        try {
            hide.invoke(mTN);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        mIsShow = false;
    }

    private void initTN() {
        try {
            Field field = mToast.getClass().getDeclaredField("mTN");
            field.setAccessible(true);
            mTN = field.get(mToast);
            show = mTN.getClass().getMethod("show");
            hide = mTN.getClass().getMethod("hide");
            Field tnParamsField = mTN.getClass().getDeclaredField("mParams");
            tnParamsField.setAccessible(true);
            WindowManager.LayoutParams params = (WindowManager.LayoutParams) tnParamsField.get(mTN);
            params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            params.gravity = Gravity.TOP;
            params.y = 0;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            params.width = WindowManager.LayoutParams.WRAP_CONTENT;
            params.format = PixelFormat.TRANSLUCENT;
            Field tnNextViewField = mTN.getClass().getDeclaredField("mNextView");
            tnNextViewField.setAccessible(true);
            tnNextViewField.set(mTN, mToast.getView());
        } catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
