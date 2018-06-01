package com.bbk.component;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bbk.activity.R;
import com.blog.www.guideview.Component;

/**
 * Created by binIoter on 16/6/17.
 */
public class HomeBijiaComponent implements Component {

  @Override
  public View getView(LayoutInflater inflater) {

    LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.layer_home_bijia, null);
    return ll;
  }

  @Override
  public int getAnchor() {
    return Component.ANCHOR_TOP ;
  }

  @Override
  public int getFitPosition() {
    return Component.FIT_END;
  }

  @Override
  public int getXOffset() {
    return 0;
  }

  @Override
  public int getYOffset() {
    return 0;
  }
}
