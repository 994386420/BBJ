package com.bbk.shopcar.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.StrikethroughSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.dialog.AlertDialog;
import com.bbk.resource.NewConstants;
import com.bbk.shopcar.entity.GoodsInfo;
import com.bbk.shopcar.entity.StoreInfo;
import com.bbk.util.SharedPreferencesUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.logg.Logg;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

//import android.support.v7.app.AlertDialog;

/**
 * 购物车适配器
 */

public class ShopcatAdapter extends BaseExpandableListAdapter {
    private List<StoreInfo> groups;
    //这个String对应着StoreInfo的Id，也就是店铺的Id
    private Map<String, List<GoodsInfo>> childrens;
    private Context mcontext;
    private CheckInterface checkInterface;
    private ModifyCountInterface modifyCountInterface;
    private GroupEditorListener groupEditorListener;
    private int count = 0;
    private boolean flag = true; //组的编辑按钮是否可见，true可见，false不可见
    private Context context;


    public ShopcatAdapter(Context context, List<StoreInfo> groups, Map<String, List<GoodsInfo>> childrens, Context mcontext) {
        this.context = context;
        this.groups = groups;
        this.childrens = childrens;
        this.mcontext = mcontext;
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        String groupId = groups.get(groupPosition).getId();
        return childrens.get(groupId).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        List<GoodsInfo> childs = childrens.get(groups.get(groupPosition).getId());
        return childs.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final GroupViewHolder groupViewHolder;
        if (convertView == null) {
            convertView = View.inflate(mcontext, R.layout.item_shopcat_group, null);
            groupViewHolder = new GroupViewHolder(convertView);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        final StoreInfo group = (StoreInfo) getGroup(groupPosition);
        if (group.getDianpuyouhui() != null && !group.getDianpuyouhui().equals("")) {
            groupViewHolder.youhuiName.setVisibility(View.VISIBLE);
            groupViewHolder.youhuiName.setText(group.getDianpuyouhui());
        }else {
            groupViewHolder.youhuiName.setVisibility(View.GONE);
        }
        groupViewHolder.storeName.setText(group.getName());
        groupViewHolder.storeCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewConstants.car = "2";
                group.setChoosed(((CheckBox) v).isChecked());
                checkInterface.checkGroup(groupPosition, ((CheckBox) v).isChecked());
            }
        });
        Logg.e("===", group.isChoosed());
        groupViewHolder.storeCheckBox.setChecked(group.isChoosed());
        groupViewHolder.rlDianou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInterface.IntentGroup(group.getId());
            }
        });

        /**【文字指的是组的按钮的文字】
         * 当我们按下ActionBar的 "编辑"按钮， 应该把所有组的文字显示"编辑",并且设置按钮为不可见
         * 当我们完成编辑后，再把组的编辑按钮设置为可见
         * 不懂，请自己操作淘宝，观察一遍
         */
        if (group.isActionBarEditor()) {
            group.setEditor(false);
            groupViewHolder.storeEdit.setVisibility(View.GONE);
            flag = false;
        } else {
            flag = true;
            groupViewHolder.storeEdit.setVisibility(View.GONE);
        }

        /**
         * 思路:当我们按下组的"编辑"按钮后，组处于编辑状态，文字显示"完成"
         * 当我们点击“完成”按钮后，文字显示"编辑“,组处于未编辑状态
         */
        if (group.isEditor()) {
            groupViewHolder.storeEdit.setText("完成");
        } else {
            groupViewHolder.storeEdit.setText("编辑");
        }

        groupViewHolder.storeEdit.setOnClickListener(new GroupViewClick(group, groupPosition, groupViewHolder.storeEdit));
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildViewHolder childViewHolder;
        if (convertView == null) {
            convertView = View.inflate(mcontext, R.layout.item_shopcat_product, null);
            childViewHolder = new ChildViewHolder(convertView);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }

        /**
         * 根据组的编辑按钮的可见与不可见，去判断是组对下辖的子元素编辑  还是ActionBar对组的下瞎元素的编辑
         * 如果组的编辑按钮可见，那么肯定是组对自己下辖元素的编辑
         * 如果组的编辑按钮不可见，那么肯定是ActionBar对组下辖元素的编辑
         */
        if (flag) {
            if (groups.get(groupPosition).isEditor()) {
                childViewHolder.editGoodsData.setVisibility(View.VISIBLE);
                childViewHolder.delGoods.setVisibility(View.VISIBLE);
                childViewHolder.goodsData.setVisibility(View.GONE);
            } else {
                childViewHolder.delGoods.setVisibility(View.VISIBLE);
                childViewHolder.goodsData.setVisibility(View.VISIBLE);
                childViewHolder.editGoodsData.setVisibility(View.GONE);
            }
        } else {

            if (groups.get(groupPosition).isActionBarEditor()) {
                childViewHolder.delGoods.setVisibility(View.GONE);
                childViewHolder.editGoodsData.setVisibility(View.VISIBLE);
                childViewHolder.goodsData.setVisibility(View.GONE);
            } else {
                childViewHolder.delGoods.setVisibility(View.VISIBLE);
                childViewHolder.goodsData.setVisibility(View.VISIBLE);
                childViewHolder.editGoodsData.setVisibility(View.GONE);
            }
        }

        final GoodsInfo child = (GoodsInfo) getChild(groupPosition, childPosition);
        childViewHolder.llCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyCountInterface.Intent(child.getId());
            }
        });

        count = child.getNum();
        childViewHolder.dialogNum.setText(count + "");
        NewConstants.car = "2";
        /**
         * 判断是否获取到焦点
         */
        childViewHolder.dialogNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    NewConstants.car = "1";
                    /**
                     * 添加数量变化监听
                     */
                    childViewHolder.dialogNum.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (s.length() > 0) {
                                int num = Integer.parseInt(childViewHolder.dialogNum.getText().toString());
                                if (!NewConstants.car.equals("2")) {
                                    modifyCountInterface.Refresh(groupPosition, childPosition, childViewHolder.goodsNum, childViewHolder.singleCheckBox.isChecked(), child.getId(), num, child.getGuige(), childViewHolder.dialogNum.getText().toString());
                                }
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                } else {
                    NewConstants.car = "2";
                }
            }
        });
        childViewHolder.dialogIncreaseNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logg.e(child.getId());
                modifyCountInterface.doIncrease(groupPosition, childPosition, childViewHolder.goodsNum, childViewHolder.singleCheckBox.isChecked(), child.getId(), child.getNum(), child.getGuige());
            }
        });
        childViewHolder.dialogReduceNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyCountInterface.doDecrease(groupPosition, childPosition, childViewHolder.goodsNum, childViewHolder.singleCheckBox.isChecked(), child.getId(), child.getNum(), child.getGuige());
            }
        });
        //删除购物车商品
        convertView.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new AlertDialog.Builder(mcontext)
