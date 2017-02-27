package com.stk.listviewedittext;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "zbw";
    private static final int DATA_CAPACITY = 20;

    private ListView mListView;
    private List<String> mList = new ArrayList<String>(DATA_CAPACITY);
    private MyAdapter mAdapter;
    private Button submit;
    private LinearLayout layout;
    private  EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.list_view);
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i < mList.size(); i++) {
                    Log.d("asfusd",mAdapter.getMindMap().get(i));
                }
                
                Log.d("234234",editText.getText().toString());
            }
        });


        //填充数据
        for (int i = 0; i < DATA_CAPACITY; i++) {
            mList.add("" + i);
        }


        layout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.list_foot,null);
        editText = (EditText) layout.findViewById(R.id.log_summary);

        //设置Adapter
        mAdapter = new MyAdapter(this, mList);

        mListView.addFooterView(layout);

        mListView.setAdapter(mAdapter);


    }


}
