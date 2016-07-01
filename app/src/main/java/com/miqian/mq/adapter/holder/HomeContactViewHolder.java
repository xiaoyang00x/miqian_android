package com.miqian.mq.adapter.holder;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.miqian.mq.R;
import com.miqian.mq.entity.HomePageInfo;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by guolei_wang on 16/5/24.
 * 首页客服电话
 */
public class HomeContactViewHolder extends HomeBaseViewHolder {
    private TextView tv_tel;

    public HomeContactViewHolder(View itemView) {
        super(itemView);

        tv_tel = (TextView) itemView.findViewById(R.id.tv_tel);
    }

    @Override
    public void bindView(final HomePageInfo mData) {
        tv_tel.setText(mData.getConsumerHotLine());
        tv_tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MobclickAgent.onEvent(view.getContext(), "1004_4");
                if (ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    MobclickAgent.onEvent(view.getContext(), "1004_5");
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    Toast.makeText(view.getContext(),"您尚未开启通话权限，请开启后再尝试。", Toast.LENGTH_SHORT).show();
                    return;
                }
                MobclickAgent.onEvent(view.getContext(), "1004_6");
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mData.getConsumerHotLine()));
                view.getContext().startActivity(intent);
            }
        });
    }
}
