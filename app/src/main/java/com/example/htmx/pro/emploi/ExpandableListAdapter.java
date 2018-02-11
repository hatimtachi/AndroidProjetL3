package com.example.htmx.pro.emploi;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.htmx.pro.R;

import java.util.List;
import java.util.Map;

/**
 * Created by htmx on 03/12/16.
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter{
    private Activity context;
    private Map<String,List<String>> itemcollections;
    private List<String> item;

    public ExpandableListAdapter(Activity context,List<String> items,Map<String ,List<String>> listitems){
        this.context=context;
        this.item=items;
        this.itemcollections=listitems;
    }

    public Object getChild(int groupPostion,int ChildPostion){
        return itemcollections.get(item.get(groupPostion)).get(ChildPostion);
    }
    public long getChildId(int groupP,int childP){
        return childP;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String items=(String) getChild(groupPosition,childPosition);
        LayoutInflater inflater=context.getLayoutInflater();
        if(convertView==null){
            convertView=inflater.inflate(R.layout.child_group_tf2,null);
        }
        TextView item=(TextView) convertView.findViewById(R.id.tf2_child_item_TextV);
        item.setText(items);
        return convertView;
    }
    public int getChildrenCount(int groupPosition){
        return itemcollections.get(item.get(groupPosition)).size();
    }
    public Object getGroup(int groupPostion){
        return item.get(groupPostion);
    }
    public int getGroupCount(){
        return item.size();
    }
    public long getGroupId(int groupPostion){
        return groupPostion;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String name=(String)getGroup(groupPosition);
        if (convertView==null){
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.group_item_tf2,null);
        }
        TextView item =(TextView) convertView.findViewById(R.id.tf2_group_item_TextV);
        item.setTypeface(null, Typeface.BOLD);
        item.setText(name);
        return convertView;
    }

    public boolean hasStableIds(){
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}