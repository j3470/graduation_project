package ga.project0511.graduationproject;


public class User {
    private final String id;
    private final String nickname;
    private final String email;
    private final String phone;

    public User(String id, String nickname, String email, String phone) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.phone = phone;
    }

    public String getId() { return this.id; }

    public String getNickname() { return this.nickname; }

    public String getEmail() {
        return this.email;
    }

    public String getPhone() {
        return this.phone;
    }




}
