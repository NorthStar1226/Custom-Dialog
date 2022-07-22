package com.zhangjq.luckydraw.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.zhangjq.luckydraw.R;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

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

    private final static String TAG = "CookiesRecyclerViewAdapter";

    private LinkedList<String> cookLists;
    private Map<Integer,Boolean> isCheckedMap = new HashMap<>();
    private Context mContext;
    private OnItemCheckedChangeListener mOnItemCheckedChangeListener;

    public CookiesRecyclerViewAdapter(Context context, LinkedList<String> list) {
        mContext = context;
        cookLists = list;
    }

    public void setOnItemCheckedChangeListener(OnItemCheckedChangeListener listener) {
        mOnItemCheckedChangeListener = listener;
    }

    public interface OnItemCheckedChangeListener {
        void onItemCheckedChange(View view, int position, String value, boolean isChecked);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cookiesbook_detail, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.check_name.setText(cookLists.get(position));
        holder.check_name.setChecked(isCheckedMap.getOrDefault(position,false));
        if (mOnItemCheckedChangeListener != null) {
            holder.check_name.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                    isCheckedMap.put(position,isChecked);
                    mOnItemCheckedChangeListener.onItemCheckedChange(view, position, cookLists.get(position), isChecked);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return cookLists == null ? 0 : cookLists.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        public final CheckBox check_name;

        public MyViewHolder(View view) {
            super(view);
            check_name = (CheckBox) view.findViewById(R.id.check_name);

        }

    }
}
