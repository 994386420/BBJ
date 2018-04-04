package com.bbk.adapter;


import java.util.List;
import java.util.Map;

import com.bbk.activity.R;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultExpandableListViewAdapter extends BaseExpandableListAdapter{

	private Context context;
    /* 布局填充器*/
    private LayoutInflater inflater;
    private List<Map<String, Object>> parentlist;
	private List<List<Map<String, Object>>> childlist;

    /* 构造,因为布局填充器填充布局需要使用到Context,通过构造来传递 */
    public ResultExpandableListViewAdapter (Context context,List<Map<String, Object>> parentlist,List<List<Map<String, Object>>> childlist){
        this.context = context;
        this.parentlist = parentlist;
        this.childlist = childlist;
         inflater = LayoutInflater.from(context);
    }


    /*组数*/
    @Override
    public int getGroupCount() {
        return parentlist.size();
    }

    /* 指定组的子Item数*/
    @Override
    public int getChildrenCount(int groupPosition) {
        return childlist.get(groupPosition).size();
    }

    /*组数据*/
    @Override
    public Object getGroup(int groupPosition) {
        return parentlist.get(groupPosition);
    }

    /*返回子选项的数据*/
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childlist.get(groupPosition).get(childPosition);
    }

    /*组ID*/
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    /*子ID*/
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    /*ID是否唯一*/
    @Override
    public boolean hasStableIds() {
        return true;
    }

    /* 组选项的视图处理 */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        /* 填充布局*/
    	 View view = convertView;
         ParentHolder holder = null;
         if(view == null){
             holder = new ParentHolder();
             view = inflater.inflate(R.layout.expandablelistview_parent, null);
             holder.mtext = (TextView)view.findViewById(R.id.mtext);
             holder.mimg = (ImageView)view.findViewById(R.id.mimg);
             view.setTag(holder);
         }else{
             holder = (ParentHolder)view.getTag();
         }
       //判断是否已经打开列表
         if(isExpanded){
        	holder.mimg.setImageResource(R.mipmap.enter_up);
         }else{
        	 holder.mimg.setImageResource(R.mipmap.enter_down);
            
         }

         holder.mtext.setText(parentlist.get(groupPosition).get("item_text").toString());
        return view;

    }

    /* 子选项的视图处理 */
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
    	 View view = convertView;
         ChildHolder holder = null;
         if(view == null){
             holder = new ChildHolder();
             view = inflater.inflate(R.layout.expandablelistview_child, null);
             holder.text = (TextView)view.findViewById(R.id.text);
             view.setTag(holder);
         }else{
             holder = (ChildHolder)view.getTag();
         }
         holder.text.setText(childlist.get(groupPosition).get(childPosition).get("item_text").toString());
        return view;
    }

    /*子选项是否可选 */
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    class ParentHolder{
        public TextView mtext;
        public ImageView mimg;
    }

    class ChildHolder{
        public TextView text;
    }

}
