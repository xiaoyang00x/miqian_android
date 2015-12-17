package com.miqian.mq.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.miqian.mq.R;
import com.miqian.mq.entity.MessageInfo;
import com.miqian.mq.utils.Uihelper;

import java.util.List;

public class MessageAdapter extends BaseAdapter {
    private Context context;
    private List<MessageInfo> messageList;

    public MessageAdapter(Context context, List<MessageInfo> messageList) {
        this.context = context;
        this.messageList = messageList;
    }

    @Override
    public int getCount() {
        if (messageList.size() > 0) {
            return messageList.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_listview_message, null);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.iv_isRead = (ImageView) convertView.findViewById(R.id.iv_isRead);
			holder.divider = convertView.findViewById(R.id.divider);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == getCount() -1) {
            holder.divider.setVisibility(View.GONE);
        } else {
            holder.divider.setVisibility(View.VISIBLE);
        }
        final MessageInfo messageInfo = messageList.get(position);
        holder.tv_title.setText(messageInfo.getTitle());
        holder.tv_content.setText(messageInfo.getContent());


        // 判断是否是未读状态
            if (messageInfo.isRead()) {
                holder.iv_isRead.setBackgroundResource(R.drawable.message_open);
            } else {
                holder.iv_isRead.setBackgroundResource(R.drawable.message_close);
            }

        String dateToChineseStrings = Uihelper.timestampToDateStr_other(Double.parseDouble(messageInfo.getSendTime()));
        holder.tv_time.setText(dateToChineseStrings);
        return convertView;
    }

    private class ViewHolder {
        TextView tv_title;
        TextView tv_content;
        TextView tv_time;
        ImageView iv_isRead;
        View divider;
    }
}
