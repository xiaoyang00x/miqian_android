package com.miqian.mq.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.miqian.mq.R;

/**
 * Created by Administrator on 2015/9/1.
 */
public  abstract class Dialog_Login extends Dialog {
    private final Context mContext;

    public Dialog_Login(Context context) {
        super(context, R.style.Dialog);
        mContext=context;
        this.setContentView(R.layout.dialog_login);
        initView();
    }

    private void initView() {
        final View relaTelephone=findViewById(R.id.rela_telephone);
        final View relaPassword=findViewById(R.id.rela_password);
        final EditText editTelephone = (EditText) findViewById(R.id.edit_telephone);
        final  EditText editPassword = (EditText) findViewById(R.id.edit_password);
        Button btnLogin= (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String telephone= editTelephone.getText().toString();
               String password= editPassword.getText().toString();
                if (!TextUtils.isEmpty(telephone)){
                    if (!TextUtils.isEmpty(password)){
                        login(telephone,password);
                    }
                    else{
                        Toast.makeText(mContext,"电话号码不能为空",Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(mContext,"密码不能为空",Toast.LENGTH_SHORT).show();
                }

            } });
        editTelephone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus){
                        relaTelephone.setBackgroundResource(R.drawable.edit_pressed);
                    }else {
                        relaTelephone.setBackgroundResource(R.drawable.edit_normal);
                    }
            }
        });
        editPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    relaPassword.setBackgroundResource(R.drawable.edit_pressed);
                }else {
                    relaPassword.setBackgroundResource(R.drawable.edit_normal);
                }
            }
        });
    }

    public  abstract void login(String telephone, String password);
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

}
