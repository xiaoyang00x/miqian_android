package com.miqian.mq.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.miqian.mq.MyApplication;
import com.miqian.mq.R;
import com.miqian.mq.activity.AnnounceActivity;
import com.miqian.mq.activity.CapitalRecordActivity;
import com.miqian.mq.activity.IntoActivity;
import com.miqian.mq.activity.MainActivity;
import com.miqian.mq.activity.SendCaptchaActivity;
import com.miqian.mq.activity.current.ActivityUserCurrent;
import com.miqian.mq.activity.setting.SettingActivity;
import com.miqian.mq.activity.user.MyTicketActivity;
import com.miqian.mq.activity.user.RegisterActivity;
import com.miqian.mq.activity.user.RolloutActivity;
import com.miqian.mq.activity.user.UserRegularActivity;
import com.miqian.mq.entity.JpushInfo;
import com.miqian.mq.entity.LoginResult;
import com.miqian.mq.entity.UserInfo;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.ExtendOperationController;
import com.miqian.mq.utils.FormatUtil;
import com.miqian.mq.utils.MobileOS;
import com.miqian.mq.utils.TypeUtil;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.utils.UserUtil;
import com.miqian.mq.views.CustomDialog;
import com.miqian.mq.views.MySwipeRefresh;
import com.umeng.analytics.MobclickAgent;

import java.math.BigDecimal;

/**
 * Description:
 *
 * @author Jackie
 * @created 2015-3-18
 */

public class FragmentUser extends BasicFragment implements View.OnClickListener, MainActivity.RefeshDataListener, ExtendOperationController.ExtendOperationListener {

    private View view;
    private TextView tv_Current, tv_Regular, tv_Ticket, tv_Redpackage;
    private UserInfo userInfo;
    private UserInfo userInfoTemp;
    private TextView tv_TotalProfit, tv_balance, tv_totalasset;
    private CustomDialog dialogTips;
    private MySwipeRefresh swipeRefresh;
    private ImageButton btn_message;
    private EditText editTelephone;
    private EditText editPassword;
    private ExtendOperationController extendOperationController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        extendOperationController = ExtendOperationController.getInstance();
        extendOperationController.registerExtendOperationListener(this);

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (savedInstanceState == null || view == null) {
            view = inflater.inflate(R.layout.frame_user, null);
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        MainActivity mainActivity = (MainActivity) getActivity();//.setReshListener(this);
        mainActivity.setReshListener(this);
        findViewById(view);
        return view;
    }

    @Override
    public void onStart() {
        int message = Uihelper.getMessageCount(4, getActivity());
        if (message > 0) {
            btn_message.setImageResource(R.drawable.btn_message);
        } else {
            btn_message.setImageResource(R.drawable.btn_message_none);
        }
        //已登录，显示我的界面
        if (UserUtil.hasLogin(getActivity())) {
            initUserView();
            obtainData();
        }
        //未登录，显示登录界面
        else {
            initLoginView();
            editPassword.setText("");
            editTelephone.setText("");
        }

        super.onStart();
    }

