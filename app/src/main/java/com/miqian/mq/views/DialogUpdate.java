package com.miqian.mq.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.entity.UpdateInfo;

/**
 * Created by Jackie on 2016/1/11.
 * 版本强制更新
 */
public abstract class DialogUpdate extends Dialog {

    private UpdateInfo updateInfo;

    public abstract void updateClick(String url);

    public DialogUpdate(Context context, UpdateInfo updateInfo) {
        super(context, R.style.Dialog);
        this.updateInfo = updateInfo;
        this.setContentView(R.layout.dialog_update);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView titleText = (TextView) findViewById(R.id.title_update);
        titleText.setText(updateInfo.getTitle());

        TextView contentText = (TextView) findViewById(R.id.umeng_update_content);
        String updateContent = updateInfo.getVersionDesc() + "\n" + updateInfo.getSoftDesc() + "\n\n" + updateInfo.getUpgradeDesc();
        contentText.setText(updateContent);

        Button cancel = (Button) findViewById(R.id.umeng_update_id_cancel);
        cancel.setVisibility(View.GONE);

        Button  updateButton = (Button) findViewById(R.id.umeng_update_id_ok);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateClick(updateInfo.getDownloadUrl());
            }
        });
    }
}
