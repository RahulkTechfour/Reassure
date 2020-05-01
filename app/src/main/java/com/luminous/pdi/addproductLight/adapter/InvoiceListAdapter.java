package com.luminous.pdi.addproductLight.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.luminous.pdi.R;
import com.luminous.pdi.addproductLight.LightRes.InvoiceQntyEntity;

import java.util.List;

public class InvoiceListAdapter extends BaseAdapter {

    Context mContext_;
    List<InvoiceQntyEntity> invoiceQntySoldList_;
    private static LayoutInflater inflater=null;
    public InvoiceListAdapter(Context mContext, List<InvoiceQntyEntity> invoiceQntySoldList) {

        this.mContext_=mContext;
        this.invoiceQntySoldList_=invoiceQntySoldList;
        inflater = (LayoutInflater)mContext_.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return invoiceQntySoldList_.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position,View convertView, ViewGroup viewGroup) {

        View vi=convertView;

        if(convertView==null)
            vi = inflater.inflate(R.layout.recycleerview_light_invoice_sold_quantity, null);

        TextView invoiceNumber = vi.findViewById(R.id.txtLightSoldInvoiceNo); // title
        TextView availableQuantity = vi.findViewById(R.id.txtLightSoldAvailableQnty); // artist name
        EditText soldQuntity = vi.findViewById(R.id.etLightSoldPdiQnty); // duration

        invoiceNumber.setText(invoiceQntySoldList_.get(position).getBillingDocumentNumner());
        availableQuantity.setText(invoiceQntySoldList_.get(position).getAvailableQuantity());

        invoiceNumber.setTag(invoiceQntySoldList_.get(position).getBillingDocumentNumner());
        availableQuantity.setTag(invoiceQntySoldList_.get(position).getAvailableQuantity());
        return vi;
    }
}
