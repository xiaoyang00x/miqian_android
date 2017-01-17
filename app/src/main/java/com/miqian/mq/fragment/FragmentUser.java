package com.miqian.mq.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.miqian.mq.MyApplication;
import com.miqian.mq.R;
import com.miqian.mq.activity.AnnounceActivity;
import com.miqian.mq.activity.CapitalRecordActivity;
import com.miqian.mq.activity.GestureLockSetActivity;
import com.miqian.mq.activity.IntoActivity;
import com.miqian.mq.activity.MainActivity;
import com.miqian.mq.activity.QQprojectRegister;
import com.miqian.mq.activity.SendCaptchaActivity;
import com.miqian.mq.activity.WebActivity;
import com.miqian.mq.activity.current.ActivityUserCurrent;
import com.miqian.mq.activity.setting.SettingActivity;
import com.miqian.mq.activity.user.MyTicketActivity;
import com.miqian.mq.activity.user.RolloutActivity;
import com.miqian.mq.activity.user.UserRegularActivity;
import com.miqian.mq.entity.JpushInfo;
import com.miqian.mq.entity.LoginResult;
import com.miqian.mq.entity.UserInfo;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.net.Urls;
import com.miqian.mq.utils.ExtendOperationController;
import com.miqian.mq.utils.FormatUtil;
import com.miqian.mq.utils.MobileOS;
import com.miqian.mq.utils.MyTextWatcher;
import com.miqian.mq.utils.Pref;
import com.miqian.mq.utils.TypeUtil;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.utils.UserUtil;
import com.miqian.mq.views.CustomDialog;
import com.miqian.mq.views.DialogTip;
import com.miqian.mq.views.Dialog_Register;
import com.miqian.mq.views.MySwipeRefresh;
import com.umeng.analytics.MobclickAgent;
import com.umeng.onlineconfig.OnlineConfigAgent;

import java.math.BigDecimal;

/**
 * Description:
 *
 * @author Jackie
 * @created 2015-3-18
 */

public class FragmentUser extends BasicFragment implements View.OnClickListener, MainActivity.RefeshDataListener, ExtendOperationController.ExtendOperationListener, QQprojectRegister.LoginLister, Dialog_Register.RegisterListenerFromDialog {

