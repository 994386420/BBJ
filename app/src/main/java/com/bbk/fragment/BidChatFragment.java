package com.bbk.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
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
import com.bbk.activity.BidDetailActivity;
import com.bbk.activity.BidHomeActivity;
import com.bbk.activity.HomeActivity;
import com.bbk.activity.R;
import com.bbk.chat.adapters.ConversationAdapter;
import com.bbk.chat.model.Conversation;
import com.bbk.chat.model.CustomMessage;
import com.bbk.chat.model.FriendshipConversation;
import com.bbk.chat.model.GroupManageConversation;
import com.bbk.chat.model.MessageFactory;
import com.bbk.chat.model.NomalConversation;
import com.bbk.chat.ui.ChatActivity;
import com.bbk.chat.utils.PushUtil;
import com.bbk.resource.Constants;
import com.bbk.resource.NewConstants;
import com.bbk.util.StringUtil;
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
import com.tencent.qcloud.sdk.Constant;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static com.tencent.open.utils.Global.getSharedPreferences;

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
    List<String> users1 = new ArrayList<String>();
    List<String> users2 = new ArrayList<String>();
    List<String> users3 = new ArrayList<String>();
    List<String> users4 = new ArrayList<String>();
    List<String> users5 = new ArrayList<String>();
    List<String> users6 = new ArrayList<String>();
    List<String> users7 = new ArrayList<String>();
    List<String> users8 = new ArrayList<String>();
    List<String> users9 = new ArrayList<String>();
    List<String> users10 = new ArrayList<String>();
    List<String> users11= new ArrayList<String>();
    List<String> users12 = new ArrayList<String>();
    List<String> users13= new ArrayList<String>();
    List<String> users14 = new ArrayList<String>();
    List<String> users15 = new ArrayList<String>();
    List<String> users16 = new ArrayList<String>();
    List<String> users17 = new ArrayList<String>();
    List<String> users18 = new ArrayList<String>();
    List<String> users19 = new ArrayList<String>();
    private TIMFriendshipManager timFriendshipManager;
    List<TIMUserProfile> result1;
    private SharedPreferences sharedPreferences;
    private SharedPreferences sharedPreferencesnickname;
    TIMMessage message;
    public static long mChatMessage;
    public BidChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /***
         * 判断Fragment是否已经添加了contentView（第一次加载时，可以将view保存下 来，再  次加载时，判断保存下来的view是否为null），
         * 如果保存的view为null，返回新的view ，否则，先将 保存的view从父view中移除，然后将该view返回出去
         */
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
            return view;
        }
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
                    break;
            }
        }
        friendshipManagerPresenter.getFriendshipLastMessage();
        groupManagerPresenter.getGroupManageLastMessage();
        Collections.sort(groupList);
        refreshView();
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
//        updateMessage(message);
        Collections.sort(conversationList);
        handler.sendEmptyMessageDelayed(1,0);
        if (getParentFragment() instanceof BidMessageFragment)
            ((BidMessageFragment)getParentFragment()).setMsgUnread(getTotalUnreadNum() == 0);
    }


    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    if (users != null){
                        users.clear();
                    }
                    if (users1 != null){
                        users1.clear();
                    }
                    if (users2 != null){
                        users2.clear();
                    }
                    if (users3 != null){
                        users3.clear();
                    }
                    if (users4 != null){
                        users4.clear();
                    }
                    if (users5 != null){
                        users5.clear();
                    }
                    if (users6 != null){
                        users6.clear();
                    }
                    if (users7 != null){
                        users7.clear();
                    }
                    if (users8 != null){
                        users8.clear();
                    }
                    if (users9 != null){
                        users9.clear();
                    }
                    if (users10 != null){
                        users10.clear();
                    }
                    if (users11 != null){
                        users11.clear();
                    }
                    if (users12 != null){
                        users12.clear();
                    }
                    if (users13 != null){
                        users13.clear();
                    }
                    if (users14 != null){
                        users14.clear();
                    }
                    if (users15 != null){
                        users15.clear();
                    }
                    if (users16 != null){
                        users16.clear();
                    }
                    if (users17 != null){
                        users17.clear();
                    }
                    if (users18 != null){
                        users18.clear();
                    }
                    if (users19 != null){
                        users19.clear();
                    }
                    switch (conversationList.size()){
                        case 1:
                            users.add(conversationList.get(0).getIdentify());
                            getfriends(users);
                            break;
                        case 2:
                            users.add(conversationList.get(0).getIdentify());
                            users1.add(conversationList.get(1).getIdentify());
                            getfriends(users);
                            break;
                        case 3:
                            users.add(conversationList.get(0).getIdentify());
                            users1.add(conversationList.get(1).getIdentify());
                            users2.add(conversationList.get(2).getIdentify());
                            getfriends(users);
                            break;
                        case 4:
                            users.add(conversationList.get(0).getIdentify());
                            users1.add(conversationList.get(1).getIdentify());
                            users2.add(conversationList.get(2).getIdentify());
                            users3.add(conversationList.get(3).getIdentify());
                            getfriends(users);
                            break;
                        case 5:
                            users.add(conversationList.get(0).getIdentify());
                            users1.add(conversationList.get(1).getIdentify());
                            users2.add(conversationList.get(2).getIdentify());
                            users3.add(conversationList.get(3).getIdentify());
                            users4.add(conversationList.get(4).getIdentify());
                            getfriends(users);
                            break;
                        case 6:
                            users.add(conversationList.get(0).getIdentify());
                            users1.add(conversationList.get(1).getIdentify());
                            users2.add(conversationList.get(2).getIdentify());
                            users3.add(conversationList.get(3).getIdentify());
                            users4.add(conversationList.get(4).getIdentify());
                            users5.add(conversationList.get(5).getIdentify());
                            getfriends(users);
                            break;
                        case 7:
                            users.add(conversationList.get(0).getIdentify());
                            users1.add(conversationList.get(1).getIdentify());
                            users2.add(conversationList.get(2).getIdentify());
                            users3.add(conversationList.get(3).getIdentify());
                            users4.add(conversationList.get(4).getIdentify());
                            users5.add(conversationList.get(5).getIdentify());
                            users6.add(conversationList.get(6).getIdentify());
                            getfriends(users);
                            break;
                        case 8:
                            users.add(conversationList.get(0).getIdentify());
                            users1.add(conversationList.get(1).getIdentify());
                            users2.add(conversationList.get(2).getIdentify());
                            users3.add(conversationList.get(3).getIdentify());
                            users4.add(conversationList.get(4).getIdentify());
                            users5.add(conversationList.get(5).getIdentify());
                            users6.add(conversationList.get(6).getIdentify());
                            users7.add(conversationList.get(7).getIdentify());
                            getfriends(users);
                            break;
                        case 9:
                            users.add(conversationList.get(0).getIdentify());
                            users1.add(conversationList.get(1).getIdentify());
                            users2.add(conversationList.get(2).getIdentify());
                            users3.add(conversationList.get(3).getIdentify());
                            users4.add(conversationList.get(4).getIdentify());
                            users5.add(conversationList.get(5).getIdentify());
                            users6.add(conversationList.get(6).getIdentify());
                            users7.add(conversationList.get(7).getIdentify());
                            users8.add(conversationList.get(8).getIdentify());
                            getfriends(users);
                            break;
                        case 10:
                            users.add(conversationList.get(0).getIdentify());
                            users1.add(conversationList.get(1).getIdentify());
                            users2.add(conversationList.get(2).getIdentify());
                            users3.add(conversationList.get(3).getIdentify());
                            users4.add(conversationList.get(4).getIdentify());
                            users5.add(conversationList.get(5).getIdentify());
                            users6.add(conversationList.get(6).getIdentify());
                            users7.add(conversationList.get(7).getIdentify());
                            users8.add(conversationList.get(8).getIdentify());
                            users9.add(conversationList.get(9).getIdentify());
                            getfriends(users);
                        case 11:
                            users.add(conversationList.get(0).getIdentify());
                            users1.add(conversationList.get(1).getIdentify());
                            users2.add(conversationList.get(2).getIdentify());
                            users3.add(conversationList.get(3).getIdentify());
                            users4.add(conversationList.get(4).getIdentify());
                            users5.add(conversationList.get(5).getIdentify());
                            users6.add(conversationList.get(6).getIdentify());
                            users7.add(conversationList.get(7).getIdentify());
                            users8.add(conversationList.get(8).getIdentify());
                            users9.add(conversationList.get(9).getIdentify());
                            users10.add(conversationList.get(10).getIdentify());
                            getfriends(users);
                            break;
                        case 12:
                            users.add(conversationList.get(0).getIdentify());
                            users1.add(conversationList.get(1).getIdentify());
                            users2.add(conversationList.get(2).getIdentify());
                            users3.add(conversationList.get(3).getIdentify());
                            users4.add(conversationList.get(4).getIdentify());
                            users5.add(conversationList.get(5).getIdentify());
                            users6.add(conversationList.get(6).getIdentify());
                            users7.add(conversationList.get(7).getIdentify());
                            users8.add(conversationList.get(8).getIdentify());
                            users9.add(conversationList.get(9).getIdentify());
                            users10.add(conversationList.get(10).getIdentify());
                            users11.add(conversationList.get(11).getIdentify());
                            getfriends(users);
                            break;
                        case 13:
                            users.add(conversationList.get(0).getIdentify());
                            users1.add(conversationList.get(1).getIdentify());
                            users2.add(conversationList.get(2).getIdentify());
                            users3.add(conversationList.get(3).getIdentify());
                            users4.add(conversationList.get(4).getIdentify());
                            users5.add(conversationList.get(5).getIdentify());
                            users6.add(conversationList.get(6).getIdentify());
                            users7.add(conversationList.get(7).getIdentify());
                            users8.add(conversationList.get(8).getIdentify());
                            users9.add(conversationList.get(9).getIdentify());
                            users10.add(conversationList.get(10).getIdentify());
                            users11.add(conversationList.get(11).getIdentify());
                            users12.add(conversationList.get(12).getIdentify());
                            getfriends(users);
                            break;
                        case 14:
                            users.add(conversationList.get(0).getIdentify());
                            users1.add(conversationList.get(1).getIdentify());
                            users2.add(conversationList.get(2).getIdentify());
                            users3.add(conversationList.get(3).getIdentify());
                            users4.add(conversationList.get(4).getIdentify());
                            users5.add(conversationList.get(5).getIdentify());
                            users6.add(conversationList.get(6).getIdentify());
                            users7.add(conversationList.get(7).getIdentify());
                            users8.add(conversationList.get(8).getIdentify());
                            users9.add(conversationList.get(9).getIdentify());
                            users10.add(conversationList.get(10).getIdentify());
                            users11.add(conversationList.get(11).getIdentify());
                            users12.add(conversationList.get(12).getIdentify());
                            users13.add(conversationList.get(13).getIdentify());
                            getfriends(users);
                            break;
                        case 15:
                            users.add(conversationList.get(0).getIdentify());
                            users1.add(conversationList.get(1).getIdentify());
                            users2.add(conversationList.get(2).getIdentify());
                            users3.add(conversationList.get(3).getIdentify());
                            users4.add(conversationList.get(4).getIdentify());
                            users5.add(conversationList.get(5).getIdentify());
                            users6.add(conversationList.get(6).getIdentify());
                            users7.add(conversationList.get(7).getIdentify());
                            users8.add(conversationList.get(8).getIdentify());
                            users9.add(conversationList.get(9).getIdentify());
                            users10.add(conversationList.get(10).getIdentify());
                            users11.add(conversationList.get(11).getIdentify());
                            users12.add(conversationList.get(12).getIdentify());
                            users13.add(conversationList.get(13).getIdentify());
                            users14.add(conversationList.get(14).getIdentify());
                            getfriends(users);
                            break;
                        case 16:
                            users.add(conversationList.get(0).getIdentify());
                            users1.add(conversationList.get(1).getIdentify());
                            users2.add(conversationList.get(2).getIdentify());
                            users3.add(conversationList.get(3).getIdentify());
                            users4.add(conversationList.get(4).getIdentify());
                            users5.add(conversationList.get(5).getIdentify());
                            users6.add(conversationList.get(6).getIdentify());
                            users7.add(conversationList.get(7).getIdentify());
                            users8.add(conversationList.get(8).getIdentify());
                            users9.add(conversationList.get(9).getIdentify());
                            users10.add(conversationList.get(10).getIdentify());
                            users11.add(conversationList.get(11).getIdentify());
                            users12.add(conversationList.get(12).getIdentify());
                            users13.add(conversationList.get(13).getIdentify());
                            users14.add(conversationList.get(14).getIdentify());
                            users15.add(conversationList.get(15).getIdentify());
                            getfriends(users);
                            break;
                        case 17:
                            users.add(conversationList.get(0).getIdentify());
                            users1.add(conversationList.get(1).getIdentify());
                            users2.add(conversationList.get(2).getIdentify());
                            users3.add(conversationList.get(3).getIdentify());
                            users4.add(conversationList.get(4).getIdentify());
                            users5.add(conversationList.get(5).getIdentify());
                            users6.add(conversationList.get(6).getIdentify());
                            users7.add(conversationList.get(7).getIdentify());
                            users8.add(conversationList.get(8).getIdentify());
                            users9.add(conversationList.get(9).getIdentify());
                            users10.add(conversationList.get(10).getIdentify());
                            users11.add(conversationList.get(11).getIdentify());
                            users12.add(conversationList.get(12).getIdentify());
                            users13.add(conversationList.get(13).getIdentify());
                            users14.add(conversationList.get(14).getIdentify());
                            users15.add(conversationList.get(15).getIdentify());
                            users16.add(conversationList.get(16).getIdentify());
                            getfriends(users);
                            break;
                        case 18:
                            users.add(conversationList.get(0).getIdentify());
                            users1.add(conversationList.get(1).getIdentify());
                            users2.add(conversationList.get(2).getIdentify());
                            users3.add(conversationList.get(3).getIdentify());
                            users4.add(conversationList.get(4).getIdentify());
                            users5.add(conversationList.get(5).getIdentify());
                            users6.add(conversationList.get(6).getIdentify());
                            users7.add(conversationList.get(7).getIdentify());
                            users8.add(conversationList.get(8).getIdentify());
                            users9.add(conversationList.get(9).getIdentify());
                            users10.add(conversationList.get(10).getIdentify());
                            users11.add(conversationList.get(11).getIdentify());
                            users12.add(conversationList.get(12).getIdentify());
                            users13.add(conversationList.get(13).getIdentify());
                            users14.add(conversationList.get(14).getIdentify());
                            users15.add(conversationList.get(15).getIdentify());
                            users16.add(conversationList.get(16).getIdentify());
                            users17.add(conversationList.get(17).getIdentify());
                            getfriends(users);
                            break;
                        case 19:
                            users.add(conversationList.get(0).getIdentify());
                            users1.add(conversationList.get(1).getIdentify());
                            users2.add(conversationList.get(2).getIdentify());
                            users3.add(conversationList.get(3).getIdentify());
                            users4.add(conversationList.get(4).getIdentify());
                            users5.add(conversationList.get(5).getIdentify());
                            users6.add(conversationList.get(6).getIdentify());
                            users7.add(conversationList.get(7).getIdentify());
                            users8.add(conversationList.get(8).getIdentify());
                            users9.add(conversationList.get(9).getIdentify());
                            users10.add(conversationList.get(10).getIdentify());
                            users11.add(conversationList.get(11).getIdentify());
                            users12.add(conversationList.get(12).getIdentify());
                            users13.add(conversationList.get(13).getIdentify());
                            users14.add(conversationList.get(14).getIdentify());
                            users15.add(conversationList.get(15).getIdentify());
                            users16.add(conversationList.get(16).getIdentify());
                            users17.add(conversationList.get(17).getIdentify());
                            users18.add(conversationList.get(18).getIdentify());
                            getfriends(users);
                            break;
                        case 20:
                            users.add(conversationList.get(0).getIdentify());
                            users1.add(conversationList.get(1).getIdentify());
                            users2.add(conversationList.get(2).getIdentify());
                            users3.add(conversationList.get(3).getIdentify());
                            users4.add(conversationList.get(4).getIdentify());
                            users5.add(conversationList.get(5).getIdentify());
                            users6.add(conversationList.get(6).getIdentify());
                            users7.add(conversationList.get(7).getIdentify());
                            users8.add(conversationList.get(8).getIdentify());
                            users9.add(conversationList.get(9).getIdentify());
                            users10.add(conversationList.get(10).getIdentify());
                            users11.add(conversationList.get(11).getIdentify());
                            users12.add(conversationList.get(12).getIdentify());
                            users13.add(conversationList.get(13).getIdentify());
                            users14.add(conversationList.get(14).getIdentify());
                            users15.add(conversationList.get(15).getIdentify());
                            users16.add(conversationList.get(16).getIdentify());
                            users17.add(conversationList.get(17).getIdentify());
                            users18.add(conversationList.get(18).getIdentify());
                            users19.add(conversationList.get(19).getIdentify());
                            getfriends(users);
                            break;
                    }
                    break;
            }
        }
    };

    private void getfriends(List<String> users){
        //获取好友资料
        TIMFriendshipManager.getInstance().getUsersProfile(users, new TIMValueCallBack<List<TIMUserProfile>>(){
            @Override
            public void onError(int code, String desc){
            }
            @Override
            public void onSuccess(final List<TIMUserProfile> result){
                        NewConstants.mChatMap.put(0 + "", result.get(0).getFaceUrl());
                        NewConstants.mChatNickMameMap.put(0+"",result.get(0).getNickName());
                        if (users1 != null && users1.size() > 0){
                            getfriends1(users1);
                        }else {
                            adapter();
                        }
            }
        });
    }
    private void getfriends1(final List<String> users){
        //获取好友资料
        TIMFriendshipManager.getInstance().getUsersProfile(users, new TIMValueCallBack<List<TIMUserProfile>>(){
            @Override
            public void onError(int code, String desc){
            }
            @Override
            public void onSuccess(final List<TIMUserProfile> result){
                NewConstants.mChatMap.put(1 + "", result.get(0).getFaceUrl());
                NewConstants.mChatNickMameMap.put(1+"",result.get(0).getNickName());
                Log.i("刷新数据====+++", NewConstants.mChatMap+"==========="+NewConstants.mChatNickMameMap+"===="+users2.toString());
                if (users2 != null && users2.size() > 0){
                    getfriends2(users2);
                }else {
                    adapter();
                }
            }
        });
    }
    private void getfriends2(List<String> users){
        //获取好友资料
        TIMFriendshipManager.getInstance().getUsersProfile(users, new TIMValueCallBack<List<TIMUserProfile>>(){
            @Override
            public void onError(int code, String desc){
            }
            @Override
            public void onSuccess(final List<TIMUserProfile> result){
                NewConstants.mChatMap.put(2 + "", result.get(0).getFaceUrl());
                NewConstants.mChatNickMameMap.put(2+"",result.get(0).getNickName());
                if (users3 != null && users3.size() > 0){
                    getfriends3(users3);
                }else {
                    adapter();
                }
            }
        });
    }

    private void getfriends3(List<String> users){
        //获取好友资料
        TIMFriendshipManager.getInstance().getUsersProfile(users, new TIMValueCallBack<List<TIMUserProfile>>(){
            @Override
            public void onError(int code, String desc){
            }
            @Override
            public void onSuccess(final List<TIMUserProfile> result){
                NewConstants.mChatMap.put(3 + "", result.get(0).getFaceUrl());
                NewConstants.mChatNickMameMap.put(3+"",result.get(0).getNickName());
                if (users4 != null&& users4.size() > 0){
                    getfriends4(users4);
                }else {
                    adapter();
                }
            }
        });
    }

    private void getfriends4(List<String> users){
        //获取好友资料
        TIMFriendshipManager.getInstance().getUsersProfile(users, new TIMValueCallBack<List<TIMUserProfile>>(){
            @Override
            public void onError(int code, String desc){
            }
            @Override
            public void onSuccess(final List<TIMUserProfile> result){
                NewConstants.mChatMap.put(4 + "", result.get(0).getFaceUrl());
                NewConstants.mChatNickMameMap.put(4+"",result.get(0).getNickName());
                if (users5 != null&& users5.size() > 0){
                    getfriends5(users5);
                }else {
                    adapter();
                }
            }
        });
    }

    private void getfriends5(List<String> users){
        //获取好友资料
        TIMFriendshipManager.getInstance().getUsersProfile(users, new TIMValueCallBack<List<TIMUserProfile>>(){
            @Override
            public void onError(int code, String desc){
            }
            @Override
            public void onSuccess(final List<TIMUserProfile> result){
                NewConstants.mChatMap.put(5 + "", result.get(0).getFaceUrl());
                NewConstants.mChatNickMameMap.put(5+"",result.get(0).getNickName());
                if (users6 != null&& users6.size() > 0){
                    getfriends6(users6);
                }else {
                    adapter();
                }
            }
        });
    }

    private void getfriends6(List<String> users){
        //获取好友资料
        TIMFriendshipManager.getInstance().getUsersProfile(users, new TIMValueCallBack<List<TIMUserProfile>>(){
            @Override
            public void onError(int code, String desc){
            }
            @Override
            public void onSuccess(final List<TIMUserProfile> result){
                NewConstants.mChatMap.put(6 + "", result.get(0).getFaceUrl());
                NewConstants.mChatNickMameMap.put(6+"",result.get(0).getNickName());
                if (users7 != null&& users7.size() > 0){
                    getfriends7(users7);
                }else {
                    adapter();
                }
            }
        });
    }

    private void getfriends7(List<String> users){
        //获取好友资料
        TIMFriendshipManager.getInstance().getUsersProfile(users, new TIMValueCallBack<List<TIMUserProfile>>(){
            @Override
            public void onError(int code, String desc){
            }
            @Override
            public void onSuccess(final List<TIMUserProfile> result){
                NewConstants.mChatMap.put(7 + "", result.get(0).getFaceUrl());
                NewConstants.mChatNickMameMap.put(7+"",result.get(0).getNickName());
                if (users8 != null&& users8.size() > 0){
                    getfriends8(users8);
                }else {
                    adapter();
                }
            }
        });
    }

    private void getfriends8(final List<String> users){
        //获取好友资料
        TIMFriendshipManager.getInstance().getUsersProfile(users, new TIMValueCallBack<List<TIMUserProfile>>(){
            @Override
            public void onError(int code, String desc){
            }
            @Override
            public void onSuccess(final List<TIMUserProfile> result){
                NewConstants.mChatMap.put(8 + "", result.get(0).getFaceUrl());
                NewConstants.mChatNickMameMap.put(8+"",result.get(0).getNickName());
                if (users9 != null&& users9.size() > 0){
                    getfriends9(users9);
                }else {
                    adapter();
                }
            }
        });
    }

    private void getfriends9(List<String> users) {
        //获取好友资料
        TIMFriendshipManager.getInstance().getUsersProfile(users, new TIMValueCallBack<List<TIMUserProfile>>() {
            @Override
            public void onError(int code, String desc) {
            }

            @Override
            public void onSuccess(final List<TIMUserProfile> result) {
                NewConstants.mChatMap.put(9 + "", result.get(0).getFaceUrl());
                NewConstants.mChatNickMameMap.put(9 + "", result.get(0).getNickName());
                if (users10 != null && users10.size() > 0) {
                    getfriends10(users10);
                } else {
                    adapter();
                }
            }
        });
    }
    private void getfriends10(List<String> users) {
        //获取好友资料
        TIMFriendshipManager.getInstance().getUsersProfile(users, new TIMValueCallBack<List<TIMUserProfile>>() {
            @Override
            public void onError(int code, String desc) {
            }

            @Override
            public void onSuccess(final List<TIMUserProfile> result) {
                NewConstants.mChatMap.put(10 + "", result.get(0).getFaceUrl());
                NewConstants.mChatNickMameMap.put(10 + "", result.get(0).getNickName());
                if (users11 != null && users11.size() > 0) {
                    getfriends11(users11);
                } else {
                    adapter();
                }
            }
        });
    }

    private void getfriends11(List<String> users){
        //获取好友资料
        TIMFriendshipManager.getInstance().getUsersProfile(users, new TIMValueCallBack<List<TIMUserProfile>>(){
            @Override
            public void onError(int code, String desc){
            }
            @Override
            public void onSuccess(final List<TIMUserProfile> result){
                NewConstants.mChatMap.put(11 + "", result.get(0).getFaceUrl());
                NewConstants.mChatNickMameMap.put(11+"",result.get(0).getNickName());
                if (users12 != null&& users12.size() > 0){
                    getfriends12(users12);
                }else {
                    adapter();
                }
            }
        });
    }
    private void getfriends12(List<String> users) {
        //获取好友资料
        TIMFriendshipManager.getInstance().getUsersProfile(users, new TIMValueCallBack<List<TIMUserProfile>>() {
            @Override
            public void onError(int code, String desc) {
            }

            @Override
            public void onSuccess(final List<TIMUserProfile> result) {
                NewConstants.mChatMap.put(12 + "", result.get(0).getFaceUrl());
                NewConstants.mChatNickMameMap.put(12 + "", result.get(0).getNickName());
                if (users13 != null && users13.size() > 0) {
                    getfriends13(users13);
                } else {
                    adapter();
                }
            }
        });
    }
    private void getfriends13(List<String> users) {
        //获取好友资料
        TIMFriendshipManager.getInstance().getUsersProfile(users, new TIMValueCallBack<List<TIMUserProfile>>() {
            @Override
            public void onError(int code, String desc) {
            }

            @Override
            public void onSuccess(final List<TIMUserProfile> result) {
                NewConstants.mChatMap.put(13 + "", result.get(0).getFaceUrl());
                NewConstants.mChatNickMameMap.put(13 + "", result.get(0).getNickName());
                if (users14 != null && users14.size() > 0) {
                    getfriends14(users14);
                } else {
                    adapter();
                }
            }
        });
    }

    private void getfriends14(List<String> users) {
        //获取好友资料
        TIMFriendshipManager.getInstance().getUsersProfile(users, new TIMValueCallBack<List<TIMUserProfile>>() {
            @Override
            public void onError(int code, String desc) {
            }

            @Override
            public void onSuccess(final List<TIMUserProfile> result) {
                NewConstants.mChatMap.put(14 + "", result.get(0).getFaceUrl());
                NewConstants.mChatNickMameMap.put(14 + "", result.get(0).getNickName());
                if (users15 != null && users15.size() > 0) {
                    getfriends15(users15);
                } else {
                    adapter();
                }
            }
        });
    }
    private void getfriends15(List<String> users) {
        //获取好友资料
        TIMFriendshipManager.getInstance().getUsersProfile(users, new TIMValueCallBack<List<TIMUserProfile>>() {
            @Override
            public void onError(int code, String desc) {
            }

            @Override
            public void onSuccess(final List<TIMUserProfile> result) {
                NewConstants.mChatMap.put(15 + "", result.get(0).getFaceUrl());
                NewConstants.mChatNickMameMap.put(15 + "", result.get(0).getNickName());
                if (users16 != null && users16.size() > 0) {
                    getfriends16(users16);
                } else {
                    adapter();
                }
            }
        });
    }
    private void getfriends16(List<String> users) {
        //获取好友资料
        TIMFriendshipManager.getInstance().getUsersProfile(users, new TIMValueCallBack<List<TIMUserProfile>>() {
            @Override
            public void onError(int code, String desc) {
            }

            @Override
            public void onSuccess(final List<TIMUserProfile> result) {
                NewConstants.mChatMap.put(16 + "", result.get(0).getFaceUrl());
                NewConstants.mChatNickMameMap.put(16 + "", result.get(0).getNickName());
                if (users17 != null && users17.size() > 0) {
                    getfriends17(users17);
                } else {
                    adapter();
                }
            }
        });
    }

    private void getfriends17(List<String> users) {
        //获取好友资料
        TIMFriendshipManager.getInstance().getUsersProfile(users, new TIMValueCallBack<List<TIMUserProfile>>() {
            @Override
            public void onError(int code, String desc) {
            }

            @Override
            public void onSuccess(final List<TIMUserProfile> result) {
                NewConstants.mChatMap.put(17 + "", result.get(0).getFaceUrl());
                NewConstants.mChatNickMameMap.put(17 + "", result.get(0).getNickName());
                if (users18 != null && users18.size() > 0) {
                    getfriends18(users18);
                } else {
                    adapter();
                }
            }
        });
    }

    private void getfriends18(List<String> users) {
        //获取好友资料
        TIMFriendshipManager.getInstance().getUsersProfile(users, new TIMValueCallBack<List<TIMUserProfile>>() {
            @Override
            public void onError(int code, String desc) {
            }

            @Override
            public void onSuccess(final List<TIMUserProfile> result) {
                NewConstants.mChatMap.put(18 + "", result.get(0).getFaceUrl());
                NewConstants.mChatNickMameMap.put(18 + "", result.get(0).getNickName());
                if (users19 != null && users19.size() > 0) {
                    getfriends19(users19);
                } else {
                    adapter();
                }
            }
        });
    }

    private void getfriends19(List<String> users) {
        //获取好友资料
        TIMFriendshipManager.getInstance().getUsersProfile(users, new TIMValueCallBack<List<TIMUserProfile>>() {
            @Override
            public void onError(int code, String desc) {
            }

            @Override
            public void onSuccess(final List<TIMUserProfile> result) {
                NewConstants.mChatMap.put(19 + "", result.get(0).getFaceUrl());
                NewConstants.mChatNickMameMap.put(19 + "", result.get(0).getNickName());
                    adapter();
            }
        });
    }
    private void adapter(){
        try {
            SharedPreferences.Editor editor = getActivity().getSharedPreferences("hotHistory", getActivity().MODE_PRIVATE).edit();
            SharedPreferences.Editor editorNickName = getActivity().getSharedPreferences("nickname", getActivity().MODE_PRIVATE).edit();
            editor.putString("name", com.alibaba.fastjson.JSONObject.toJSON(NewConstants.mChatMap).toString());
            editorNickName.putString("nickname", com.alibaba.fastjson.JSONObject.toJSON(NewConstants.mChatNickMameMap).toString());
            editor.commit();
            editorNickName.commit();
            Log.i("刷新数据-----====", NewConstants.mChatMap+"==========="+NewConstants.mChatNickMameMap);

            sharedPreferences = getActivity().getSharedPreferences("hotHistory", getActivity().MODE_PRIVATE);
            sharedPreferencesnickname = getActivity().getSharedPreferences("nickname",getActivity().MODE_PRIVATE);
            if(sharedPreferences.getString("name",null)!=null &&sharedPreferencesnickname.getString("nickname",null)!=null ){
                String result1 = sharedPreferences.getString("name",null);
                String result2 = sharedPreferencesnickname.getString("nickname",null);
                if (result1 != null && result1.length() > 0 && result2 != null && result2.length() > 0 ) {
//                mHistoryLayout.setVisibility(View.VISIBLE);
                    NewConstants.mChatMap = NewConstants.getJsonObject(result1);
                    NewConstants.mChatNickMameMap = NewConstants.getJsonObject(result2);
                    adapter = new ConversationAdapter(getActivity(), R.layout.item_conversation, conversationList ,NewConstants.mChatMap,NewConstants.mChatNickMameMap);
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            conversationList.get(position).navToDetail(getActivity());
                            if (conversationList.get(position) instanceof GroupManageConversation) {
                                groupManagerPresenter.getGroupManageLastMessage();
                            }
                        }
                    });
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
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
        BidChatFragment.mChatMessage = num;
        //发镖主页跟主页消息数字角标显示
        if (BidHomeActivity.mNumImage != null){
            BidHomeActivity.mNumImage.setNum((int) num);
        }
        if (HomeActivity.mNumImageView != null){
            HomeActivity.mNumImageView.setNum((int) num);
        }
        return num;
    }


    private void refreshView(){
//        if (groupList != null){
//            //获取好友资料
//            timFriendshipManager.getInstance().getUsersProfile(groupList, new TIMValueCallBack<List<TIMUserProfile>>(){
//                @Override
//                public void onError(int code, String desc){
//                }
//                @Override
//                public void onSuccess(final List<TIMUserProfile> result){
//                    result1 = result;
//                    if (conversationList != null && result1 != null && getActivity() != null){
//                        adapter = new ConversationAdapter(getActivity(), R.layout.item_conversation, conversationList ,result1);
//                        listView.setAdapter(adapter);
//                        adapter.notifyDataSetChanged();
//                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                conversationList.get(position).navToDetail(getActivity());
//                                if (conversationList.get(position) instanceof GroupManageConversation) {
//                                    groupManagerPresenter.getGroupManageLastMessage();
//                                }
//                            }
//                        });
//                    }
//                }
//            });
//        }
    }
}
