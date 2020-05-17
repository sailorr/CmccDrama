package com.king.cmccdrama.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class PlayHistoryActivity extends BaseActivity {

    private ImageView imageView1, imageView2, imageView3, imageView4, imageView5,
            imageView6, imageView7, imageView8, imageView9;
    private TextView textView1,textView2,textView3,textView4,textView5,textView6,
            textView7,textView8,textView9;
    private LinearLayout historyLayout1,historyLayout2,historyLayout3,historyLayout4,
            historyLayout5,historyLayout6,historyLayout7,historyLayout8;
    private Button buttonUp,buttonDown;
    private TextView historyPageCount,historyVideoTotal,historyPageIndex;

    Integer[] ImageIdList;
    ImageView[] ImageObjList;
    Integer[] TextIdList;
    TextView[] TextObjList;
    Integer[] LayoutIdList;
    LinearLayout[] LayoutObjList;
    List<Integer> videoIDList;
    List<String> videoTitleList;

    private int currPage = 0; // 0为第一页
    private int totalCount = 0; // 总记录数
    private int maxPageSize = 0; // 总共多少页
    private int PAGE_COUNT = 8; // 每页多少
    //
    private ImageLoader imageLoader = ImageLoader.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_history);
        //
        initView();
        initData();
        setClick();
    }

    private void initView() {
        buttonUp = findViewById(R.id.history_button_up);
        buttonDown = findViewById(R.id.history_button_down);
        historyPageCount = findViewById(R.id.history_page_count);
        historyVideoTotal = findViewById(R.id.history_video_total);
        historyPageIndex = findViewById(R.id.history_page_index);
        //
        ImageIdList = new Integer[] {
                R.id.history_img_1,R.id.history_img_2,R.id.history_img_3,R.id.history_img_4,
                R.id.history_img_5,R.id.history_img_6,R.id.history_img_7,R.id.history_img_8
        };
        ImageObjList = new ImageView[] {
                imageView1,imageView2,imageView3,imageView4,imageView5,imageView6,
                imageView7,imageView8,imageView9
        };
        TextIdList = new Integer[] {
                R.id.history_text_1,R.id.history_text_2,R.id.history_text_3,R.id.history_text_4,
                R.id.history_text_5,R.id.history_text_6,R.id.history_text_7,R.id.history_text_8
        };
        TextObjList = new TextView[] {
                textView1,textView2,textView3,textView4,textView5,textView6,textView7,textView8
        };
        LayoutIdList = new Integer[] {
                R.id.history_layout_1,R.id.history_layout_2,R.id.history_layout_3,R.id.history_layout_4,
                R.id.history_layout_5,R.id.history_layout_6,R.id.history_layout_7,R.id.history_layout_8
        };
        LayoutObjList = new LinearLayout[] {
                historyLayout1,historyLayout2,historyLayout3,historyLayout4,
                historyLayout5,historyLayout6,historyLayout7,historyLayout8
        };
        for(int i = 0; i < PAGE_COUNT; i++) {
            ImageObjList[i] = findViewById(ImageIdList[i]);
            TextObjList[i] = findViewById(TextIdList[i]);
            LayoutObjList[i] = findViewById(LayoutIdList[i]);
            LayoutObjList[i].setOnClickListener(this);
        }
    }

    private void initData() {
        getVodData();
        /*
        historyVideoTotal.setText(String.valueOf(totalCount));
        historyPageCount.setText(String.valueOf(maxPageSize));
        historyPageIndex.setText(String.valueOf(currPage + 1));
         */
    }

    private void setClick() {
        buttonUp.setOnClickListener(this);   // 上一页
        buttonDown.setOnClickListener(this); // 下一页
    }

    @Override
    public void onClick(View view) {
        int videoID;
        for (int i = 0; i < videoIDList.size(); i++) {
            videoID = videoIDList.get(i);
            if (LayoutObjList[i].hasFocus()) {
                MySharedPre.setBackUrl(this,"history");
                startActivity(VodDetailActivity.class, setNameBundle("videoID", String.valueOf(videoID)));
                finish();
            }
        }
        switch (view.getId()) {
            case R.id.history_button_up: // 上一页
                currPage -= 1;
                if (currPage <= 0) {
                    currPage = 0;
                }
                getVodData();
                break;
            case R.id.history_button_down: // 下一页
                currPage += 1;
                if (currPage + 1 > maxPageSize) {
                    currPage = maxPageSize;
                }
                getVodData();
            default:
                break;
        }
    }

    private void getVodData() {
        com.shcmcc.tools.GetSysInfo sysinfo = com.shcmcc.tools.GetSysInfo.getInstance(
                "10086", "", this);
        String mobile = sysinfo.getEpgAccountIdentity();
        APIManager.getSingleton().userInfo(userInfoHandler, mobile);
    }

    int userID = 0;
    AsyncHttpResponseHandler userInfoHandler = new TextHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
                Log.i(Config.PROJECT_TAG, responseString);
                JSONObject response = new JSONObject(responseString);
                int result = response.getInt("Result");
                if (result == Config.API_RESULT_OK) {
                    JSONArray data = response.getJSONArray("Data");
                    JSONObject value = data.getJSONObject(0);
                    Log.i(Config.PROJECT_TAG,"value:" + value.toString());
                    userID = value.getInt("user_id");
                }
                APIManager.getSingleton().getPlayHistory(respHandler,userID,currPage,PAGE_COUNT);
            } catch (org.json.JSONException ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {}
    };

    AsyncHttpResponseHandler respHandler = new TextHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
                Log.i(Config.PROJECT_TAG, responseString);
                String video_poster_img;
                int videoId;
                String videoTitle;
                //
                JSONObject response = new JSONObject(responseString);
                Log.i(Config.PROJECT_TAG, response.toString());
                int result = response.getInt("Result");
                totalCount = Integer.valueOf(response.getInt("Total"));
                maxPageSize = totalCount / PAGE_COUNT;
                if (result == Config.API_RESULT_OK) {
                    videoIDList = new ArrayList<>();
                    videoTitleList = new ArrayList<>();
                    JSONArray data = response.getJSONArray("Data");
                    int count = data.length();
                    for (int i = 0; i < count; i++) {
                        JSONObject value = data.getJSONObject(i);
                        video_poster_img = value.getString("video_poster_img");
                        videoId = Integer.valueOf(value.getString("video_id"));
                        videoTitle = value.getString("video_name");
                        if (!Utils.isBlank(video_poster_img)) {
                            TextObjList[i].setText(videoTitle);
                            imageLoader.displayImage(
                                    video_poster_img, ImageObjList[i], ImageLoadOptions.getDisplayImageOptions()); // 加载网络图片
                            videoIDList.add(videoId);
                            LayoutObjList[i].setVisibility(View.VISIBLE);
                        }
                    }
                    Log.i(Config.PROJECT_TAG,"count:" + count);
                    Log.i(Config.PROJECT_TAG,"pageCount:" + PAGE_COUNT);
                    if (count != PAGE_COUNT) {
                        //int disable = count;
                        //Log.i(Config.PROJECT_TAG, "从几开始？：" + disable);
                        for (int i = count; i < PAGE_COUNT; i++) {
                            LayoutObjList[i].setVisibility(View.INVISIBLE);
                        }
                    }
                    historyVideoTotal.setText(String.valueOf(totalCount)); // 共多少部
                    if (totalCount == 0) {
                        historyPageIndex.setText(String.valueOf(currPage));// 当前第几页
                    } else {
                        historyPageIndex.setText(String.valueOf(currPage + 1));// 当前第几页
                    }
                    Log.i(Config.PROJECT_TAG, maxPageSize+"");
                    if (maxPageSize == 0) {
                        historyPageCount.setText("1"); // 共多少页
                    } else if (maxPageSize == 1) {
                        if (totalCount == PAGE_COUNT) {
                            historyPageCount.setText("1"); // 共多少页
                        } else {
                            historyPageCount.setText(String.valueOf(maxPageSize + 1));// 当前第几页
                        }
                    } else {
                        historyPageCount.setText(String.valueOf(maxPageSize + 1));// 当前第几页
                    }
                }
            } catch (org.json.JSONException ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {}
    };

    private String otherSplit(String original, int count) {

        StringBuilder sb = new StringBuilder();
        // 这儿是 count 和 i 两头并进
        // i 每次都 +1 ,每次都会至少找到1个字符（英文1个，中文2个）
        // 如果是中文字符，count 就-1
        // 对于半个中文
        for (int i = 0; i < count - 1; i++) {
            char c = original.charAt(i);
            if (String.valueOf(c).getBytes().length > 1) {
                count--;
            }
            sb.append(c);
        }
        return sb.toString();
    }
}
