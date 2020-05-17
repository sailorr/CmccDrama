package com.king.cmccdrama.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.king.cmccdrama.R;

public class PromotionActivity extends BaseActivity {
    /* 图片布局
     *  1 2
     *  3 4
     */
    private ImageView imageView1,imageView2,imageView3,imageView4;
    private ImageView layoutNew, layoutNotice, layoutHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion);
        //
        initView();
        setClick();
    }

    private void initView() {
        imageView1 = findViewById(R.id.promo_image1);
        imageView2 = findViewById(R.id.promo_image2);
        imageView3 = findViewById(R.id.promo_image3);
        imageView4 = findViewById(R.id.promo_image4);
        layoutNew = findViewById(R.id.promo_layout_new);
        layoutNotice = findViewById(R.id.promo_layout_notice);
        layoutHistory = findViewById(R.id.promo_layout_history);
    }

    private void setClick() {
        // 监听焦点变化
        layoutNew.setOnFocusChangeListener(this);
        layoutNotice.setOnFocusChangeListener(this);
        layoutHistory.setOnFocusChangeListener(this);
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (hasFocus) {
            switch (view.getId()) {
                case R.id.promo_layout_new:     // 活动预告
                    imageView1.setImageResource(R.drawable.temp_category_img1);
                    imageView2.setImageResource(R.drawable.temp_category_img2);
                    imageView3.setImageResource(R.drawable.temp_category_img3);
                    imageView4.setImageResource(R.drawable.temp_category_img4);
                    break;
                case R.id.promo_layout_notice:  // 活动公告
                    imageView1.setImageResource(R.drawable.temp_category_img5);
                    imageView2.setImageResource(R.drawable.temp_category_img6);
                    imageView3.setImageResource(R.drawable.temp_main_hb01);
                    imageView4.setImageResource(R.drawable.temp_main_hb02);
                    break;
                case R.id.promo_layout_history: // 历史活动
                    imageView1.setImageResource(R.drawable.temp_main_hb03);
                    imageView2.setImageResource(R.drawable.temp_main_hb04);
                    imageView3.setImageResource(R.drawable.temp_category_img1);
                    imageView4.setImageResource(R.drawable.temp_category_img2);
                    break;
            }
        }
    }
}
