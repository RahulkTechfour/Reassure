package com.luminous.pdi.completed.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.luminous.pdi.R;
import com.luminous.pdi.completed.dto.CompletedDetail;
import com.luminous.pdi.completed.dto.GetVisitDatum;
import com.luminous.pdi.completed.interfaces.OnRecyclerViewCompletedItemClickListener;
import com.luminous.pdi.core.OnRecyclerViewItemClickListener;
import com.luminous.pdi.databinding.CompletedVisitListItemBinding;
import com.luminous.pdi.home.clicklistener.OnRecyclerViewHomeItemClickListener;

import java.util.List;

public class CompletedVisitAdapter  extends RecyclerView.Adapter<CompletedVisitAdapter.MyItemViewHolder>{

    private Context mContext;
    private List<CompletedDetail>  mVisitList;
    private OnRecyclerViewCompletedItemClickListener itemClickListener;

    public CompletedVisitAdapter(Context context, List<CompletedDetail> visitList, OnRecyclerViewCompletedItemClickListener itemClickListener) {
        mContext = context;
        mVisitList = visitList;
        this.itemClickListener = itemClickListener;

    }

    @NonNull
    @Override
    public MyItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        CompletedVisitListItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.completed_visit_list_item, parent, false);
        return new MyItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyItemViewHolder holder, int position) {

        try {
            CompletedDetail cardEntity = mVisitList.get(position);
            if(cardEntity!=null) {
                int rem = position%2;
                if(rem==0){
                   // Drawable d = mContext.getResources().getDrawable(R.drawable.botton_bg_greenmix_round_corner);
                    MyItemViewHolder.bindings.linCompGredient.setBackgroundResource(R.drawable.botton_bg_greenmix_round_corner);
                }else{
                    MyItemViewHolder.bindings.linCompGredient.setBackgroundResource(R.drawable.button_bg_orange_rounded_corners);
                }

                if (!TextUtils.isEmpty(cardEntity.getVisitdate())) {
                    String Date=cardEntity.getVisitdate();
                    String DateSplitOne[]=Date.split(" ");
                    MyItemViewHolder.bindings.txtCompDate.setText(""+DateSplitOne[0]);

                }

                if (!TextUtils.isEmpty(cardEntity.getDistributorname())) {
                    MyItemViewHolder.bindings.txtCompName.setText(cardEntity.getDistributorname());
                }

                if (!TextUtils.isEmpty(cardEntity.getRequestno())) {
                    MyItemViewHolder.bindings.txtRequestNoData.setText(cardEntity.getRequestno());
                }

                if (!TextUtils.isEmpty(cardEntity.getDivisionname())) {
                    MyItemViewHolder.bindings.txtCompDiv.setText(cardEntity.getDivisionname());
                } else {

                }

                if (!TextUtils.isEmpty(cardEntity.getStatus())) {
                    MyItemViewHolder.bindings.txtCompStatus.setText(cardEntity.getStatus());
                }

                MyItemViewHolder.bindings.linCompGredient.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            itemClickListener.onItemCLicked(v, holder.getAdapterPosition(), cardEntity);
                        }catch(Exception ex){
                            ex.getMessage();
                        }
                    }
                });
            }



        }catch (Exception ex){
            ex.getMessage();
        }
    }

    @Override
    public int getItemCount() {
        return mVisitList == null ? 0 : mVisitList.size();
    }

    public static class MyItemViewHolder extends RecyclerView.ViewHolder {

        static CompletedVisitListItemBinding bindings;
        public MyItemViewHolder(CompletedVisitListItemBinding binding) {
            super(binding.getRoot());
            bindings = binding;
        }
    }
}
