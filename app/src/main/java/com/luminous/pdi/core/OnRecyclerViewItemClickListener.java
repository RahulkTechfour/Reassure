package com.luminous.pdi.core;

import android.view.View;

public interface OnRecyclerViewItemClickListener {

    void onItemCLicked(View view, int position);

    void onItemCLicked(View view, int position, String source);


}
