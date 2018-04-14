package com.pbus.utility;

/**
 * Created by mindiii on 14/4/18.
 */


import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import com.pbus.R;


public class Custom_Progress_Bus extends Dialog {

    static Custom_Progress_Bus instance;
    static int count = 0;
    Context context;


    public Custom_Progress_Bus(Context context, int Layout) {
        super(context, R.style.DialogThemes);
        this.context = context;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(Layout);
      /*  Glide.with(context)
                .asGif()
                .load(R.drawable.preloader)
                .into((ImageView) LayoutInflater.from(context).inflate(R.layout.custom_progress_dialog_layout ,null).findViewById(R.id.image));*/
        setColor();
        instance = this;

        //Util.setStatusBarColor((Activity) context,R.color.colorPrimary);
    }

    public static Custom_Progress_Bus getInstance(Context context) {
        if (instance == null) new Custom_Progress_Bus(context, R.layout.custom_prog_2);
        count++;
        return instance;

    }

    public void setColor() {
        this.getWindow().setBackgroundDrawableResource(R.color.black50);
    }

    public void setColor(int colorcode) {
        this.getWindow().setBackgroundDrawableResource(colorcode);


    }

    public void dismisser() {
        count--;
        if (count == 0) {
            this.dismiss();
            instance = null;
        }

    }

}