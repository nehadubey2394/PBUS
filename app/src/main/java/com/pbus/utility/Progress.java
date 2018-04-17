package com.pbus.utility;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import com.pbus.R;


/*
 * Created by mindiii on 12/12/17.
 */


public class Progress extends Dialog {


    public Progress(Context context) {
        super(context, R.style.ProgressBarTheme);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.custom_progress_dialog_layout);
    }

}