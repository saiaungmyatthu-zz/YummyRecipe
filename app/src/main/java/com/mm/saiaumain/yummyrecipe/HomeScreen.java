package com.mm.saiaumain.yummyrecipe;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mm.saiaumain.yummyrecipe.adapters.RecipeAdapter;
import com.mm.saiaumain.yummyrecipe.utils.UIComponentUtils;
import com.mm.saiaumain.yummyrecipe.utils.YummyRecipeUtils;
import com.mm.saiaumain.yummyrecipe.vo.Recipe;
import com.mm.saiaumain.yummyrecipe.vo.RecipeItem;

import java.util.List;

/**
 * Created by Sai Aung Myat Thu on 10/25/2017.
 */

public class HomeScreen extends Fragment {

    private View view;
    private LinearLayout main;
    private List<RecipeItem> recipeItemList;

    private static final String TAG = "Yummy-HomeScreen";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (view == null)
            view = inflater.inflate(R.layout.home_screen, container, false);

        main = (LinearLayout) view.findViewById(R.id.mainLayout);
        main.removeAllViews();
        recipeItemList = YummyRecipeUtils.getAssetsRecipeList(getContext());
        Log.e(TAG, "Total Recipe Item List Count >>>>> " + recipeItemList.size());

        if (null != recipeItemList && !recipeItemList.isEmpty()) {
            for (RecipeItem item : recipeItemList) {
                int type = item.getDisplayType();
                switch (type) {
                    case 1:
                        View singleView = UIComponentUtils.getSingleView(getContext(), getFragmentManager(),
                                inflater, container, item);
                        main.addView(singleView);
                        break;
                    case 2:
                        View scrollView = UIComponentUtils.getScrollView(getContext(), getFragmentManager(),
                                inflater, container, item);
                        main.addView(scrollView);
                        break;
                    case 3:
                        View galleryView = UIComponentUtils.getGalleryView(getContext(), getFragmentManager(),
                                inflater, container, item);
                        main.addView(galleryView);
                        break;
                }
            }
        }
        return view;
    }
}
