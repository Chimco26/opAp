package com.operators.reportrejectnetworkbridge.server.response.activateJob;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Job {

    @SerializedName("Actions")
    @Expose
    private List<Action> actions = null;
    @SerializedName("ID")
    @Expose
    private Integer iD;
    @SerializedName("Materials")
    @Expose
    private List<Material> materials = null;
    @SerializedName("Mold")
    @Expose
    private Mold mold;
    @SerializedName("ProductFiles")
    @Expose
    private List<String> productFiles = null;
    @SerializedName("Recipe")
    @Expose
    private Recipe recipe;

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    public Integer getID() {
        return iD;
    }

    public void setID(Integer iD) {
        this.iD = iD;
    }

    public List<Material> getMaterials() {
        return materials;
    }

    public void setMaterials(List<Material> materials) {
        this.materials = materials;
    }

    public Mold getMold() {
        return mold;
    }

    public void setMold(Mold mold) {
        this.mold = mold;
    }

    public List<String> getProductFiles() {
        return productFiles;
    }

    public void setProductFiles(List<String> productFiles) {
        this.productFiles = productFiles;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
}
