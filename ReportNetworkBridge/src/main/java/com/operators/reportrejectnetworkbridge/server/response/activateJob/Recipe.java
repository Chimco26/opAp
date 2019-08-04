package com.operators.reportrejectnetworkbridge.server.response.activateJob;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.operators.reportrejectnetworkbridge.server.response.Recipe.ProductData;
import com.example.common.StandardResponse;

import java.util.List;

public class Recipe extends StandardResponse implements Parcelable {

    @SerializedName("ProductData")
    @Expose
    private ProductData productData;
    @SerializedName("RecipeData")
    @Expose
    private List<com.operators.reportrejectnetworkbridge.server.response.Recipe.Recipe> recipeData = null;

    public ProductData getProductData() {
        return productData;
    }

    public void setProductData(ProductData productData) {
        this.productData = productData;
    }

    public List<com.operators.reportrejectnetworkbridge.server.response.Recipe.Recipe> getRecipeData() {
        return recipeData;
    }

    public void setRecipeData(List<com.operators.reportrejectnetworkbridge.server.response.Recipe.Recipe> recipeData) {
        this.recipeData = recipeData;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.productData, flags);
        dest.writeTypedList(this.recipeData);
    }

    public Recipe() {
    }

    protected Recipe(Parcel in) {
        this.productData = in.readParcelable(ProductData.class.getClassLoader());
        this.recipeData = in.createTypedArrayList(com.operators.reportrejectnetworkbridge.server.response.Recipe.Recipe.CREATOR);
    }

    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel source) {
            return new Recipe(source);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
}
