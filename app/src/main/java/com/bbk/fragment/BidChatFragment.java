package com.bbk.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bbk.activity.BidBillDetailActivity;
import com.bbk.activity.BidHomeActivity;
import com.bbk.activity.R;
import com.bbk.chat.adapters.ConversationAdapter;
import com.bbk.chat.model.Conversation;
import com.bbk.chat.model.CustomMessage;
import com.bbk.chat.model.FriendshipConversation;
import com.bbk.chat.model.GroupManageConversation;
import com.bbk.chat.model.MessageFactory;
import com.bbk.chat.model.NomalConversation;
import com.bbk.chat.ui.ChatActivity;
import com.bbk.chat.ui.HomeActivity;
import com.bbk.chat.utils.PushUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupCacheInfo;
import com.tencent.imsdk.ext.group.TIMGroupPendencyItem;
import com.tencent.imsdk.ext.sns.TIMFriendFutureItem;
import com.tencent.qcloud.presentation.presenter.ConversationPresenter;
import com.tencent.qcloud.presentation.presenter.FriendshipManagerPresenter;
import com.tencent.qcloud.presentation.presenter.GroupManagerPresenter;
import com.tencent.qcloud.presentation.viewfeatures.ConversationView;
import com.tencent.qcloud.presentation.viewfeatures.FriendshipMessageView;
import com.tencent.qcloud.presentation.viewfeatures.GroupManageMessageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * 会话列表界面
 */
public class BidChatFragment extends Fragment implements ConversationView,FriendshipMessageView,GroupManageMessageView {

    private final String TAG = "ConversationFragment";

    private View view;
    private List<Conversation> conversationList = new LinkedList<>();
    private ConversationAdapter adapter;
    private ListView listView;
    private ConversationPresenter presenter;
    private FriendshipManagerPresenter friendshipManagerPresenter;
    private GroupManagerPresenter groupManagerPresenter;
    private List<String> groupList;
    private FriendshipConversation friendshipConversation;
    private GroupManageConversation groupManageConversation;
    List<String> users = new ArrayList<String>();
    private TIMFriendshipManager timFriendshipManager;
    List<TIMUserProfile> result1;

