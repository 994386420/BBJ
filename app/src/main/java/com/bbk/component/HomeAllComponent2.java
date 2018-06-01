package com.bbk.component;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.bbk.activity.R;
import com.blog.www.guideview.Component;

/**
 * Created by binIoter on 16/6/17.
 */
public class HomeAllComponent2 implements Component {

  @Override
  public View getView(LayoutInflater inflater) {

    LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.layer_home_all, null);
    return ll;
  }

  @Override
  public int getAnchor() {
    return Component.ANCHOR_BOTTOM;
  }

  @Override
  public int getFitPosition() {
    return Component.FIT_END;
  }

  @Override
  public int getXOffset() {
    return -120;
  }

  @Override
  public int getYOffset() {
    return 230;
  }
}
