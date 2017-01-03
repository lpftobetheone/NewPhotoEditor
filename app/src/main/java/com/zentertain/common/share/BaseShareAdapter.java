package com.zentertain.common.share;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.zenjoy.photoeditor.R;


public abstract class BaseShareAdapter extends BaseAdapter {

    private LayoutInflater inflater;

    private List<Sharer> sharerList;

    public BaseShareAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    public void setSharerList(List<Sharer> sharerList) {
        this.sharerList = sharerList;
    }

    @Override
    public int getCount() {
        return (sharerList != null) ? sharerList.size() : 0;
    }

    @Override
    public Sharer getItem(int position) {
        if (sharerList != null && sharerList.size() > position) {
            return sharerList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(getLayoutResource(), parent, false);

            viewHolder = new ViewHolder();
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.icon);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Sharer sharer = sharerList.get(position);

        viewHolder.icon.setImageResource(sharer.getIcon());
        viewHolder.name.setText(sharer.getName());

        return convertView;
    }

    protected abstract int getLayoutResource();

    private class ViewHolder {
        ImageView icon;
        TextView name;
    }

}
