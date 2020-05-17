package com.king.cmccdrama.Activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.king.cmccdrama.Const.Config;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import com.king.cmccdrama.Interface.APIManager;
import com.king.cmccdrama.R;

public class PersonalActivity extends BaseActivity {

    private LinearLayout layoutWin,layoutDesc,layoutOther,layoutFav,layoutService;
    private TextView textTitle, textContent, personal_sign_days;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        //
        initView();
        initData();
        setClick();
    }

    private void initView() {
        layoutWin = findViewById(R.id.person_layout_win);          // 获奖通知
        layoutDesc = findViewById(R.id.person_layout_desc);        // 兑奖说明
        layoutOther = findViewById(R.id.person_layout_other);      // 领奖攻略
        layoutFav = findViewById(R.id.person_layout_fav);          // 我的收藏
        layoutService = findViewById(R.id.person_layout_service);  // 积分服务
        textTitle = findViewById(R.id.person_text_title);
        textContent = findViewById(R.id.person_text_content);
        personal_sign_days = findViewById(R.id.personal_sign_days); // 签到天数
        //
        layoutFav.setVisibility(View.GONE); // TODO，隐藏了
    }

    private void initData() {
        com.shcmcc.tools.GetSysInfo sysinfo = com.shcmcc.tools.GetSysInfo.getInstance(
                "10086", "", this);
        String mobile = sysinfo.getEpgAccountIdentity();
        APIManager.getSingleton().userInfo(respHandler, mobile);
    }

    private void setClick() {
        // 监听焦点变化
        layoutWin.setOnFocusChangeListener(this);
        layoutDesc.setOnFocusChangeListener(this);
        layoutOther.setOnFocusChangeListener(this);
        layoutFav.setOnFocusChangeListener(this);
        layoutService.setOnFocusChangeListener(this);
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (hasFocus) {
            switch (view.getId()) {
                case R.id.person_layout_win: // 获奖通知
                    layoutWin.setBackgroundResource(R.drawable.person_btn_bg);
                    //
                    textTitle.setText("获奖通知");
                    textContent.setText("尊敬的客户：您好！\r\n中国移动提示您在中国移动-芒果TV/未来电视进行的抽奖活动中，获得贝拉小镇门票一张，有效期至2019年1月31日。请您尽快在中国移动-芒果TV/未来电视端的短信中心查看。凭短信中获奖码在贝拉小镇门票站换取门票。更多信息点击http://dx.10086.cn/U3MZFnu 祝您使用愉快。【中国移动 和你一起】");
                    // posterImage.setImageResource(R.drawable.temp_personal_image1);
                    // posterImage.setMaxWidth(520);
                    // posterImage.setMaxWidth(200);
                    break;
                case R.id.person_layout_desc: // 兑奖说明
                    layoutDesc.setBackgroundResource(R.drawable.person_btn_bg);
                    //
                    textTitle.setText("兑奖说明");
                    textContent.setText("1、\t仅限湖南移动手机用户领取，每次领取可100%获得奖品。\n" +
                            "2、\t领奖攻略：\n" +
                            "（1）\t同一用户，活动期间每天可登陆领奖一次，节假日登陆加赠一次领奖机会，此类领奖机会使有效期至当日24时。\n" +
                            "（2）\t湖南移动新入网用户可领奖一次，领奖机会使用有效期至3月31日24时\n" +
                            "（3）\t用户参加指定活动可领奖一次，此类领奖机会有效至3月31日24时\n" +
                            "（4）\t用户可通过50积分换取一次领奖机会\n" +
                            "3、\t用户获得的奖品，需在奖品的有效期内使用，具体使用方式及相关规则将以下短信下发给用户，用户也可在领奖记录中查看奖品明细及使用期限。\n" +
                            "4、\t本次活动发放的购物券均为中国移动和包电子券，可在湖南移动线上线下合作商户使用。\n" +
                            "5、\t用户获得的奖品仅限本人使用，其中获赠流量为国内通用流量（不包含港澳台），当月话费面单特权最高面单额为188元，所有奖品不可转赠与共享。\n" +
                            "6、\t本活动最终解释权归湖南移动所有。\n");
                    // posterImage.setImageResource(R.drawable.temp_personal_image1);
                    // posterImage.setMaxWidth(520);
                    // posterImage.setMaxWidth(200);
                    break;
                case R.id.person_layout_other: // 其他消息
                    layoutOther.setBackgroundResource(R.drawable.person_btn_bg);
                    textTitle.setText("其他消息");
                    textContent.setText("尊敬的移动用户\n" +
                            "温馨提示：您在中国移动-芒果TV/未来电视端有一份中奖奖品将于2019年1月31日过期，请您尽快去兑换地兑换该活动奖品！过期该中奖短信无效。\n" +
                            "最终解释权归中国移动公司所有。\n");
                    // posterImage.setImageResource(R.drawable.temp_personal_image1);
                    // posterImage.setMaxWidth(520);
                    // posterImage.setMaxWidth(200);
                    break;
                case R.id.person_layout_fav:
                    layoutFav.setBackgroundResource(R.drawable.person_btn_bg);
                    //
                    textTitle.setText("");
                    textContent.setText("");
                    break;
                case R.id.person_layout_service:
                    textTitle.setText("积分服务");
                    textContent.setText("1、积分计划开通规则：\n" +
                            "2015年4月起，中国移动计划全新升级，只要您是中国移动的个人客户，就可参与中国移动积分计划。\n" +
                            "2、“和积分”组成：\n" +
                            "“和积分”由消费积分与促销回馈积分两个部分组成。\n" +
                            "（1）消费积分：是指根据您被评定的星级及当月话费账单金额（不含赠送减免部分）累积的积分。具体累积规则如下：\n" +
                            "（2）促销回馈积分：是指您通过参与营销活动等其他方式获得的积分。不论您是否被评定为星级客户，均可通过参与营销活动获得促销回馈积分，获得的积分值根据具体的营销活动来执行。\n" +
                            "3、“和积分”使用有效期规则：\n" +
                            "“和积分”中的消费积分有效期暂无限制，且无滚动清零,后续如有变更会提前通知您。\n" +
                            "“和积分”中的促销回馈积分有效期根据参与的营销活动规则执行，有使用有效期限制的促销回馈积分到期后将自动清零,请您在有效期内进行使用。\n");
                    // posterImage.setImageResource(R.drawable.temp_personal_image2);
                    // posterImage.setMaxWidth(379);
                    // posterImage.setMaxWidth(287);
                    //
                    layoutService.setBackgroundResource(R.drawable.person_btn_bg);
                    break;
            }
        } else {
            switch (view.getId()) {
                case R.id.person_layout_win:
                    layoutWin.setBackgroundResource(0);
                    break;
                case R.id.person_layout_desc:
                    layoutDesc.setBackgroundResource(0);
                    break;
                case R.id.person_layout_other:
                    layoutOther.setBackgroundResource(0);
                    break;
                case R.id.person_layout_fav:
                    layoutFav.setBackgroundResource(0);
                    break;
                case R.id.person_layout_service:
                    layoutService.setBackgroundResource(0);
                    break;
            }
        }
    }

    AsyncHttpResponseHandler respHandler = new TextHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
                Log.i(Config.PROJECT_TAG, responseString);
                JSONObject response = new JSONObject(responseString);
                int result = response.getInt("Result");
                if (result == Config.API_RESULT_OK) {
                    JSONArray data = response.getJSONArray("Data");
                    JSONObject value = data.getJSONObject(0);
                    personal_sign_days.setText(
                            "已连续签到" + value.getString("sign_days") + "天");
                }
            } catch (org.json.JSONException ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {}
    };
}
