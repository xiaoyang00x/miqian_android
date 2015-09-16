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
import com.miqian.mq.entity.JpushInfo;
import com.miqian.mq.utils.Uihelper;

import java.util.List;

public class MessageAdapter extends BaseAdapter {
	private Context context;
	private List<JpushInfo> jpushInfolist;

	public MessageAdapter(Context context, List<JpushInfo> jpushInfolist) {
		this.context = context;
		this.jpushInfolist = jpushInfolist;
	}

	@Override
	public int getCount() {
		if (jpushInfolist.size() > 0) {
			return jpushInfolist.size();
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
//			holder.view_line = convertView.findViewById(R.id.view_line);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final JpushInfo jpushInfo = jpushInfolist.get(position);
		holder.tv_title.setText(jpushInfo.getTitle());
		holder.tv_content.setText(jpushInfo.getContent());

//		// 判断是否是未读状态
//		if (Integer.valueOf(jpushInfo.getState()) == 1) {
//			holder.iv_isRead.setBackgroundResource(R.drawable.announce_circle_red);
//			holder.iv_isRead.setVisibility(View.VISIBLE);
//		} else {
//			holder.iv_isRead.setBackgroundResource(R.drawable.announce_circle_white);
//			holder.iv_isRead.setVisibility(View.INVISIBLE);
//		}

		if (!TextUtils.isEmpty(jpushInfo.getTime())) {
			double add_time = Double.valueOf(jpushInfo.getTime());
			String dateToChineseStrings = Uihelper.dateToChineseStrings(context, add_time);
			if (!TextUtils.isEmpty(dateToChineseStrings)) {
				holder.tv_time.setText(dateToChineseStrings);
			}
		}
		return convertView;
	}

	private class ViewHolder {
		View view_line;
		TextView tv_title;
		TextView tv_content;
		TextView tv_time;
		ImageView iv_isRead;
	}
}
