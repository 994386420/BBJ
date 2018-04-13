package com.bbk.chat.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


import com.bbk.activity.R;
import com.bbk.chat.model.Conversation;
import com.bbk.chat.model.FriendProfile;
import com.bbk.chat.model.FriendshipInfo;
import com.bbk.chat.model.GroupInfo;
import com.bbk.chat.ui.AddFriendActivity;
import com.bbk.chat.ui.ChatActivity;
import com.bbk.chat.ui.GroupProfileActivity;
import com.bbk.chat.ui.ProfileActivity;
import com.bbk.chat.utils.TimeUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.qcloud.presentation.presenter.ChatPresenter;
import com.tencent.qcloud.ui.ChatInput;
import com.tencent.qcloud.ui.CircleImageView;
import com.tencent.qcloud.ui.TemplateTitle;
import com.tencent.qcloud.ui.VoiceSendingView;


import java.util.ArrayList;
import java.util.List;

/**
 * 会话界面adapter
 */
public class ConversationAdapter extends ArrayAdapter<Conversation> {

    private int resourceId;
    private View view;
    private ViewHolder viewHolder;
    private TIMFriendshipManager timFriendshipManager;
    private String tag = "ConversationAdapter=====";
    private Context context;

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param objects  The objects to represent in the ListView.
     */
    public ConversationAdapter(Context context, int resource, List<Conversation> objects) {
        super(context, resource, objects);
        resourceId = resource;
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView != null){
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }else{
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.tvName = (TextView) view.findViewById(R.id.name);
            viewHolder.avatar = (CircleImageView) view.findViewById(R.id.avatar);
            viewHolder.lastMessage = (TextView) view.findViewById(R.id.last_message);
            viewHolder.time = (TextView) view.findViewById(R.id.message_time);
            viewHolder.unread = (TextView) view.findViewById(R.id.unread_num);
            view.setTag(viewHolder);
        }
        final Conversation data = getItem(position);
        viewHolder.tvName.setText(data.getName());
        viewHolder.avatar.setImageResource(R.mipmap.logo);
//        getFriendsProfile(data,position);
        viewHolder.lastMessage.setText(data.getLastMessageSummary());
        viewHolder.time.setText(TimeUtil.getTimeStr(data.getLastMessageTime()));
        long unRead = data.getUnreadNum();
        if (unRead <= 0){
            viewHolder.unread.setVisibility(View.INVISIBLE);
        }else{
            viewHolder.unread.setVisibility(View.VISIBLE);
            String unReadStr = String.valueOf(unRead);
            if (unRead < 10){
                viewHolder.unread.setBackground(getContext().getResources().getDrawable(R.drawable.point1));
            }else{
                viewHolder.unread.setBackground(getContext().getResources().getDrawable(R.drawable.point2));
                if (unRead > 99){
                    unReadStr = getContext().getResources().getString(R.string.time_more);
                }
            }
            viewHolder.unread.setText(unReadStr);
        }
        return view;
    }

    public class ViewHolder{
        public TextView tvName;
        public CircleImageView avatar;
        public TextView lastMessage;
        public TextView time;
        public TextView unread;

    }
    private void getFriendsProfile(Conversation data,int postion){
        data = getItem(postion);
        //待获取用户资料的好友列表
        List<String> users = new ArrayList<String>();
        users.add(data.getIdentify());
        Log.e(tag, "getUsersProfile " + " id"+data.getIdentify()+users);
        //获取好友资料
        timFriendshipManager.getInstance().getUsersProfile(users, new TIMValueCallBack<List<TIMUserProfile>>(){
            @Override
            public void onError(int code, String desc){
                //错误码code和错误描述desc，可用于定位请求失败原因
                //错误码code列表请参见错误码表
                Log.e(tag, "getUsersProfile failed: " + code + " desc");
            }

            @Override
            public void onSuccess(List<TIMUserProfile> result){
                Log.e(tag, "getUsersProfile succ");
                for(TIMUserProfile res : result){
                    Log.e(tag, "identifier: " + res.getIdentifier() + " nickName: " + res.getNickName()
                            + " remark: " + res.getRemark()+"imageUrl :"+res.getFaceUrl());
                    viewHolder.tvName.setText(res.getNickName());
//                    viewHolder.avatar.setImageResource(data.getAvatar());
                    Glide.with(context)
                            .load(res.getFaceUrl())
                            .priority(Priority.HIGH)
                            .placeholder(R.mipmap.zw_img_300)
                            .into(viewHolder.avatar);
                }
            }
        });
    }
}
