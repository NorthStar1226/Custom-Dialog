package com.zhangjq.luckydraw.view;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhangjq.luckydraw.R;
import com.zhangjq.luckydraw.adapter.CookiesRecyclerViewAdapter;
import com.zhangjq.luckydraw.util.MConstant;

import java.util.LinkedList;
import java.util.function.Consumer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import static com.zhangjq.luckydraw.MyApplication.getMmkv;

public class CookiesBookActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "CookiesBookActivity";

    private CookiesRecyclerViewAdapter mCookiesRecyclerViewAdapter;
    private LinkedList<String> cookLists;
    private Button mDeleteBtn;
    private Button mAddBtn;
    private Button mCancleBtn;
    private TextView mTitleTv;
    private RecyclerView mCookiesListRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cookiesboot);
        initView();
        cookLists = getCookLists();
        mCookiesRecyclerViewAdapter = new CookiesRecyclerViewAdapter(this, cookLists);
        mCookiesListRv.setAdapter(mCookiesRecyclerViewAdapter);
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
    }

    private LinkedList<String> getCookLists() {
        Gson gson = new Gson();
        LinkedList<String> lists = gson.fromJson(getMmkv().decodeString(MConstant.mmkv_key), new TypeToken<LinkedList<String>>() {
        }.getType());
        lists.forEach(new Consumer<String>() {
            @Override
            public void accept(String item) {
                Log.d(TAG, item);
            }
        });
        return lists;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_delete:
                // TODO 22/07/21
                break;
            case R.id.btn_add:
                // TODO 22/07/21
                break;
            case R.id.btn_cancle:
                // TODO 22/07/21
                break;
            default:
                break;
        }
    }
}