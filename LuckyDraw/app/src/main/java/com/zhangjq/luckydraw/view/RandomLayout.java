package com.zhangjq.luckydraw.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhangjq.luckydraw.R;

import java.util.ArrayList;
import java.util.Random;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Constraints;

/**
 * 创建日期：2022/7/21 10:25
 *
 * @author zhangjq
 * @version 1.0
 * 包名： com.zhangjq.luckydraw.view
 * 类说明：
 */

/**
 * 可以添加随机位置View的布局!
 * 主要的思路是：
 * 1、位置随机
 * 2、防止覆盖
 */
public class RandomLayout<T> extends RelativeLayout {

    private static final String TAG = "RandomLayout";

    /**
     * 此列表用于保存随机的View视图
     * 在添加随机View的时候应当判断此视图是否有覆盖的
     * 有的话应该重新进行随机!
     */
    private ArrayList<View> randomViewList = new ArrayList<>();
    private OnRandomItemClickListener<T> onRandomItemClickListener;
    private OnRandomItemLongClickListener<T> onRandomItemLongClickListener;
    private boolean itemClickable = true;
    private boolean itemLongClickable = true;
    private ConstraintLayout container;
    private TextView tv_tips;
    private Button btn_start;
    private boolean ifStart = false;

    public RandomLayout(Context context) {
        super(context);
    }

