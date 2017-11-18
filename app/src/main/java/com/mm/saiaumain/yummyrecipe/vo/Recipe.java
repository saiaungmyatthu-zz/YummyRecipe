package com.mm.saiaumain.yummyrecipe.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sai Aung Myat Thu on 10/25/2017.
 */

public class Recipe {
    private int id;
    private String name;
    private String totalTime;
    private String preparationTime;
    private String cookingTime;
    private String servingCount;
    private String referenceLink;
    private List<String> steps;
    private List<String> ingredients;
    private ImageInfo imageInfo;
    
    public Recipe(){
    	steps = new ArrayList<String>();
    	ingredients = new ArrayList<String>();
    }
    
    public String getPreparationTime() {
        return preparationTime;
    }

    public void setPreparationTime(String preparationTime) {
        this.preparationTime = preparationTime;
    }

    public String getCookingTime() {
        return cookingTime;
    }

    public void setCookingTime(String cookingTime) {
        this.cookingTime = cookingTime;
    }

    public String getServingCount() {
        return servingCount;
    }

    public void setServingCount(String servingCount) {
        this.servingCount = servingCount;
    }

    public String getReferenceLink() {
        return referenceLink;
    }

    public void setReferenceLink(String referenceLink) {
        this.referenceLink = referenceLink;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public List<String> getSteps() {
        return steps;
    }

    public void setSteps(List<String> steps) {
        this.steps = steps;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public ImageInfo getImageInfo() {
        return imageInfo;
    }

    public void setImageInfo(ImageInfo imageInfo) {
        this.imageInfo = imageInfo;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Recipe{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", totalTime='").append(totalTime).append('\'');
        sb.append(", preparationTime='").append(preparationTime).append('\'');
        sb.append(", cookingTime='").append(cookingTime).append('\'');
        sb.append(", servingCount='").append(servingCount).append('\'');
        sb.append(", referenceLink='").append(referenceLink).append('\'');
        sb.append(", steps=").append(steps);
        sb.append(", ingredients=").append(ingredients);
        sb.append(", imageInfo=").append(imageInfo);
        sb.append('}');
        return sb.toString();
    }
}
