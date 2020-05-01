package com.luminous.pdi.calendar.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.luminous.pdi.R;
import com.luminous.pdi.calendar.CalenderActivity.AttendenceFragment;
import com.luminous.pdi.calendar.CalenderInterface;

import com.luminous.pdi.databinding.CalenderEventItemViewBinding;
import com.luminous.pdi.home.dto.PendingvisitDetail;

import java.util.List;

import io.reactivex.Observer;

/**
 * Created by Dhaval Soneji on 4/4/16.
 */
public class CalendarDataAdapter extends RecyclerView.Adapter<CalendarDataAdapter.MyViewHolderAddedItems> {

    private List<com.luminous.pdi.calendar.responce.CalenderData.PendingvisitDetail> tableitemaddedlist;
    static PendingvisitDetail cardEntity= new PendingvisitDetail();
    private CalenderInterface itemClickListener;

    public CalendarDataAdapter(List<com.luminous.pdi.calendar.responce.CalenderData.PendingvisitDetail> tableitemaddedlist, AttendenceFragment itemClickListener) {
        this.tableitemaddedlist = tableitemaddedlist;
        this.itemClickListener = itemClickListener;

    }

    @NonNull
    @Override
    public CalendarDataAdapter.MyViewHolderAddedItems onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        CalenderEventItemViewBinding recycleviewaddeditemBinding= DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.calender_event_item_view,parent,false);


        CalendarDataAdapter.MyViewHolderAddedItems myViewHolderAddedItems=new CalendarDataAdapter.MyViewHolderAddedItems(recycleviewaddeditemBinding);
        return myViewHolderAddedItems;

    }

    @Override
    public void onBindViewHolder(@NonNull CalendarDataAdapter.MyViewHolderAddedItems holder, int position) {


        com.luminous.pdi.calendar.responce.CalenderData.PendingvisitDetail tableitemadapteradded = tableitemaddedlist.get(position);
        holder.recycleviewaddeditemBinding.txtName.setText(tableitemadapteradded.getDistributorname());
        holder.recycleviewaddeditemBinding.txtTitle.setText(tableitemadapteradded.getDivisionname());
        holder.recycleviewaddeditemBinding.txtSubject.setText(tableitemadapteradded.getStatus());
        holder.recycleviewaddeditemBinding.txtRequestNO.setText(tableitemadapteradded.getRequestno());

        holder.recycleviewaddeditemBinding.llCalander.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    cardEntity.setAddress(tableitemadapteradded.getAddress());
                    cardEntity.setContactNo(tableitemadapteradded.getContactNo());
                    cardEntity.setDistributorid(Integer.valueOf(tableitemadapteradded.getDistributorid()));
                    cardEntity.setDivisionid(tableitemadapteradded.getDivisionid());
                    cardEntity.setQuantity(tableitemadapteradded.getQuantity());
                    cardEntity.setDivisionname(tableitemadapteradded.getDivisionname());
                    cardEntity.setDistributorname(tableitemadapteradded.getDistributorname());
                    cardEntity.setRequesttype(tableitemadapteradded.getRequesttype());


                    cardEntity.setVisitdate(tableitemadapteradded.getVisitdate());

                    cardEntity.setRequestno(tableitemadapteradded.getRequestno());
                    cardEntity.setStatus(tableitemadapteradded.getStatus());
                    cardEntity.setId(tableitemadapteradded.getId());

                    itemClickListener.onItemCLicked(v, holder.getAdapterPosition(), cardEntity);


                }catch(Exception ex){
                    ex.getStackTrace();
                }
            }
        });

        holder.recycleviewaddeditemBinding.llCall.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {

                try {

                    itemClickListener.onItemCLicked(v, holder.getAdapterPosition(), cardEntity.getContactNo());
                    //   isTelephonyEnabled();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return tableitemaddedlist.size();

    }

    public class MyViewHolderAddedItems extends RecyclerView.ViewHolder {



        CalenderEventItemViewBinding recycleviewaddeditemBinding;


        public MyViewHolderAddedItems(@NonNull CalenderEventItemViewBinding itemView) {
            super(itemView.getRoot());

            recycleviewaddeditemBinding=itemView;





        }
    }
}