    private void obtainData() {
        if (!swipeRefresh.isRefreshing() && userInfo == null) {
            begin();
        }
        HttpRequest.getUserInfo(getActivity(), new ICallback<LoginResult>() {
            @Override
            public void onSucceed(LoginResult result) {
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
                    userInfoTemp.setMobilePhone(userInfo.getMobilePhone());
                    userInfoTemp.setRealName(userInfo.getRealName());
                    setData(userInfo);
                }
            }

            @Override
            public void onFail(String error) {
                swipeRefresh.setRefreshing(false);
                end();
                setData(null);
                Uihelper.showToast(getActivity(), error);
//                initLoginView();
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
        //拾财券
        if (userInfo != null && !TextUtils.isEmpty(userInfo.getWealthTicket())) {
            tv_Ticket.setText(userInfo.getWealthTicket() + "张");
        } else {
            tv_Ticket.setText("--");
        }
        //红包
        if (userInfo != null && !TextUtils.isEmpty(userInfo.getRedBag())) {
            tv_Redpackage.setText(userInfo.getRedBag() + "个");
        } else {
            tv_Redpackage.setText("--");
        }
        //总资产
        if (userInfo != null && !TextUtils.isEmpty(userInfo.getTotalAsset())) {
            tv_totalasset.setText(userInfo.getTotalAsset());
        } else {
            tv_totalasset.setText("--.--");
        }
    }

    @Override
    public void onResume() {

        super.onResume();
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

    private void findViewById(View view) {


        swipeRefresh = (MySwipeRefresh) view.findViewById(R.id.swipe_refresh);
        swipeRefresh.setOnPullRefreshListener(new MySwipeRefresh.OnPullRefreshListener() {
            @Override
            public void onRefresh() {
                obtainData();
            }
        });


        TextView tv_Title = (TextView) view.findViewById(R.id.title);
        tv_Title.setText("我的");

        btn_message = (ImageButton) view.findViewById(R.id.bt_left);
        btn_message.setOnClickListener(this);

        ImageButton btn_setting = (ImageButton) view.findViewById(R.id.bt_right);
        btn_setting.setImageResource(R.drawable.btn_setting);
        btn_setting.setOnClickListener(this);

        //*********已登录的的Ui***************
        Button btn_RollIn = (Button) view.findViewById(R.id.btn_rollin);
        Button btn_RollOut = (Button) view.findViewById(R.id.btn_rollout);

        btn_RollIn.setOnClickListener(this);
        btn_RollOut.setOnClickListener(this);

        tv_Current = (TextView) view.findViewById(R.id.account_current);
        tv_Regular = (TextView) view.findViewById(R.id.account_regular);
        tv_Ticket = (TextView) view.findViewById(R.id.account_ticket);
        tv_Redpackage = (TextView) view.findViewById(R.id.account_redpackage);

        tv_balance = (TextView) view.findViewById(R.id.tv_balance);
        tv_TotalProfit = (TextView) view.findViewById(R.id.tv_totalProfit);
        tv_totalasset = (TextView) view.findViewById(R.id.tv_totalasset);

        View frame_current = view.findViewById(R.id.frame_account_current);
        View frame_regular = view.findViewById(R.id.frame_regular);
        View frame_record = view.findViewById(R.id.frame_record);
        View frame_ticket = view.findViewById(R.id.frame_ticket);
        View frame_redpackage = view.findViewById(R.id.frame_redpackage);

        frame_current.setOnClickListener(this);
        frame_regular.setOnClickListener(this);
        frame_record.setOnClickListener(this);
        frame_ticket.setOnClickListener(this);
        frame_redpackage.setOnClickListener(this);


        //*********未登录的的Ui***************

        final View relaTelephone = view.findViewById(R.id.rela_telephone);
        final View relaPassword = view.findViewById(R.id.rela_password);
        editTelephone = (EditText) view.findViewById(R.id.edit_telephone);
        editPassword = (EditText) view.findViewById(R.id.edit_password);

        Button btnLogin = (Button) view.findViewById(R.id.btn_login);
        view.findViewById(R.id.tv_login_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(getActivity(), "1048");
                //跳到注册页
                getActivity().startActivity(new Intent(getActivity(), RegisterActivity.class));

            }
        });
        view.findViewById(R.id.tv_login_forgetpw).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(getActivity(), "1047");
                SendCaptchaActivity.enterActivity(getActivity(), TypeUtil.SENDCAPTCHA_FORGETPSW, false);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(getActivity(), "1046");
                userInfo = null;
                String telephone = editTelephone.getText().toString();
                String password = editPassword.getText().toString();
                if (!TextUtils.isEmpty(telephone)) {
                    if (MobileOS.isMobileNO(telephone) && telephone.length() == 11) {
                        if (!TextUtils.isEmpty(password)) {
                            if (password.length() >= 6 && password.length() <= 16) {
                                login(telephone, password);
                            } else {
                                Uihelper.showToast(getActivity(), R.string.tip_password_login);
                            }
                        } else {
                            Uihelper.showToast(getActivity(), "密码不能为空");
                        }
                    } else {
                        Uihelper.showToast(getActivity(), R.string.phone_noeffect);
                    }
                } else {
                    Uihelper.showToast(getActivity(), "手机号码不能为空");
                }

            }
        });

    }

    private void initUserView() {
        swipeRefresh.setVisibility(View.VISIBLE);
        view.findViewById(R.id.layout_nologin).setVisibility(View.GONE);

        if (userInfo != null) {
            setData(userInfo);
        }
    }

    private void initLoginView() {
        swipeRefresh.setVisibility(View.GONE);
        view.findViewById(R.id.layout_nologin).setVisibility(View.VISIBLE);
    }

    private void login(String telephone, String password) {
        begin();
        HttpRequest.login(getActivity(), new ICallback<LoginResult>() {
            @Override
            public void onSucceed(LoginResult result) {
                end();
                UserInfo userInfo = result.getData();
                UserUtil.saveUserInfo(getActivity(), userInfo);
                onStart();
            }

            @Override
            public void onFail(String error) {
                end();
                Uihelper.showToast(getActivity(), error);
            }
        }, telephone, password);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //充值
            case R.id.btn_rollin:
                if (userInfo == null) {
                    return;
                }
                MobclickAgent.onEvent(getActivity(), "1017");
                //未认证
//                if ("0".equals(userInfo.getRealNameStatus())) {
//                    Intent intent = new Intent(getActivity(), ActivityRealname.class);
//                    startActivity(intent);
//                } else {
                    UserUtil.isLogin(getActivity(), IntoActivity.class);
//                }

                break;
            //取现
            case R.id.btn_rollout:
                MobclickAgent.onEvent(getActivity(), "1018");

                if (userInfo == null) {
                    return;
                }
                String balance = userInfo.getBalance();
                if (!TextUtils.isEmpty(balance)) {
                    if (new BigDecimal(balance).compareTo(new BigDecimal(0)) > 0) {
                        Intent intent = new Intent(getActivity(), RolloutActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("userInfo", userInfoTemp);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else {
                        Uihelper.showToast(getActivity(), "账户无余额，无法提现");
                    }

                }

                break;
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
            //红包/券
            case R.id.frame_ticket:
                MobclickAgent.onEvent(getActivity(), "1022");
                startActivity(new Intent(getActivity(), MyTicketActivity.class));
                break;
//            //我的红包
//            case R.id.frame_redpackage:
//                MobclickAgent.onEvent(getActivity(), "1023");
//                startActivity(new Intent(getActivity(), RedPaperActivity.class));
//                break;
            //我的消息
            case R.id.bt_left:
                MobclickAgent.onEvent(getActivity(), "1015");
                startActivity(new Intent(getActivity(), AnnounceActivity.class));
                break;
            //我的设置
            case R.id.bt_right:
                MobclickAgent.onEvent(getActivity(), "1016");
                Intent intent_setting = new Intent(getActivity(), SettingActivity.class);
                Bundle extra = new Bundle();
                extra.putSerializable("userInfo", userInfoTemp);
                intent_setting.putExtras(extra);
                startActivity(intent_setting);
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
                dialogTips = new CustomDialog(getActivity(), CustomDialog.CODE_TIPS) {

                    @Override
                    public void positionBtnClick() {
                        dismiss();
                    }

                    @Override
                    public void negativeBtnClick() {

                    }
                };
            }
            if (jpushInfo != null) {
                dialogTips.setRemarks(jpushInfo.getContent());
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
                int message = Uihelper.getMessageCount(4, getActivity());
                if (message > 0) {
                    btn_message.setImageResource(R.drawable.btn_message);
                } else {
                    btn_message.setImageResource(R.drawable.btn_message_none);
                }
                break;
        }

    }
}
