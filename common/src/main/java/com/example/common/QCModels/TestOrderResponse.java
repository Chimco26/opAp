package com.example.common.QCModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TestOrderResponse {
    @SerializedName("JoshID")
    @Expose
    private Integer joshID;
    @SerializedName("QualityGroupID")
    @Expose
    private Integer qualityGroupID;
    @SerializedName("ProductGroupID")
    @Expose
    private Integer productGroupID;
    @SerializedName("ProductID")
    @Expose
    private Integer productID;
    @SerializedName("SubType")
    @Expose
    private Integer subType;
    @SerializedName("error")
    @Expose
    private Object error;
    @SerializedName("Data")
    @Expose
    private List<Object> data = null;
    @SerializedName("ResponseList")
    @Expose
    private Object responseList;
    @SerializedName("Response")
    @Expose
    private Object response;
    @SerializedName("ResponseDictionary")
    @Expose
    private Object responseDictionary;
    @SerializedName("ResponseDictionaryDT")
    @Expose
    private ResponseDictionaryDT responseDictionaryDT;
    @SerializedName("ResponseExpandoObjectDictionary")
    @Expose
    private Object responseExpandoObjectDictionary;
    @SerializedName("ResponseDataTable")
    @Expose
    private Object responseDataTable;

    public Integer getJoshID() {
        return joshID;
    }

    public void setJoshID(Integer joshID) {
        this.joshID = joshID;
    }

    public Integer getQualityGroupID() {
        return qualityGroupID;
    }

    public void setQualityGroupID(Integer qualityGroupID) {
        this.qualityGroupID = qualityGroupID;
    }

    public Integer getProductGroupID() {
        return productGroupID;
    }

    public void setProductGroupID(Integer productGroupID) {
        this.productGroupID = productGroupID;
    }

    public Integer getProductID() {
        return productID;
    }

    public void setProductID(Integer productID) {
        this.productID = productID;
    }

    public Integer getSubType() {
        return subType;
    }

    public void setSubType(Integer subType) {
        this.subType = subType;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }

    public Object getResponseList() {
        return responseList;
    }

    public void setResponseList(Object responseList) {
        this.responseList = responseList;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    public Object getResponseDictionary() {
        return responseDictionary;
    }

    public void setResponseDictionary(Object responseDictionary) {
        this.responseDictionary = responseDictionary;
    }

    public ResponseDictionaryDT getResponseDictionaryDT() {
        return responseDictionaryDT;
    }

    public void setResponseDictionaryDT(ResponseDictionaryDT responseDictionaryDT) {
        this.responseDictionaryDT = responseDictionaryDT;
    }

    public Object getResponseExpandoObjectDictionary() {
        return responseExpandoObjectDictionary;
    }

    public void setResponseExpandoObjectDictionary(Object responseExpandoObjectDictionary) {
        this.responseExpandoObjectDictionary = responseExpandoObjectDictionary;
    }

    public Object getResponseDataTable() {
        return responseDataTable;
    }

    public void setResponseDataTable(Object responseDataTable) {
        this.responseDataTable = responseDataTable;
    }

}