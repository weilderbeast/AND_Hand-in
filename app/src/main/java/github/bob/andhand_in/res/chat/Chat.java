package github.bob.andhand_in.res.chat;

public class Chat {
    private String user;
    private String last_text;
    private String timestamp;
    private int profile_picture;

    public Chat(String user, String last_text, String timestamp, int profile_picture) {
        this.user= user;
        this.last_text = last_text;
        this.timestamp = timestamp;
        this.profile_picture = profile_picture;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getLast_text() {
        return last_text;
    }

    public void setLast_text(String last_text) {
        this.last_text = last_text;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(int profile_picture) {
        this.profile_picture = profile_picture;
    }
}
