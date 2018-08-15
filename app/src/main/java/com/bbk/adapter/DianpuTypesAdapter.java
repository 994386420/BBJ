package com.bbk.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bbk.Bean.DianPuTypesBean;
import com.bbk.activity.R;
import com.bbk.resource.NewConstants;
import com.bbk.view.AdaptionSizeTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rtj on 2018/3/7.
 */

public class DianpuTypesAdapter extends RecyclerView.Adapter {
    //    private List<Map<String,String>> list;
    private Context context;
    List<DianPuTypesBean> dianPuTypesBeans;
    private int Currrntposition;
    private TypeInterface typeInterface;

    public DianpuTypesAdapter(Context context, List<DianPuTypesBean> dianPuTypesBeans) {
//        this.list = list;
        this.context = context;
        this.dianPuTypesBeans = dianPuTypesBeans;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder ViewHolder = new ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.dianputypes_item_layout, parent, false));
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
        return dianPuTypesBeans.size();
    }

    // 标识选择的Item
    public void setSeclection(int position) {
        Currrntposition = position;
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_type)
        AdaptionSizeTextView tvType;
        @BindView(R.id.item_types)
        LinearLayout itemTypes;

        public ViewHolder(View mView) {
            super(mView);
            ButterKnife.bind(this, mView);
        }
    }

    private void initTop(final ViewHolder viewHolder, final int position) {
        try {
            final DianPuTypesBean canshuBean = dianPuTypesBeans.get(position);
            viewHolder.tvType.setText(canshuBean.getName() + "（" + canshuBean.getCount() + "）");
            if (NewConstants.postion == position){
                viewHolder.tvType.setTextColor(context.getResources().getColor(R.color.color_line_top));
            }else {
                viewHolder.tvType.setTextColor(context.getResources().getColor(R.color.tuiguang_color3));
            }
            viewHolder.itemTypes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    typeInterface.doChoose(canshuBean.getName(),position);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTypeInterface(TypeInterface modifyCountInterface) {
        this.typeInterface = modifyCountInterface;
    }

    public interface TypeInterface {
        void doChoose(String keyword,int position);
    }
}
