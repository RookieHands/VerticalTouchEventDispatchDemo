package com.hudson.worktest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private MyVerticalScrollView mView;
    private ArrayList<String> mDatas;
    private ListView mListView;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) this.findViewById(R.id.listview);
        mView = (MyVerticalScrollView) findViewById(R.id.view);
        mView.post(new Runnable() {
            @Override
            public void run() {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)
                        mListView.getLayoutParams();
                layoutParams.height = mView.getHeight();
                mListView.setLayoutParams(layoutParams);
                mListView.requestLayout();
            }
        });
        mDatas = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            mDatas.add("你好啊hudson" + i);
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                R.layout.item_layout, R.id.textView, mDatas);
        mListView.setAdapter(arrayAdapter);
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
                if (mListView.getFirstVisiblePosition() == 0) {
                    View childAt = mListView.getChildAt(0);
                    if (childAt != null) {
                        int top = childAt.getTop();
                        if (top == 0) {
                            mView.setListViewScrollTop(true);
                        } else {
                            mView.setListViewScrollTop(false);
                        }
                    }
                }
            }
        });
        mView.setListViewScrollTop(false);
    }
}