    public RandomLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        createStartView(context);
    }

    private void createStartView(Context context) {
        container = (ConstraintLayout) LayoutInflater.from(context).inflate(R.layout.activity_main1, null, false);
        final Context mContext = context;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(CENTER_IN_PARENT);
        container.setLayoutParams(layoutParams);
        tv_tips = container.findViewById(R.id.title);
        btn_start = container.findViewById(R.id.btn_start);
        btn_start.setText("开始");
        btn_start.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext.getApplicationContext(), "调用布局的点击监听", Toast.LENGTH_SHORT).show();
                if (ifStart) {
                    btn_start.setText("停止");
                    ifStart = false;
                } else {
                    btn_start.setText("开始");
                    ifStart = true;
                }
            }
        });
        addView(container);
    }

    /**
     * 添加到一个随机的XY位置，且不重复。
     */
    public void addViewAtRandomXY(final View view, final T t) {
        if (view == null) {
            return;
        }
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        post(new Runnable() {
            @Override
            public void run() {
                randomViewList.remove(view);
                // 100次随机上限
                for (int i = 0; i < 100; i++) {
                    int[] xy = createXY(view.getMeasuredHeight(), view.getMeasuredWidth());
                    if (isCoverButton(view, xy[0], xy[1], t)) {
                        return;
                    }
                    if (randomViewList.size() == 0) {
                        addViewAndSetXY(view, xy[0], xy[1], t);
                    } else {
                        boolean isRepeat = false;
                        // 迭代已经存在的View，判断是否重叠!
                        for (View subView : randomViewList) {
                            // 得到XY
                            int x = (int) subView.getX();
                            int y = (int) subView.getY();
                            int width = subView.getMeasuredWidth();
                            int height = subView.getMeasuredHeight();
                            // 创建矩形
                            Rect v1Rect = new Rect(x, y, width + x, height + y);
                            Rect v2Rect = new Rect(xy[0], xy[1],
                                    view.getMeasuredWidth() + xy[0],
                                    view.getMeasuredHeight() + xy[1]
                            );
                            if (Rect.intersects(v1Rect, v2Rect)) {
                                isRepeat = true;
                                break;
                            }
                        }
                        if (!isRepeat) {
                            addViewAndSetXY(view, xy[0], xy[1], t);
                            return;
                        }
                    }
                }
            }
        });
    }

    private boolean isCoverButton(final View view, int x, int y, final T t) {
        int centerContainerX = (int)container.getX();
        int centerContainerY = (int)container.getY();
        int centerContainerWidth = view.getMeasuredWidth();
        int centerContainerHeight = view.getMeasuredHeight();
        Rect centerContainerRect = new Rect(centerContainerX, centerContainerY, centerContainerWidth + x, centerContainerHeight + y);
        Rect newRect = new Rect(x, y, view.getMeasuredWidth() + x, view.getMeasuredHeight() + y);
        if (Rect.intersects(centerContainerRect, newRect)) {
            if(view instanceof TextView){
                TextView textView = (TextView)view;
                Log.d(TAG, "过滤重复视图 "+textView.getText().toString());
            }else{
                Log.d(TAG, "过滤重复视图,该视图不是TextView");
            }
            return true;
        }
        return false;
    }

    private void addViewAndSetXY(View view, int x, int y, final T t) {
        removeView(view);
        addView(view);
        randomViewList.add(view);
        view.setX(x);
        view.setY(y);
        // 设置单击事件!
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onRandomItemClickListener != null && isItemClickable()) {
                    onRandomItemClickListener.onRandomItemClick(v, t);
                }
            }
        });
        // 设置长按事件!
        view.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onRandomItemLongClickListener != null && itemLongClickable) {
                    return onRandomItemLongClickListener.onRandomItemLongClick(v, t);
                }
                return false;
            }
        });
    }

    /**
     * 添加一个View到随机列表中，以此达到防止覆盖的效果!
     */
    public void addViewToRandomList(View view) {
        randomViewList.add(view);
    }

    /**
     * 清除所有的随机视图!
     */
    public void removeAllRandomView() {
        for (View v : randomViewList) {
            removeView(v);
        }
        randomViewList.clear();
    }

    /**
     * 从列表中移除一个随机视图!
     */
    public void removeRandomViewFromList(View view) {
        randomViewList.remove(view);
        removeView(view);
    }

    /**
     * 随机生成一个 0 到指定区间的值!
     *
     * @param max 0到max但是不包括max
     * @return 同上
     */
    private int random(int max) {
        // LogUtils.d("Max是：" + max);
        return new Random().nextInt(max);
    }

    /**
     * 根据传入的宽和高返回一个随机的坐标!
     */
    private int[] createXY(int height, int width) {
        int[] xyRet = new int[]{0, 0};
        // 初始化我们当前布局的屏幕XY!
        int layoutHeight = getMeasuredHeight();
        int layoutWidth = getMeasuredWidth();
        // 先随机一个X，注意一下就是，X轴是从View的左向右延申的
        // 注意，要减去内部填充!!!
        // LogUtils.d("paddingEnd: " + paddingEnd);
        xyRet[0] = random(
                layoutWidth - (
                        width + getPaddingStart() + getPaddingEnd()
                )
        );
        // LogUtils.d(" 布局宽度：" + layoutWidth + "，X轴：" + xyRet[0] + "，最终宽度：" + (xyRet[0] + width + paddingEnd + paddingStart));
        // 然后从Y是从View的上向下延申，所以我们需要进行下限值限制，避免越界!
        xyRet[1] = random(
                layoutHeight - (
                        height + getPaddingBottom() + getPaddingTop()
                )
        );
        return xyRet;
    }

    public boolean isItemClickable() {
        return itemClickable;
    }

    public void setItemClickable(boolean itemClickable) {
        this.itemClickable = itemClickable;
    }

    public boolean isItemLongClickable() {
        return itemLongClickable;
    }

    public void setItemLongClickable(boolean itemLongClickable) {
        this.itemLongClickable = itemLongClickable;
    }

    public OnRandomItemClickListener getOnRandomItemClickListener() {
        return onRandomItemClickListener;
    }

    public void setOnRandomItemClickListener(OnRandomItemClickListener<T> onRandomItemClickListener) {
        this.onRandomItemClickListener = onRandomItemClickListener;
    }

    public OnRandomItemLongClickListener<T> getOnRandomItemLongClickListener() {
        return onRandomItemLongClickListener;
    }

    public void setOnRandomItemLongClickListener(OnRandomItemLongClickListener<T> onRandomItemLongClickListener) {
        this.onRandomItemLongClickListener = onRandomItemLongClickListener;
    }

    public interface OnRandomItemClickListener<T> {
        void onRandomItemClick(View view, T t);
    }

    public interface OnRandomItemLongClickListener<T> {
        boolean onRandomItemLongClick(View view, T t);
    }
}