    public BidChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null){
            view = inflater.inflate(R.layout.fragment_bid_chat_ytx, container, false);
            listView = (ListView) view.findViewById(R.id.list);
            friendshipManagerPresenter = new FriendshipManagerPresenter(this);
            groupManagerPresenter = new GroupManagerPresenter(this);
            presenter = new ConversationPresenter(this);
            presenter.getConversation();
            registerForContextMenu(listView);
        }
        return view;

    }

    @Override
    public void onResume(){
        super.onResume();
        refresh();
        PushUtil.getInstance().reset();
    }



    /**
     * 初始化界面或刷新界面
     *
     * @param timconversationList
     */
    @Override
    public void initView(List<TIMConversation> timconversationList) {
        this.conversationList.clear();
        groupList = new ArrayList<>();
        for (TIMConversation item:timconversationList){
            switch (item.getType()){
                case C2C:
                case Group:
                    this.conversationList.add(new NomalConversation(item));
                    groupList.add(item.getPeer());
                    try {
                        for (int i= 0;i<this.conversationList.size();i++){
                            //待获取用户资料的好友列表
                            users.add(this.conversationList.get(i).getIdentify());
                        }
                        removeDuplicate(users);
                        Log.e("==========", "getUsersProfile " + users);
                        if (users != null && users.size() >0){
                            getFriendsProfile();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
            }
        }
        friendshipManagerPresenter.getFriendshipLastMessage();
        groupManagerPresenter.getGroupManageLastMessage();
    }

    public   static   List  removeDuplicate(List list)  {
        for  ( int  i  =   0 ; i  <  list.size()  -   1 ; i ++ )  {
            for  ( int  j  =  list.size()  -   1 ; j  >  i; j -- )  {
                if  (list.get(j).equals(list.get(i)))  {
                    list.remove(j);
                }
            }
        }
        return list;
    }
    private void getFriendsProfile(){
        //获取好友资料
        timFriendshipManager.getInstance().getUsersProfile(users, new TIMValueCallBack<List<TIMUserProfile>>(){
            @Override
            public void onError(int code, String desc){
                //错误码code和错误描述desc，可用于定位请求失败原因
                //错误码code列表请参见错误码表
                Log.e("==========", "getUsersProfile failed: " + code + " desc");
            }

            @Override
            public void onSuccess(final List<TIMUserProfile> result){
                                Log.e("==========", "getUsersProfile succ"+result);
                                result1 = result;
                adapter = new ConversationAdapter(getActivity(), R.layout.item_conversation, conversationList ,result1);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        conversationList.get(position).navToDetail(getActivity());
                        try {
                            Intent intent = new Intent(getActivity(),ChatActivity.class);
                            intent.putExtra("identify",result1.get(position).getIdentifier());
                            intent.putExtra("type", TIMConversationType.C2C);
                            startActivity(intent);
                            if (conversationList.get(position) instanceof GroupManageConversation) {
                                groupManagerPresenter.getGroupManageLastMessage();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
                if (adapter != null){
                    adapter.notifyDataSetChanged();
                }
                for(TIMUserProfile res : result){
//                    Log.e("================", "identifier: " + res);
//                    viewHolder.tvName.setText(res.getNickName());
////                    viewHolder.avatar.setImageResource(data.getAvatar());
//                    Glide.with(context)
//                            .load(res.getFaceUrl())
//                            .priority(Priority.HIGH)
//                            .placeholder(R.mipmap.zw_img_300)
//                            .into(viewHolder.avatar);
                }
            }
        });
    }
    /**
     * 更新最新消息显示
     *
     * @param message 最后一条消息
     */
    @Override
    public void updateMessage(TIMMessage message) {
        if (message == null){
            if (adapter != null){
                adapter.notifyDataSetChanged();
            }
            return;
        }
        if (message.getConversation().getType() == TIMConversationType.System){
            groupManagerPresenter.getGroupManageLastMessage();
            return;
        }
        if (MessageFactory.getMessage(message) instanceof CustomMessage) return;
        NomalConversation conversation = new NomalConversation(message.getConversation());
        Iterator<Conversation> iterator =conversationList.iterator();
        while (iterator.hasNext()){
            Conversation c = iterator.next();
            if (conversation.equals(c)){
                conversation = (NomalConversation) c;
                iterator.remove();
                break;
            }
        }
        conversation.setLastMessage(MessageFactory.getMessage(message));
        conversationList.add(conversation);
        Collections.sort(conversationList);
        refresh();
    }

    /**
     * 更新好友关系链消息
     */
    @Override
    public void updateFriendshipMessage() {
        friendshipManagerPresenter.getFriendshipLastMessage();
    }

    /**
     * 删除会话
     *
     * @param identify
     */
    @Override
    public void removeConversation(String identify) {
        Iterator<Conversation> iterator = conversationList.iterator();
        while(iterator.hasNext()){
            Conversation conversation = iterator.next();
            if (conversation.getIdentify()!=null&&conversation.getIdentify().equals(identify)){
                iterator.remove();
                if (adapter != null){
                    adapter.notifyDataSetChanged();
                }
                return;
            }
        }
    }

    /**
     * 更新群信息
     *
     * @param info
     */
    @Override
    public void updateGroupInfo(TIMGroupCacheInfo info) {
        for (Conversation conversation : conversationList){
            if (conversation.getIdentify()!=null && conversation.getIdentify().equals(info.getGroupInfo().getGroupId())){
                if (adapter != null){
                    adapter.notifyDataSetChanged();
                }
                return;
            }
        }
    }

    /**
     * 刷新
     */
    @Override
    public void refresh() {
        Collections.sort(conversationList);
        if (adapter != null){
            adapter.notifyDataSetChanged();
        }
        if (getParentFragment() instanceof BidMessageFragment)
            ((BidMessageFragment)getParentFragment()).setMsgUnread(getTotalUnreadNum() == 0);
    }



    /**
     * 获取好友关系链管理系统最后一条消息的回调
     *
     * @param message 最后一条消息
     * @param unreadCount 未读数
     */
    @Override
    public void onGetFriendshipLastMessage(TIMFriendFutureItem message, long unreadCount) {
        if (friendshipConversation == null){
            friendshipConversation = new FriendshipConversation(message);
            conversationList.add(friendshipConversation);
        }else{
            friendshipConversation.setLastMessage(message);
        }
        friendshipConversation.setUnreadCount(unreadCount);
        Collections.sort(conversationList);
        refresh();
    }

    /**
     * 获取好友关系链管理最后一条系统消息的回调
     *
     * @param message 消息列表
     */
    @Override
    public void onGetFriendshipMessage(List<TIMFriendFutureItem> message) {
        friendshipManagerPresenter.getFriendshipLastMessage();
    }

    /**
     * 获取群管理最后一条系统消息的回调
     *
     * @param message     最后一条消息
     * @param unreadCount 未读数
     */
    @Override
    public void onGetGroupManageLastMessage(TIMGroupPendencyItem message, long unreadCount) {
        if (groupManageConversation == null){
            groupManageConversation = new GroupManageConversation(message);
            conversationList.add(groupManageConversation);
        }else{
            groupManageConversation.setLastMessage(message);
        }
        groupManageConversation.setUnreadCount(unreadCount);
        Collections.sort(conversationList);
        refresh();
    }

    /**
     * 获取群管理系统消息的回调
     *
     * @param message 分页的消息列表
     */
    @Override
    public void onGetGroupManageMessage(List<TIMGroupPendencyItem> message) {

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        Conversation conversation = conversationList.get(info.position);
        if (conversation instanceof NomalConversation){
            menu.add(0, 1, Menu.NONE, getString(R.string.conversation_del));
        }
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        NomalConversation conversation = (NomalConversation) conversationList.get(info.position);
        switch (item.getItemId()) {
            case 1:
                if (conversation != null){
                    if (presenter.delConversation(conversation.getType(), conversation.getIdentify())){
                        conversationList.remove(conversation);
                        if (adapter != null){
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    private long getTotalUnreadNum(){
        long num = 0;
        for (Conversation conversation : conversationList){
            num += conversation.getUnreadNum();
        }
        return num;
    }




}
