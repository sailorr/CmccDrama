package com.king.cmccdrama.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.graphics.Color;
import android.widget.TextView;
import android.view.Gravity;

import com.king.cmccdrama.Widget.FocusLayout;

public abstract class BaseActivity extends AppCompatActivity
        implements View.OnFocusChangeListener, View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        // 绑定UI焦点变化事件
        FocusLayout mFocusLayout = new FocusLayout(this);
        focusBindListener(mFocusLayout);
    }

    /**
     * 绑定焦点变化事件
     *
     * @param focusLayout
     */
    private void focusBindListener(FocusLayout focusLayout) {
        View mContainerView = this.getWindow().getDecorView();
        ViewTreeObserver viewTreeObserver = mContainerView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalFocusChangeListener(focusLayout);
        addContentView(focusLayout,
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));//添加焦点层
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
    }

    @Override
    public void onClick(View view) {
    }

    /**
     * 页面跳转
     *
     * @param clz
     */
    protected void startActivity(Class<?> clz) {
        startActivity(new Intent(BaseActivity.this, clz));
    }

    /**
     * 携带数据的页面跳转
     *
     * @param clz
     * @param bundle
     */
    protected void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * 携带数据打开页面带返回值
     *
     * @param cls
     * @param bundle
     * @param requestCode
     */
    protected void startActivityForResult(Class<?> cls, Bundle bundle,
                                          int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * 打开页面带返回值
     *
     * @param cls
     * @param requestCode
     */
    protected void startActivityForResult(Class<?> cls,
                                          int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void finish() {
        super.finish();
    }

    /**
     * 显示简单toast
     *
     * @param msg
     */
    protected void showToast(String msg) {
        //Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        Toast toast = new Toast(this);
        TextView view = new TextView(this);
        view.setBackgroundResource(android.R.color.black);
        view.setTextSize(28);
        view.setTextColor(Color.WHITE);
        view.setText(msg);
        view.setPadding(10, 10, 10, 10);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(view);
        toast.show();
    }

    /**
     * 参数传递，VOD文件名
     *
     * @param name
     * @return
     */
    protected Bundle setVideoNameBundle(String name) {
        Bundle bundle = new Bundle();
        bundle.putString("videoName", name);
        return bundle;
    }

    protected Bundle setNameBundle(String key, String value) {
        Bundle bundle = new Bundle();
        bundle.putString(key, value);
        return bundle;
    }

    /*
    private void test() {
        Log.i("9999999999", "onCreate: " + myContext.getResources().getDisplayMetrics().density);
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels; // 屏幕宽度（像素）
        int height = metric.heightPixels; // 屏幕高度（像素）
        float density = metric.density; // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = metric.densityDpi; // 屏幕密度DPI（120 / 160 / 240）
        String info = "手机型号: " + android.os.Build.MODEL + ",\nSDK版本:" + android.os.Build.VERSION.SDK + ",\n系统版本:"
                + android.os.Build.VERSION.RELEASE + "\n屏幕宽度（像素）: " + width + "\n屏幕高度（像素）: " + height + "\n屏幕密度:  " + density + "\n屏幕密度DPI: " + densityDpi;
        Log.d("system enviriment", info);
    }
    */
}
