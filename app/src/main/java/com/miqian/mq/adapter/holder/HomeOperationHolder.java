package com.miqian.mq.adapter.holder;

import android.view.View;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.entity.HomeOperationData;
import com.miqian.mq.entity.HomePageInfo;
import com.miqian.mq.utils.FormatUtil;

/**
 * Created by guolei_wang on 16/5/24.
 * 首页运营数据
 */
public class HomeOperationHolder extends HomeBaseViewHolder {

    private TextView tv_register;
    private TextView tv_operation_days;
    private View layout_register;
    private View layout_operation_days;

    /**
     * 此版本不需要累计收益和成交额
     *
      private View layout_volume;
     private View layout_earnings;
     private TextView tv_volume;
     private TextView tv_earnings;
     */


    public HomeOperationHolder(View itemView) {
        super(itemView);

        layout_register = itemView.findViewById(R.id.layout_register);
//        layout_volume = itemView.findViewById(R.id.layout_volume);
//        layout_earnings = itemView.findViewById(R.id.layout_earnings);
        layout_operation_days = itemView.findViewById(R.id.layout_operation_days);
        tv_register = (TextView)itemView.findViewById(R.id.tv_register);
//        tv_volume = (TextView)itemView.findViewById(R.id.tv_volume);
//        tv_earnings = (TextView)itemView.findViewById(R.id.tv_earnings);
        tv_operation_days = (TextView)itemView.findViewById(R.id.tv_operation_days);


    }

    @Override
    public void bindView(HomePageInfo mData) {
        layout_register.setVisibility(View.GONE);
//        layout_volume.setVisibility(View.GONE);
//        layout_earnings.setVisibility(View.GONE);
        layout_operation_days.setVisibility(View.GONE);

        if(mData != null && mData.getOperationDatas() != null) {

            if(mData.getOperationDatas().size() > 0) {
                for(int i = 0; i < mData.getOperationDatas().size(); i++) {
                    HomeOperationData operationData = mData.getOperationDatas().get(i);
                    switch (operationData.getType()) {
                        case HomeOperationData.TYPE_REGISTER:
                            tv_register.setText(FormatUtil.formatAmountStr(operationData.getValue()));
                            layout_register.setVisibility(View.VISIBLE);
                            break;
//                        case HomeOperationData.TYPE_VOLUME:
//                            tv_volume.setText(FormatUtil.formatAmountStr(operationData.getValue()));
//                            layout_volume.setVisibility(View.VISIBLE);
//                            break;
//                        case HomeOperationData.TYPE_EARNINGS:
//                            tv_earnings.setText(FormatUtil.formatAmountStr(operationData.getValue()));
//                            layout_earnings.setVisibility(View.VISIBLE);
//                            break;
                        case HomeOperationData.TYPE_OPERATION_DAYS:
                            tv_operation_days.setText(FormatUtil.formatAmountStr(operationData.getValue()));
                            layout_operation_days.setVisibility(View.VISIBLE);
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }

}
