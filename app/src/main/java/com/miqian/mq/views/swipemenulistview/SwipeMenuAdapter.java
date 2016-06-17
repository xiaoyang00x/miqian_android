package com.miqian.mq.views.swipemenulistview;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.WrapperListAdapter;

import com.miqian.mq.R;
import com.miqian.mq.entity.MessageInfo;
import com.miqian.mq.utils.Uihelper;

import java.util.IllegalFormatCodePointException;
import java.util.List;

public class SwipeMenuAdapter implements WrapperListAdapter,
        SwipeMenuView.OnItemClickListener {

    private ListAdapter mAdapter;
    private Context mContext;
    List<MessageInfo> mList;
    private SwipeMenuListView.OnMenuItemClickListener onMenuItemClickListener;

    public SwipeMenuAdapter(Context context, ListAdapter adapter, List<MessageInfo> list) {
        mAdapter = adapter;
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mAdapter.getCount();
    }

    @Override
    public Object getItem(int position) {
        return mAdapter.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return mAdapter.getItemId(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SwipeMenuLayout layout = null;
        View contentView = mAdapter.getView(position, convertView, parent);
        SwipeMenu menu = new SwipeMenu(mContext);
        createMenu(menu, mList.get(position));
        SwipeMenuView menuView = new SwipeMenuView(menu,
                (SwipeMenuListView) parent);
        menuView.setOnItemClickListener(this);
        SwipeMenuListView listView = (SwipeMenuListView) parent;
        layout = new SwipeMenuLayout(contentView, menuView,
                listView.getCloseInterpolator(),
                listView.getOpenInterpolator());
        layout.setPosition(position);
        layout.closeMenu();
        return layout;
    }

    public void createMenu(SwipeMenu menu, MessageInfo messageInfo) {

        SwipeMenuItem item1 = new SwipeMenuItem(mContext);
        item1.setBackground(mContext.getResources().getDrawable(R.drawable.shape_swip_red));
        item1.setWidth(Uihelper.dip2px(mContext, 90));
        if (messageInfo.isRead()) {
            item1.setWidth(Uihelper.dip2px(mContext, 0));
            item1.setTitleColor(Color.GRAY);
        } else {
            item1.setTitle("置为已读");
            item1.setTitleColor(Color.WHITE);
        }

        item1.setTitleSize(18);
        menu.addMenuItem(item1);
//		// Test Code
//		SwipeMenuItem item = new SwipeMenuItem(mContext);
//		item.setTitle("Item 1");
//		item.setBackground(new ColorDrawable(Color.GRAY));
//		item.setWidth(300);
//		menu.addMenuItem(item);
//
//		item = new SwipeMenuItem(mContext);
//		item.setTitle("Item 2");
//		item.setBackground(new ColorDrawable(Color.RED));
//		item.setWidth(300);
//		menu.addMenuItem(item);
    }

    @Override
    public void onItemClick(SwipeMenuView view, SwipeMenu menu, int index) {
        if (onMenuItemClickListener != null) {
            onMenuItemClickListener.onMenuItemClick(view.getPosition(), menu,
                    index);
        }
    }

    public void setOnMenuItemClickListener(
            SwipeMenuListView.OnMenuItemClickListener onMenuItemClickListener) {
        this.onMenuItemClickListener = onMenuItemClickListener;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        mAdapter.registerDataSetObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        mAdapter.unregisterDataSetObserver(observer);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return mAdapter.areAllItemsEnabled();
    }

    @Override
    public boolean isEnabled(int position) {
        return mAdapter.isEnabled(position);
    }

    @Override
    public boolean hasStableIds() {
        return mAdapter.hasStableIds();
    }

    @Override
    public int getItemViewType(int position) {
        return mAdapter.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return mAdapter.getViewTypeCount();
    }

    @Override
    public boolean isEmpty() {
        return mAdapter.isEmpty();
    }

    @Override
    public ListAdapter getWrappedAdapter() {
        return mAdapter;
    }

}
