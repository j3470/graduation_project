package ga.project0511.graduationproject.datatype;

import android.os.Parcel;
import android.os.Parcelable;

public class PlantClassification implements Parcelable {

    // Constants

    // Fields
    private String phylum;
    private String plantClass;
    private String order;
    private String family;
    private String genus;
    private String species;

    // Constructors
    public PlantClassification() {
        phylum = null;
        plantClass = null;
        order = null;
        family = null;
        genus = null;
        species = null;
    }

    public PlantClassification(String phylum, String plantClass, String order
                               , String family, String genus, String species) {
        this.phylum = phylum;
        this.plantClass = plantClass;
        this.order = order;
        this.family = family;
        this.genus = genus;
        this.species = species;
    }

    public PlantClassification(PlantClassification classification) {

        this.phylum = classification.phylum;
        this.plantClass = classification.plantClass;
        this.order = classification.order;
        this.family = classification.family;
        this.genus = classification.genus;
        this.species = classification.species;
    }

    public PlantClassification(Parcel src){

        this.phylum = src.readString();
        this.plantClass = src.readString();
        this.order = src.readString();
        this.family = src.readString();
        this.genus = src.readString();
        this.species = src.readString();
    }

    // implements Parcelable abstract methods
    public static final Parcelable.Creator<PlantClassification> CREATOR = new Parcelable.Creator<PlantClassification>() {
        @Override
        public PlantClassification createFromParcel(Parcel in) { return new PlantClassification(in); }

        @Override
        public PlantClassification[] newArray(int size) { return new PlantClassification[size]; }
    };

    public int describeContents() { return 0; }

    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(this.phylum);
        dest.writeString(this.plantClass);
        dest.writeString(this.order);
        dest.writeString(this.family);
        dest.writeString(this.genus);
        dest.writeString(this.species);
    }


    // Methods
    public String getPhylum() { if(this.phylum.equals("")) return "해당없음"; else return this.phylum; }
    public String getPlantClass() { if(this.plantClass.equals("")) return "해당없음"; else return this.plantClass; }
    public String getOrder() { if(this.order.equals("")) return "해당없음"; else return this.order; }
    public String getFamily() { if(this.family.equals("")) return "해당없음"; else return this.family; }
    public String getGenus() { if(this.genus.equals("")) return "해당없음"; else return this.genus; }
    public String getSpecies() { if(this.species.equals("")) return "해당없음"; else  return this.species; }

}
