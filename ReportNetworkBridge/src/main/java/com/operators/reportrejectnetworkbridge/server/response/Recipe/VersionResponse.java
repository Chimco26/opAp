package com.operators.reportrejectnetworkbridge.server.response.Recipe;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VersionResponse {

    @SerializedName("CompanyName")
    @Expose
    private String companyName;
    @SerializedName("ModifiedDate")
    @Expose
    private String modifiedDate;
    @SerializedName("ProductName")
    @Expose
    private String productName;
    @SerializedName("PublishDate")
    @Expose
    private String publishDate;
    @SerializedName("Version")
    @Expose
    private String version;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

}
