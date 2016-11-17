package com.miqian.mq.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.WebActivity;
import com.miqian.mq.activity.current.SubscriptionRecordsActivity;
import com.miqian.mq.entity.CurrentMathProjectData;
import com.miqian.mq.net.Urls;
import com.miqian.mq.utils.FormatUtil;

import java.util.ArrayList;

/**
 * @author wgl
 * @description: 我的秒钱宝-项目匹配适配器
 * @date: 16/11/16
 */
public class ProjectMathAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_HEAD = 0;
    public static final int TYPE_PROJECT = 1;

    private ArrayList<CurrentMathProjectData.MatchProject> mList; // 所有item
    private CurrentMathProjectData currentMathProjectData;
    private Context mContext;

    public ProjectMathAdapter(Context mContext, CurrentMathProjectData currentMathProjectData) {
        this.mContext = mContext;
        this.currentMathProjectData = currentMathProjectData;
        mList = currentMathProjectData.getProjectList();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CurrentListHolder(LayoutInflater.from(mContext).inflate(R.layout.item_my_current_match, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((CurrentListHolder) holder).bindData(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return null == mList? 0 : mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0? TYPE_HEAD : TYPE_PROJECT;
    }



    private class CurrentListHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private View itemView;
        private TextView tv_name;                               // 名称
        private TextView tv_principal;                          // 本金
        private TextView tv_view_notes;                         // 查看认购记录
        private CurrentMathProjectData.MatchProject info;

        public CurrentListHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            initView();
        }

        private void initView() {
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_principal = (TextView) itemView.findViewById(R.id.tv_principal);
            tv_view_notes = (TextView) itemView.findViewById(R.id.tv_view_notes);
        }

        public void bindData(final CurrentMathProjectData.MatchProject info) {
            if(info == null) return;
            this.info = info;
            tv_name.setText(info.getProjectName());
            tv_principal.setText(FormatUtil.formatAmount(info.getRemainAmount()));
            tv_view_notes.setOnClickListener(this);
            tv_name.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_view_notes:
                    SubscriptionRecordsActivity.startActivity(v.getContext(), info.getProjectCode());
                    break;
                case R.id.tv_name:
                    WebActivity.startActivity(v.getContext(), Urls.web_math_project_details + info.getProjectCode());
                    break;
                default:
                    break;
            }
        }

    }

}
