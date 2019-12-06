package ga.project0511.graduationproject.datatype;

import android.os.Parcel;
import android.os.Parcelable;

public class PlantInformation implements Parcelable {

    // Constants

    // Fields
    private PlantClassification classification;
    private String flowering;
    private String sowing;
    private String habitat;
    private String scientificName;
    private String size;

    // Constructors
    public PlantInformation() {

        this.classification = new PlantClassification();

        this.flowering = null;
        this.sowing = null;
        this.habitat = null;
        this.scientificName = null;
        this.size = null;
    }

    public PlantInformation(PlantClassification classification
                            ,String flowering, String sowing, String habitat
                            ,String scientificName, String size) {

        this.classification = new PlantClassification(classification);

        this.flowering = flowering;
        this.sowing = sowing;
        this.habitat = habitat;
        this.scientificName = scientificName;
        this.size = size;
    }

    public PlantInformation(PlantInformation info) {

        this.classification = info.classification;
        this.flowering = info.flowering;
        this.sowing = info.sowing;
        this.habitat = info.habitat;
        this.scientificName = info.scientificName;
        this.size = info.size;
    }

    public PlantInformation(Parcel src) {

        this.classification = src.readParcelable(getClass().getClassLoader());

        this.flowering = src.readString();
        this.sowing = src.readString();
        this.habitat = src.readString();
        this.scientificName = src.readString();
        this.size = src.readString();
    }

    // Implementation Parcelable abstract methods
    public static final Parcelable.Creator<PlantInformation> CREATOR = new Parcelable.Creator<PlantInformation>(){
        @Override
        public PlantInformation createFromParcel(Parcel in) { return new PlantInformation(in);}

        @Override
        public PlantInformation[] newArray(int size) { return new PlantInformation[size];}
    };

    public int describeContents() { return 0; }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.classification, flags);

        dest.writeString(flowering);
        dest.writeString(sowing);
        dest.writeString(habitat);
        dest.writeString(scientificName);
        dest.writeString(size);
    }

    // Methods

    public PlantClassification getClassification() { return this.classification; }
    public String getFlowering() { if(this.flowering.equals("")) return "해당없음"; else return this.flowering; }
    public String getSowing() { if(this.sowing.equals("")) return "해당없음"; else return this.sowing; }
    public String getHabitat() { if(this.habitat.equals("")) return "서식지 불명"; else return this.habitat; }
    public String getScientificName() { if(this.scientificName.equals("")) return "해당없음"; else return this.scientificName; }
    public String getSize() { if(this.size.equals("")) return "해당없음"; else return size; }
}
