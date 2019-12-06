package ga.project0511.graduationproject.datatype;

import android.os.Parcel;
import android.os.Parcelable;

public class PlantRequirement implements Parcelable {

    // Constants
    public final static int TEMP_NOT_INSERTED = -1;

    // Field
    private String size;
    private int tmp_max;
    private int tmp_min;
    private String[] light;
    private String[] soil;
    private String[] ph;
    private int difficulty;
    private String drainage;

    // Constructors
    public PlantRequirement() {
        this.size = null;
        this.tmp_max = TEMP_NOT_INSERTED;
        this.tmp_min = TEMP_NOT_INSERTED;
        this.light = null;
        this.soil = null;
        this.ph = null;
        this.difficulty = TEMP_NOT_INSERTED;
        this.drainage = null;
    }

    public PlantRequirement(String size, int tmp_max, int tmp_min
                            , String[] light, String[] soil, String[] ph
                            , int difficulty, String drainage) {
        this.size = size;
        this.tmp_max = tmp_max;
        this.tmp_min = tmp_min;
        this.light = light;
        this.soil = soil;
        this.ph = ph;
        this.difficulty = difficulty;
        this.drainage = drainage;
    }

    public PlantRequirement(PlantRequirement plantRequirement) {

        this.size = plantRequirement.size;
        this.tmp_max = plantRequirement.tmp_max;
        this.tmp_min = plantRequirement.tmp_min;
        this.light = plantRequirement.light;
        this.soil = plantRequirement.soil;
        this.ph = plantRequirement.ph;
        this.difficulty = plantRequirement.difficulty;
        this.drainage = plantRequirement.drainage;
    }

    public PlantRequirement(Parcel src) {

        this.size = src.readString();
        this.tmp_max = src.readInt();
        this.tmp_min = src.readInt();
        this.light = src.createStringArray();
        this.soil = src.createStringArray();
        this.ph = src.createStringArray();
        this.difficulty = src.readInt();
        this.drainage = src.readString();

    }

    // Implementation Parcelable abstract methods

    public static final Parcelable.Creator<PlantRequirement> CREATOR = new Parcelable.Creator<PlantRequirement>() {
        @Override
        public PlantRequirement createFromParcel(Parcel in) { return new PlantRequirement(in); }
        @Override
        public PlantRequirement[] newArray(int size) { return new PlantRequirement[size]; }
    };

    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(this.size);
        dest.writeInt(this.tmp_max);
        dest.writeInt(this.tmp_min);
        dest.writeStringArray(this.light);
        dest.writeStringArray(this.soil);
        dest.writeStringArray(this.ph);
        dest.writeInt(difficulty);
        dest.writeString(this.drainage);
    }

    // Method
    public String getSize() { if(this.size.equals("")) return "해당없음"; else return this.size; }
    public int getTmp_max() { return this.tmp_max; }
    public int getTmp_min() { return this.tmp_min; }
    public String[] getLight() { return this.light; }
    public String[] getSoil() { return this.soil; }
    public String[] getPh() { return this.ph; }
    public int getDifficulty() { return this.difficulty; }
    public String getDrainage() { if(this.drainage.equals("")) return "해당없음"; else return this.drainage; }
}
