package com.mm.saiaumain.yummyrecipe.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mm.saiaumain.yummyrecipe.R;

import java.util.List;

/**
 * Created by Sai Aung Myat Thu on 11/12/2017.
 */

public class SwipableRouter extends RecyclerView.Adapter<SwipableRouter.ItemHolder> {

    private Context context;
    private List<String> itemList;
    private int resourceId;

    public SwipableRouter(Context context, List<String> itemList, int resourceId){
        this.context = context;
        this.itemList = itemList;
        this.resourceId = resourceId;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(resourceId, parent, false);
        return new ItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        String item = itemList.get(position);
        holder.item.setText(item);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void deleteItem(int index){
        itemList.remove(index);
        notifyItemRemoved(index);
    }

    public void addItem(int index, String item){
        itemList.add(index, item);
        notifyItemInserted(index);
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        private TextView item;
        public ItemHolder(View itemView) {
            super(itemView);
            item = (TextView) itemView.findViewById(R.id.item);
        }
    }
}
