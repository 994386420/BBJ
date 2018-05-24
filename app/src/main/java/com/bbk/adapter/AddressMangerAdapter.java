package com.bbk.adapter;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.baichuan.android.trade.adapter.login.AlibcLogin;
import com.alibaba.baichuan.android.trade.callback.AlibcLoginCallback;
import com.bbk.Bean.AddressMangerBean;
import com.bbk.Bean.NewHomeCzgBean;
import com.bbk.Bean.OnAddressListioner;
import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.activity.WebViewActivity;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by rtj on 2018/3/7.
 */

public class AddressMangerAdapter extends RecyclerView.Adapter{
    private Context context;
    List<AddressMangerBean> addressMangerBeans;
    private OnAddressListioner onAddressListioner;
    public AddressMangerAdapter(Context context, List<AddressMangerBean> addressMangerBeans){
        this.context = context;
        this.addressMangerBeans = addressMangerBeans;
    }
    public void setOnDeleteListioner(OnAddressListioner onDeleteListioner) {
        this.onAddressListioner = onDeleteListioner;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AddressMangerAdapter.ViewHolder ViewHolder = new AddressMangerAdapter.ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.address_manger_item, parent, false));
        return ViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        try {
            AddressMangerAdapter.ViewHolder viewHolder = (AddressMangerAdapter.ViewHolder) holder;
            initTop(viewHolder,position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return addressMangerBeans.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView receiver,phone,tag,address,ismoren;
        CheckBox checkBox;
        LinearLayout mDeleteLayout;
        public ViewHolder(View mView) {
            super(mView);
            receiver = mView.findViewById(R.id.receiver);
            phone =mView.findViewById(R.id.phone);
            tag = mView.findViewById(R.id.tag);
            address =mView.findViewById(R.id.address);
            ismoren = mView.findViewById(R.id.ismoren);
            checkBox = mView.findViewById(R.id.checkbox_set_moren);
            mDeleteLayout = mView.findViewById(R.id.delete_address_layout);
        }
    }
    private void initTop(final AddressMangerAdapter.ViewHolder viewHolder, final int position) {
        try {
            String receiver = addressMangerBeans.get(position).getReceiver();
            String phone = addressMangerBeans.get(position).getPhone();
            String tag = addressMangerBeans.get(position).getTag();
            String address = addressMangerBeans.get(position).getArea()+addressMangerBeans.get(position).getStreet();
            viewHolder.receiver.setText(receiver);
            viewHolder.phone.setText(phone);
            viewHolder.tag.setText(tag);
            viewHolder.address.setText(address);
            if (addressMangerBeans.get(position).getOriginal().equals("1")){
                viewHolder.checkBox.setChecked(true);
                viewHolder.ismoren.setText("默认地址");
            }else {
                viewHolder.checkBox.setChecked(false);
                viewHolder.ismoren.setText("设为默认");
            }
            viewHolder.mDeleteLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onAddressListioner != null){
                        onAddressListioner.onDelete(addressMangerBeans.get(position).getId());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
