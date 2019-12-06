package ga.project0511.graduationproject.datatype;


import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class Gardening implements Parcelable {

    public static final String KEY_ACTIVITY_DATA = "activity";

    public static final String NO_PARTICIPANTS = "No participants";
    public static final String NOT_ENDED_YET = "Not ended yet";
    public static final String NOT_DOWNLOADED_YET = "Not downloaded yet";
    public static final String ENDED_ACTIVITY = "Activity ended";

    private String activityName;
    private String createdAt;
    private String endedAt;
    private String plantName;
    private String managerID;
    private String participantsID;
    private String imgPath_server;
    private String imgPath_local;

    public Gardening() {
        activityName = null;
        createdAt = null;
        endedAt = null;
        plantName = null;
        managerID = null;
        participantsID = null;
        imgPath_server = null;
        imgPath_local = null; }

    public Gardening(String activityName, String createdAt, String endedAt,
                     String plantName, String managerID, String participantsID, String imgPath_server, String imgPath_local) {

        this.activityName = activityName;
        this.createdAt = createdAt;
        this.endedAt = endedAt;
        this.plantName = plantName;
        this.managerID = managerID;
        this.participantsID = participantsID;
        this.imgPath_server = imgPath_server;
        this.imgPath_local = imgPath_local;
        }

    public String getActivityName() { return this.activityName; }
    public String getCreatedAt() { return this.createdAt; }
    public String getEndedAt() { return this.endedAt; }
    public String getPlantName() { return this.plantName; }
    public String getManagerID() { return this.managerID; }
    public String getParticipantsID() { return this.participantsID; }
    public String getImgPath_server() { return this.imgPath_server; }
    public String getImgPath_local() { return this.imgPath_local; }
    public void setImgPath_local(String imgPath_local) {
        this.imgPath_local = imgPath_local;
    }


    public Gardening(Parcel src){
        this.activityName = src.readString();
        this.createdAt = src.readString();
        this.endedAt = src.readString();
        this.plantName = src.readString();
        this.managerID = src.readString();
        this.participantsID = src.readString();
        this.imgPath_server = src.readString();
        this.imgPath_local = src.readString();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Gardening createFromParcel (Parcel in) {
            return new Gardening(in);
        }
        public Gardening[] newArray(int size) {
            return new Gardening[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    public void writeToParcel (Parcel dest, int flags) {
        dest.writeString(activityName);
        dest.writeString(createdAt);
        dest.writeString(endedAt);
        dest.writeString(plantName);
        dest.writeString(managerID);
        dest.writeString(participantsID);
        dest.writeString(imgPath_server);
        dest.writeString(imgPath_local);
    }

    public static Gardening getGardeningInfoFromIntent(Intent intent){
        if(intent!=null){
            Bundle bundle = intent.getExtras();
            return bundle.getParcelable(KEY_ACTIVITY_DATA);
        }
        else
            return new Gardening();
    }
}
