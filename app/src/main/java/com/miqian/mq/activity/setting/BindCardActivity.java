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
import com.miqian.mq.entity.BankCard;
import com.miqian.mq.entity.BankCardResult;
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
    private TextView textAgreement,bindBankName,bindBankNumber;
    private EditText etCardNum;
    private View frameBank,frameTip;
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

        textAgreement = (TextView) findViewById(R.id.text_agreement);
        etCardNum = (EditText) findViewById(R.id.edit_bank_number);

       frameBank=findViewById(R.id.frame_bank);
       frameTip=findViewById(R.id.frame_tip);
        bindBankName = (TextView) findViewById(R.id.bind_bank_name);
        bindBankNumber = (TextView) findViewById(R.id.bind_bank_number);
        iconBank=(ImageView)findViewById(R.id.icon_bank);

        SpannableString span = getAgreementSpan();
        textAgreement.setText(span);

        Intent intent = getIntent();
        userInfo = (UserInfo) intent.getSerializableExtra("userInfo");
         String bindCardStatus= userInfo.getBindCardStatus();
        if ("1".equals(bindCardStatus)){

            if (userInfo.getSupportStatus().equals("0")) {
                frameBank.setVisibility(View.VISIBLE);
                frameTip.setVisibility(View.VISIBLE);
                bankNumber = RSAUtils.decryptByPrivate(userInfo.getBankCardNo());
                if (!TextUtils.isEmpty(bankNumber) && bankNumber.length() > 4) {
                    bankNumber = "**** **** **** " + bankNumber.substring(bankNumber.length() - 4, bankNumber.length());
                }
                bindBankNumber.setText(bankNumber);
                bindBankName.setText(userInfo.getBankName());
                if (!TextUtils.isEmpty(userInfo.getBankUrlSmall())){
                    imageLoader.displayImage(userInfo.getBankUrlSmall(),iconBank,options);
                }
            }
        }


    }

    private SpannableString getAgreementSpan() {
        SpannableString spanableInfo = new SpannableString("同意《财旺旺服务协议》和《授权委托书》");

        spanableInfo.setSpan(new Clickable(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                    WebViewActivity.doIntent(mActivity, Urls.agreement_server, true, null);
            }
        }), 2, 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spanableInfo.setSpan(new Clickable(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                    WebViewActivity.doIntent(mActivity, Urls.agreement_entrust, true, null);
            }
        }), 12, 19, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanableInfo;
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
            mWaitingDialog.show();
            HttpRequest.autoIdentifyBankCard(mActivity, new ICallback<BankCardResult>() {
                @Override
                public void onSucceed(BankCardResult result) {
                    mWaitingDialog.dismiss();
                    //绑定银行卡
                    bindCard(result,cardNum);
                }

                @Override
                public void onFail(String error) {
                    mWaitingDialog.dismiss();
                    Uihelper.showToast(mActivity,error);
                }
            }, cardNum);


        } else {
            Uihelper.showToast(mActivity, "银行卡号不能为空");
        }


    }

    private void bindCard(final BankCardResult bankCardResult,String cardNum) {
           BankCard bankCard= bankCardResult.getData();
        if (bankCardResult!=null){
            HttpRequest.bindBank(mActivity, new ICallback<Meta>() {
                @Override
                public void onSucceed(Meta result) {

                    Intent intent=new Intent(mActivity,SetBankActivity.class);
                    Bundle extra=new Bundle();
                    extra.putSerializable("bankresult",bankCardResult.getData());
                    extra.putSerializable("userInfo",userInfo);
                    intent.putExtras(extra);
                    startActivity(intent);

                }

                @Override
                public void onFail(String error) {

                }
            },cardNum,"XZ",bankCard.getBankCode(),bankCard.getBankName(),"","","");
        }

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
