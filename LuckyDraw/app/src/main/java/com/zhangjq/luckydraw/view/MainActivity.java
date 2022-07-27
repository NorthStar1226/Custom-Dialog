package com.zhangjq.luckydraw.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zhangjq.luckydraw.R;
import com.zhangjq.luckydraw.util.ThreadPoolExecutorUtil;

import java.lang.ref.WeakReference;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import static com.zhangjq.luckydraw.MyApplication.getCookiesLists;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private final int START_UPDATE_VIEW = 1;
    private final int STOP_UPDATE_VIEW = 0;

    private RandomLayout mContainer;
    private Random random;
    private ViewHanler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        random = new Random();
        //获取Looper并传递
        handler = new ViewHanler(Looper.myLooper(), this);
        initView();
        startThreadPool();
    }

    private void initView() {
        mContainer = (RandomLayout) findViewById(R.id.container);
        mContainer.btn_start.setOnClickListener(this);
/*        mContainer.setOnRandomItemClickListener(new RandomLayout.OnRandomItemClickListener() {
            @Override
            public void onRandomItemClick(View view, Object o) {
                Toast.makeText(MainActivity.this, String.valueOf(o), Toast.LENGTH_SHORT).show();
                mContainer.removeSpecificViewFromList(view);
            }
        });
        */
    }

    private void loadSomeView() {
        for (int i = 0; i <= 20; i++) {
            TextView textView = new TextView(this);
            textView.setText(randomLoadData());
            textView.setTextSize((random.nextFloat() * 20 + 10));
            if (random.nextBoolean()) {
                textView.setEms(1);
            }
            if (TextUtils.isEmpty(textView.getText().toString())) {
                mContainer.tv_tips.setText("菜单都是空的");
                return;
            }
            mContainer.addViewAtRandomXY(textView, "这个是textView" + i);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        alive=false;
        ThreadPoolExecutorUtil.stop();
    }

    private void randomCreateView() {
        TextView textView = new TextView(this);
        String cookiesName = randomLoadData();
        textView.setText(cookiesName);
        textView.setTextSize((random.nextFloat() * 20 + 10));
        if (random.nextBoolean()) {
            textView.setEms(1);
        }
        if (TextUtils.isEmpty(textView.getText().toString())) {
            mContainer.tv_tips.setText("菜单都是空的");
            return;
        }
        mContainer.addViewAtRandomXY(textView, "这个是textView " + cookiesName);
    }

    private void randomRemoveView() {
        mContainer.removeRandomViewFromList();
    }

    private String randomLoadData() {
        int indexMin = 0;
        int indexMax = getCookiesLists().size() - 1;
        if (indexMax > indexMin) {
            int indexRandom = random.nextInt(indexMax) % (indexMax - indexMin + 1) + indexMin;
            Log.d(TAG, "随机索引：" + indexRandom + " , 填充菜名：" + getCookiesLists().get(indexRandom).getCookiesName());
            return getCookiesLists().get(indexRandom).getCookiesName();
        } else {
            return "";
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.addMenu) {
            Intent intent = new Intent(MainActivity.this, CookiesBookActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        } else {
            Toast.makeText(this, "关闭文件", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean alive = true, running = false;

    private void startThreadPool() {
        ThreadPoolExecutorUtil.exec(new Runnable() {
            @Override
            public void run() {
                while (alive) {
                    while (isStart) {
                        handler.sendEmptyMessage(START_UPDATE_VIEW);
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    private boolean isStart = false;
    private int randomCount = 0;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                Toast.makeText(MainActivity.this, "调用MainActivity点击监听", Toast.LENGTH_SHORT).show();
                if (isStart) {
                    mContainer.btn_start.setText("开始");
                    isStart = false;
                    handler.sendEmptyMessage(STOP_UPDATE_VIEW);
                    randomCount++;
                } else {
                    if (randomCount > 0 && randomCount % 3 == 0) {
                        mContainer.tv_tips.setText("这么矫情，那别吃了。");
                        randomCount=0;
                        return;
                    }
                    mContainer.removeAllRandomView();
                    loadSomeView();
                    mContainer.btn_start.setText("停止");
                    isStart = true;
                }
                break;
            default:
                break;
        }
    }

    static class ViewHanler extends Handler {

        WeakReference<MainActivity> mainActivity;

        public ViewHanler(Looper looper, MainActivity activity) {
            super(looper);
            mainActivity = new WeakReference<MainActivity>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            MainActivity activity = mainActivity.get();
            if (activity == null) {
                //avtivity都没了还处理个der
                return;
            }
            if (msg.what == activity.START_UPDATE_VIEW) {
                activity.randomCreateView();
                activity.mContainer.showRandomResult(false);
                if (activity.mContainer.getListSize() > 20) {
                    try {
                        Thread.sleep(350);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    activity.randomRemoveView();
                }
            } else if (msg.what == activity.STOP_UPDATE_VIEW) {
                activity.mContainer.showRandomResult(true);
            }
        }
    }
}