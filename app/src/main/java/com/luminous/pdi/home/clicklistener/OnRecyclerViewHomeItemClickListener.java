package com.luminous.pdi.home.clicklistener;

import android.view.View;

import com.luminous.pdi.home.dto.PendingvisitDetail;
import com.luminous.pdi.home.fragments.CreateListDTO.ResponseDatum;

public interface OnRecyclerViewHomeItemClickListener {

    void onItemCLicked(View v, int adapterPosition, ResponseDatum cardEntity);
}
