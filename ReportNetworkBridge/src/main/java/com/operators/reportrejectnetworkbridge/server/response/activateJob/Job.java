package com.operators.reportrejectnetworkbridge.server.response.activateJob;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.operators.reportrejectnetworkbridge.server.response.Recipe.RecipeResponse;

import java.util.List;

public class Job implements Parcelable {

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
    @SerializedName("Notes")
    @Expose
    private String notes;

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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.actions);
        dest.writeValue(this.iD);
        dest.writeTypedList(this.materials);
        dest.writeParcelable(this.mold, flags);
        dest.writeStringList(this.productFiles);
        dest.writeString(this.notes);
    }

    public Job() {
    }

    protected Job(Parcel in) {
        this.actions = in.createTypedArrayList(Action.CREATOR);
        this.iD = (Integer) in.readValue(Integer.class.getClassLoader());
        this.materials = in.createTypedArrayList(Material.CREATOR);
        this.mold = in.readParcelable(Mold.class.getClassLoader());
        this.productFiles = in.createStringArrayList();
        this.notes = in.readString();
    }

    public static final Parcelable.Creator<Job> CREATOR = new Parcelable.Creator<Job>() {
        @Override
        public Job createFromParcel(Parcel source) {
            return new Job(source);
        }

        @Override
        public Job[] newArray(int size) {
            return new Job[size];
        }
    };
}
