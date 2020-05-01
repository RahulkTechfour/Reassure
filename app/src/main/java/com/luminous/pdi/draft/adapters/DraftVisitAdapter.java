package com.luminous.pdi.draft.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.luminous.pdi.R;
import com.luminous.pdi.addproductLight.dto.InvoiceQntyEntity;
import com.luminous.pdi.addproductLight.fragment.AddProductLightDetailFragment;
import com.luminous.pdi.core.OnRecyclerViewItemClickListener;
import com.luminous.pdi.databinding.DraftVisitListItemBinding;
import com.luminous.pdi.draft.dto.DraftVisitDto;
import com.luminous.pdi.draft.interfaces.onRecyclerViewDraftItemClickListener;
import com.luminous.pdi.home.adapter.UpComingVisitAdapter;

import java.util.List;

public class DraftVisitAdapter   extends RecyclerView.Adapter<DraftVisitAdapter.MyItemViewHolder>{

    private Context mContext;
    private List<DraftVisitDto> mVisitList;
    private OnRecyclerViewItemClickListener itemClickListener;
    private onRecyclerViewDraftItemClickListener itemCardClickListener;

    public DraftVisitAdapter(Context context, List<DraftVisitDto> visitList, OnRecyclerViewItemClickListener itemClickListener, onRecyclerViewDraftItemClickListener itemCardClickListener) {

        mContext = context;
        mVisitList = visitList;
        this.itemClickListener = itemClickListener;
        this.itemCardClickListener = itemCardClickListener;
    }



    @NonNull
    @Override
    public MyItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        DraftVisitListItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.draft_visit_list_item, parent, false);
        return new MyItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyItemViewHolder holder, int position) {

        try {
            DraftVisitDto cardEntity = mVisitList.get(position);
            if(cardEntity!=null) {
/*
                int rem = position%2;
                if(rem==0){
                    // Drawable d = mContext.getResources().getDrawable(R.drawable.botton_bg_greenmix_round_corner);
                    DraftVisitAdapter.MyItemViewHolder.bindings.linCompGredient.setBackgroundResource(R.drawable.botton_bg_greenmix_round_corner);
                }else{
                    DraftVisitAdapter.MyItemViewHolder.bindings.linCompGredient.setBackgroundResource(R.drawable.button_bg_orange_rounded_corners);
                }
*/

                if (!TextUtils.isEmpty(cardEntity.getVisitdate())) {
                    String Date=cardEntity.getVisitdate();
                    String DateSplitOne[]=Date.split(" ");
                    MyItemViewHolder.bindings.txtDraftDate.setText(""+DateSplitOne[0]);
                }

                if (!TextUtils.isEmpty(cardEntity.getDistributorname())) {
                    MyItemViewHolder.bindings.txtDraftName.setText(cardEntity.getDistributorname());
                }

                if (!TextUtils.isEmpty(cardEntity.getRequestno())) {
                    MyItemViewHolder.bindings.txtDrafRequestNo.setText(cardEntity.getRequestno());
                }

                if (!TextUtils.isEmpty(cardEntity.getDivisionname())) {
                    MyItemViewHolder.bindings.txtDraftDiv.setText(cardEntity.getDivisionname());
                } else {

                }

                if (!TextUtils.isEmpty(cardEntity.getStatus())) {
                    MyItemViewHolder.bindings.txtDraftStatus.setText(cardEntity.getStatus());
                }
            }

            MyItemViewHolder.bindings.ivDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemCLicked(v,position, String.valueOf(cardEntity.getDistributorid()));
                }
            });

            MyItemViewHolder.bindings.linDraftGredient.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   itemCardClickListener.onItemDraftCardCLicked(v,position, cardEntity);
                }
            });

        }catch (Exception ex){
            ex.getMessage();
        }
    }

    @Override
    public int getItemCount() {
        return mVisitList == null ? 0 : mVisitList.size();
    }

    public static class MyItemViewHolder extends RecyclerView.ViewHolder {

        static DraftVisitListItemBinding bindings;
        public MyItemViewHolder(DraftVisitListItemBinding binding) {
            super(binding.getRoot());
            bindings = binding;
        }
    }
}

