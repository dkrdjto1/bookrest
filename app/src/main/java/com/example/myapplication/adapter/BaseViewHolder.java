package com.example.myapplication.adapter;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseViewHolder extends RecyclerView.ViewHolder {
    private int mCurrentPosition;
    public BaseViewHolder(View itemView) {
        super(itemView);
    }
    protected abstract void clear();
    public void onBind(int position) {
        mCurrentPosition = position;
        clear();
    }
    public int getCurrentPosition() {
        return mCurrentPosition;
    }
}