    private View view;
    private TextView tv_Current, tv_Regular, tv_Ticket;
    private UserInfo userInfo;
    private UserInfo userInfoTemp;
    private TextView tv_TotalProfit;//总收益
    private TextView tv_balance;//可用余额
    private TextView tv_totalasset;//总资产
    private TextView tv_ydayprofit;//昨日收益
    private DialogTip dialogTips;
    private MySwipeRefresh swipeRefresh;
    private ImageButton btn_message;
    private EditText editTelephone;
    private EditText editPassword;
    private ExtendOperationController extendOperationController;
    private CustomDialog tipDialog;
    private boolean hasMessage;
    private boolean loginMode;
    private boolean isQQproject;// 手Q活动开关
    private ImageView ivQQ;
    private Dialog_Register dialog_register;
    public static boolean refresh = true;
    private QQprojectRegister qQprojectRegister;
    private TextView tvForgetPW;
    private Button btnLogin;
    private CheckBox checkBoxLaw;

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
        //手Q开关
        if (Uihelper.getConfigCrowd(mContext)) {
            isQQproject = true;
        }
        refresh = true;
        findViewById(view);
        qQprojectRegister = new QQprojectRegister(getActivity(), view, swipeRefresh);
        qQprojectRegister.setLoginLister(this);
        return view;
    }

    @Override
    public void onStart() {
        if (UserUtil.hasLogin(getActivity())) {
            initUserView();
            obtainData();
        }
        //未登录,  显示注册页面
        else {
            if (loginMode) {
                toLogin();
            } else {
                swipeRefresh.setVisibility(View.GONE);
                view.findViewById(R.id.layout_register).setVisibility(View.VISIBLE);
                tvForgetPW.setEnabled(false);
                btnLogin.setEnabled(false);
            }
        }
        if (dialog_register == null) {
            dialog_register = new Dialog_Register(getActivity()) {
                @Override
                public void toLogin() {
                    dismiss();
                }

                @Override
                public void registerSuccess() {
                }
            };
            dialog_register.setLoginLister(FragmentUser.this);
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
        QQprojectRegister.isTimer = false;
        super.onDestroy();
    }

    private void findViewById(final View view) {

        swipeRefresh = (MySwipeRefresh) view.findViewById(R.id.swipe_refresh);
        swipeRefresh.setOnPullRefreshListener(new MySwipeRefresh.OnPullRefreshListener() {
            @Override
            public void onRefresh() {
                refresh = true;
                obtainData();
            }
        });

        TextView tv_Title = (TextView) view.findViewById(R.id.title);
        tv_Title.setText("我的资产");

        btn_message = (ImageButton) view.findViewById(R.id.bt_left);
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

        ImageButton btn_setting = (ImageButton) view.findViewById(R.id.bt_right);
        btn_setting.setImageResource(R.drawable.btn_setting);
        btn_setting.setOnClickListener(this);

        //*********已登录的的Ui***************
        final Button btn_RollIn = (Button) view.findViewById(R.id.btn_rollin);
        Button btn_RollOut = (Button) view.findViewById(R.id.btn_rollout);

        btn_RollIn.setOnClickListener(this);
        btn_RollOut.setOnClickListener(this);

        tv_Current = (TextView) view.findViewById(R.id.account_current);
        tv_Regular = (TextView) view.findViewById(R.id.account_regular);
        tv_Ticket = (TextView) view.findViewById(R.id.account_ticket);
        ivQQ = (ImageView) view.findViewById(R.id.iv_qq);

        tv_balance = (TextView) view.findViewById(R.id.tv_balance);
        tv_TotalProfit = (TextView) view.findViewById(R.id.tv_totalProfit);
        tv_totalasset = (TextView) view.findViewById(R.id.tv_totalasset);
        tv_ydayprofit = (TextView) view.findViewById(R.id.tv_ydayprofit);

        View frame_current = view.findViewById(R.id.frame_account_current);
        View frame_regular = view.findViewById(R.id.frame_regular);
        View frame_record = view.findViewById(R.id.frame_record);
        View frame_ticket = view.findViewById(R.id.frame_ticket);
        View frame_invite = view.findViewById(R.id.frame_invite);

        frame_current.setOnClickListener(this);
        frame_regular.setOnClickListener(this);
        frame_record.setOnClickListener(this);
        frame_ticket.setOnClickListener(this);
        frame_invite.setOnClickListener(this);


        //*********未登录的的Ui***************

        final View relaTelephone = view.findViewById(R.id.rela_telephone);
        final View relaPassword = view.findViewById(R.id.rela_password);
        editTelephone = (EditText) view.findViewById(R.id.edit_telephone);
        editPassword = (EditText) view.findViewById(R.id.edit_password);
        btnLogin = (Button) view.findViewById(R.id.btn_login);
        checkBoxLaw = (CheckBox) view.findViewById(R.id.check_law);

        TextView tvLaw = (TextView) view.findViewById(R.id.text_law);
        SpannableString spanLaw = new SpannableString("我已阅读并同意《网络借贷风险");
        spanLaw.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getActivity(), R.color.mq_b2)), 0, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvLaw.setText(spanLaw);

        checkBoxLaw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    btnLogin.setEnabled(true);
                } else {
                    btnLogin.setEnabled(false);
                }
            }
        });
        view.findViewById(R.id.layout_net_law).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebActivity.startActivity(mContext, Urls.web_register_law_net);
            }
        });


        view.findViewById(R.id.tv_login_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(getActivity(), "1048");
                //跳到注册页
                loginMode = false;
                swipeRefresh.setVisibility(View.GONE);
                view.findViewById(R.id.layout_register).setVisibility(View.VISIBLE);
                tvForgetPW.setEnabled(false);
                btnLogin.setEnabled(false);
                QQprojectRegister.initData();

            }
        });
        tvForgetPW = (TextView) view.findViewById(R.id.tv_login_forgetpw);
        tvForgetPW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(getActivity(), "1047");
                SendCaptchaActivity.enterActivity(getActivity(), TypeUtil.SENDCAPTCHA_FORGETPSW, false);
            }
        });
        editTelephone.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void myAfterTextChanged(Editable arg0) {
                String phone = editTelephone.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    editPassword.setText("");
                }
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
        tvForgetPW.setEnabled(true);
        btnLogin.setEnabled(true);

        if (!isQQproject) {
            view.findViewById(R.id.layout_record_invite).setVisibility(View.VISIBLE);
        }
        if (userInfo != null) {
            setData(userInfo);
        }
    }

    private void initLoginView() {
        swipeRefresh.setVisibility(View.GONE);
        view.findViewById(R.id.layout_register).setVisibility(View.GONE);
        view.findViewById(R.id.layout_nologin).setVisibility(View.VISIBLE);
        tvForgetPW.setEnabled(true);
        if (checkBoxLaw.isChecked()) {
            btnLogin.setEnabled(true);
        }
    }

    private void login(String telephone, String password) {
        begin();
        HttpRequest.login(getActivity(), new ICallback<LoginResult>() {
            @Override
            public void onSucceed(LoginResult result) {
                end();
                UserInfo userInfo = result.getData();
                UserUtil.saveUserInfo(getActivity(), userInfo);
                if (Pref.getBoolean(Pref.GESTURESTATE, getActivity(), true)) {
                    GestureLockSetActivity.startActivity(getActivity(), null);
                } else {
                    onStart();
                }
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
                startActivity(new Intent(getActivity(), IntoActivity.class));
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
            //我的设置
            case R.id.bt_right:
                refresh = false;
                MobclickAgent.onEvent(getActivity(), "1016");
                Intent intent_setting = new Intent(getActivity(), SettingActivity.class);
                Bundle extra = new Bundle();
                extra.putSerializable("userInfo", userInfoTemp);
                intent_setting.putExtras(extra);
                startActivity(intent_setting);
                break;
            //我的邀请
            case R.id.frame_invite:
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
                loginMode = true;
                ivQQ.setVisibility(View.GONE);
                break;
            default:
                break;
        }

    }

    @Override
    public void toLogin() {
        initLoginView();
        editPassword.setText("");
        String telphone = Pref.getString(Pref.TELEPHONE, getActivity(), "");
        editTelephone.setText(telphone);
        editTelephone.setSelection(telphone.length());
        loginMode = true;
    }

    @Override
    public void registerSuccessfromNative() {
        if (Pref.getBoolean(Pref.GESTURESTATE, mContext, true)) {
            GestureLockSetActivity.startActivity(mContext, null);
        } else {
            onStart();
        }
    }

    @Override
    public void registerSuccessfromDialog() {
        if (Pref.getBoolean(Pref.GESTURESTATE, mContext, true)) {
            GestureLockSetActivity.startActivity(mContext, null);
        } else {
            onStart();
        }
        dialog_register.dismiss();

    }
}
