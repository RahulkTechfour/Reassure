package com.luminous.pdi.search.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.luminous.pdi.R;
import com.luminous.pdi.databinding.SearchVisitListItemBinding;
import com.luminous.pdi.search.dto.SearchRecordByEngineerVisit;
import com.luminous.pdi.search.dto.SearchRecordWithstatus;

import java.util.List;

public class SearchVisitAdapter  extends RecyclerView.Adapter<SearchVisitAdapter.SearchItemViewHolder>{

    private Context mContext;
    private List<SearchRecordByEngineerVisit> engVisitList;


    public SearchVisitAdapter(Context context, List<SearchRecordByEngineerVisit> engVisitList) {
       
        this.mContext = context;
        this.engVisitList = engVisitList;
    }

    @NonNull
    @Override
    public SearchItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        SearchVisitListItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.search_visit_list_item, parent, false);
        return new SearchItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchItemViewHolder holder, int position) {

        try {
            SearchRecordByEngineerVisit cardEntity = engVisitList.get(position);
            if(cardEntity!=null) {
                int rem = position%2;
                if(rem==0){
                    // Drawable d = mContext.getResources().getDrawable(R.drawable.botton_bg_greenmix_round_corner);
                    holder.bindings.linSearchGredient.setBackgroundResource(R.drawable.botton_bg_greenmix_round_corner);
                }else{
                    holder.bindings.linSearchGredient.setBackgroundResource(R.drawable.button_bg_orange_rounded_corners);
                }

                if (!TextUtils.isEmpty(cardEntity.getMaterialName())) {
                    holder.bindings.txtSearchMaterialName.setText(cardEntity.getMaterialName());
                    holder.bindings.txtSearchMaterialName.setVisibility(View.VISIBLE);

                }else {
                    holder.bindings.txtSearchMaterialName.setVisibility(View.GONE);

                }

                if (!TextUtils.isEmpty(cardEntity.getMFG_Date())) {
                    holder.bindings.txtMFGValues.setText(cardEntity.getMFG_Date());
                    holder.bindings.txtMfgDate.setVisibility(View.VISIBLE);
                    holder.bindings.txtMFGValues.setVisibility(View.VISIBLE);


                }else {
                    holder.bindings.txtMFGValues.setVisibility(View.GONE);
                    holder.bindings.txtMfgDate.setVisibility(View.GONE);


                }

                if (!TextUtils.isEmpty(cardEntity.getFaultQnty())) {
                    holder.bindings.txtFaultQuanValue.setText(cardEntity.getFaultQnty());
                    holder.bindings.txtFaultQuan.setVisibility(View.VISIBLE);
                    holder.bindings.txtFaultQuanValue.setVisibility(View.VISIBLE);


                }else {
                    holder.bindings.txtFaultQuanValue.setVisibility(View.GONE);
                    holder.bindings.txtFaultQuan.setVisibility(View.GONE);


                }

                if (!TextUtils.isEmpty(cardEntity.getBatchNo())) {
                    holder.bindings.txtBatchValue.setText(cardEntity.getBatchNo());
                    holder.bindings.txtBatch.setVisibility(View.VISIBLE);
                    holder.bindings.txtBatchValue.setVisibility(View.VISIBLE);


                }else {
                    holder.bindings.txtBatchValue.setVisibility(View.GONE);
                    holder.bindings.txtBatch.setVisibility(View.GONE);


                }

                if (!TextUtils.isEmpty(cardEntity.getQnty())) {
                    holder.bindings.txtSearchQtyValue.setText(cardEntity.getQnty());
                    holder.bindings.txtQuantity.setVisibility(View.VISIBLE);
                    holder.bindings.txtSearchQtyValue.setVisibility(View.VISIBLE);


                }else {

                    holder.bindings.txtQuantity.setVisibility(View.GONE);
                    holder.bindings.txtSearchQtyValue.setVisibility(View.GONE);


                }


                Log.e("getInvoiceId","getInvoiceID"+cardEntity.getInvoiceID());

               // holder.bindings.txtInvoiceFanValue.setText(cardEntity.getInvoiceID());

                if (cardEntity.getInvoiceID()!=null && !TextUtils.isEmpty(cardEntity.getInvoiceID())) {
                    holder.bindings.txtInvoiceValue.setText(cardEntity.getInvoiceID());
                    holder.bindings.txtInvoiceID.setVisibility(View.VISIBLE);
                    holder.bindings.txtInvoiceValue.setVisibility(View.VISIBLE);


                }else {
                    holder.bindings.txtInvoiceID.setVisibility(View.GONE);
                    holder.bindings.txtInvoiceValue.setVisibility(View.GONE);


                }




                if (!TextUtils.isEmpty(cardEntity.getSerialNo())) {
                    holder.bindings.txtSearchSerialNo.setText(cardEntity.getSerialNo());
                    holder.bindings.txtSearchSerialNo.setVisibility(View.VISIBLE);

                }else {
                    holder.bindings.txtSearchSerialNo.setVisibility(View.GONE);

                }

                if (!TextUtils.isEmpty(cardEntity.getFaultName())) {
                    holder.bindings.txtSearchFaultName.setText(cardEntity.getFaultName());
                    holder.bindings.txtSearchFaultName.setVisibility(View.VISIBLE);
                } else {
                    holder.bindings.txtSearchFaultName.setVisibility(View.GONE);
                    holder.bindings.txtSearchFault.setVisibility(View.GONE);


                }

                if (!TextUtils.isEmpty(cardEntity.getInspectionDate())) {
                    holder.bindings.txtSearchInspectionValue.setText(cardEntity.getInspectionDate());
                    holder.bindings.txtSearchInspectionValue.setVisibility(View.VISIBLE);
                    holder.bindings.txtSearchInspection.setVisibility(View.VISIBLE);
                }else {
                    holder.bindings.txtSearchInspectionValue.setVisibility(View.GONE);
                    holder.bindings.txtSearchInspection.setVisibility(View.GONE);
                }

                //
                if (!TextUtils.isEmpty(cardEntity.getWarrantyStatus())) {
                    holder.bindings.txtSearchWarrantyValue.setText(cardEntity.getWarrantyStatus());
                    holder.bindings.txtSearchWarrantyValue.setVisibility(View.VISIBLE);
                    holder.bindings.txtSearchWarranty.setVisibility(View.VISIBLE);
                } else {
                    holder.bindings.txtSearchWarrantyValue.setVisibility(View.GONE);
                    holder.bindings.txtSearchWarranty.setVisibility(View.GONE);

                }

                if (cardEntity.getProductStatus()!=null && !TextUtils.isEmpty(cardEntity.getProductStatus())) {
                    holder.bindings.txtSearchProdStatusValue.setText(cardEntity.getProductStatus());
                    holder.bindings.txtSearchProdStatusValue.setVisibility(View.VISIBLE);
                    holder.bindings.txtSearchProdStatus.setVisibility(View.VISIBLE);


                }else {
                    holder.bindings.txtSearchProdStatusValue.setVisibility(View.GONE);
                    holder.bindings.txtSearchProdStatus.setVisibility(View.GONE);

                }

                if (!TextUtils.isEmpty(cardEntity.getDateofPurchase())) {
                    holder.bindings.txtSearchDOPValue.setText(cardEntity.getDateofPurchase());
                    holder.bindings.txtSearchDOPValue.setVisibility(View.VISIBLE);
                    holder.bindings.txtSearchDOP.setVisibility(View.VISIBLE);
                } else {
                    holder.bindings.txtSearchDOPValue.setVisibility(View.GONE);
                    holder.bindings.txtSearchDOP.setVisibility(View.GONE);


                }

                Log.e("getDivisionName","getDivision"+cardEntity.getDivisionName());
                if (cardEntity.getDivisionName().equalsIgnoreCase("FAN")){


                    if (!TextUtils.isEmpty(cardEntity.getClaimStatus())) {
                        holder.bindings.txtSearchClaimStatusValue.setText(cardEntity.getClaimStatus());
                        holder.bindings.txtSearchClaimStatusValue.setVisibility(View.VISIBLE);
                        holder.bindings.txtSearchClaimStatus.setVisibility(View.VISIBLE);
                    }else {
                        holder.bindings.txtSearchClaimStatusValue.setVisibility(View.GONE);
                        holder.bindings.txtSearchClaimStatus.setVisibility(View.GONE);

                    }
                }else {
                    if (!TextUtils.isEmpty(cardEntity.getClaimStatus())) {
                        holder.bindings.txtSearchClaimStatusValue.setText(cardEntity.getClaimStatus());
                        holder.bindings.txtSearchClaimStatusValue.setVisibility(View.GONE);
                        holder.bindings.txtSearchClaimStatus.setVisibility(View.GONE);
                    }else {
                        holder.bindings.txtSearchClaimStatusValue.setVisibility(View.GONE);
                        holder.bindings.txtSearchClaimStatus.setVisibility(View.GONE);

                    }
                }



                if (!TextUtils.isEmpty(cardEntity.getFaultAccept())) {
                    holder.bindings.txtFaultAcceptValue.setText(cardEntity.getFaultAccept());
                    holder.bindings.txtFaultAcceptValue.setVisibility(View.VISIBLE);
                    holder.bindings.txtFaultAccept.setVisibility(View.VISIBLE);
                } else {
                    holder.bindings.txtFaultAcceptValue.setVisibility(View.GONE);
                    holder.bindings.txtFaultAccept.setVisibility(View.GONE);


                }

                if (!TextUtils.isEmpty(cardEntity.getFaultReject())) {
                    holder.bindings.txtFaultRejectValue.setText(cardEntity.getFaultReject());
                    holder.bindings.txtFaultReject.setVisibility(View.VISIBLE);
                    holder.bindings.txtFaultRejectValue.setVisibility(View.VISIBLE);
                } else {
                    holder.bindings.txtFaultReject.setVisibility(View.GONE);
                    holder.bindings.txtFaultRejectValue.setVisibility(View.GONE);


                }
             
            }



        }catch (Exception ex){
            ex.getMessage();
        }
    }

    @Override
    public int getItemCount() {
        return engVisitList.size();
    }

    public static class SearchItemViewHolder extends RecyclerView.ViewHolder {

        static SearchVisitListItemBinding bindings;
        public SearchItemViewHolder(SearchVisitListItemBinding binding) {
            super(binding.getRoot());
            bindings = binding;
        }
    }
}
