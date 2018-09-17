package com.bbk.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bbk.activity.BidHomeActivity;
import com.bbk.activity.BidListDetailActivity;
import com.bbk.activity.BidMyListDetailActivity;
import com.bbk.activity.BrowseActivity;
import com.bbk.activity.CollectionActivity;
import com.bbk.activity.MyApplication;
import com.bbk.activity.MyCoinActivity;
import com.bbk.activity.R;
import com.bbk.activity.UserAccountActivity;
import com.bbk.activity.UserLoginNewActivity;
import com.bbk.flow.DataFlow6;
import com.bbk.flow.ResultEvent;
import com.bbk.util.ImmersionUtil;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.view.CircleImageView1;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * 我的_01_首页
 */

public class BidUserFragment extends BaseViewPagerFragment implements ResultEvent ,View.OnClickListener{
    private View mView;
    private DataFlow6 dataFlow;
    private LinearLayout mmybid,mmyfabid;
    private RelativeLayout newpinglun;
    private RelativeLayout mshenhe,mjie,mpl,mcomplete,mbidjie,mbidpl,mbidcomplete;
    private TextView mshenhenum,mjietext,mpltext,mbidjietext,mbidpltext,mnewmsg,musername,mcollectnum,mfootnum;
    private ImageView muserimg;
    private LinearLayout  mcollection, mfoot;
    private View data_head;
    public static int mMessage;
    private String LogFlag;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // return super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.fragment_bid_user, null);
//        data_head = mView.findViewById(R.id.data_head);
//        ImmersionUtil.initstateView(getActivity(),data_head);
        dataFlow = new DataFlow6(getActivity());
        initView();
        initData();
        return mView;
    }
    public void initView(){
        mcollectnum =  mView.findViewById(R.id.mcollectnum);
        mfootnum =  mView.findViewById(R.id.mfootnum);
        String footprint = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(),"userInfor", "footprint");
        String collect = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(),"userInfor", "collect");
        mcollectnum.setText(collect);
        mfootnum.setText(footprint);
        mcollection = mView.findViewById(R.id.mcollection);
        mfoot = mView.findViewById(R.id.mfoot);
        mmybid = mView.findViewById(R.id.mmybid);
        mmyfabid =  mView.findViewById(R.id.mmyfabid);
        newpinglun = mView.findViewById(R.id.newpinglun);
        mshenhe =  mView.findViewById(R.id.mshenhe);
        mjie =  mView.findViewById(R.id.mjie);
        mpl =  mView.findViewById(R.id.mpl);
        mcomplete =  mView.findViewById(R.id.mcomplete);
        mbidjie =  mView.findViewById(R.id.mbidjie);
        mbidpl =  mView.findViewById(R.id.mbidpl);
        mbidcomplete =  mView.findViewById(R.id.mbidcomplete);
        mshenhenum =  mView.findViewById(R.id.mshenhenum);
        mjietext =  mView.findViewById(R.id.mjietext);
        mpltext =  mView.findViewById(R.id.mpltext);
        mbidjietext = mView.findViewById(R.id.mbidjietext);
        mbidpltext =  mView.findViewById(R.id.mbidpltext);
        mnewmsg =  mView.findViewById(R.id.mnewmsg);
        musername =  mView.findViewById(R.id.musername);
        muserimg =  mView.findViewById(R.id.muserimg);

        mcollection.setOnClickListener(this);
        mfoot.setOnClickListener(this);
        mmybid.setOnClickListener(this);
        mmyfabid.setOnClickListener(this);
        mshenhe.setOnClickListener(this);
        mjie.setOnClickListener(this);
        mpl.setOnClickListener(this);
        mcomplete.setOnClickListener(this);
        mbidjie.setOnClickListener(this);
        mbidpl.setOnClickListener(this);
        musername.setOnClickListener(this);
        muserimg.setOnClickListener(this);
        newpinglun.setOnClickListener(this);
        mbidcomplete.setOnClickListener(this);

}
    public void initData(){
        HashMap<String, String> paramsMap = new HashMap<>();
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(),"userInfor", "userID");
        paramsMap.put("userid",userID);
        dataFlow.requestData(1, "bid/queryMyBiaoMsg", paramsMap, this,false);
    }


    @Override
    protected void loadLazyData() {

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(),"userInfor", "userID");
        switch (v.getId()){
            case R.id.mmyfabid:
                if (TextUtils.isEmpty(userID)) {
                    LogFlag = "1";
                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    intent = new Intent(getActivity(), BidListDetailActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.mshenhe:
                LogFlag = "2";
                mJieBiaoDetail(userID,"1");
                break;
            case R.id.mjie:
                LogFlag = "3";
                mJieBiaoDetail(userID,"2");
                break;
            case R.id.mpl:
                LogFlag = "4";
                mJieBiaoDetail(userID,"3");
                break;
            case R.id.mcomplete: LogFlag = "5";
                mJieBiaoDetail(userID,"4");
                break;
            case R.id.mmybid:
                if (TextUtils.isEmpty(userID)) {
                    LogFlag = "6";
                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    intent = new Intent(getActivity(), BidMyListDetailActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.mbidjie:
                LogFlag = "7";
                mDetail(userID,"1");
                break;
            case R.id.mbidpl:
                LogFlag = "8";
                mDetail(userID,"2");
                break;
            case R.id.mbidcomplete:
                LogFlag = "9";
                mDetail(userID,"3");
                break;
            case R.id.muserimg:
                LogFlag = "10";
                islogin();
                break;
            case R.id.musername:
                LogFlag = "11";
                islogin();
                break;
            case R.id.newpinglun:
                if (TextUtils.isEmpty(userID)) {
                    LogFlag = "12";
                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                }else {
                    BidHomeActivity.initThree();
                }
                break;
            case R.id.mcollection:
                if (TextUtils.isEmpty(userID)) {
                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    intent = new Intent(getActivity(), CollectionActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.mfoot:
                intent = new Intent(getActivity(), BrowseActivity.class);
                startActivity(intent);
                break;

        }
    }
    //接镖点击事件
    private void mJieBiaoDetail(String userid ,String value){
        Intent intent;
        if (TextUtils.isEmpty(userid)) {
            intent = new Intent(getActivity(), UserLoginNewActivity.class);
            startActivityForResult(intent, 1);
        } else {
            intent = new Intent(getActivity(), BidListDetailActivity.class);
            intent.putExtra("status",value);
            startActivity(intent);
        }
    }
    //发镖点击事件
    private void mDetail(String userid ,String value){
        Intent intent;
        if (TextUtils.isEmpty(userid)) {
            intent = new Intent(getActivity(), UserLoginNewActivity.class);
            startActivityForResult(intent, 1);
        } else {
            intent = new Intent(getActivity(), BidMyListDetailActivity.class);
            intent.putExtra("status",value);
            startActivity(intent);
        }
    }
    public void islogin(){
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(),"userInfor", "userID");
        if (TextUtils.isEmpty(userID)){
            Intent intent = new Intent(getActivity(), UserLoginNewActivity.class);
            startActivity(intent);
        }else {
            Intent intent = new Intent(getActivity(), UserAccountActivity.class);
            startActivity(intent);
        }
    }
    public void setnum(TextView textView,String num){
        if ("0".equals(num)){
            textView.setVisibility(View.GONE);
        }else {
            textView.setText(num);
            textView.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onResultData(int requestCode, String api, JSONObject dataJo, String content){
        try {
            String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(),"userInfor", "userID");
            if (TextUtils.isEmpty(userID)){
                musername.setText("请登录");
//                CircleImageView1.getImg(getActivity(),R.mipmap.logo_01,muserimg);
                mshenhenum.setVisibility(View.GONE);
                mnewmsg.setVisibility(View.GONE);
            }else {
                String imgUrl = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(),"userInfor", "imgUrl");
                String nickname = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(),"userInfor", "nickname");
                musername.setText(nickname);
                mnewmsg.setVisibility(View.VISIBLE);
                mshenhenum.setVisibility(View.VISIBLE);
                CircleImageView1.getImg(getActivity(),imgUrl,muserimg);
                JSONObject object = new JSONObject(content);
                setnum(mshenhenum,object.optString("shenhe"));
                setnum(mjietext,object.optString("jie"));
                setnum(mpltext,object.optString("pl"));
                setnum(mbidjietext,object.optString("bidjie"));
                setnum(mbidpltext,object.optString("bidpl"));
                setnum(mnewmsg,object.optString("sysmsg"));
                BidUserFragment.mMessage = object.optInt("sysmsg");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(),"userInfor", "userID");
        Intent intent;
        if (userID != null && !userID.equals("")) {
            switch (requestCode) {
                case 1:
                    switch (LogFlag) {
                        case "1":
                            intent = new Intent(getActivity(), BidListDetailActivity.class);
                            startActivity(intent);
                            break;
                        case "2":
                            mJieBiaoDetail(userID, "1");
                            break;
                        case "3":
                            mJieBiaoDetail(userID, "2");
                            break;
                        case "4":
                            mJieBiaoDetail(userID, "3");
                            break;
                        case "5":
                            mJieBiaoDetail(userID, "4");
                            break;
                        case "6":
                            intent = new Intent(getActivity(), BidMyListDetailActivity.class);
                            startActivity(intent);
                            break;
                        case "7":
                            mDetail(userID, "1");
                            break;
                        case "8":
                            mDetail(userID, "2");
                            break;
                        case "9":
                            mDetail(userID, "3");
                            break;
                        case "10":
                            break;
                        case "11":
                            break;
                        case "12":
                            BidHomeActivity.initThree();
                            break;
                    }
                    break;
            }
        }
    }
}
