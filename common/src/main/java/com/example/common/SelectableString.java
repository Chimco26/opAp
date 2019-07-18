package com.example.common;

import java.util.ArrayList;

public class SelectableString {
    public static final String SELECT_ALL_ID = "SELECT_ALL_ID";
    private int color;
    private String id;
    String string;
    boolean isSelected;

    public SelectableString(String string, boolean isSelected, String id, int color) {
        this.string = string;
        this.isSelected = isSelected;
        this.id = id;
        this.color = color;
    }

    public SelectableString(String string, boolean isSelected, String id) {
        this.string = string;
        this.isSelected = isSelected;
        this.id = id;
    }

    public SelectableString(String string, boolean isSelected) {
        this.string = string;
        this.isSelected = isSelected;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public static ArrayList<SelectableString> updateList(ArrayList<SelectableString> oldList, ArrayList<SelectableString> newList){

        for (SelectableString newListItem: newList){
            for (SelectableString oldListItem: oldList){
                if (newListItem.getId().equals(oldListItem.getId())){
                    newListItem.setSelected(oldListItem.isSelected);
                }
            }
        }

        return newList;
    }
}
