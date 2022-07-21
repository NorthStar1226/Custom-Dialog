package com.zhangjq.luckydraw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.zhangjq.luckydraw.R;

import java.util.LinkedList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 创建日期：2022/7/21 15:42
 *
 * @author zhangjq
 * @version 1.0
 * 包名： com.zhangjq.luckydraw.adapter
 * 类说明：
 */

public class CookiesRecyclerViewAdapter extends RecyclerView.Adapter<CookiesRecyclerViewAdapter.MyViewHolder> {

    private LinkedList<String> cookLists;
    private Context mContext;
    private OnItemCheckedChangeListener mOnItemCheckedChangeListener;

    public CookiesRecyclerViewAdapter(Context context, LinkedList<String> list) {
        mContext = context;
        cookLists = list;
    }

    public void setOnItemCheckedChangeListener(OnItemCheckedChangeListener litener) {

    }

    public interface OnItemCheckedChangeListener {
        void onItemCheckedChange(View view, boolean isChecked);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cookiesbook_detail, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (mOnItemCheckedChangeListener != null) {
            holder.check_name.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                    mOnItemCheckedChangeListener.onItemCheckedChange(view, isChecked);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return cookLists==null ? 0 : cookLists.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        public final CheckBox check_name;

        public MyViewHolder(View view) {
            super(view);
            check_name = (CheckBox) view.findViewById(R.id.check_name);

        }

    }
}
