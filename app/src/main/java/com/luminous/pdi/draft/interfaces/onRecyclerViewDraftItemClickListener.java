package com.luminous.pdi.draft.interfaces;

import android.view.View;

import com.luminous.pdi.draft.dto.DraftVisitDto;

public interface onRecyclerViewDraftItemClickListener {

    void onItemDraftCardCLicked(View view, int position, DraftVisitDto draftDto);
}
