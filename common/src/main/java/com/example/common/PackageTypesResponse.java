package com.example.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PackageTypesResponse extends StandardResponse {

    @SerializedName("ResponseDictionaryDT")
    @Expose
    private ResponseDictionaryDT responseDictionaryDT;

    public ResponseDictionaryDT getResponseDictionaryDT() {
        return responseDictionaryDT;
    }

    public void setResponseDictionaryDT(ResponseDictionaryDT responseDictionaryDT) {
        this.responseDictionaryDT = responseDictionaryDT;
    }

    public class ResponseDictionaryDT {

        @SerializedName("PackageTypes")
        @Expose
        private List<PackageType> packageTypes = null;

        public List<PackageType> getPackageTypes() {
            return packageTypes;
        }

        public void setPackageTypes(List<PackageType> packageTypes) {
            this.packageTypes = packageTypes;
        }


    }

    public class PackageType {

        @SerializedName("ID")
        @Expose
        private Integer id;
        @SerializedName("Color")
        @Expose
        private String color;
        @SerializedName("EffectiveAmount")
        @Expose
        private Integer effectiveAmount;
        @SerializedName("EffectiveAmountMultiplication")
        @Expose
        private Boolean effectiveAmountMultiplication;
        @SerializedName("EffectiveAmountMultiplicationTable")
        @Expose
        private Object effectiveAmountMultiplicationTable;
        @SerializedName("EffectiveAmountMultiplicationField")
        @Expose
        private Object effectiveAmountMultiplicationField;
        @SerializedName("LName")
        @Expose
        private String lName;
        @SerializedName("EName")
        @Expose
        private String eName;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public Integer getEffectiveAmount() {
            return effectiveAmount;
        }

        public void setEffectiveAmount(Integer effectiveAmount) {
            this.effectiveAmount = effectiveAmount;
        }

        public Boolean getEffectiveAmountMultiplication() {
            return effectiveAmountMultiplication;
        }

        public void setEffectiveAmountMultiplication(Boolean effectiveAmountMultiplication) {
            this.effectiveAmountMultiplication = effectiveAmountMultiplication;
        }

        public Object getEffectiveAmountMultiplicationTable() {
            return effectiveAmountMultiplicationTable;
        }

        public void setEffectiveAmountMultiplicationTable(Object effectiveAmountMultiplicationTable) {
            this.effectiveAmountMultiplicationTable = effectiveAmountMultiplicationTable;
        }

        public Object getEffectiveAmountMultiplicationField() {
            return effectiveAmountMultiplicationField;
        }

        public void setEffectiveAmountMultiplicationField(Object effectiveAmountMultiplicationField) {
            this.effectiveAmountMultiplicationField = effectiveAmountMultiplicationField;
        }

        public String getLName() {
            return lName;
        }

        public void setLName(String lName) {
            this.lName = lName;
        }

        public String getEName() {
            return eName;
        }

        public void setEName(String eName) {
            this.eName = eName;
        }
    }
}
