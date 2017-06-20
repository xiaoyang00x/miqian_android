package com.miqian.mq.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.miqian.mq.MyApplication;
import com.miqian.mq.R;
import com.miqian.mq.activity.AnnounceActivity;
import com.miqian.mq.activity.CapitalRecordActivity;
import com.miqian.mq.activity.IntoActivity;
import com.miqian.mq.activity.MainActivity;
import com.miqian.mq.activity.QQprojectRegister;
import com.miqian.mq.activity.WebActivity;
import com.miqian.mq.activity.current.ActivityUserCurrent;
import com.miqian.mq.activity.setting.SettingActivity;
import com.miqian.mq.activity.user.MyTicketActivity;
import com.miqian.mq.activity.user.RolloutActivity;
import com.miqian.mq.activity.user.UserRegularActivity;
import com.miqian.mq.entity.JpushInfo;
import com.miqian.mq.entity.UserInfo;
import com.miqian.mq.entity.UserInfoResult;
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
        obtainData();
        super.onStart();
    }

    private void obtainData() {
        if (isQQproject && !refresh) {
            return;
        }
        if (!swipeRefresh.isRefreshing() && userInfo == null) {
            begin();
        }
        HttpRequest.getUserInfo(getActivity(), new ICallback<UserInfoResult>() {
            @Override
            public void onSucceed(UserInfoResult result) {
                swipeRefresh.setRefreshing(false);
                end();
                userInfo = result.getData();
                if (userInfo != null) {
                    userInfoTemp = new UserInfo();
                    userInfoTemp.setBindCardStatus(userInfo.getBindCardStatus());
                    userInfoTemp.setBankNo(userInfo.getBankNo());
                    userInfoTemp.setBankName(userInfo.getBankName());
                    userInfoTemp.setBankUrlSmall(userInfo.getBankUrlSmall());
                    userInfoTemp.setBankCode(userInfo.getBankCode());
                    userInfoTemp.setBalance(userInfo.getBalance());
                    userInfoTemp.setSupportStatus(userInfo.getSupportStatus());
                    userInfoTemp.setRealNameStatus(userInfo.getRealNameStatus());
                    userInfoTemp.setPayPwdStatus(userInfo.getPayPwdStatus());
                    userInfoTemp.setMobile(userInfo.getMobile());
                    userInfoTemp.setUserName(userInfo.getUserName());
                    setData(userInfo);
                } else {
                    Uihelper.showToast(getActivity(), result.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                swipeRefresh.setRefreshing(false);
                end();
                setData(null);
                Uihelper.showToast(getActivity(), error);
            }
        });

    }

    private void setData(UserInfo userInfo) {

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
        //我的活期
        if (userInfo != null && !TextUtils.isEmpty(userInfo.getCurAmt())) {
            tv_Current.setText(FormatUtil.formatAmountStr(userInfo.getCurAmt()) + "元");
        } else {
            tv_Current.setText("--");
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
            boolean isHasQQImage = Pref.getBoolean(UserUtil.getPrefKey(getActivity(), Pref.HAS_QQ), getActivity(), false);
            if (isQQproject && (isHasQQImage || count == 5)) {
                ivQQ.setVisibility(View.VISIBLE);
                Pref.saveBoolean(UserUtil.getPrefKey(getActivity(), Pref.HAS_QQ), true, getActivity());
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
    }

    @Override
    protected String getPageName() {
        return "首页-我的";
    }

    @Override
    public void onDestroy() {
        extendOperationController.unRegisterExtendOperationListener(this);
        QQprojectRegister.isTimer = false;
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
    }

    @OnClick(R.id.bt_right)//设置
    public void setting() {
        if (UserUtil.hasLogin(getActivity())) {
            refresh = false;
        }
        MobclickAgent.onEvent(getActivity(), "1016");
        Intent intent_setting = new Intent(getActivity(), SettingActivity.class);
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
        MobclickAgent.onEvent(getActivity(), "1017");
        startActivity(new Intent(getActivity(), IntoModeAcitvity.class));
    }

    @OnClick(R.id.bt_rollout)
    public void rollOut() {//取现
        MobclickAgent.onEvent(getActivity(), "1018");
        if (userInfo == null) {
            return;
        }
        String balance = userInfo.getBalance();
        if (!TextUtils.isEmpty(balance)) {
            if (new BigDecimal(balance).compareTo(new BigDecimal(0)) > 0) {

                if ("1".equals(userInfo.getBindCardStatus())) {
                    if ("0".equals(userInfo.getWithdrawCashSwitch())) {
                        Uihelper.showToast(getActivity(), R.string.qq_project_rollout);
                    } else {
                        Intent intent = new Intent(getActivity(), RolloutActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("userInfo", userInfoTemp);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                } else {//提示绑卡
                    if (tipDialog == null) {
                        tipDialog = new CustomDialog(getActivity(), CustomDialog.CODE_TIPS) {
                            @Override
                            public void positionBtnClick() {
                                dismiss();
                            }

                            @Override
                            public void negativeBtnClick() {
                            }
                        };
                        tipDialog.setTitle("提示");
                        tipDialog.setRemarks("请先充值完成绑卡流程");
                    }
                    tipDialog.show();
                }


            } else {
                Uihelper.showToast(getActivity(), "账户无余额，无法提现");
            }

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //我的活期
            case R.id.frame_account_current:
                MobclickAgent.onEvent(getActivity(), "1019");
                startActivity(new Intent(getActivity(), ActivityUserCurrent.class));
                break;
            //我的定期
            case R.id.frame_regular:
                MobclickAgent.onEvent(getActivity(), "1020");
                startActivity(new Intent(getActivity(), UserRegularActivity.class));
                break;
            //资金记录
            case R.id.frame_record:
                MobclickAgent.onEvent(getActivity(), "1021");
                startActivity(new Intent(getActivity(), CapitalRecordActivity.class));
                break;
            //优惠券
            case R.id.frame_ticket:
                refresh = false;
                MobclickAgent.onEvent(getActivity(), "1022");
                startActivity(new Intent(getActivity(), MyTicketActivity.class));
                break;
            //我的消息
            case R.id.bt_left:
                MobclickAgent.onEvent(getActivity(), "1015");
                startActivity(new Intent(getActivity(), AnnounceActivity.class));
                hasMessage = false;
                if (!isQQproject) {
                    btn_message.setImageResource(R.drawable.btn_message);
                }
                break;
            //我的邀请
            case R.id.frame_invite:
                refresh = false;
                WebActivity.startActivity(mContext, Urls.web_my_invite);
                break;
            //我的秒钱宝
            case R.id.account_miaoqianbao:
                refresh = false;
                WebActivity.startActivity(mContext, Urls.web_my_invite);
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
                dialogTips = new DialogTip(getActivity()) {
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
                if (UserUtil.hasLogin(getActivity())) {
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
                userInfoTemp.setPayPwdStatus("1");
                break;
            case ExtendOperationController.OperationKey.BACK_USER:
                ivQQ.setVisibility(View.GONE);
                break;
            default:
                break;
        }

    }
}