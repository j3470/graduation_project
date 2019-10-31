package ga.project0511.graduationproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;


public class User implements Parcelable {

    public static final String KEY_USER_DATA = "user";

    private String id;
    private String nickname;
    private String email;

    public User() {
        id = null;
        nickname = null;
        email = null;
    }

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

    public static User getUserInfoFromIntent(Intent intent){
        if(intent!=null){
            Bundle bundle = intent.getExtras();
            return bundle.getParcelable(KEY_USER_DATA);
        }
        else
            return new User();
    }
}
