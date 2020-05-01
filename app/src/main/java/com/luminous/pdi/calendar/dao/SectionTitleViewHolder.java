package com.luminous.pdi.calendar.dao;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.luminous.pdi.R;


/**
 * Created by Dhaval Soneji on 4/4/16.
 */
public class SectionTitleViewHolder extends RecyclerView.ViewHolder {
    private TextView txtSection;

    public TextView getTxtSection() {
        return txtSection;
    }

    public void setTxtSection(TextView txtSection) {
        this.txtSection = txtSection;
    }

    public SectionTitleViewHolder(View v) {
        super(v);
        txtSection = (TextView) v.findViewById(R.id.txtSection);
        txtSection.setVisibility(View.GONE);
       // txtSection.setVisibility(View.GONE);
    }
}
