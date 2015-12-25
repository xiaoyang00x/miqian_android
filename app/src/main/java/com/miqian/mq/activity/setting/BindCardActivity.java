package com.miqian.mq.activity.setting;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.encrypt.RSAUtils;
import com.miqian.mq.entity.AutoIdentyCard;
import com.miqian.mq.entity.AutoIdentyCardResult;
import com.miqian.mq.entity.Meta;
import com.miqian.mq.entity.UserInfo;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.WFYTitle;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * Created by Joy on 2015/9/22.
 */
public class BindCardActivity extends BaseActivity {
    private TextView  bindBankName, bindBankNumber;
    private EditText etCardNum;
    private View frameBank, frameTip;
    private UserInfo userInfo;
    private String bankNumber;
    private ImageView iconBank;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;

    @Override
    public void obtainData() {

    }

    @Override
    public void initView() {

        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).displayer(new RoundedBitmapDisplayer(0)).build();

        etCardNum = (EditText) findViewById(R.id.edit_bank_number);

        frameBank = findViewById(R.id.frame_bank);
        frameTip = findViewById(R.id.frame_tip);
        bindBankName = (TextView) findViewById(R.id.bind_bank_name);
        bindBankNumber = (TextView) findViewById(R.id.bind_bank_number);
        iconBank = (ImageView) findViewById(R.id.icon_bank);


        Intent intent = getIntent();
        userInfo = (UserInfo) intent.getSerializableExtra("userInfo");
        if (userInfo == null) {
            return;
        }
        String bindCardStatus = userInfo.getBindCardStatus();
        if ("1".equals(bindCardStatus)) {

            if (userInfo.getSupportStatus().equals("0")) {
                frameBank.setVisibility(View.VISIBLE);
                frameTip.setVisibility(View.VISIBLE);
                bankNumber = RSAUtils.decryptByPrivate(userInfo.getBankNo());
                if (!TextUtils.isEmpty(bankNumber) && bankNumber.length() > 4) {
                    bankNumber = "**** **** **** " + bankNumber.substring(bankNumber.length() - 4, bankNumber.length());
                }
                bindBankNumber.setText(bankNumber);
                bindBankName.setText(userInfo.getBankName());
                if (!TextUtils.isEmpty(userInfo.getBankUrlSmall())) {
                    imageLoader.displayImage(userInfo.getBankUrlSmall(), iconBank, options);
                }
            }
        }


    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_bindcard;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("绑定银行卡");

    }

    public void btn_click(View v) {

        final String cardNum = etCardNum.getText().toString();
        if (!TextUtils.isEmpty(cardNum)) {
            begin();
            HttpRequest.autoIdentifyBankCard(mActivity, new ICallback<AutoIdentyCardResult>() {
                @Override
                public void onSucceed(AutoIdentyCardResult result) {
                    end();
                    //绑定银行卡
                    AutoIdentyCard autoIdentyCard = result.getData();
                    String type = autoIdentyCard.getCardType();
                    String status = autoIdentyCard.getSupportStatus();
                    if (!TextUtils.isEmpty(type)) {
                        if ("2".equals(type)) {
                            //2为储蓄卡
                            if (!TextUtils.isEmpty(status)) {
                                if ("1".equals(status)) {
                                    bindCard(autoIdentyCard, cardNum);
                                } else {
                                    Uihelper.showToast(mActivity, "此储蓄卡不支持连连支付");
                                }
                            }

                        } else if ("3".equals(type)) {
                            //3为信用卡
                            Uihelper.showToast(mActivity, "信用卡不支持连连支付");
                        }
                    }

                }

                @Override
                public void onFail(String error) {
                    end();
                    Uihelper.showToast(mActivity, error);
                }
            }, cardNum);


        } else {
            Uihelper.showToast(mActivity, "银行卡号不能为空");
        }


    }

    private void bindCard(final AutoIdentyCard bankCardResult, final String cardNum) {
        final AutoIdentyCard autoIdentyCard = bankCardResult;
        if (bankCardResult != null) {
            HttpRequest.bindBank(mActivity, new ICallback<Meta>() {
                @Override
                public void onSucceed(Meta result) {

                    Intent intent = new Intent(mActivity, SetBankActivity.class);
                    Bundle extra = new Bundle();
                    intent.putExtra("cardNo", cardNum);
                    extra.putSerializable("userInfo", userInfo);
                    intent.putExtras(extra);
                    startActivity(intent);
                    finish();

                }
                @Override
                public void onFail(String error) {

                    Uihelper.showToast(mActivity,error);

                }
            }, cardNum, "XZ", autoIdentyCard.getBankCode(), autoIdentyCard.getBankName(), "", "", "");
        }

    }

    @Override
    protected String getPageName() {
        return "绑定银行卡";
    }

    class Clickable extends ClickableSpan implements View.OnClickListener {
        private final View.OnClickListener mListener;

        public Clickable(View.OnClickListener l) {
            mListener = l;
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(Color.rgb(251, 86, 67));
            ds.setUnderlineText(false);
        }
    }
}
