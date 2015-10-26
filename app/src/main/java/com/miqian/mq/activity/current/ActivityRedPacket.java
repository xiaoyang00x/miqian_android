package com.miqian.mq.activity.current;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.marshalchen.ultimaterecyclerview.divideritemdecoration.HorizontalDividerItemDecoration;
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
public class ActivityRedPacket extends BaseActivity implements View.OnClickListener, AdapterPacket.MyItemClickListener {

    private RecyclerView recyclerView;
    private Button btConfirm;

    private ProducedOrder producedOrder;
    private List<Promote> promList;

    private int position = -1;

    @Override
    public void onCreate(Bundle bundle) {
        Intent intent = getIntent();
        producedOrder = JsonUtil.parseObject(intent.getStringExtra("producedOrder"), ProducedOrder.class);
        position = intent.getIntExtra("position", -1);
        promList = producedOrder.getPromList();
        super.onCreate(bundle);
    }

    @Override
    public void obtainData() {

    }

    @Override
    public void initView() {
        btConfirm = (Button) findViewById(R.id.bt_confirm);
        btConfirm.setOnClickListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).colorResId(R.color.mq_b4).size(1).build());

        AdapterPacket adapterPacket = new AdapterPacket(promList);
        adapterPacket.setOnItemClickListener(this);
        adapterPacket.setPosition(position);
        recyclerView.setAdapter(adapterPacket);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_red_packet;
    }

    @Override
    public void initTitle(WFYTitle mTitle) {
        mTitle.setTitleText("红包/拾财券");
        mTitle.setRightText("使用规则");
    }

    @Override
    protected String getPageName() {
        return "红包/拾财券";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_confirm:
                Intent intent = new Intent();
                intent.putExtra("position", position);
                setResult(CurrentInvestment.SUCCESS, intent);
                ActivityRedPacket.this.finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(View view, int postion) {
        position = postion;
    }
}
