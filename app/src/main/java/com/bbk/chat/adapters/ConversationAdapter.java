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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


import com.bbk.activity.R;
import com.bbk.chat.model.Conversation;
import com.bbk.chat.model.FriendProfile;
import com.bbk.chat.model.FriendshipInfo;
import com.bbk.chat.model.GroupInfo;
import com.bbk.chat.model.GroupManageConversation;
import com.bbk.chat.ui.AddFriendActivity;
import com.bbk.chat.ui.ChatActivity;
import com.bbk.chat.ui.GroupProfileActivity;
import com.bbk.chat.ui.ProfileActivity;
import com.bbk.chat.utils.TimeUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
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
    List<String> users;
    List<TIMUserProfile> result1;
    List<Conversation> object;
    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param objects  The objects to represent in the ListView.
     */
    public ConversationAdapter(Context context, int resource, List<Conversation> objects,List<TIMUserProfile> result) {
        super(context, resource, objects);
        resourceId = resource;
        this.context = context;
        this.result1 = result;
//        this.object =  objects;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
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
        try {
//            Log.i("头像","======="+data.getName()+"==="+data.getIdentify());
//            List<String> users = new ArrayList<String>();
//            users.add(data.getAvatarUrls());
//            //获取好友资料
//            timFriendshipManager.getInstance().getUsersProfile(users, new TIMValueCallBack<List<TIMUserProfile>>() {
//                @Override
//                public void onError(int code, String desc) {
//                    //错误码code和错误描述desc，可用于定位请求失败原因
//                    //错误码code列表请参见错误码表
//                    Log.e("==========", "getUsersProfile failed: " + code + " desc");
//                }
//
//                @Override
//                public void onSuccess(final List<TIMUserProfile> result) {
//                    Log.e("==========", "getUsersProfile succ" + result);
////                    for(TIMUserProfile res : result){
//            if (result1.get(position).getIdentifier().equals(data.getIdentify())){
                viewHolder.tvName.setText(result1.get(position).getNickName().toString());
                Glide.with(context).load(result1.get(position).getFaceUrl().toString())
                        .priority(Priority.HIGH)
                        .placeholder(R.mipmap.logo)
                        .into(viewHolder.avatar);
//            }else {
//                viewHolder.tvName.setText(data.getName());
//                viewHolder.avatar.setImageResource(data.getAvatar());
//            }
        }catch (Exception e){
            e.printStackTrace();
        }
//        getFriendsProfile();
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
}