//                        .setMessage("确定要删除该商品吗")
//                        .setNegativeButton("取消", null)
//                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                modifyCountInterface.childDelete(groupPosition, childPosition, child.getId(), child.getNum(), child.getGuige());
//                            }
//                        })
//                        .create()
//                        .show();
                new AlertDialog(mcontext).builder().setTitle("提示")
                        .setMsg("确认要删除该商品吗?")
                        .setPositiveButton("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                modifyCountInterface.childDelete(groupPosition, childPosition, child.getId(), child.getNum(), child.getGuige());
                            }
                        }).setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        return;
                    }
                }).show();

            }
        });
        if (child != null) {
            childViewHolder.goodsName.setText(child.getTitle());
            childViewHolder.goodsPrice.setText(child.getPrice() + "");
            childViewHolder.goodsNum.setText(String.valueOf(child.getNum()));
//            childViewHolder.goodsImage.setImageResource(R.mipmap.new_app_icon);
            Glide.with(context)
                    .load(child.getImgurl())
                    .priority(Priority.HIGH)
                    .placeholder(R.mipmap.zw_img_300)
                    .into(childViewHolder.goodsImage);
            childViewHolder.goods_size.setText(child.getGuige());
            //设置打折前的原价
            SpannableString spannableString = new SpannableString((child) + "");
            StrikethroughSpan span = new StrikethroughSpan();
            spannableString.setSpan(span, 0, spannableString.length() - 1 + 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            //避免无限次的append
            if (childViewHolder.goodsPrimePrice.length() > 0) {
                childViewHolder.goodsPrimePrice.setText("");
            }
            childViewHolder.goodsPrimePrice.setText(spannableString);
            childViewHolder.goodsBuyNum.setText("x" + child.getNum() + "");
//            Logg.e(child.isChoosed());
            childViewHolder.singleCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NewConstants.car = "2";
                    child.setChoosed(((CheckBox) v).isChecked());
                    childViewHolder.singleCheckBox.setChecked(((CheckBox) v).isChecked());
                    checkInterface.checkChild(groupPosition, childPosition, ((CheckBox) v).isChecked());
                }
            });
            childViewHolder.singleCheckBox.setChecked(child.isChoosed());
            childViewHolder.increaseGoodsNum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    modifyCountInterface.doIncrease(groupPosition, childPosition, childViewHolder.goodsNum, childViewHolder.singleCheckBox.isChecked(), child.getId(), child.getNum(), child.getGuige());
                }
            });
            childViewHolder.reduceGoodsNum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    modifyCountInterface.doDecrease(groupPosition, childPosition, childViewHolder.goodsNum, childViewHolder.singleCheckBox.isChecked(), child.getId(), child.getNum(), child.getGuige());
                }
            });
