package com.example.cherif.androidtp.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.cherif.androidtp.R;
import com.example.cherif.androidtp.entity.Task;

import java.util.ArrayList;


public class ListViewAdapter extends BaseAdapter{

    Context context;
    ArrayList<Task> listTasks;

    public ListViewAdapter(Context context, ArrayList<Task> listTasks){
        this.context = context;
        this.listTasks = listTasks;
    }

    @Override
    public int getCount() {
        return this.listTasks.size();
    }

    @Override
    public Object getItem(int position) {
        return listTasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_list, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.taskName = (TextView) convertView.findViewById(R.id.taskName);
            viewHolder.taskDesc = (TextView) convertView.findViewById(R.id.taskDesc);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Task currTask = (Task)getItem(position);
        viewHolder.taskName.setText(currTask.getTask());
        viewHolder.taskDesc.setText(currTask.getDescription());
        return convertView;
    }

    class ViewHolder {
        TextView taskName,taskDesc;
    }
}
