package com.luminous.pdi.home.clicklistener;

import android.view.View;

import com.luminous.pdi.home.dto.PendingvisitDetail;

public interface OnRecyclerOnItemClick {

    void onItemCLicked(View v, int adapterPosition, String call);
}
