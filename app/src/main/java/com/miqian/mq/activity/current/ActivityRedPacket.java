package com.miqian.mq.activity.current;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.miqian.mq.R;
import com.miqian.mq.activity.BaseActivity;
import com.miqian.mq.adapter.AdapterPacket;
import com.miqian.mq.entity.ProducedOrder;
import com.miqian.mq.entity.Promote;
import com.miqian.mq.utils.JsonUtil;
import com.miqian.mq.views.WFYTitle;

import java.util.List;

/**
 * Created by Jackie on 2015/9/25.
 */
public class ActivityRedPacket extends BaseActivity {

    private RecyclerView recyclerView;
    private ProducedOrder producedOrder;
    private List<Promote> promList;


    @Override
    public void onCreate(Bundle bundle) {
        Intent intent = getIntent();
        producedOrder = JsonUtil.parseObject(intent.getStringExtra("producedOrder"), ProducedOrder.class);
        promList = producedOrder.getPromList();
        super.onCreate(bundle);
    }

    @Override
    public void obtainData() {

    }

    @Override
    public void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

//        recyclerView.addItemDecoration();
        AdapterPacket adapterPacket = new AdapterPacket(promList);
        recyclerView.setAdapter(adapterPacket);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_red_packet;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("红包|拾财券");
    }
}
