package com.luminous.pdi.calendar;

import android.view.View;

import com.luminous.pdi.home.dto.PendingvisitDetail;

public interface CalenderInterface {

    void onItemCLicked(View v, int adapterPosition, PendingvisitDetail cardEntity);
    void onItemCLicked(View v, int adapterPosition, String call);



}
