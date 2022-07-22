package com.zhangjq.luckydraw.view;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhangjq.luckydraw.R;
import com.zhangjq.luckydraw.adapter.CookiesRecyclerViewAdapter;
import com.zhangjq.luckydraw.util.MConstant;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.function.Consumer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.zhangjq.luckydraw.MyApplication.getMmkv;

public class CookiesBookActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "CookiesBookActivity";

    private CookiesRecyclerViewAdapter mCookiesRecyclerViewAdapter;
    private LinkedList<String> cookLists;
    private LinkedList<String> waitDeleteLists, deletedLists;
    private Button mDeleteBtn;
    private Button mAddBtn;
    private Button mCancleBtn;
    private TextView mTitleTv;
    private RecyclerView mCookiesListRv;
    private Gson gson;
    private AlertDialog deleteDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cookiesboot);
        initView();
        cookLists = getCookLists();
        waitDeleteLists = new LinkedList<>();
        deletedLists = new LinkedList<>();
        mCookiesRecyclerViewAdapter = new CookiesRecyclerViewAdapter(this, cookLists);
        mCookiesListRv.setAdapter(mCookiesRecyclerViewAdapter);
        checkBoxListener();
    }

    private void initView() {
        mDeleteBtn = (Button) findViewById(R.id.btn_delete);
        mDeleteBtn.setOnClickListener(this);
        mAddBtn = (Button) findViewById(R.id.btn_add);
        mAddBtn.setOnClickListener(this);
        mCancleBtn = (Button) findViewById(R.id.btn_cancle);
        mCancleBtn.setOnClickListener(this);
        mTitleTv = (TextView) findViewById(R.id.tv_title);
        mCookiesListRv = (RecyclerView) findViewById(R.id.rv_cookies_list);
        mCookiesListRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mCookiesListRv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    private void checkBoxListener() {
        mCookiesRecyclerViewAdapter.setOnItemCheckedChangeListener(new CookiesRecyclerViewAdapter.OnItemCheckedChangeListener() {
            @Override
            public void onItemCheckedChange(View view, int position, String value, boolean isChecked) {
                if (isChecked) {
                    Log.d(TAG, "选中了 " + cookLists.get(position));
                    waitDeleteLists.add(value);
                } else {
                    Log.d(TAG, "取消选中 " + cookLists.get(position));
                    waitDeleteLists.remove(value);
                }
            }
        });
    }

    private LinkedList<String> getCookLists() {
        gson = new Gson();
        LinkedList<String> lists = gson.fromJson(getMmkv().decodeString(MConstant.mmkv_key), new TypeToken<LinkedList<String>>() {
        }.getType());
        if (lists != null) {
            lists.forEach(new Consumer<String>() {
                @Override
                public void accept(String item) {
                    Log.d(TAG, "菜名：" + item);
                }
            });
            return lists;
        }
        lists = new LinkedList<String>();
        lists.add("aaa");
        lists.add("bbb");
        lists.add("ccc");
        lists.add("ddd");
        lists.add("eee");
        lists.add("fff");
        lists.add("ggg");
        return lists;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_delete:
                deleteTipsDialog();
                break;
            case R.id.btn_add:
                showAddDialog();
                break;
            case R.id.btn_cancle:
                finish();
                break;
            default:
                break;
        }
    }

    private void showAddDialog() {
        //点击弹出对话框
        final AddCookiesDialog addDialog = new AddCookiesDialog(this);
        addDialog.setYesOnclickListener("确定", new AddCookiesDialog.onYesOnclickListener() {
            @Override
            public void onYesClick(String name) {
                if (TextUtils.isEmpty(name)) {
                    setDialogIsShowing(addDialog, false);
                    Toast.makeText(CookiesBookActivity.this, "菜名不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    cookLists.add(name);
                    mCookiesRecyclerViewAdapter.notifyDataSetChanged();
                    setDialogIsShowing(addDialog, true);
                    addDialog.dismiss();
                }
            }
        });
        addDialog.setNoOnclickListener("取消", new AddCookiesDialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                addDialog.dismiss();
            }
        });
        addDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getMmkv().encode(MConstant.mmkv_key, gson.toJson(cookLists));
    }

    private void deleteTipsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("删除菜名")
                .setMessage("确认删除勾选项？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (waitDeleteLists != null && cookLists != null) {
                            Collections.sort(waitDeleteLists, Collections.reverseOrder());
                            for (int i = 0; i < waitDeleteLists.size(); i++) {
/*                                int index = waitDeleteLists.get(i);
                                Log.d(TAG, "删除了(" + index + ")，即" + cookLists.get(index));*/
                                cookLists.remove(waitDeleteLists.get(i));
                            }
                            mCookiesRecyclerViewAdapter.notifyDataSetChanged();
                            waitDeleteLists.clear();
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteDialog.dismiss();
                    }
                });
        deleteDialog = builder.create();
        deleteDialog.show();
    }

    /**
     * 设置对话框是否显示
     *
     * @param dialog  对话框
     * @param isClose 是否显示. true为关闭，false为不关闭
     */
    private void setDialogIsShowing(DialogInterface dialog, boolean isClose) {
        try {
            // 获取到android.app.Dialog类
            Field mShowing = dialog.getClass().getSuperclass().getSuperclass().getDeclaredField("mShowing");
            mShowing.setAccessible(true); // 设置可访问
            mShowing.set(dialog, isClose); // 设置是否关闭
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}