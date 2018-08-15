package com.bbk.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bbk.Bean.ButtonListBean;
import com.bbk.Bean.PlBean;
import com.bbk.activity.R;
import com.bbk.activity.ShopDetailActivty;
import com.bbk.shopcar.adapter.ShopcatAdapter;
import com.bbk.util.EventIdIntentUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.logg.Logg;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rtj on 2018/3/7.
 */

public class MyButtonListAdapter extends RecyclerView.Adapter {
    private List<ButtonListBean> buttonListBeans;
    private Context context;
    private userInterface userInterface;

    public MyButtonListAdapter(Context context, List<ButtonListBean> plBeans) {
        this.buttonListBeans = plBeans;
        this.context = context;
    }

    public MyButtonListAdapter.userInterface getUserInterface() {
        return userInterface;
    }

    public void setUserInterface(MyButtonListAdapter.userInterface userInterface) {
        this.userInterface = userInterface;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder ViewHolder = new ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.button_item, parent, false));
        return ViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        initTop(viewHolder, position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (buttonListBeans != null && buttonListBeans.size() > 0){
            return buttonListBeans.size();
        }else {
            return 0;
        }
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.button_img)
        ImageView buttonImg;
        @BindView(R.id.button_name)
        TextView buttonName;
        @BindView(R.id.ll_button)
        LinearLayout llButton;

        public ViewHolder(View mView) {
            super(mView);
            ButterKnife.bind(this, mView);
        }
    }

    private void initTop(final ViewHolder viewHolder, final int position) {
        try {
            final ButtonListBean buttonListBean = buttonListBeans.get(position);
            viewHolder.buttonName.setText(buttonListBean.getName());
            Glide.with(context)
                    .load(buttonListBean.getImg())
                    .priority(Priority.HIGH)
                    .placeholder(R.mipmap.zw_img_300)
                    .into(viewHolder.buttonImg);
            viewHolder.llButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userInterface.Intent(buttonListBean.getName());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface userInterface {
        void Intent(String name);
    }
}
