package com.example.common.QCModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ResponseDictionaryDT {
    @SerializedName("JoshIDs")
    @Expose
    private List<ResponseDictionnaryItemsBaseModel> joshIDs = null;
    @SerializedName("QualityGroups")
    @Expose
    private List<ResponseDictionnaryItemsBaseModel> qualityGroups = null;
    @SerializedName("ProductGroups")
    @Expose
    private List<ResponseDictionnaryItemsBaseModel> productGroups = null;
    @SerializedName("Products")
    @Expose
    private List<ResponseDictionnaryItemsBaseModel> products = null;
    @SerializedName("SubTypes")
    @Expose
    private List<SubType> subTypes = null;

    public List<ResponseDictionnaryItemsBaseModel> getJoshIDs() {
        return joshIDs != null ? joshIDs : new ArrayList<ResponseDictionnaryItemsBaseModel>();
    }

    public void setJoshIDs(List<ResponseDictionnaryItemsBaseModel> joshIDs) {
        this.joshIDs = joshIDs;
    }

    public List<ResponseDictionnaryItemsBaseModel> getQualityGroups() {
        return qualityGroups != null ? qualityGroups : new ArrayList<ResponseDictionnaryItemsBaseModel>();
    }

    public void setQualityGroups(List<ResponseDictionnaryItemsBaseModel> qualityGroups) {
        this.qualityGroups = qualityGroups;
    }

    public List<ResponseDictionnaryItemsBaseModel> getProductGroups() {
        return productGroups != null ? productGroups : new ArrayList<ResponseDictionnaryItemsBaseModel>();
    }

    public void setProductGroups(List<ResponseDictionnaryItemsBaseModel> productGroups) {
        this.productGroups = productGroups;
    }

    public List<ResponseDictionnaryItemsBaseModel> getProducts() {
        return products != null ? products : new ArrayList<ResponseDictionnaryItemsBaseModel>();
    }

    public void setProducts(List<ResponseDictionnaryItemsBaseModel> products) {
        this.products = products;
    }

    public List<SubType> getSubTypes() {
        return subTypes != null ? subTypes : new ArrayList<SubType>();
    }

    public void setSubTypes(List<SubType> subTypes) {
        this.subTypes = subTypes;
    }

}