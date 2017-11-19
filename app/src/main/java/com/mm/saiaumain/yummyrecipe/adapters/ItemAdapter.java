package com.mm.saiaumain.yummyrecipe.adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageButton;

import com.mm.saiaumain.yummyrecipe.R;
import com.mm.saiaumain.yummyrecipe.utils.UIComponentUtils;

import java.util.List;

/**
 * Created by Sai Aung Myat Thu on 11/19/2017.
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemHolder> {

    private Context context;
    private List<String> itemList;
    private int resourceId;
    private OnItemClickAction action;

    public ItemAdapter(Context context, List<String> itemList, int resourceId, OnItemClickAction action){
        this.context = context;
        this.itemList = itemList;
        this.resourceId = resourceId;
        this.action = action;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View recipeView = LayoutInflater.from(context).inflate(resourceId, parent, false);
        return new ItemAdapter.ItemHolder(recipeView);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, final int position) {
        final String itemText = itemList.get(position);
        holder.item.setText(itemText);
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                action.onItemClickAction(itemText, position);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIComponentUtils.playSound(context, R.raw.item_delete);
                removeItem(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void removeItem(int position){
        itemList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, itemList.size());
    }

    public class ItemHolder extends RecyclerView.ViewHolder{
        public CheckedTextView item;
        public ImageButton delete;

        public ItemHolder(View itemView) {
            super(itemView);
            item = (CheckedTextView) itemView.findViewById(R.id.item);
            delete = (ImageButton) itemView.findViewById(R.id.itemDelete);
        }
    }

    public interface OnItemClickAction{
        void onItemClickAction(String item, int position);
    }
}
