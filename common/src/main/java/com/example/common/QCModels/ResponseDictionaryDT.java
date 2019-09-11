package com.example.common.QCModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseDictionaryDT {
    @SerializedName("JoshIDs")
    @Expose
    private List<JoshID> joshIDs = null;
    @SerializedName("QualityGroups")
    @Expose
    private List<QualityGroup> qualityGroups = null;
    @SerializedName("ProductGroups")
    @Expose
    private List<ProductGroup> productGroups = null;
    @SerializedName("Products")
    @Expose
    private List<Product> products = null;
    @SerializedName("SubTypes")
    @Expose
    private List<SubType> subTypes = null;

    public List<JoshID> getJoshIDs() {
        return joshIDs;
    }

    public void setJoshIDs(List<JoshID> joshIDs) {
        this.joshIDs = joshIDs;
    }

    public List<QualityGroup> getQualityGroups() {
        return qualityGroups;
    }

    public void setQualityGroups(List<QualityGroup> qualityGroups) {
        this.qualityGroups = qualityGroups;
    }

    public List<ProductGroup> getProductGroups() {
        return productGroups;
    }

    public void setProductGroups(List<ProductGroup> productGroups) {
        this.productGroups = productGroups;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<SubType> getSubTypes() {
        return subTypes;
    }

    public void setSubTypes(List<SubType> subTypes) {
        this.subTypes = subTypes;
    }

}