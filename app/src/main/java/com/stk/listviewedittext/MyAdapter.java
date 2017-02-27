package com.stk.listviewedittext;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/2/16.
 */

public class MyAdapter  extends BaseAdapter {
    //定义成员变量mTouchItemPosition,用来记录手指触摸的EditText的位置
    private int mTouchItemPosition = -1;
    private ViewHolder mViewHolder;
    private LayoutInflater mLayoutInflater;
    private List<String> mList;
    private Map<Integer,Boolean> isSelectMap;
    private Map<Integer,String> mindMap;

    public MyAdapter(Context context, List<String> list) {
        mLayoutInflater = LayoutInflater.from(context);
        mList = list;
        isSelectMap = new HashMap<>();
        mindMap = new HashMap<>();
        initDate();
    }

    public Map<Integer, String> getMindMap() {
        return mindMap;
    }

    public void setMindMap(Map<Integer, String> mindMap) {
        this.mindMap = mindMap;
    }

    public Map<Integer, Boolean> getIsSelectMap() {
        return isSelectMap;
    }

    public void setIsSelectMap(Map<Integer, Boolean> isSelectMap) {
        this.isSelectMap = isSelectMap;
    }

    private void initDate(){
        for (int i = 0; i < mList.size(); i++) {
            isSelectMap.put(i,false);
            mindMap.put(i,mList.get(i));
        }
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            mViewHolder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.edittext_item, null);
            mViewHolder.mTextView = (TextView) convertView.findViewById(R.id.text_view);
            mViewHolder.mEditText = (EditText) convertView.findViewById(R.id.edit_text);
            mViewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.mCheckBox);

            mViewHolder.mEditText.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    //注意，此处必须使用getTag的方式，不能将position定义为final，写成mTouchItemPosition = position
                    mTouchItemPosition = (Integer) view.getTag();

                    //触摸的是EditText并且当前EditText可以滚动则将事件交给EditText处理；否则将事件交由其父类处理
                    if ((view.getId() == R.id.edit_text && canVerticalScroll((EditText)view))) {
                        view.getParent().requestDisallowInterceptTouchEvent(true);
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            view.getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    }
                    return false;
                }
            });

            // 让ViewHolder持有一个TextWathcer，动态更新position来防治数据错乱；不能将position定义成final直接使用，必须动态更新
            mViewHolder.mTextWatcher = new MyTextWatcher();
            mViewHolder.mEditText.addTextChangedListener(mViewHolder.mTextWatcher);
            mViewHolder.updatePosition(position);

            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
            //动态更新TextWathcer的position
            mViewHolder.updatePosition(position);
        }

        if (position <= 9) {
            mViewHolder.mTextView.setText("0" + (position));
        } else {
            mViewHolder.mTextView.setText("" + (position));
        }

        //mViewHolder.mEditText.setText(mList.get(position));
        mViewHolder.mEditText.setText(mindMap.get(position));

        mViewHolder.mEditText.setTag(position);

        if (mTouchItemPosition == position) {
            mViewHolder.mEditText.requestFocus();
            mViewHolder.mEditText.setSelection(mViewHolder.mEditText.getText().length());
        } else {
            mViewHolder.mEditText.clearFocus();
        }


        mViewHolder.checkBox.setChecked(isSelectMap.get(position));
        mViewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    isSelectMap.put(position,true);
                }else{
                    isSelectMap.put(position,false);
                }
            }
        });

        return convertView;
    }


    /**
     * EditText竖直方向是否可以滚动
     * @param editText 需要判断的EditText
     * @return true：可以滚动 false：不可以滚动
     */
    private boolean canVerticalScroll(EditText editText) {
        //滚动的距离
        int scrollY = editText.getScrollY();
        //控件内容的总高度
        int scrollRange = editText.getLayout().getHeight();
        //控件实际显示的高度
        int scrollExtent = editText.getHeight() - editText.getCompoundPaddingTop() -editText.getCompoundPaddingBottom();
        //控件内容总高度与实际显示高度的差值
        int scrollDifference = scrollRange - scrollExtent;

        if(scrollDifference == 0) {
            return false;
        }

        return (scrollY > 0) || (scrollY < scrollDifference - 1);
    }

    static final class ViewHolder {
        TextView mTextView;
        EditText mEditText;
        CheckBox checkBox;
        MyTextWatcher mTextWatcher;

        //动态更新TextWathcer的position
        public void updatePosition(int position) {
            mTextWatcher.updatePosition(position);
        }

    }
    class MyTextWatcher implements TextWatcher {
        //由于TextWatcher的afterTextChanged中拿不到对应的position值，所以自己创建一个子类
        private int mPosition;

        public void updatePosition(int position) {
            mPosition = position;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            //mList.set(mPosition, s.toString());
            mindMap.put(mPosition,s.toString());
        }
    }
}