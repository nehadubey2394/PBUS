package com.pbus.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pbus.R;
import com.pbus.listener.AdapterListener;

import java.util.ArrayList;

/**
 * Created by mindiii on 12/4/18.
 */

public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.ViewHolder>{

    private ArrayList<String> drawerItemList;
    private Context context;
    private AdapterListener listener;

    public DrawerAdapter(Context context, ArrayList<String> drawerItemList){
        this.context=context;
        this.drawerItemList=drawerItemList;
    }

    public void setDrawerListener(AdapterListener listener){
        this.listener=listener;
    }

    @NonNull
    @Override
    public DrawerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_drawer_adapter,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DrawerAdapter.ViewHolder holder, int position) {
        holder.tvDrawerItem.setText(drawerItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return drawerItemList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvDrawerItem;
        ViewHolder(View itemView) {
            super(itemView);
            tvDrawerItem=itemView.findViewById(R.id.tvDrawerItem);

            tvDrawerItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (listener!=null&&getAdapterPosition()!=-1){
                listener.drawerItemSelected(getAdapterPosition());
            }
        }
    }
}
