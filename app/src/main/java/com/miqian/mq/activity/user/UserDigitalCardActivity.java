package com.miqian.mq.activity.user;

import android.content.Context;
import android.graphics.Paint;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.entity.BankCardInfo;
import com.miqian.mq.entity.BankCardInfoResult;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.WFYTitle;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserDigitalCardActivity extends BaseActivity {
    @BindView(R.id.tv_name)
    TextView  tvName;
    @BindView(R.id.tv_cardno)
    TextView  tvCardNo;
    @BindView(R.id.tv_openbank)
    TextView  tvOpenbank;
    @BindView(R.id.tv_copy)
    TextView  tvCopy;
    @BindView(R.id.view_content)
    View  viewContent;
    private String electronicNo;


    @Override
    public void obtainData() {
        begin();
        HttpRequest.jxCustCardInfo(mActivity, new ICallback<BankCardInfoResult>() {
            @Override
            public void onSucceed(BankCardInfoResult result) {
                end();
                BankCardInfo data= result.getData();
                    if (data!=null){
                        viewContent.setVisibility(View.VISIBLE);
                        tvName.setText(data.getCustName());
                        tvOpenbank.setText(data.getBankNo());
                        electronicNo = data.getElectronicNo();
                        tvCardNo.setText(electronicNo);
                    }
            }

            @Override
            public void onFail(String error) {
                 end();
                Uihelper.showToast(mActivity,error);
            }
        });
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        tvCopy.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        tvCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 //复制卡号
                // 从API11开始android推荐使用android.content.ClipboardManager
                // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                if (!TextUtils.isEmpty(electronicNo)){
                    cm.setText(electronicNo);
                    Uihelper.showToast(mActivity,"卡号复制成功");
                }else {
                    Uihelper.showToast(mActivity,"卡号不存在");
                }
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_digitalcard;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("我的电子帐户");

    }

    @Override
    protected String getPageName() {
        return "我的电子帐户";
    }
}
