package com.king.cmccdrama.Activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.king.cmccdrama.Const.Config;
import com.king.cmccdrama.Const.MySharedPre;
import com.king.cmccdrama.Interface.APIManager;
import com.king.cmccdrama.R;
import com.king.cmccdrama.Utils.ImageLoadOptions;
import com.king.cmccdrama.Utils.Utils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import java.util.ArrayList;
import java.util.List;

import static android.view.KeyEvent.KEYCODE_DPAD_CENTER;
import static android.view.KeyEvent.KEYCODE_ENTER;

public class VodCategoryActivity extends BaseActivity {

    /** 图片布局
     *   1   2
     *  3 4 5 6
     */
    private ImageView imageVideo_1, imageVideo_2, imageVideo_3,
            imageVideo_4, imageVideo_5,imageVideo_6;
    private TextView category_text_1,category_text_2,category_text_3,category_text_4,
            category_text_5,category_text_6;
    private Button buttonUp,buttonDown;
    private TextView categoryPageIndex,category_video_total,category_page_count;

    Integer[] ImageIdList;
    ImageView[] ImageObjList;
    //
    Integer[] CategoryTitleIDList;
    TextView[] CategoryTitleList;
    List<Integer> videoList;
    //
    private int PAGE_COUNT = 6;
    private int categoryID = 6; // 6京剧,7晋剧,8越调,9粤剧,10湘剧,11相声
    private int currPage = 0; // 0为第一页
    private int totalCount = 0; // 总记录数
    private int maxPageSize = 0; // 总共多少页
    //
    private ImageLoader imageLoader = ImageLoader.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vod_category);
        //
        initView();
        initData();
        setClick();
    }

    private void initView() {
        buttonUp = findViewById(R.id.category_button_up);
        buttonDown = findViewById(R.id.category_button_down);
        categoryPageIndex = findViewById(R.id.category_page_index);
        category_video_total = findViewById(R.id.category_video_total);
        category_page_count = findViewById(R.id.category_page_count);
        //
        CategoryTitleIDList = new Integer[] {
                R.id.category_text_1,R.id.category_text_2,R.id.category_text_3,
                R.id.category_text_4,R.id.category_text_5,R.id.category_text_6
        };
        CategoryTitleList = new TextView[] {
                category_text_1,category_text_2,category_text_3,
                category_text_4,category_text_5,category_text_6
        };
        for(int i = 0; i < 6; i++) {
            CategoryTitleList[i] = findViewById(CategoryTitleIDList[i]);
            CategoryTitleList[i].setOnFocusChangeListener(this);
        }
        //
        ImageIdList = new Integer[] {
                R.id.imageVideo_1,R.id.imageVideo_2,R.id.imageVideo_3,
                R.id.imageVideo_4,R.id.imageVideo_5,R.id.imageVideo_6,
        };
        ImageObjList = new ImageView[] {
                imageVideo_1,imageVideo_2,imageVideo_3,imageVideo_4,imageVideo_5,imageVideo_6
        };
        for(int i = 0; i < 6; i++) {
            ImageObjList[i] = findViewById(ImageIdList[i]);
            ImageObjList[i].setOnClickListener(this);
        }
    }

    private void initData() {
        ImageLoader.getInstance().clearDiskCache();
        ImageLoader.getInstance().clearMemoryCache();
        getVodData(currPage);
    }

    private void getVodData(int pageIndex) {
        APIManager.getSingleton().getCategoryVodList(respHandler,categoryID,pageIndex,PAGE_COUNT);
    }

    private void setClick() {
        buttonUp.setOnClickListener(this);   // 上一页
        buttonDown.setOnClickListener(this); // 下一页
    }

    @Override
    public void onClick(View view) {
        int videoID;
        for (int i = 0; i < videoList.size(); i++) {
            videoID = videoList.get(i);
            if (ImageObjList[i].hasFocus()) {
                MySharedPre.setBackUrl(this,"category");
                startActivity(VodDetailActivity.class, setNameBundle("videoID", String.valueOf(videoID)));
                finish();
            }
        }
        switch (view.getId()) {
            case R.id.category_button_up: // 上一页
                currPage -= 1;
                if (currPage <= 0) {
                    currPage = 0;
                }
                //categoryPageIndex.setText(String.valueOf(currPage + 1));
                getVodData(currPage);
                break;
            case R.id.category_button_down: // 下一页
                currPage += 1;
                if (currPage + 1 > maxPageSize) {
                    currPage = maxPageSize;
                }
                //categoryPageIndex.setText(String.valueOf(currPage + 1));
                getVodData(currPage);
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i(Config.PROJECT_TAG,"keycode:" + keyCode);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (hasFocus) {
            switch (view.getId()) {
                case R.id.category_text_1: // 6京剧,7晋剧,8越调,9粤剧,10湘剧,11相声
                    categoryID = 6;
                    currPage = 0;
                    getVodData(currPage);
                    break;
                case R.id.category_text_2:
                    categoryID = 7;
                    currPage = 0;
                    getVodData(currPage);
                    break;
                case R.id.category_text_3:
                    categoryID = 8;
                    currPage = 0;
                    getVodData(currPage);
                    break;
                case R.id.category_text_4:
                    categoryID = 9;
                    currPage = 0;
                    getVodData(currPage);
                    break;
                case R.id.category_text_5:
                    categoryID = 10;
                    currPage = 0;
                    getVodData(currPage);
                    break;
                case R.id.category_text_6:
                    categoryID = 11;
                    currPage = 0;
                    getVodData(currPage);
                    break;
            }
        }
    }

    private void setCategoryHigh(int categoryID, int status) {
        for(int i = 0; i < 6; i++) {
            CategoryTitleList[i].setTextColor(Color.parseColor("#f1f1f1"));
        }
        switch (categoryID) {
            case 6: // 6京剧,7晋剧,8越调,9粤剧,10湘剧,11相声
                CategoryTitleList[0].setTextColor(Color.parseColor("#fbdb72"));
                if (status == 0) {
                    CategoryTitleList[0].setVisibility(View.INVISIBLE);
                }
                break;
            case 7:
                CategoryTitleList[1].setTextColor(Color.parseColor("#fbdb72"));
                if (status == 0) {
                    CategoryTitleList[1].setVisibility(View.INVISIBLE);
                }
                break;
            case 8:
                CategoryTitleList[2].setTextColor(Color.parseColor("#fbdb72"));
                if (status == 0) {
                    CategoryTitleList[2].setVisibility(View.INVISIBLE);
                }
                break;
            case 9:
                CategoryTitleList[3].setTextColor(Color.parseColor("#fbdb72"));
                if (status == 0) {
                    CategoryTitleList[3].setVisibility(View.INVISIBLE);
                }
                break;
            case 10:
                CategoryTitleList[4].setTextColor(Color.parseColor("#fbdb72"));
                if (status == 0) {
                    CategoryTitleList[4].setVisibility(View.INVISIBLE);
                }
                break;
            case 11:
                CategoryTitleList[5].setTextColor(Color.parseColor("#fbdb72"));
                if (status == 0) {
                    CategoryTitleList[5].setVisibility(View.INVISIBLE);
                }
                break;
        }
    }

    AsyncHttpResponseHandler respHandler = new TextHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            String video_poster_img;
            int videoId;
            try {
                JSONObject response = new JSONObject(responseString);
                Log.i(Config.PROJECT_TAG,response.toString());
                int result = response.getInt("Result");
                totalCount = Integer.valueOf(response.getInt("Total"));
                if (totalCount != 0) {
                    maxPageSize = totalCount / PAGE_COUNT;
                    if ((totalCount % PAGE_COUNT) == 0) {
                        maxPageSize = totalCount / PAGE_COUNT;
                    } else {
                        maxPageSize = totalCount / PAGE_COUNT + 1;
                    }
                    //
                    categoryPageIndex.setText(String.valueOf(currPage + 1)); // 当前第几页
                    category_video_total.setText(String.valueOf(totalCount)); // 共有多少部视频
                    category_page_count.setText(String.valueOf(maxPageSize)); // 共多少页
                    if (result == Config.API_RESULT_OK) {
                        videoList = new ArrayList<>();
                        JSONArray data = response.getJSONArray("Data");
                        int count = data.length();
                        for (int i = 0; i < count; i++) {
                            JSONObject value = data.getJSONObject(i);
                            Log.i(Config.PROJECT_TAG,"category list:" + value.toString());
                            video_poster_img = value.getString("video_poster_img");
                            videoId = Integer.valueOf(value.getString("video_id"));
                            int categoryID = value.getInt("category_id");
                            int categoryStatus = value.getInt("category_status");
                            setCategoryHigh(categoryID,categoryStatus);
                            if (!Utils.isBlank(video_poster_img)) {
                                ImageObjList[i].setVisibility(View.VISIBLE);
                                imageLoader.displayImage(
                                        video_poster_img, ImageObjList[i], ImageLoadOptions.getDisplayImageOptions()); // 加载网络图片
                                videoList.add(videoId);
                            }
                        }
                        if (count != PAGE_COUNT) {
                            for (int i = count; i < PAGE_COUNT; i++) {
                                ImageObjList[i].setVisibility(View.INVISIBLE);
                            }
                        }
                    }
                }
            } catch (org.json.JSONException ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {}
    };
}
