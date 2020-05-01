package com.luminous.pdi.addproductLight.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.luminous.pdi.R;

import com.luminous.pdi.addproductLight.dto.InvoiceQntyEntity;
import com.luminous.pdi.core.OnRecyclerViewItemClickListener;
import com.luminous.pdi.databinding.RecycleerviewLightInvoiceSoldQuantityBinding;

import java.util.List;

public class LightInvoiceSoldAdapter  extends RecyclerView.Adapter<LightInvoiceSoldAdapter.MyItemViewHolder> {

    private List<InvoiceQntyEntity> invoiceQntySoldList;
    private Context mContext;
    private OnRecyclerViewItemClickListener itemClickListener;



    public LightInvoiceSoldAdapter(Context mContext, List<InvoiceQntyEntity> invoiceQntySoldList, OnRecyclerViewItemClickListener itemClickListener) {
    this.invoiceQntySoldList = invoiceQntySoldList;
    this.mContext = mContext;
    this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public MyItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecycleerviewLightInvoiceSoldQuantityBinding recycletableViewitemBinding= DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.recycleerview_light_invoice_sold_quantity,parent,false);

        MyItemViewHolder MyItemViewHolder=new MyItemViewHolder(recycletableViewitemBinding);
        return MyItemViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull MyItemViewHolder holder, int position) {

        try {
            InvoiceQntyEntity qntyEntity = invoiceQntySoldList.get(0);
            if(qntyEntity.getBillingDocumentNumner()!=null && !qntyEntity.getBillingDocumentNumner().equalsIgnoreCase(""))
            holder.recycletableViewitemBinding.txtLightSoldInvoiceNo.setText(""+qntyEntity.getBillingDocumentNumner());
            if(qntyEntity.getAvailableQuantity()!=null && !qntyEntity.getAvailableQuantity().equalsIgnoreCase(""))
            holder.recycletableViewitemBinding.txtLightSoldAvailableQnty.setText(""+qntyEntity.getAvailableQuantity());
          //  holder.recycletableViewitemBinding.etLightSoldPdiQnty.setText()
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {

        return invoiceQntySoldList.size();
    }

    public class MyItemViewHolder extends RecyclerView.ViewHolder{

        RecycleerviewLightInvoiceSoldQuantityBinding recycletableViewitemBinding;
        public MyItemViewHolder(@NonNull RecycleerviewLightInvoiceSoldQuantityBinding itemView) {
            super(itemView.getRoot());
            recycletableViewitemBinding=itemView;
        }
    }

}