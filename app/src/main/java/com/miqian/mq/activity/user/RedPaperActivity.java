package com.miqian.mq.activity.user;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.marshalchen.ultimaterecyclerview.divideritemdecoration.HorizontalDividerItemDecoration;
import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.adapter.AdapterMyRedPaper;
import com.miqian.mq.entity.RedPaperData;
import com.miqian.mq.entity.Redpaper;
import com.miqian.mq.net.HttpRequest;
import com.miqian.mq.net.ICallback;
import com.miqian.mq.utils.Uihelper;
import com.miqian.mq.views.WFYTitle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/10/8.
 */
public class RedPaperActivity extends BaseActivity{
    private RecyclerView recyclerView;
    private ArrayList<Redpaper.CustPromotion> promList;
    private AdapterMyRedPaper adapterPacket;

    @Override
    public void obtainData() {
        mWaitingDialog.show();
        HttpRequest.getCustPromotion(mActivity, new ICallback<RedPaperData>() {
            public List<Redpaper.CustPromotion> promList;

            @Override
            public void onSucceed(RedPaperData result) {
                mWaitingDialog.dismiss();
             Redpaper  redpaper= result.getData();
                if (redpaper!=null){
                    promList= redpaper.getCustPromotion();
                    adapterPacket = new AdapterMyRedPaper(promList);
                    recyclerView.setAdapter(adapterPacket);
                }

            }

            @Override
            public void onFail(String error) {
                mWaitingDialog.dismiss();
                Uihelper.showToast(mActivity,error);

            }
        },"HB","","1","999999");

    }
    @Override
    public void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).colorResId(R.color.mq_b4).size(1).marginResId(R.dimen.margin_left_right).build());
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_myredpaper;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("红包");
        mTitle.setRightText("使用规则");
        mTitle.setOnRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

}
