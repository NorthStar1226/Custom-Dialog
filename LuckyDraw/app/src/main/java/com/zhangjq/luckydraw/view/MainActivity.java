package com.zhangjq.luckydraw.view;

import android.content.Intent;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RandomLayout mContainer;
/*    private Button mStartBtn;
    private TextView mTitle;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mContainer = (RandomLayout) findViewById(R.id.container);
        Random random = new Random();
/*        final TextView textView1 = new TextView(this);
        textView1.setText("111");
        Log.d("MainActivity", "生成随机数：" + (random.nextFloat() * 10 + 10));
        textView1.setTextSize((random.nextFloat() * 20 + 10));

        TextView textView2 = new TextView(this);
        textView2.setText("222");
        textView2.setTextSize((random.nextFloat() * 20 + 10));

        TextView textView3 = new TextView(this);
        textView3.setText("333");
        textView3.setTextSize((random.nextFloat() * 20 + 10));*/
        for (int i = 0; i <= 7; i++) {
            TextView textView = new TextView(this);
            textView.setText(i+""+i+""+i);
            textView.setTextSize((random.nextFloat() * 20 + 10));
            mContainer.addViewAtRandomXY(textView, "这个是textView"+i);
        }

/*        mContainer.addViewAtRandomXY(textView1, "这个是textView1");
        mContainer.addViewAtRandomXY(textView2, "这个是textView2");
        mContainer.addViewAtRandomXY(textView3, "这个是textView3");*/

        mContainer.setOnRandomItemClickListener(new RandomLayout.OnRandomItemClickListener() {
            @Override
            public void onRandomItemClick(View view, Object o) {
                Toast.makeText(MainActivity.this, String.valueOf(o), Toast.LENGTH_SHORT).show();
                mContainer.removeRandomViewFromList(view);
            }
        });
/*        mStartBtn = (Button) findViewById(R.id.btn_start);
        mTitle = (TextView) findViewById(R.id.title);*/
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