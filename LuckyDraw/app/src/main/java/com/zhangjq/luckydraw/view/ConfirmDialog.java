package com.zhangjq.luckydraw.view;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zhangjq.luckydraw.R;

/**
 * 创建日期：2022/7/21 17:35
 *
 * @author zhangjq
 * @version 1.0
 * 包名： com.zhangjq.luckydraw.view
 * 类说明：
 */

public class ConfirmDialog extends AlertDialog implements View.OnClickListener {
    EditText mEtPasswd;
    Button mBtnCancel, mBtnConfirm;
    Context mContext;
    public ConfirmDialog(Context context) {
        super(context);
        mContext = context;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_layout);
        mEtPasswd = (EditText) findViewById(R.id.et_cook_name);
        //保证EditText能弹出键盘
        this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        this.setCancelable(false);
        mBtnCancel = (Button) findViewById(R.id.btn_cancel);
        mBtnCancel.setOnClickListener(this);
        mBtnConfirm = (Button) findViewById(R.id.btn_confirm);
        mBtnConfirm.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                this.dismiss();
                break;
            case R.id.btn_confirm:
                if (TextUtils.isEmpty(mEtPasswd.getText())) {
                    Toast.makeText(mContext, "密码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    this.dismiss();
                    Toast.makeText(mContext, mEtPasswd.getText().toString(), Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private CookiesDialogInterface mCookiesDialogInterface;
    public interface CookiesDialogInterface {
        void addCookies(String cookieName);
    }

    public void setAddCookiesListener(CookiesDialogInterface listener){
        mCookiesDialogInterface = listener;
    }
}
