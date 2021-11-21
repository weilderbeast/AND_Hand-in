package github.bob.andhand_in.res.user;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class User {
    private String username;
    private String password;
    private String name;
    private ShapeableImageView icon;
    private UserPreferences preferences;
    private ArrayList<User> friends_list;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserPreferences getPreferences() {
        return preferences;
    }

    public void setPreferences(UserPreferences preferences) {
        this.preferences = preferences;
    }

    public ArrayList<User> getFriends_list() {
        return friends_list;
    }

    public void setFriends_list(ArrayList<User> friends_list) {
        this.friends_list = friends_list;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ShapeableImageView getIcon() {
        //TODO change this to contain a resId or base64 encoding for images
        return icon;
    }

    public void setIcon(ShapeableImageView icon) {
        //TODO change this to contain a resId or base65 encoding for images
        this.icon = icon;
    }
}
