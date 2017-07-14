package com.miqian.mq.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.miqian.mq.MyApplication;
import com.miqian.mq.R;
import com.miqian.mq.activity.AnnounceActivity;
import com.miqian.mq.activity.MainActivity;
import com.miqian.mq.activity.WebActivity;
import com.miqian.mq.activity.current.ActivityUserCurrent;
import com.miqian.mq.activity.rollin.IntoCheckAcitvity;
import com.miqian.mq.activity.rollin.IntoModeAcitvity;
import com.miqian.mq.activity.save.SaveAcitvity;
import com.miqian.mq.activity.setting.SettingActivity;
import com.miqian.mq.activity.user.LoginActivity;
import com.miqian.mq.activity.user.MyTicketActivity;
import com.miqian.mq.activity.user.RolloutActivity;
import com.miqian.mq.activity.user.UserMqbActivity;
import com.miqian.mq.activity.user.UserRecordActivity;
import com.miqian.mq.activity.user.UserRegularActivity;
import com.miqian.mq.entity.JpushInfo;
import com.miqian.mq.entity.MqResult;
import com.miqian.mq.entity.UserInfo;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.net.Urls;
import com.miqian.mq.utils.ExtendOperationController;
import com.miqian.mq.utils.FormatUtil;
import com.miqian.mq.utils.Pref;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.utils.UserUtil;
import com.miqian.mq.views.CustomDialog;
import com.miqian.mq.views.DialogTip;
import com.miqian.mq.views.MySwipeRefresh;
import com.umeng.analytics.MobclickAgent;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description:
 *
 * @author Jackie
 * @created 2015-3-18
 */

public class FragmentUser extends BasicFragment implements View.OnClickListener, MainActivity.RefeshDataListener, ExtendOperationController.ExtendOperationListener {
    @BindView(R.id.title)
    TextView tv_Title;
    @BindView(R.id.account_current)
    TextView tv_Current;
    @BindView(R.id.account_regular)
    TextView tv_Regular;
    @BindView(R.id.account_ticket)
    TextView tv_Ticket;
    @BindView(R.id.tv_question)
    TextView tvQuestion;
    @BindView(R.id.tv_balance)
    TextView tv_balance;//可用余额
    @BindView(R.id.tv_totalProfit)//总收益
            TextView tv_TotalProfit;
    @BindView(R.id.tv_totalasset)//总资产
            TextView tv_totalasset;
    @BindView(R.id.tv_ydayprofit)//昨日收益
            TextView tv_ydayprofit;
    @BindView(R.id.iv_qq)
    ImageView ivQQ;
    @BindView(R.id.bt_right)
    ImageView btn_setting;

    @BindView(R.id.bt_left)
    ImageView btn_message;
    @BindView(R.id.btn_eye)
    ImageButton btnEye;
    @BindView(R.id.bt_login)
    Button btnLogin;

    @BindView(R.id.frame_account_current)
    View frame_current;
    @BindView(R.id.frame_regular)
    View frame_regular;
    @BindView(R.id.frame_record)
    View frame_record;
    @BindView(R.id.frame_ticket)
    View frame_ticket;
    @BindView(R.id.frame_invite)
    View frame_invite;
    @BindView(R.id.frame_account_miaoqianbao)
    View frame_miaoqianbao;
    @BindView(R.id.frame_logined)
    View frame_logined;

    @BindView(R.id.swipe_refresh)
    MySwipeRefresh swipeRefresh;

