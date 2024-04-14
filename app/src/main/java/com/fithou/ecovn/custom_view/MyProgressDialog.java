package com.fithou.ecovn.custom_view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fithou.ecovn.R;

public class MyProgressDialog {
    private Dialog dialog;

    public MyProgressDialog(Context context) {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.progess_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public void show() {
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }

    public void setTitle(String s){
        TextView title = dialog.findViewById(R.id.dialog_title);
        title.setText(s);
    }


}
