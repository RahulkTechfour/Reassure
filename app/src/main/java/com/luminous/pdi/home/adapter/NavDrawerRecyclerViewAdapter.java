package com.luminous.pdi.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.luminous.pdi.R;
import com.luminous.pdi.core.OnRecyclerViewItemClickListener;
import com.luminous.pdi.databinding.NavDrawerRowItemBinding;
import com.luminous.pdi.home.dto.NavigationMenuDatum;

import java.util.List;

public class NavDrawerRecyclerViewAdapter  extends RecyclerView.Adapter<NavDrawerRecyclerViewAdapter.ItemViewHolder>{

    private Context mContext;
    private List<String> mDrawerDataList;
    private List<NavigationMenuDatum> mDrawerDataList_static;
    private List<Integer> mDrawerDataList_ImageStatic;

    private OnRecyclerViewItemClickListener itemClickListener;

    public NavDrawerRecyclerViewAdapter(Context homePageActivity, List<String> drawerDataList, List<Integer> mDrawerDataList_static_, OnRecyclerViewItemClickListener itemClickListener) {
        mContext = homePageActivity;
        mDrawerDataList = drawerDataList;
        mDrawerDataList_ImageStatic = mDrawerDataList_static_;
        this.itemClickListener = itemClickListener;
    }

   /* public NavDrawerRecyclerViewAdapter(Context context, List<NavigationMenuDatum> visitList, OnRecyclerViewItemClickListener itemClickListener) {
        mContext = context;
        mDrawerDataList = visitList;
        this.itemClickListener = itemClickListener;

    }*/

    @NonNull
    @Override
    public NavDrawerRecyclerViewAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        NavDrawerRowItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.nav_drawer_row_item, parent, false);
        return new NavDrawerRecyclerViewAdapter.ItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NavDrawerRecyclerViewAdapter.ItemViewHolder holder, int position) {

        try {
       /*     NavigationMenuDatum cardEntity = mDrawerDataList.get(position);
            cardEntity.setModuleName("DasBoard");
            cardEntity.setModuleName("DasBoard");
            cardEntity.setModuleName("DasBoard");
            cardEntity.setModuleName("DasBoard");
            cardEntity.setModuleName("DasBoard");*/

            if(mDrawerDataList!=null) {

/*
                if(cardEntity.getModuleName()!=null && !cardEntity.getModuleName().equalsIgnoreCase("")){
                    holder.bindings.navText.setText(cardEntity.getModuleName());


                }else {
                    mDrawerDataList.add("DasBoard");
                    mDrawerDataList.add("My Profile");
                    mDrawerDataList.add("Pending");
                    mDrawerDataList.add("Completed");
                    mDrawerDataList.add("Change Password");
                    mDrawerDataList.add("Logout");




                    for (int i=0;i<mDrawerDataList.size();i++){
                        String name=mDrawerDataList.get(i);
                        holder.bindings.navText.setText(name);

                    }



                }*/
                /*try {
                    if (cardEntity.getModuleImage() != null && !cardEntity.getModuleImage().equalsIgnoreCase(""))
                        Glide.with(mContext).load(cardEntity.getModuleImage()).into(holder.bindings.imageView);
                }catch(Exception ex){
                    ex.getMessage();
                }*/


                holder.bindings.navText.setText(mDrawerDataList.get(position));
                Glide.with(mContext).load(mDrawerDataList_ImageStatic.get(position)).into(holder.bindings.imageView);

                holder.bindings.navItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            itemClickListener.onItemCLicked(v, holder.getAdapterPosition(), mDrawerDataList.get(position));
                        }catch(Exception ex){
                            ex.getMessage();
                        }
                    }
                });
            }else {


            }



        }catch (Exception ex){
            ex.getMessage();
        }
    }
   /* @Override
    public int getItemCount() {
        return mDrawerDataList == null ? 0 : mDrawerDataList.size();
    }
*/
    @Override
    public int getItemCount() {
        return mDrawerDataList == null ? 0 : mDrawerDataList.size();
    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        static NavDrawerRowItemBinding bindings;
        public ItemViewHolder(NavDrawerRowItemBinding binding) {
            super(binding.getRoot());
            bindings = binding;
        }
    }
}