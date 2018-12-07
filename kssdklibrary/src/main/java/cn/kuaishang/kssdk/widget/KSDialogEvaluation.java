package cn.kuaishang.kssdk.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.kuaishang.kssdk.KSConfig;
import cn.kuaishang.kssdk.R;
import cn.kuaishang.util.StringUtil;

/**
 * Created by Admin on 2017/3/22.
 */

public class KSDialogEvaluation extends Dialog implements View.OnClickListener{

    private Context context;
    private ImageView evaStart1;
    private ImageView evaStart2;
    private ImageView evaStart3;
    private ImageView evaStart4;
    private ImageView evaStart5;
    private ImageView evaFace;
    private TextView evaValue;
    private EditText evaContent;

    public KSDialogEvaluation(Context context) {
        super(context, R.style.KSDialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View contentView = LayoutInflater.from(context).inflate(R.layout.ks_dialog_evaluation, null);
        setContentView(contentView);

        evaStart1 = (ImageView) findViewById(R.id.evaStart1);
        evaStart2 = (ImageView) findViewById(R.id.evaStart2);
        evaStart3 = (ImageView) findViewById(R.id.evaStart3);
        evaStart4 = (ImageView) findViewById(R.id.evaStart4);
        evaStart5 = (ImageView) findViewById(R.id.evaStart5);
        evaFace = (ImageView) findViewById(R.id.evaFace);
        evaValue = (TextView) findViewById(R.id.evaValue);
        evaContent = (EditText) findViewById(R.id.evaContent);

        evaStart1.setOnClickListener(this);
        evaStart2.setOnClickListener(this);
        evaStart3.setOnClickListener(this);
        evaStart4.setOnClickListener(this);
        evaStart5.setOnClickListener(this);
        findViewById(R.id.evaClose).setOnClickListener(this);
        findViewById(R.id.evaConfirm).setOnClickListener(this);

        doSelect(R.id.evaStart5);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.evaClose){
            dismiss();
        }else if(id == R.id.evaStart1
                || id == R.id.evaStart2
                || id == R.id.evaStart3
                || id == R.id.evaStart4
                || id == R.id.evaStart5){
            doSelect(id);
        }else if(id == R.id.evaConfirm){
            dismiss();
            KSConfig.getController(context).sendServiceEvaluate(StringUtil.getString(evaValue.getText()), StringUtil.getString(evaContent.getText()));
        }
    }

    private void doSelect(int id){
        int start = 0;
        if(id == R.id.evaStart1){
            start = 1;
            evaFace.setImageResource(R.drawable.eva_model1_1);
            evaValue.setText(R.string.ks_eva_start1);
        }else if(id == R.id.evaStart2){
            start = 2;
            evaFace.setImageResource(R.drawable.eva_model1_2);
            evaValue.setText(R.string.ks_eva_start2);
        }else if(id == R.id.evaStart3){
            start = 3;
            evaFace.setImageResource(R.drawable.eva_model1_3);
            evaValue.setText(R.string.ks_eva_start3);
        }else if(id == R.id.evaStart4){
            start = 4;
            evaFace.setImageResource(R.drawable.eva_model1_4);
            evaValue.setText(R.string.ks_eva_start4);
        }else if(id == R.id.evaStart5){
            start = 5;
            evaFace.setImageResource(R.drawable.eva_model1_5);
            evaValue.setText(R.string.ks_eva_start5);
        }
        List images = new ArrayList();
        images.add(evaStart1);
        images.add(evaStart2);
        images.add(evaStart3);
        images.add(evaStart4);
        images.add(evaStart5);
        for(int i=1;i<=images.size();i++){
            ImageView imageView = (ImageView) images.get(i-1);
            if(start>=i){
                imageView.setImageResource(R.drawable.start_selected);
            }else{
                imageView.setImageResource(R.drawable.start_normal);
            }
        }
    }
}
