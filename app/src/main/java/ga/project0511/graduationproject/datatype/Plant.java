package ga.project0511.graduationproject.datatype;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class Plant implements Parcelable {

    // Constant
    public static String KEY_DATA_PLANT = "plant";
    public static String NOT_DOWNLOADED_YET = "Not downloaded yet";

    // Field
    private String id;
    private String name;
    private String imgPath_server;
    private String imgPath_local;

    private PlantInformation information;
    private PlantRequirement requirement;
    private PlantGuide guide;
    private PlantComments[] comments;

    // Constructor
    public Plant() {
        id = null;
        name = null;
        imgPath_server = null;
        imgPath_local = null;

        information = new PlantInformation();
        requirement = new PlantRequirement();
        guide = new PlantGuide();
        comments = null;
    }

    public Plant(String id, String name, String imgPath_server, String imgPath_local
                 ,PlantInformation information, PlantRequirement requirement
                 ,PlantGuide guide, PlantComments[] comments) {

        this.id = id; this.name = name; this.imgPath_server = imgPath_server; this.imgPath_local = imgPath_local;

        this.information = new PlantInformation(information);
        this.requirement = new PlantRequirement(requirement);
        this.guide = new PlantGuide(guide);
        this.comments = comments;
    }

    public Plant(Parcel src) {

        this.id = src.readString();
        this.name = src.readString();
        this.imgPath_server = src.readString();
        this.imgPath_local = src.readString();

        this.information = src.readParcelable(getClass().getClassLoader());
        this.requirement = src.readParcelable(getClass().getClassLoader());
        this.guide = src.readParcelable(getClass().getClassLoader());
        this.comments = src.createTypedArray(PlantComments.CREATOR);
    }

    // implementataion Parcelable abstract methods
    public static final Parcelable.Creator<Plant> CREATOR = new Parcelable.Creator<Plant>() {
        @Override
        public Plant createFromParcel(Parcel in) { return new Plant(in);}

        @Override
        public Plant[] newArray(int size) { return new Plant[size]; }
    };

    public int describeContents() { return 0; }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.imgPath_server);
        dest.writeString(this.imgPath_local);

        dest.writeParcelable(this.information, flags);
        dest.writeParcelable(this.requirement, flags);
        dest.writeParcelable(this.guide, flags);
        dest.writeTypedArray(this.comments, flags);
    }

    // Method

    public String getId() { return this.id; }
    public String getName() { return this.name; }
    public String getImgPath_server() { return this.imgPath_server; }
    public String getImgPath_local() { return this.imgPath_local; }

    public PlantInformation getInformation() { return this.information; }
    public PlantRequirement getRequirement() { return this.requirement; }
    public PlantGuide getGuide() { return this.guide; }
    public PlantComments[] getComments() { return this.comments; }

    public void setImgPath_server(String path) { this.imgPath_server = path; }
    public void setImgPath_local(String path) { this.imgPath_local = path; }

    public static Plant getPlantInfoFromIntent(Intent intent) {

        if(intent != null) {
            Bundle bundle = intent.getExtras();
            return bundle.getParcelable(Plant.KEY_DATA_PLANT);
        }
        else
            return new Plant();
    }

}
