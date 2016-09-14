package com.miqian.mq.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.entity.UpdateInfo;
import com.miqian.mq.utils.Pref;

/**
 * Created by Jackie on 2016/1/11.
 * 版本更新 1：建议更新， 2：强制更新
 */
public abstract class DialogUpdate extends Dialog {

    private UpdateInfo updateInfo;
    private Context mContext;
    private Button ignore;

    public abstract void updateClick(String url);

    public DialogUpdate(Context context, UpdateInfo updateInfo) {
        super(context, R.style.Dialog);
        this.mContext = context;
        this.updateInfo = updateInfo;
        this.setContentView(R.layout.dialog_update);
        initViewCode();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initViewCode() {
        TextView titleText = (TextView) findViewById(R.id.title_update);
        titleText.setText(updateInfo.getTitle());

        TextView contentText = (TextView) findViewById(R.id.umeng_update_content);
        String updateContent = updateInfo.getVersionDesc() + "\n" + updateInfo.getSoftDesc() + "\n\n" + updateInfo.getUpgradeDesc();
        contentText.setText(updateContent);

        ignore = (Button) findViewById(R.id.umeng_update_id_ignore);
        Button cancel = (Button) findViewById(R.id.umeng_update_id_cancel);
        if ("2".equals(updateInfo.getUpgradeSign())) {
            cancel.setVisibility(View.GONE);
        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        ignore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pref.saveString(Pref.IGNORE_VERSION, updateInfo.getVersion(), mContext);
                dismiss();
            }
        });

        Button  updateButton = (Button) findViewById(R.id.umeng_update_id_ok);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateClick(updateInfo.getDownloadUrl());
            }
        });
    }

    public void setIgnoreVisility(int visibility) {
        if (ignore != null) {
            ignore.setVisibility(visibility);
        }
    }
}
