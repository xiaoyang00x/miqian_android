package com.miqian.mq.views;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.miqian.mq.R;

/**
 * @author Tuliangtan
 */
public class ProgressDialogView {

    private View view = null;
    private Dialog dialog = null;

    public ProgressDialogView(Context mContext) {
        view = LayoutInflater.from(mContext).inflate(R.layout.progressdialog_layout, null);
        ImageView iv_animation = (ImageView) view.findViewById(R.id.iv_animation);
        iv_animation.setImageResource(R.drawable.animation_loading);
        AnimationDrawable animationDrawable = (AnimationDrawable) iv_animation.getDrawable();
        animationDrawable.start();

        dialog = new Dialog(mContext, R.style.loading_dialog);
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
//		lp.width = Uihelper.px2dip(context, 80); // 宽度
//		lp.height = Uihelper.px2dip(context, 80); // 高度
        lp.alpha = 0.8f; // 透明度
        dialogWindow.setAttributes(lp);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialogWindow.setContentView(view, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    public void show() {
        if (dialog != null) {
            dialog.show();
        }
    }

    public void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public void destroy() {
        view = null;
        dialog = null;
    }

}
