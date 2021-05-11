package com.example.swiperefreshlayoutrecyclerview;
//1.RecyclerView基本處理
//2.複寫getItemViewType

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<String> mItemList;


    //3.兩種item常數設定
    final int VIEW_TYPE_LOADING = 0; //laoding的ui
    final int VIEW_TYPE_ITEM = 1; //資料的ui

    public RecyclerViewAdapter(List<String> mItemList) {
        this.mItemList = mItemList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //5.如果沒有滑到底部時viewType ＝＝ VIEW_TYPE_ITEM,走item_row的頁面
        if (viewType == VIEW_TYPE_ITEM) {
            Log.v(MainActivity.TAG, "onCreateViewHolder -> VIEW_TYPE_ITEM:" + viewType);
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
            ItemViewHolder itemViewHolder = new ItemViewHolder(view);
            return itemViewHolder;
        } else {
            //滑到底部時viewType ＝＝ VIEW_TYPE_LOADING,走item_loading的頁面
            Log.v(MainActivity.TAG, "onCreateViewHolder -> VIEW_TYPE_LOADING:" + viewType);
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //7.區分要實作的ItemViewHolder/loadingViewHolder
        if (holder instanceof ItemViewHolder) {
            //含ItemViewHolder
            Log.v(MainActivity.TAG, "onBindViewHolder -> ItemViewHolder:" + holder.getClass().getName());
            populateItemRows((ItemViewHolder) holder, position);
        } else {
            Log.v(MainActivity.TAG, "onBindViewHolder -> LoadingItemViewHolder:" + holder.getClass().getName());
            //含LoadingViewHolder,這邊loadin頁面只負責顯示progressBar所以就不處理
        }
    }


    @Override
    public int getItemCount() {
        Log.v(MainActivity.TAG, "getItemCount->");
        return mItemList == null ? 0 : mItemList.size();
    }

    //2.複寫getItemViewType
    @Override
    public int getItemViewType(int position) {
        Log.v(MainActivity.TAG, "getItemViewType-> position:" + position);
        //4.當這個資料為空時,代表滑到底部,那就用loadingUi,沒有滑到底時就正常item顯示,資料為空是因為我在getMore時偷加的
        return mItemList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }


    //6.準備loadingItemViewHolder取得loading頁面的元件
    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);

            Log.v(MainActivity.TAG, "LoadingViewHolder");
        }
    }


    //item的ViewHolder
    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView tvItem;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.v(MainActivity.TAG, "ItemViewHolder");
            tvItem = itemView.findViewById(R.id.tvItem);
        }
    }

    //設定ItemView的設定資料
    private void populateItemRows(@NonNull ItemViewHolder holder, int position) {
        holder.tvItem.setText(mItemList.get(position));
    }
}


