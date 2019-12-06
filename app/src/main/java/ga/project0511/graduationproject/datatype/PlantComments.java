package ga.project0511.graduationproject.datatype;

import android.os.Parcel;
import android.os.Parcelable;

public class PlantComments implements Parcelable {

    // Constants

    // Field
    private String date;
    private String author;
    private String content;

    // Constructors
    public PlantComments() {
        this.date = null;
        this.author = null;
        this.content = null;
    }

    public PlantComments(String date, String author, String content) {
        this.date = date;
        this.author = author;
        this.content = content;
    }

    public PlantComments(PlantComments plantComments) {
        this.date = plantComments.date;
        this.author = plantComments.author;
        this.content = plantComments.content;
    }

    public PlantComments(Parcel src){
        this.date = src.readString();
        this.author = src.readString();
        this.content = src.readString();
    }

    // Implementation Parcelable
    public static final Parcelable.Creator<PlantComments> CREATOR = new Parcelable.Creator<PlantComments>(){
        @Override
        public PlantComments createFromParcel(Parcel in) { return new PlantComments(in); }

        @Override
        public PlantComments[] newArray(int size) { return new PlantComments[size]; }
    };

    public void writeToParcel(Parcel dest, int flag) {
        dest.writeString(date);
        dest.writeString(author);
        dest.writeString(content);
    }

    public int describeContents() { return 0; }

    // Method
    public String getDate() { return this.date; }
    public String getAuthor() { return this.author; }
    public String getContent() { return this.content; }
}
