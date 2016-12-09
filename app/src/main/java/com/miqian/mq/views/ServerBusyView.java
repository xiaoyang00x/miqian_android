package com.miqian.mq.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.activity.WebActivity;
import com.miqian.mq.utils.MobileOS;

/**
 * Created by wangduo on 16/1/23.
 * 服务器繁忙显示页面－新增了无网络显示页面
 */
public class ServerBusyView extends ScrollView {

    private Context mContext;
    private IRequestAgainListener requestAgainListener;

    private ImageView iv_houzi;
    private TextView tv_tip;

    private static final String SERVERBUSY = "秒钱被财主们挤爆啦，请您稍后再来访问。\n万分对不起您，耽误您赚钱了！";
    private static final String NONETWORK = SERVERBUSY;

    public ServerBusyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    private Button btn_refresh;
    private TextView tv_lookAround1;
    private TextView tv_lookAround2;
    private TextView tv_lookAround3;
    private TextView tv_lookAround4;

    public void init() {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.server_busy, this, false);
        iv_houzi = (ImageView) mView.findViewById(R.id.iv_houzi);
        tv_tip = (TextView) mView.findViewById(R.id.tv_tip);
        btn_refresh = (Button) mView.findViewById(R.id.btn_refresh);
        tv_lookAround1 = (TextView) mView.findViewById(R.id.tv_lookAround1);
        tv_lookAround2 = (TextView) mView.findViewById(R.id.tv_lookAround2);
        tv_lookAround3 = (TextView) mView.findViewById(R.id.tv_lookAround3);
        tv_lookAround4 = (TextView) mView.findViewById(R.id.tv_lookAround4);
        addView(mView);
    }

    // 服务器繁忙页面 - 显示
    public void showServerBusy() {
        tv_tip.setText(SERVERBUSY);
        iv_houzi.setImageResource(R.drawable.icon_page_error);
        setVisibility(View.VISIBLE);
    }

    // 无网络页面 - 显示
    public void showNoNetwork() {
        tv_tip.setText(NONETWORK);
        iv_houzi.setImageResource(R.drawable.icon_page_error);
        setVisibility(View.VISIBLE);
    }

    // 服务器繁忙页面 - 隐藏
    public void hide() {
        setVisibility(View.GONE);
    }

    public void setListener(IRequestAgainListener requestAgainListener) {
        this.requestAgainListener = requestAgainListener;
        btn_refresh.setOnClickListener(mOnClickListener);
        tv_lookAround1.setOnClickListener(mOnClickListener);
        tv_lookAround2.setOnClickListener(mOnClickListener);
        tv_lookAround3.setOnClickListener(mOnClickListener);
        tv_lookAround4.setOnClickListener(mOnClickListener);
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_refresh:
                    if (null != requestAgainListener) {
                        requestAgainListener.request();
                    }
                    break;
                case R.id.tv_lookAround1:
                    WebActivity.startActivity(getContext(), "file:///android_asset/know-mq.html");
                    break;
                case R.id.tv_lookAround2:
                    WebActivity.startActivity(getContext(), "file:///android_asset/risk-control.html");
                    break;
                case R.id.tv_lookAround3:
                    WebActivity.startActivity(getContext(), "file:///android_asset/mq-property.html");
                    break;
                case R.id.tv_lookAround4:
                    WebActivity.startActivity(getContext(), "file:///android_asset/know-more.html");
                    break;
            }
        }
    };

    public interface IRequestAgainListener {
        void request();
    }

}
