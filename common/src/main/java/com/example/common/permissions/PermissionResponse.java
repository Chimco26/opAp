package com.example.common.permissions;

import com.example.common.StandardResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PermissionResponse extends StandardResponse {

    @SerializedName("Data")
    @Expose
    private List<Object> data = null;
    @SerializedName("ResponseList")
    @Expose
    private List<WidgetInfo> widgetInfo = null;
    @SerializedName("Response")
    @Expose
    private Object response;
    @SerializedName("ResponseDictionary")
    @Expose
    private Object responseDictionary;
    @SerializedName("ResponseDictionaryDT")
    @Expose
    private Object responseDictionaryDT;
    @SerializedName("ResponseExpandoObjectDictionary")
    @Expose
    private Object responseExpandoObjectDictionary;
    @SerializedName("ResponseDataTable")
    @Expose
    private Object responseDataTable;


    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }

    public List<WidgetInfo> getWidgetInfo() {
        return widgetInfo;
    }

    public void setWidgetInfo(List<WidgetInfo> widgetInfo) {
        this.widgetInfo = widgetInfo;
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

    public Object getResponseDictionaryDT() {
        return responseDictionaryDT;
    }

    public void setResponseDictionaryDT(Object responseDictionaryDT) {
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
