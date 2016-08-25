package com.she.customview.slidedeletelistview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{
    private List<MessageItem> mMessageItems = new ArrayList<MainActivity.MessageItem>();
    private SlideListView mListView;

    private SlideAdapter slideAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mListView = (SlideListView) findViewById(R.id.list);

        for (int i = 0; i < 120; i++) {
            MessageItem item = new MessageItem();
            if (i % 3 == 0) {
                item.iconRes = R.drawable.default_qq_avatar;
                item.title = i + " >> this is first title";
                item.msg = "this is first message ";
                item.time = "2014-7-22";
            } else {
                item.iconRes = R.drawable.wechat_icon;
                item.title = i + " >> this is secend title";
                item.msg = "this is secend message ";
                item.time = "2014-8-22";
            }
            mMessageItems.add(item);
        }
        slideAdapter = new SlideAdapter();
        mListView.setAdapter(slideAdapter);
    }



    public class MessageItem {
        public int iconRes;
        public String title;
        public String msg;
        public String time;
    }

    private static class ViewHolder {
        public ImageView icon;
        public TextView title;
        public TextView msg;
        public TextView time;
        public ViewGroup deleteHolder;

        ViewHolder(View view) {
            icon = (ImageView) view.findViewById(R.id.icon);
            title = (TextView) view.findViewById(R.id.title);
            msg = (TextView) view.findViewById(R.id.msg);
            time = (TextView) view.findViewById(R.id.time);
            deleteHolder = (ViewGroup) view.findViewById(R.id.holder);
        }
    }

    private class SlideAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        SlideAdapter() {
            super();
            mInflater = getLayoutInflater();
        }

        @Override
        public int getCount() {
            return mMessageItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mMessageItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            SlideDeleteView slideView = (SlideDeleteView) convertView;


            if (slideView == null) {
                View itemView = mInflater.inflate(R.layout.list_item, null);slideView = new SlideDeleteView(MainActivity.this);
                slideView.setContentView(itemView);
                holder = new ViewHolder(slideView);
                slideView.setTag(holder);
            } else {
                holder = (ViewHolder) slideView.getTag();
                Log.d("getview", position + "");
            }
            slideView.reset();
            MessageItem item = mMessageItems.get(position);
            //slideView.shrink();
            holder.icon.setImageResource(item.iconRes);
            holder.title.setText(item.title);
            holder.msg.setText(item.msg);
            holder.time.setText(item.time);
            holder.deleteHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Toast.makeText(MainActivity.this,"已删除",Toast.LENGTH_SHORT).show();
                    mMessageItems.remove(position);
                    slideAdapter.notifyDataSetChanged();
                }
            });

            return slideView;
        }

    }
}
