package com.luminous.pdi.home.adapter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.luminous.pdi.R;
import com.luminous.pdi.completed.adapter.CompletedVisitAdapter;
import com.luminous.pdi.completed.fragments.CompletedFragment;
import com.luminous.pdi.core.OnRecyclerViewItemClickListener;
import com.luminous.pdi.databinding.UpcomingVisitListItemBinding;
import com.luminous.pdi.home.clicklistener.OnRecyclerOnItemClick;
import com.luminous.pdi.home.clicklistener.OnRecyclerViewHomeItemClickListener;
import com.luminous.pdi.home.dto.HomePageBaseResponse;
import com.luminous.pdi.home.dto.PendingvisitDetail;
import com.luminous.pdi.home.dto.UpComingVisitDto;
import com.luminous.pdi.home.fragments.CreateListDTO.ResponseDatum;

import java.util.List;

import io.reactivex.Observer;

import static android.content.Context.TELEPHONY_SERVICE;

public class UpComingVisitAdapter  extends RecyclerView.Adapter<UpComingVisitAdapter.MyItemViewHolder>{

    private Context mContext;
    private List<ResponseDatum> mVisitList;
    private OnRecyclerViewHomeItemClickListener itemClickListener;
    private OnRecyclerOnItemClick recyclerOnItemClick;

    public UpComingVisitAdapter(Context context, List<ResponseDatum> visitList, OnRecyclerViewHomeItemClickListener itemClickListener, OnRecyclerOnItemClick recyclerOnItemClick) {

        mContext = context;
        mVisitList = visitList;
        this.itemClickListener = itemClickListener;
        this.recyclerOnItemClick = recyclerOnItemClick;

    }

    @NonNull
    @Override
    public MyItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        UpcomingVisitListItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.upcoming_visit_list_item, parent, false);
        return new UpComingVisitAdapter.MyItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyItemViewHolder holder, int position) {
        try {
            ResponseDatum cardEntity = mVisitList.get(position);
            if(cardEntity!=null) {
                if (!TextUtils.isEmpty(cardEntity.getClNatureOfLoss())) {  /*should be date*/
                    String Date=cardEntity.getClNatureOfLoss();
                    String DateSplitOne[]=Date.split(" ");
                   // MyItemViewHolder.bindings.txtVisitDate.setText(""+DateSplitOne[0]);
                    MyItemViewHolder.bindings.txtVisitDate.setText(""+"10 March 2020 10:30 AM");

                }else {
                    MyItemViewHolder.bindings.txtVisitDate.setText(""+"10 March 2020 10:30 AM");

                }

                if (!TextUtils.isEmpty(cardEntity.getCuFirstName())) {
                    MyItemViewHolder.bindings.txtName.setText(""+cardEntity.getCuFirstName());
                }
                if (!TextUtils.isEmpty(cardEntity.getCuPhone())) {
                    MyItemViewHolder.bindings.txtRequestNO.setText(""+cardEntity.getCuPhone());
                }

                if (!TextUtils.isEmpty(cardEntity.getCuCity())) {
                  //  MyItemViewHolder.bindings.txtVisitRequestNO.setText(cardEntity.getCuCity());
                    MyItemViewHolder.bindings.txtVisitRequestNO.setText("2.5Km");
                } else {

                }

                if (!TextUtils.isEmpty(cardEntity.getCuPincode())) {
                    MyItemViewHolder.bindings.txtVisitStatus.setText(cardEntity.getCuPincode());
                }

                MyItemViewHolder.bindings.linUpcomingVisit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemClickListener.onItemCLicked(v, holder.getAdapterPosition(), cardEntity);
                    }
                });

                // call
                MyItemViewHolder.bindings.ivVisitStatusCall.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onClick(View v) {

                        try {

                            recyclerOnItemClick.onItemCLicked(v, holder.getAdapterPosition(), cardEntity.getCuPhone());
                         //   isTelephonyEnabled();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });


              //

            }



        }catch (Exception ex){
            ex.getMessage();
        }
    }

    @Override
    public int getItemCount() {
        return mVisitList == null ? 0 : mVisitList.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    public static class MyItemViewHolder extends RecyclerView.ViewHolder {

       static UpcomingVisitListItemBinding bindings;
        public MyItemViewHolder(UpcomingVisitListItemBinding binding) {
            super(binding.getRoot());
            bindings = binding;
        }
    }
}
