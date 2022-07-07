package com.genexus.android.core.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerStringAdapter extends RecyclerView.Adapter<RecyclerStringAdapter.MyViewHolder> {
	private final LayoutInflater mInflater;
	private final ArrayList<String> mData;
	private OnClickListener mOnClickListener;

	public RecyclerStringAdapter(Context context) {
		mInflater = LayoutInflater.from(context);
		mData = new ArrayList<>();
	}

	@NonNull
	@Override
	public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = mInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
		return new MyViewHolder(view, mOnClickListener);
	}

	@Override
	public void onBindViewHolder(MyViewHolder holder, int position) {
		holder.text.setText(mData.get(position));
	}

	@Override
	public int getItemCount() {
		return mData.size();
	}

	public void setOnClickListener(OnClickListener listener) {
		mOnClickListener = listener;
		notifyDataSetChanged();
	}

	public void clear() {
		mData.clear();
		notifyDataSetChanged();
	}

	public void add(String value) {
		mData.add(value);
		notifyDataSetChanged();
	}

	public interface OnClickListener {
		void onClick(int position);
	}

	static class MyViewHolder extends RecyclerView.ViewHolder
	{
		TextView text;
		public MyViewHolder(View itemView, OnClickListener clickListener) {
			super(itemView);
			text = (TextView)itemView;
			if (clickListener != null) {
				itemView.setOnClickListener(view -> clickListener.onClick(getAdapterPosition()));
			}
		}
	}
}