//            childViewHolder.goodsNum.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    showDialog(groupPosition, childPosition, childViewHolder.goodsNum, childViewHolder.singleCheckBox.isChecked(), child);
//                }
//            });
            childViewHolder.delGoods.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    new AlertDialog.Builder(mcontext)
//                            .setMessage("确定要删除该商品吗")
//                            .setNegativeButton("取消", null)
//                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
////                                    modifyCountInterface.childDelete(groupPosition,childPosition);
//                                }
//                            })
//                            .create()
//                            .show();
                    ;
                }
            });
        }
        return convertView;
    }

//    /**
//     * @param groupPosition
//     * @param childPosition
//     * @param showCountView
//     * @param isChecked
//     * @param child
//     */
//    private void showDialog(final int groupPosition, final int childPosition, final View showCountView, final boolean isChecked, final GoodsInfo child) {
//        final AlertDialog.Builder alertDialog_Builder = new AlertDialog.Builder(mcontext);
//        View view = LayoutInflater.from(mcontext).inflate(R.layout.dialog_change_num, null);
//        final AlertDialog dialog = alertDialog_Builder.create();
//        dialog.setView(view);//errored,这里是dialog，不是alertDialog_Buidler
//        count = child.getNum();
//        final EditText num = (EditText) view.findViewById(R.id.dialog_num);
//        num.setText(count + "");
//        //自动弹出键盘
//        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
//            @Override
//            public void onShow(DialogInterface dialog) {
//                UtilTool.showKeyboard(mcontext, showCountView);
//            }
//        });
//        final TextView increase = (TextView) view.findViewById(R.id.dialog_increaseNum);
//        final TextView DeIncrease = (TextView) view.findViewById(R.id.dialog_reduceNum);
//        final TextView pButton = (TextView) view.findViewById(R.id.dialog_Pbutton);
//        final TextView nButton = (TextView) view.findViewById(R.id.dialog_Nbutton);
//        nButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//        pButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int number = Integer.parseInt(num.getText().toString().trim());
//                if (number == 0) {
//                    dialog.dismiss();
//                } else {
//                    UtilsLog.i("数量=" + number + "");
//                    num.setText(String.valueOf(number));
//                    child.setNum(number);
//                    modifyCountInterface.doUpdate(groupPosition, childPosition, showCountView, isChecked);
//                    dialog.dismiss();
//                }
//            }
//        });
//        increase.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                count++;
//                num.setText(String.valueOf(count));
//            }
//        });
//        DeIncrease.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (count > 1) {
//                    count--;
//                    num.setText(String.valueOf(count));
//                }
//            }
//        });
//        dialog.show();
//    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }


    public GroupEditorListener getGroupEditorListener() {
        return groupEditorListener;
    }

    public void setGroupEditorListener(GroupEditorListener groupEditorListener) {
        this.groupEditorListener = groupEditorListener;
    }

    public CheckInterface getCheckInterface() {
        return checkInterface;
    }

    public void setCheckInterface(CheckInterface checkInterface) {
        this.checkInterface = checkInterface;
    }

    public ModifyCountInterface getModifyCountInterface() {
        return modifyCountInterface;
    }

    public void setModifyCountInterface(ModifyCountInterface modifyCountInterface) {
        this.modifyCountInterface = modifyCountInterface;
    }


    static class GroupViewHolder {
        @BindView(R.id.store_checkBox)
        CheckBox storeCheckBox;
        @BindView(R.id.store_name)
        TextView storeName;
        @BindView(R.id.store_edit)
        TextView storeEdit;
        @BindView(R.id.rl_dianou)
        RelativeLayout rlDianou;
        @BindView(R.id.youhui_name)
        TextView youhuiName;

        public GroupViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    /**
     * 店铺的复选框
     */
    public interface CheckInterface {
        /**
         * 组选框状态改变触发的事件
         *
         * @param groupPosition 组元素的位置
         * @param isChecked     组元素的选中与否
         */
        void checkGroup(int groupPosition, boolean isChecked);

        /**
         * 子选框状态改变触发的事件
         *
         * @param groupPosition 组元素的位置
         * @param childPosition 子元素的位置
         * @param isChecked     子元素的选中与否
         */
        void checkChild(int groupPosition, int childPosition, boolean isChecked);

        void IntentGroup(String dianpuid);
    }


    /**
     * 改变数量的接口
     */
    public interface ModifyCountInterface {
        /**
         * 增加操作
         *
         * @param groupPosition 组元素的位置
         * @param childPosition 子元素的位置
         * @param showCountView 用于展示变化后数量的View
         * @param isChecked     子元素选中与否
         */
        void doIncrease(int groupPosition, int childPosition, View showCountView, boolean isChecked, String id, int num, String guige);

        void doDecrease(int groupPosition, int childPosition, View showCountView, boolean isChecked, String id, int num, String guige);

        void doUpdate(int groupPosition, int childPosition, View showCountView, boolean isChecked);

        /**
         * 删除子Item
         *
         * @param groupPosition
         * @param childPosition
         */
        void childDelete(int groupPosition, int childPosition, String id, int num, String guige);

        void Intent(String id);

        void Refresh(int groupPosition, int childPosition, View showCountView, boolean isChecked, String id, int num, String guige, String dialogNum);
    }

    /**
     * 监听编辑状态
     */
    public interface GroupEditorListener {
        void groupEditor(int groupPosition);
    }

    /**
     * 使某个小组处于编辑状态
     */
    private class GroupViewClick implements View.OnClickListener {
        private StoreInfo group;
        private int groupPosition;
        private TextView editor;

        public GroupViewClick(StoreInfo group, int groupPosition, TextView editor) {
            this.group = group;
            this.groupPosition = groupPosition;
            this.editor = editor;
        }

        @Override
        public void onClick(View v) {
            if (editor.getId() == v.getId()) {
                groupEditorListener.groupEditor(groupPosition);
                if (group.isEditor()) {
                    group.setEditor(false);
                } else {
                    group.setEditor(true);
                }
                notifyDataSetChanged();
            }
        }
    }


    static class ChildViewHolder {
        @BindView(R.id.single_checkBox)
        CheckBox singleCheckBox;
        @BindView(R.id.goods_image)
        ImageView goodsImage;
        @BindView(R.id.goods_name)
        TextView goodsName;
        @BindView(R.id.goods_size)
        TextView goods_size;
        @BindView(R.id.goods_price)
        TextView goodsPrice;
        @BindView(R.id.goods_prime_price)
        TextView goodsPrimePrice;
        @BindView(R.id.goods_buyNum)
        TextView goodsBuyNum;
        @BindView(R.id.goods_data)
        RelativeLayout goodsData;
        @BindView(R.id.reduce_goodsNum)
        TextView reduceGoodsNum;
        @BindView(R.id.goods_Num)
        TextView goodsNum;
        @BindView(R.id.increase_goods_Num)
        TextView increaseGoodsNum;
        @BindView(R.id.goodsSize)
        TextView goodsSize;
        @BindView(R.id.del_goods)
        TextView delGoods;
        @BindView(R.id.edit_goods_data)
        LinearLayout editGoodsData;
        @BindView(R.id.dialog_reduceNum)
        TextView dialogReduceNum;
        @BindView(R.id.dialog_num)
        EditText dialogNum;
        @BindView(R.id.dialog_increaseNum)
        TextView dialogIncreaseNum;
        @BindView(R.id.ll_car)
        LinearLayout llCar;

        public ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    /**
     * 购物车操作
     * type
     * 3表示增加数量
     * 4表示减少数量
     * -1表示删除物品
     * 1表示添加物品
     */
    private void doShoppingCart(String productid, String type, String number, String guige) {
        Map<String, String> maps = new HashMap<String, String>();
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        maps.put("userid", userID);
        maps.put("productid", productid);
        maps.put("type", type);
        maps.put("number", number);
        maps.put("guige", guige);
        RetrofitClient.getInstance(context).createBaseApi().doShoppingCart(
                maps, new BaseObserver<String>(context) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.optString("status").equals("1")) {
                            } else {

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void hideDialog() {
                    }

                    @Override
                    protected void showDialog() {
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                    }
                });
    }

}
