package com.miqian.mq.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.current.ActivityCurrentRecord;
import com.miqian.mq.activity.current.ActivityProjectMath;
import com.miqian.mq.entity.FundFlow;
import com.miqian.mq.entity.UserCurrentData;
import com.miqian.mq.utils.FormatUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

/**
 * @author wgl
 * @description: 我的秒钱宝
 * @date: 16/10/24
 */
public class MyCurrentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private UserCurrentData userCurrentData;
    private Context mContext;

    public MyCurrentAdapter(Context mContext, UserCurrentData userCurrentData) {
        this.mContext = mContext;
        this.userCurrentData = userCurrentData;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CurrentHeadHolder(LayoutInflater.from(mContext).inflate(R.layout.item_my_current_header, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((CurrentHeadHolder) holder).bindData(userCurrentData.getUserCurrent());
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    private class CurrentHeadHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView textEarning;                   //昨日收益
        private TextView textCaptial;                   //在投本金
        private TextView textTotalEarning;              //总收益
        private TextView textInterest;                  //总收益
        private RelativeLayout frameCurrentRecord;      //资金记录
        private RelativeLayout frameProjectMatch;       //匹配项目

        private ImageLoader imageLoader;
        private DisplayImageOptions options;

        public CurrentHeadHolder(View itemView) {
            super(itemView);
            initView(itemView);
            imageLoader = ImageLoader.getInstance();
            options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).build();
        }

        private void initView(View itemView) {
            textEarning = (TextView) itemView.findViewById(R.id.earning);
            textCaptial = (TextView) itemView.findViewById(R.id.captial);
            textTotalEarning = (TextView) itemView.findViewById(R.id.total_earning);
            textInterest = (TextView) itemView.findViewById(R.id.text_interest);

            frameCurrentRecord = (RelativeLayout) itemView.findViewById(R.id.frame_current_record);
            frameProjectMatch = (RelativeLayout) itemView.findViewById(R.id.frame_project_match);

            frameCurrentRecord.setOnClickListener(this);
            frameProjectMatch.setOnClickListener(this);
        }

        public void bindData(UserCurrentData.UserCurrent userCurrent) {
            if (userCurrent == null) return;
            textEarning.setText(FormatUtil.formatAmount(userCurrent.getYesterdayInterest()));
            textCaptial.setText(FormatUtil.formatAmount(userCurrent.getPrnAmt()));
            textTotalEarning.setText(FormatUtil.formatAmount(userCurrent.getRegIncome()));

            String tempInterest = userCurrent.getYearInterest().toString();
            if (TextUtils.isEmpty(tempInterest)) {
                textInterest.setVisibility(View.GONE);
            } else {
                textInterest.setVisibility(View.VISIBLE);
                textInterest.setText(tempInterest + "%");
            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.frame_current_record:
                    MobclickAgent.onEvent(v.getContext(), "1035");
                    ActivityCurrentRecord.startActivity(v.getContext(), FundFlow.BILL_TYPE_ALL, "3");
                    break;
                case R.id.frame_project_match:
                    v.getContext().startActivity(new Intent(v.getContext(), ActivityProjectMath.class));
                    break;
                default:
                    break;
            }
        }

    }

}
