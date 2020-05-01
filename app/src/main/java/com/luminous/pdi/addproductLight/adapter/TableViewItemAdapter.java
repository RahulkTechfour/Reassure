package com.luminous.pdi.addproductLight.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.luminous.pdi.R;
import com.luminous.pdi.addproductLight.dto.Items_quanitySHow;
import com.luminous.pdi.databinding.RecycleerviewLightShowQuantityBinding;
;

import java.util.List;

public class TableViewItemAdapter extends RecyclerView.Adapter<TableViewItemAdapter.MyViewHolder> {

    private List<Items_quanitySHow> tableList;

    public TableViewItemAdapter(List<Items_quanitySHow> tableList) {

        this.tableList = tableList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecycleerviewLightShowQuantityBinding recycletableViewitemBinding= DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.recycleerview_light_show_quantity,parent,false);

        MyViewHolder myViewHolder=new MyViewHolder(recycletableViewitemBinding);
        return myViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Items_quanitySHow tableItems=tableList.get(position);
        holder.recycletableViewitemBinding.setTableItems(tableItems);

    }

    @Override
    public int getItemCount() {

        return tableList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{



        RecycleerviewLightShowQuantityBinding recycletableViewitemBinding;


        public MyViewHolder(@NonNull RecycleerviewLightShowQuantityBinding itemView) {
            super(itemView.getRoot());

            recycletableViewitemBinding=itemView;

        }
    }

}
