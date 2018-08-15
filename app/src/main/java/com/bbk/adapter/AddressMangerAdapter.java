package com.bbk.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bbk.Bean.AddressMangerBean;
import com.bbk.Bean.OnAddressListioner;
import com.bbk.activity.AddressActivity;
import com.bbk.activity.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rtj on 2018/3/7.
 */

public class AddressMangerAdapter extends RecyclerView.Adapter {
    private Context context;
    List<AddressMangerBean> addressMangerBeans;
    private OnAddressListioner onAddressListioner;

    public AddressMangerAdapter(Context context, List<AddressMangerBean> addressMangerBeans) {
        this.context = context;
        this.addressMangerBeans = addressMangerBeans;
    }

    public void setOnDeleteListioner(OnAddressListioner onDeleteListioner) {
        this.onAddressListioner = onDeleteListioner;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder ViewHolder = new ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.address_manger_item, parent, false));
        return ViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        try {
            ViewHolder viewHolder = (ViewHolder) holder;
            initTop(viewHolder, position);
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


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView receiver, phone, tag, address, ismoren;
        ImageView checkBox;
        LinearLayout mDeleteLayout;
        @BindView(R.id.ll_address)
        LinearLayout llAddress;
        @BindView(R.id.ll_moren)
        LinearLayout llMoren;
        @BindView(R.id.ll_bianji)
        LinearLayout llBianji;

        public ViewHolder(View mView) {
            super(mView);
            ButterKnife.bind(this, mView);
            receiver = mView.findViewById(R.id.receiver);
            phone = mView.findViewById(R.id.phone);
            tag = mView.findViewById(R.id.tag);
            address = mView.findViewById(R.id.address);
            ismoren = mView.findViewById(R.id.ismoren);
            checkBox = mView.findViewById(R.id.checkbox_set_moren);
            mDeleteLayout = mView.findViewById(R.id.delete_address_layout);
        }
    }

    private void initTop(final ViewHolder viewHolder, final int position) {
        try {
            final AddressMangerBean addressMangerBean = addressMangerBeans.get(position);
            String receiver = addressMangerBeans.get(position).getReceiver();
            String phone = addressMangerBeans.get(position).getPhone();
            String tag = addressMangerBeans.get(position).getTag();
            String address = addressMangerBeans.get(position).getArea() + addressMangerBeans.get(position).getStreet();
            viewHolder.receiver.setText(receiver);
            viewHolder.phone.setText(phone);
            viewHolder.tag.setText(tag);
            viewHolder.address.setText(address);
            if (addressMangerBeans.get(position).getOriginal().equals("1")) {
                viewHolder.checkBox.setBackgroundResource(R.drawable.dizhi_check);
                viewHolder.ismoren.setText("默认地址");
            } else {
                viewHolder.checkBox.setBackgroundResource(R.drawable.weixuanzhongyuan);
                viewHolder.ismoren.setText("设为默认");
            }
            viewHolder.mDeleteLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onAddressListioner != null) {
                        onAddressListioner.onDelete(addressMangerBeans.get(position).getId());
                    }
                }
            });
            viewHolder.llAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    onAddressListioner.onItemCick(addressMangerBean.getId(), addressMangerBean.getPhone(), addressMangerBean.getReceiver(), addressMangerBean.getTag(), addressMangerBean.getStreet(), addressMangerBean.getArea());
                }
            });
            viewHolder.llMoren.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onAddressListioner.onDefaultAdressCheck(addressMangerBean.getId());
                }
            });
            viewHolder.llBianji.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AddressActivity.class);
                    intent.putExtra("addrid",addressMangerBean.getId());
                    intent.putExtra("original",addressMangerBean.getOriginal());
                    context.startActivity(intent);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
