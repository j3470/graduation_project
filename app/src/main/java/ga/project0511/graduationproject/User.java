package ga.project0511.graduationproject;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    private final String id;
    private final String nickname;
    private final String email;

    public User(String id, String nickname, String email) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
    }

    public User(Parcel src) {
        this.id = src.readString();
        this.nickname = src.readString();
        this.email = src.readString();
    }

    public String getId() { return this.id; }

    public String getNickname() { return this.nickname; }

    public String getEmail() {
        return this.email;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public User createFromParcel (Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    public void writeToParcel (Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(nickname);
        dest.writeString(email);
    }
}