    private View view;
    private UserInfo userInfo;
    private UserInfo userInfoTemp;
    private DialogTip dialogTips;
    private ExtendOperationController extendOperationController;
    private CustomDialog tipDialog;
    private boolean hasMessage;
    private boolean isQQproject;// 手Q活动开关
    public static boolean refresh = true;
    private boolean isOpeneye;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        extendOperationController = ExtendOperationController.getInstance();
        extendOperationController.registerExtendOperationListener(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.frame_user, null);
        }
        ButterKnife.bind(this, view);
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        MainActivity mainActivity = (MainActivity) getActivity();//.setReshListener(this);
        mainActivity.setReshListener(this);
        //手Q开关
        if (Uihelper.getConfigCrowd(mContext)) {
            isQQproject = true;
        }
        refresh = true;
        findViewById();
        return view;
    }

    @Override
    public void onStart() {
        if (UserUtil.hasLogin(mContext)) {
            frame_logined.setVisibility(View.VISIBLE);
            btnLogin.setVisibility(View.GONE);
            obtainData();
            swipeRefresh.setEnabled(true);
        } else {
            btnLogin.setVisibility(View.VISIBLE);
            frame_logined.setVisibility(View.GONE);
            tv_totalasset.setText("****");
            tv_Current.setText("");
            tv_Regular.setText("");
            tv_Ticket.setText("");
            btnEye.setBackgroundResource(R.drawable.icon_closeeye);
            tv_balance.setText("--.--");
            tv_TotalProfit.setText("--.--");
            tv_ydayprofit.setText("--.--");
            swipeRefresh.setEnabled(false);
        }

        super.onStart();
    }

    private void obtainData() {
        if (isQQproject && !refresh) {
            return;
        }
        if (!swipeRefresh.isRefreshing() && userInfo == null) {
            begin();
        }
        HttpRequest.getUserInfo(mContext, new ICallback<MqResult<UserInfo>>() {
            @Override
            public void onSucceed(MqResult<UserInfo> result) {
                swipeRefresh.setRefreshing(false);
                end();
                userInfo = result.getData();
                if (userInfo != null) {
                    userInfoTemp = new UserInfo();
                    userInfoTemp.setRealNameStatus(userInfo.getRealNameStatus());
                    userInfoTemp.setMobile(userInfo.getMobile());
                    userInfoTemp.setUserName(userInfo.getUserName());
                    setData(userInfo);
                } else {
                    Uihelper.showToast(mContext, result.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                swipeRefresh.setRefreshing(false);
                end();

                setData(null);
                Uihelper.showToast(mContext, error);
            }
        });

    }

    private void setData(UserInfo userInfo) {
        btnEye.setBackgroundResource(R.drawable.icon_openeye);
        isOpeneye = true;
        tv_totalasset.setTextColor(ContextCompat.getColor(mContext, R.color.mq_b1_v2));
        //历史收益
        if (userInfo != null && !TextUtils.isEmpty(userInfo.getTotalProfit())) {
            tv_TotalProfit.setText(FormatUtil.formatAmountStr(userInfo.getTotalProfit()));
        } else {
            tv_TotalProfit.setText("--.--");
        }
        //账户余额
        if (userInfo != null && !TextUtils.isEmpty(userInfo.getBalance())) {
            tv_balance.setText(FormatUtil.formatAmountStr(userInfo.getBalance()));
        } else {
            tv_balance.setText("--.--");
        }
        //我的定期
        if (userInfo != null && !TextUtils.isEmpty(userInfo.getRegTotal())) {
            tv_Regular.setText(userInfo.getRegTotal() + "笔");
        } else {
            tv_Regular.setText("--");
        }
        //红包优惠券
        if (userInfo != null) {
            int count = userInfo.getTotalPromotion();
            boolean isHasQQImage = Pref.getBoolean(UserUtil.getPrefKey(mContext, Pref.HAS_QQ), mContext, false);
            if (isQQproject && (isHasQQImage || count == 5)) {
                ivQQ.setVisibility(View.VISIBLE);
                Pref.saveBoolean(UserUtil.getPrefKey(mContext, Pref.HAS_QQ), true, mContext);
            }
            tv_Ticket.setText(count + "张");
        } else {
            tv_Ticket.setText("--");
        }

        //总资产
        if (userInfo != null && !TextUtils.isEmpty(userInfo.getTotalAsset())) {
            tv_totalasset.setText(userInfo.getTotalAsset());
        } else {
            tv_totalasset.setText("--.--");
        }
        //昨日收益
        if (userInfo != null && !TextUtils.isEmpty(userInfo.getYdayProfit())) {
            tv_ydayprofit.setText(userInfo.getYdayProfit());
        } else {
            tv_ydayprofit.setText("--.--");
        }

        //新用户不会给currentAmt这个字段，  老用户 没给这个字段 则不显示 ‘‘我的活期’’
        if (userInfo != null && userInfo.getCurrentAmt() != null && userInfo.getCurrentAmt().compareTo(BigDecimal.ZERO) > 0) {
            frame_current.setVisibility(View.VISIBLE);
            view.findViewById(R.id.divider_current).setVisibility(View.VISIBLE);
            tv_Current.setText(FormatUtil.formatAmount(userInfo.getCurrentAmt()) + "元");
        }

    }

    @Override
    protected String getPageName() {
        return "首页-我的";
    }

    @Override
    public void onDestroy() {
        extendOperationController.unRegisterExtendOperationListener(this);
        super.onDestroy();
    }

    private void findViewById() {

        swipeRefresh.setOnPullRefreshListener(new MySwipeRefresh.OnPullRefreshListener() {
            @Override
            public void onRefresh() {
                refresh = true;
                obtainData();
            }
        });

        tv_Title.setText("我的资产");

        if (!isQQproject) {
            if (hasMessage) {
                btn_message.setImageResource(R.drawable.bt_hasmessage);
            } else {
                btn_message.setImageResource(R.drawable.btn_message);
            }
        } else {
            btn_message.setVisibility(View.GONE);
        }
        btn_message.setOnClickListener(this);
        frame_current.setOnClickListener(this);
        frame_regular.setOnClickListener(this);
        frame_record.setOnClickListener(this);
        frame_ticket.setOnClickListener(this);
        frame_invite.setOnClickListener(this);
        frame_miaoqianbao.setOnClickListener(this);
        btn_setting.setImageResource(R.drawable.btn_setting);
        tvQuestion.setOnClickListener(this);
    }

    @OnClick(R.id.btn_eye)//资金等是否可见
    public void eyeState() {
        if (UserUtil.hasLogin(mActivity)) {
            if (isOpeneye) {
                btnEye.setBackgroundResource(R.drawable.icon_closeeye);
                tv_totalasset.setText("****");
                tv_Current.setText("");
                tv_Regular.setText("");
                tv_Ticket.setText("");
                tv_balance.setText("--.--");
                tv_TotalProfit.setText("--.--");
                tv_ydayprofit.setText("--.--");
                isOpeneye = false;
            } else {
                btnEye.setBackgroundResource(R.drawable.icon_openeye);
                isOpeneye = true;
                setData(userInfo);
            }
        }
    }

    @OnClick(R.id.bt_login)//登录
    public void loGin() {
        LoginActivity.start(mActivity);

    }

    @OnClick(R.id.bt_right)//设置
    public void setting() {
        if (UserUtil.hasLogin(mContext)) {
            refresh = false;
        }
        MobclickAgent.onEvent(mContext, "1016");
        Intent intent_setting = new Intent(mContext, SettingActivity.class);
        Bundle extra = new Bundle();
        extra.putSerializable("userInfo", userInfoTemp);
        intent_setting.putExtras(extra);
        startActivity(intent_setting);
    }

    @OnClick(R.id.bt_rollin)
    public void rollIn() {//充值
        if (userInfo == null) {
            return;
        }
        if (UserUtil.isFinishSave(mContext)) {
            MobclickAgent.onEvent(mContext, "1017");
            startActivity(new Intent(mActivity, IntoModeAcitvity.class));
        } else {
            SaveAcitvity.startActivity(mActivity);
        }
    }

    @OnClick(R.id.bt_rollout)
    public void rollOut() {//提现
        if (userInfo == null) {
            return;
        }
        if (UserUtil.isFinishSave(mContext)) {
            String balance = userInfo.getBalance();
            if (!TextUtils.isEmpty(balance)) {
                BigDecimal bdBalance = new BigDecimal(balance);
                if (bdBalance != null && bdBalance.compareTo(BigDecimal.ZERO) > 0) {
                    //取现
                    startActivity(new Intent(mActivity, RolloutActivity.class));
                } else {
                    Uihelper.showToast(mContext, "可用余额为0,无法提现");
                }
            }
        } else {
            SaveAcitvity.startActivity(mActivity);
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //我的活期
            case R.id.frame_account_current:
                if (UserUtil.hasLogin(mContext)) {
                    MobclickAgent.onEvent(mContext, "1019");
                    startActivity(new Intent(mContext, ActivityUserCurrent.class));
                } else {
                    LoginActivity.start(mActivity);
                }
                break;
            //我的定期
            case R.id.frame_regular:
                if (UserUtil.hasLogin(mContext)) {
                    MobclickAgent.onEvent(mContext, "1020");
                    startActivity(new Intent(mContext, UserRegularActivity.class));
                } else {
                    LoginActivity.start(mActivity);
                }
                break;
            //资金记录
            case R.id.frame_record:
                if (UserUtil.hasLogin(mContext)) {
                    MobclickAgent.onEvent(mContext, "1021");
                    startActivity(new Intent(mContext, UserRecordActivity.class));
                } else {
                    LoginActivity.start(mActivity);
                }

                break;
            //优惠券
            case R.id.frame_ticket:
                if (UserUtil.hasLogin(mContext)) {
                    refresh = false;
                    MobclickAgent.onEvent(mContext, "1022");
                    startActivity(new Intent(mContext, MyTicketActivity.class));
                } else {
                    LoginActivity.start(mActivity);
                }
                break;
            //我的消息
            case R.id.bt_left:
                MobclickAgent.onEvent(mContext, "1015");
                startActivity(new Intent(mContext, AnnounceActivity.class));
                hasMessage = false;
                if (!isQQproject) {
                    btn_message.setImageResource(R.drawable.btn_message);
                }
                break;
            //我的邀请
            case R.id.frame_invite:
                if (UserUtil.hasLogin(mContext)) {
                    refresh = false;
                    WebActivity.startActivity(mContext, Urls.web_my_invite);
                } else {
                    LoginActivity.start(mActivity);
                }
                break;
            //我的秒钱宝
            case R.id.frame_account_miaoqianbao:
                if (UserUtil.hasLogin(mContext)) {
                    refresh = false;
                    startActivity(new Intent(mContext, UserMqbActivity.class));
                } else {
                    LoginActivity.start(mActivity);
                }
                break;
            //充值未到账
            case R.id.tv_question:
                if (UserUtil.hasLogin(mContext)) {
                    startActivity(new Intent(mContext, IntoCheckAcitvity.class));
                } else {
                    LoginActivity.start(mActivity);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void changeData(JpushInfo jpushInfo) {
        if (MyApplication.getInstance().isShowTips()) {
            MyApplication.getInstance().setShowTips(false);
            if (dialogTips == null) {
                dialogTips = new DialogTip(mActivity) {
                };
            }
            if (jpushInfo != null) {
                dialogTips.setInfo(jpushInfo.getContent());
                dialogTips.setTitle(jpushInfo.getTitle());
                dialogTips.show();
            }
        }
        onStart();
    }

    @Override
    public void excuteExtendOperation(int operationKey, Object data) {
        switch (operationKey) {
            case ExtendOperationController.OperationKey.RERESH_JPUSH:
                // 更新数据
                if (UserUtil.hasLogin(mContext)) {
                    hasMessage = true;
                    if (!isQQproject) {
                        btn_message.setImageResource(R.drawable.bt_hasmessage);
                    }

                } else {
                    hasMessage = false;
                    if (!isQQproject) {
                        btn_message.setImageResource(R.drawable.btn_message);
                    }
                }
                break;
            case ExtendOperationController.OperationKey.MessageState:
                // 更新数据
                hasMessage = false;
                if (!isQQproject) {
                    btn_message.setImageResource(R.drawable.btn_message);
                }
                break;
            case ExtendOperationController.OperationKey.SETTRADPASSWORD_SUCCESS:
                userInfoTemp.setJxPayPwdStatus("1");
                break;
            case ExtendOperationController.OperationKey.BACK_USER:
                ivQQ.setVisibility(View.GONE);
                break;
            default:
                break;
        }

    }
}