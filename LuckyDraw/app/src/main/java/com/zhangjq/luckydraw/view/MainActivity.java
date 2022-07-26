package com.zhangjq.luckydraw.view;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.Random;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import static com.zhangjq.luckydraw.MyApplication.getCookiesLists;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RandomLayout mContainer;
    /*    private Button mStartBtn;
        private TextView mTitle;*/
    private Random random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        random = new Random();
        initView();
    }

    private void initView() {
        mContainer = (RandomLayout) findViewById(R.id.container);
        Random random = new Random();
        mContainer.setOnRandomItemClickListener(new RandomLayout.OnRandomItemClickListener() {
            @Override
            public void onRandomItemClick(View view, Object o) {
                Toast.makeText(MainActivity.this, String.valueOf(o), Toast.LENGTH_SHORT).show();
                mContainer.removeRandomViewFromList(view);
            }
        });
        for (int i = 0; i <= 7; i++) {
            TextView textView = new TextView(this);
            textView.setText(randomLoadData());
            textView.setTextSize((random.nextFloat() * 20 + 10));
            if (TextUtils.isEmpty(textView.getText().toString())){
                TextView title = findViewById(R.id.title);
                return;
            }
            mContainer.addViewAtRandomXY(textView, "这个是textView" + i);
        }
/*        mStartBtn = (Button) findViewById(R.id.btn_start);
        mTitle = (TextView) findViewById(R.id.title);*/
    }

    private String randomLoadData() {
        int indexMin = 0;
        int indexMax = getCookiesLists().size() - 1;
        if (indexMax>indexMin){
            int indexRandom = random.nextInt(indexMax) % (indexMax - indexMin + 1) + indexMin;
            return getCookiesLists().get(indexRandom).getCookiesName();
        }else {
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
                    while (running) {

                    }
                }
            }
        });
    }

    private boolean ifStart = false;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                Toast.makeText(MainActivity.this, "调用MainActivity点击监听", Toast.LENGTH_SHORT).show();
                /*if (ifStart) {
                    mStartBtn.setText("停止");
                    ifStart = false;
                } else {
                    mStartBtn.setText("开始");
                    ifStart = true;
                }*/
                break;
            default:
                break;
        }
    }
}