package com.miqian.mq.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.entity.BankCard;
import com.miqian.mq.entity.BankCardResult;
import com.miqian.mq.entity.UserInfo;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.FormatUtil;
import com.miqian.mq.utils.MyTextWatcher;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.CustomDialog;
import com.miqian.mq.views.WFYTitle;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * Created by Joy on 2015/9/22.
 */
public class RolloutActivity extends BaseActivity {
    private UserInfo userInfo;
    private TextView bindBankId, bindBankName, textBranch, textTotalMoney, tv_arrivetime;
    private ImageView iconBank;
    private View frame_bindbranch,frame_bank_branch,frame_bank_province;
    private EditText editMoney;
    private CustomDialog dialogTips;
    private String  bankOpenName;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;


    @Override
    public void obtainData() {

    }

    @Override
    public void initView() {

        findView();

        Intent intent = getIntent();
        userInfo = (UserInfo) intent.getSerializableExtra("userInfo");
        if (userInfo == null) {
            return;
        }

        initBindView();
        initBindBranchView();

    }

    private void initBindBranchView() {
        bankOpenName="";
        if (TextUtils.isEmpty(bankOpenName)){
            frame_bindbranch.setVisibility(View.VISIBLE);
            frame_bank_branch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


//                    Intent intent=new Intent(mActivity,RolloutActivity.class);
//                    startActivityForResult(0,intent);
                }
            });

            frame_bank_province.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


//                    Intent intent=new Intent(mActivity,RolloutActivity.class);
//                    startActivityForResult(0,intent);
                }
            });


        }
        else {
            frame_bindbranch.setVisibility(View.GONE);
        }



//        HttpRequest.getUserBankCard(mActivity, new ICallback<BankCardResult>() {
//            @Override
//            public void onSucceed(BankCardResult result) {
//                BankCard bankCard = result.getData();
//                bankCard.getBankOpenName();
//            }
//
//            @Override
//            public void onFail(String error) {
//
//            }
//        });

    }

    private void initBindView() {
        String bindCardStatus = userInfo.bindCardStatus;
        //未绑定
        if ("0".equals(bindCardStatus)) {
            dialogTips = new CustomDialog(this, CustomDialog.CODE_TIPS) {

                @Override
                public void positionBtnClick() {
                    //跳到绑定支行的页面
                    dismiss();
                    Uihelper.showToast(mActivity,"跳到绑定支行的页面");
                }

                @Override
                public void negativeBtnClick() {
                    dismiss();
                    finish();
                }
            };
            dialogTips.setNegative(View.VISIBLE);
            dialogTips.setRemarks("    您尚未绑定银行卡，请先绑定银行卡");
            dialogTips.setNegative("取消");
            dialogTips.show();
            dialogTips.setCancelable(false);
        }

    }

    private void findView() {
        bindBankId = (TextView) findViewById(R.id.bind_bank_id);
        bindBankName = (TextView) findViewById(R.id.bind_bank_name);
        textBranch = (TextView) findViewById(R.id.tv_bank_branch);
        tv_arrivetime = (TextView) findViewById(R.id.tv_arrivetime);
        iconBank = (ImageView) findViewById(R.id.icon_bank);


        frame_bindbranch = findViewById(R.id.frame_bindbranch);
        frame_bank_province = findViewById(R.id.frame_bank_province);
        frame_bank_branch = findViewById(R.id.frame_bank_branch);


        textTotalMoney = (TextView) findViewById(R.id.total_money);
        editMoney = (EditText) findViewById(R.id.edit_money);
        editMoney.addTextChangedListener(new MyTextWatcher() {

            @Override
            public void myAfterTextChanged(Editable s) {
                try {
                    String temp = s.toString();
                    if (temp.matches(FormatUtil.PATTERN_MONEY)) {
                        return;
                    }
                    s.delete(temp.length() - 1, temp.length());
                } catch (Exception e) {
                }
            }
        });

        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).displayer(new RoundedBitmapDisplayer(0)).build();


    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_rollout;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("提现");
        mTitle.setRightText("转出说明");
        mTitle.setOnRightClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
//                WebViewActivity.doIntent(mActivity, Urls.help_rollout_url, true, null);
            }
        });

    }
}
