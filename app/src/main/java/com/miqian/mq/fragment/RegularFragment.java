package com.miqian.mq.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.views.RegularProjectView;
import com.miqian.mq.views.RegularTransferView;

/**
 * Created by guolei_wang on 15/9/16.
 */
public class RegularFragment extends BasicFragment {

    private TextView tv_regular_project; // 定期项目标题
    private TextView tv_regular_transfer; // 定期转让标题
    private View title_line; // 定期项目 定期转让标题选中后下面显示的红色横线

    private final int LEFTPAGE = 0x01; // 定期项目 被选中
    private final int RIGHTPAGE = 0x02; // 定期转让 被选中
    private int curSwitch = LEFTPAGE; // 当前已选中

    private FrameLayout content;

    private RegularProjectView regularProjectView; // 定期项目内容页面
    private RegularTransferView regularTransferView; // 定期转让内容页面

    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null == rootView) {
            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rootView = mInflater.inflate(R.layout.fragment_regular, null);
            initView();
            initListener();
            switchTitleTo(curSwitch);
            switchContentTo(curSwitch);
        }
        obtainData(curSwitch);
        return rootView;
    }

    private void initView() {
        tv_regular_project = (TextView) rootView.findViewById(R.id.tv_regular_project);
        tv_regular_transfer = (TextView) rootView.findViewById(R.id.tv_regular_transfer);
        title_line = rootView.findViewById(R.id.title_line);
        content = (FrameLayout) rootView.findViewById(R.id.content);
    }

    private void initListener() {
        tv_regular_project.setOnClickListener(mOnclicklistener);
        tv_regular_transfer.setOnClickListener(mOnclicklistener);
    }

    private View.OnClickListener mOnclicklistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_regular_project:
                    if (curSwitch != LEFTPAGE) {
                        switchTitleTo(LEFTPAGE);
                        switchContentTo(LEFTPAGE);
                    }
                    break;
                case R.id.tv_regular_transfer:
                    if (curSwitch != RIGHTPAGE) {
                        switchTitleTo(RIGHTPAGE);
                        switchContentTo(RIGHTPAGE);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    // 标题切换
    private void switchTitleTo(int curSwitch) {
        this.curSwitch = curSwitch;
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) title_line.getLayoutParams();
        switch (curSwitch) {
            case LEFTPAGE:
                tv_regular_project.setAlpha(1.0f);
                tv_regular_transfer.setAlpha(0.7f);
                params.addRule(RelativeLayout.ALIGN_LEFT, R.id.tv_regular_project);
                params.addRule(RelativeLayout.ALIGN_RIGHT, R.id.tv_regular_project);
                break;
            case RIGHTPAGE:
                tv_regular_transfer.setAlpha(1.0f);
                tv_regular_project.setAlpha(0.7f);
                params.addRule(RelativeLayout.ALIGN_LEFT, R.id.tv_regular_transfer);
                params.addRule(RelativeLayout.ALIGN_RIGHT, R.id.tv_regular_transfer);
                break;
            default:
                break;
        }
        title_line.setLayoutParams(params);
    }

    // 内容切换
    private void switchContentTo(int curSwitch) {
        switch (curSwitch) {
            case LEFTPAGE:
                if (null == regularProjectView) {
                    regularProjectView = new RegularProjectView(mContext);
                    content.addView(regularProjectView.getView());
                }
                regularProjectView.setVisibility(View.VISIBLE);
                if (null != regularTransferView) {
                    regularTransferView.setVisibility(View.GONE);
                }
                break;
            case RIGHTPAGE:
                if (null == regularTransferView) {
                    regularTransferView = new RegularTransferView(mContext);
                    content.addView(regularTransferView.getView());
                }
                regularTransferView.setVisibility(View.VISIBLE);
                if (null != regularProjectView) {
                    regularProjectView.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }
    }

    // 刷新当前页面数据(预留)
    private void obtainData(int curSwitch) {
        switch (curSwitch) {
            case LEFTPAGE:
                if (null != regularProjectView) {
                }
                break;
            case RIGHTPAGE:
                if (null != regularTransferView) {
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected String getPageName() {
        return "定期首页";
    }

}
