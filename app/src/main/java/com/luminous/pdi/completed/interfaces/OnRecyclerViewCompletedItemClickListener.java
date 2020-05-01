package com.luminous.pdi.completed.interfaces;

import android.view.View;

import com.luminous.pdi.completed.dto.CompletedDetail;
import com.luminous.pdi.completed.dto.GetVisitDatum;

public interface OnRecyclerViewCompletedItemClickListener {

    void onItemCLicked(View v, int adapterPosition, CompletedDetail cardEntity);
}
