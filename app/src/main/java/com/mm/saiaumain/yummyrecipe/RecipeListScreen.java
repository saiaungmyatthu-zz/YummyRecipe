package com.mm.saiaumain.yummyrecipe;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mm.saiaumain.yummyrecipe.adapters.RecipeAdapter;
import com.mm.saiaumain.yummyrecipe.vo.Recipe;
import com.mm.saiaumain.yummyrecipe.vo.RecipeItem;

import java.util.List;

/**
 * Created by Sai Aung Myat Thu on 10/29/2017.
 */

public class RecipeListScreen extends Fragment {

    private final static String TAG = "Yummy-RecipeListScreen";

    private View view;
    private RecyclerView recyclerView;
    private RecipeAdapter recipeAdapter;
    private RecipeItem item;
    private TextView error;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extra = getArguments();
        if(extra != null)
            item = new Gson().fromJson(extra.getString("item"), RecipeItem.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if(view == null)
            view = inflater.inflate(R.layout.recipe_list, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recipeList);
        error = (TextView) view.findViewById(R.id.error);

        if(item != null){
            List<Recipe> recipeList = item.getRecipeList();
            if(null != recipeList && !recipeList.isEmpty()){
                error.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                Log.e(TAG, "RecipeListCount >>>> " + item.getRecipeList().size());
                recipeAdapter = new RecipeAdapter(getContext(), recipeList, R.layout.recipe_card);
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(recipeAdapter);
            }else{
                displayErrorMessage(item.getLabel());
            }
            String label = item.getLabel().replace("@@", "");
            getActivity().setTitle(label.toUpperCase());
        }else{
            displayErrorMessage(null);
        }
        return view;
    }

    private void displayErrorMessage(String category){
        recyclerView.setVisibility(View.GONE);
        error.setVisibility(View.VISIBLE);
        category = (null != category)? category : "display";
        String message = getString(R.string.recipe_list_error).replace("{category}", category);
        error.setText(message);
    }
}
