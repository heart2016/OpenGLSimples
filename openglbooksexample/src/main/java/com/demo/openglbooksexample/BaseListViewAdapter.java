package com.demo.openglbooksexample;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.Collection;
import java.util.List;

/**
 * Created by lenovo on 2017/10/18.
 */

public abstract class BaseListViewAdapter<T> extends BaseAdapter {
    protected Context context;
    protected List<T> data;
    protected int itemLayoutId;

    public BaseListViewAdapter(Context context, List<T> data, int itemLayoutId) {
        this.context = context;
        this.data = data;
        this.itemLayoutId = itemLayoutId;
    }

    public synchronized void addAll(Collection<T> collection) {
        if (collection != null) {
            data.addAll(collection);
            notifyDataSetChanged();
        }
    }

    public synchronized void setData(Collection<T> collection) {
        if (collection != null) {
            data.clear();
            data.addAll(collection);
            notifyDataSetChanged();
        }
    }

    public synchronized void add(T item) {
        if (item != null) {
            data.add(item);
            notifyDataSetChanged();
        }
    }

    public synchronized void clearData() {
        if (data != null) {
            data.clear();
            notifyDataSetChanged();
        }
    }

    public synchronized T remove(int index) {
        if (index < 0 || index >= data.size())
            throw new IndexOutOfBoundsException("越界了");
        T result = data.remove(index);
        notifyDataSetChanged();
        return result;
    }

    public boolean remove(T o) {
        boolean removed = data.remove(o);
        if (removed)
            notifyDataSetChanged();
        return removed;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public T getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder = getViewHolder(position, convertView, parent);
        convert(viewHolder, getItem(position), position);
        return viewHolder.getConvertView();
    }

    public abstract void convert(ViewHolder holder, T item, int position);

    protected ViewHolder getViewHolder(int position, View convertView, ViewGroup parent) {
        return ViewHolder.get(context, convertView, parent, itemLayoutId);
    }

    public static class ViewHolder {
        private final SparseArray<View> views;
        private View convertView;

        private ViewHolder(Context context, ViewGroup parent, int layoutId) {
            this.views = new SparseArray<>();
            // L.e("====",layoutId+parent.toString()+layoutId+"===="+context);
            convertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
            convertView.setTag(this);
        }

        public static ViewHolder get(Context context, View convertView, ViewGroup parent, int layoutId) {
            if (convertView == null) {
                return new ViewHolder(context, parent, layoutId);
            }
            return (ViewHolder) convertView.getTag();
        }

        @SuppressWarnings("unchecked")
        public <T extends View> T getView(int viewId) {
            View view = views.get(viewId);
            if (view == null) {
                view = convertView.findViewById(viewId);
                views.put(viewId, view);
            }
            return (T) view;
        }

        public View getConvertView() {
            return convertView;
        }
    }
}
