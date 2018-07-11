package com.bbk.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bbk.activity.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class TmpFragment extends Fragment {

    @BindView(R.id.pic4)
    ImageView pic4;
    Unbinder unbinder;
    private List<String> urlList = new ArrayList<>();
    //    private TextView title;
    private List<ImageView> imageList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tmp, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        view.setScaleY(0.7f);
        view.setScaleX(0.7f);
        Bundle bundle = getArguments();
        String title = bundle.getString("title");
        for (int i = 0; i < 5; i++) {
            urlList.add(bundle.getString("pic" + i));
        }
        imageList.add(((ImageView) view.findViewById(R.id.pic4)));
        for (int i = 0; i < imageList.size(); i++) {
            Glide.with(getActivity())
                    .load(urlList.get(i))
                    .priority(Priority.HIGH)
                    .placeholder(R.mipmap.zw_img_332)
                    .into(imageList.get(i));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
