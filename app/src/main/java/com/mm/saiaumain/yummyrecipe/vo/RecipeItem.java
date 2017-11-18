package com.mm.saiaumain.yummyrecipe.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sai Aung Myat Thu on 10/25/2017.
 */

public class RecipeItem {

    private String label;
    private ImageInfo background;
    private List<Recipe> recipeList;
    private int displayType;
    
    public RecipeItem(){
    	recipeList = new ArrayList<Recipe>();
    }
    
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public ImageInfo getBackground() {
        return background;
    }

    public void setBackground(ImageInfo background) {
        this.background = background;
    }

    public List<Recipe> getRecipeList() {
        return recipeList;
    }

    public void setRecipeList(List<Recipe> recipeList) {
        this.recipeList = recipeList;
    }

    public int getDisplayType() {
        return displayType;
    }

    public void setDisplayType(int displayType) {
        this.displayType = displayType;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("RecipeItem{");
        sb.append("label='").append(label).append('\'');
        sb.append(", background=").append(background);
        sb.append(", recipeList=").append(recipeList);
        sb.append(", displayType=").append(displayType);
        sb.append('}');
        return sb.toString();
    }
}
