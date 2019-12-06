package ga.project0511.graduationproject.datatype;

import android.os.Parcel;
import android.os.Parcelable;

public class PlantGuide implements Parcelable {

    // Constants

    // Field
    private String propagation;
    private String disease;
    private String climate;
    private String soil;
    private String water;
    private String fertilizer;

    // Constructors
    public PlantGuide() {
        this.propagation = null;
        this.disease = null;
        this.climate = null;
        this.soil = null;
        this.water = null;
        this.fertilizer = null;
    }

    public PlantGuide(String propagation, String disease, String climate
                      , String soil, String water, String fertilizer) {
        this.propagation = propagation;
        this.disease = disease;
        this.climate = climate;
        this.soil = soil;
        this.water = water;
        this.fertilizer = fertilizer;
    }

    public PlantGuide(PlantGuide plantGuide) {
        this.propagation = plantGuide.propagation;
        this.disease = plantGuide.disease;
        this.climate = plantGuide.climate;
        this.soil = plantGuide.soil;
        this.water = plantGuide.water;
        this.fertilizer = plantGuide.fertilizer;
    }

    public PlantGuide(Parcel src){
        this.propagation = src.readString();
        this.disease = src.readString();
        this.climate = src.readString();
        this.soil = src.readString();
        this.water = src.readString();
        this.fertilizer = src.readString();
    }

    // Implementation Parcelable
    public static final Parcelable.Creator<PlantGuide> CREATOR = new Parcelable.Creator<PlantGuide>() {
        @Override
        public PlantGuide createFromParcel(Parcel in) { return new PlantGuide(in); }

        @Override
        public PlantGuide[] newArray(int size) { return new PlantGuide[size]; }
    };

    public int describeContents() { return 0; }

    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(this.propagation);
        dest.writeString(this.disease);
        dest.writeString(this.climate);
        dest.writeString(this.soil);
        dest.writeString(this.water);
        dest.writeString(this.fertilizer);

    }

    // Method
    public String getPropagation() { return this.propagation; }
    public String getDisease() { return this.disease; }
    public String getClimate() { return this.climate; }
    public String getSoil() { return this.soil; }
    public String getWater() { return this.water; }
    public String getFertilizer() { return this.fertilizer; }
}
