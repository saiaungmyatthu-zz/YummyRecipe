package com.mm.saiaumain.yummyrecipe.vo;

/**
 * Created by Sai Aung Myat Thu on 10/25/2017.
 */

public class ImageInfo {

    private String url;
    private int resourceId;

    public ImageInfo(){super();}

    public ImageInfo(String url, int resourceId){
        super();
        this.url = url;
        this.resourceId = resourceId;
    }

    public ImageInfo(String url){
        super();
        this.url = url;
    }

    public ImageInfo(int resourceId){
        super();
        this.resourceId = resourceId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ImageInfo{");
        sb.append("url='").append(url).append('\'');
        sb.append(", resourceId=").append(resourceId);
        sb.append('}');
        return sb.toString();
    }
}
