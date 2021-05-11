package com.example.swiperefreshlayoutrecyclerview;
//目的：實作Recyclerview上拉載入更多功能
//1. 創建一個loading Ui (item_loading)
//2.實作getItemViewType 去回傳loadingUi 當RecyclerView滑到底時,設定資料為空時,走loading頁面
//3.新增 scrollListener 去監測滑到底的狀態,及是否要加入loading ui


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<String> dataList = new ArrayList<>();
    public final static String TAG = "hank";
    private boolean isLoading = false;
    private RecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        populateData();
        setAdapter();

        //5.設定addOnScrollListener監聽,當他滑到底時讀取更多,因為最後一個狀態是空資料才會laoding,所以data -2 代表當你滑倒最後兩個item我會準備getMore
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                //如果正在loading
                if(!isLoading){
                    Log.v(TAG,"!isLoading:" + !isLoading);

                    //如果滑到資料尾數8時的資料,才去載入更多資料
                    if(linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == dataList.size() -2){
                        Log.v(TAG,"findLastCompletelyVisibleItemPosition()" + linearLayoutManager.findLastCompletelyVisibleItemPosition() +"/dataList.size():" + (dataList.size()-2));
                        isLoading = true;
                        getMore();
                    }
                }
            }
        });
    }

    private void getMore() {
        Log.v(TAG,"getMore->");

        //4.重點新增一筆空資料,因為如果data是空的時候這邊itemViewType會回傳設定走loadingView,因為資料沒了圖如下,這樣就會laoding
        dataList.add(null);
        Log.v(TAG, "populateData -> dataList.add(null)");

        //6.dataList最後一筆數據是空資料,在空資料前叉路一比新數據
        recyclerViewAdapter.notifyItemInserted(dataList.size() -1 );
        Log.v(TAG,"getMore-> dataList.size() -1" + dataList.get((dataList.size() -1)));


        //這邊另外加執行緒讓loading有時間顯示,也可以不加
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //刪除loadingItem準備新增資料,這時loading就會消逝
                dataList.remove(dataList.size() -1);
                Log.v(TAG,"getMore-> dataList.remove -(dataList.size() -1)" + dataList.size());

                //準備新增新的資料灌進去
                int currentSize = dataList.size(); //10
                int nextSize = currentSize + 10;  //20;

                //10 ~20  新拿到的item 11 慢慢加到20 比資料
                while (currentSize < nextSize){
                    dataList.add("item" + currentSize);
                    currentSize ++;
                    Log.v(TAG,"getMore-> currentSize  " + currentSize);

                }

                //資料更新處理
                recyclerViewAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        },100); //delayMillis目前是讓loading有時間顯示的配置


    }

    //2.設定條便器
    private void setAdapter() {
        recyclerViewAdapter = new RecyclerViewAdapter(dataList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    //1.準備假資料
    private void populateData() {
        int i = 0;

        while (i < 10) {
            dataList.add("item" + i);
            i++;
            Log.v(TAG, "populateData -> while i< 10" + "item:" + i);
        };
    }

    private void init() {
        recyclerView = findViewById(R.id.recyclerView);
    }
}