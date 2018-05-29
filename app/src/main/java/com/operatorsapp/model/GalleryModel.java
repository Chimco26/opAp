package com.operatorsapp.model;

public class GalleryModel {

    private String url;

    private boolean selected;

    public GalleryModel(String url, boolean selected) {
        this.url = url;
        this.selected = selected;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}

