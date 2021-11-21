package github.bob.andhand_in.res.chat;

import androidx.annotation.DrawableRes;

import java.util.ArrayList;

import github.bob.andhand_in.res.user.User;

public class Chat {
    String username;
    String last_sent_text;
    String last_sent_text_timestamp;
    @DrawableRes
    int user_icon;

    public Chat(String username, String last_sent_text, String last_sent_text_timestamp, int user_icon) {
        this.username = username;
        this.last_sent_text = last_sent_text;
        this.last_sent_text_timestamp = last_sent_text_timestamp;
        this.user_icon = user_icon;
    }

    public Chat(String username, String last_sent_text, String last_sent_text_timestamp) {
        this.username = username;
        this.last_sent_text = last_sent_text;
        this.last_sent_text_timestamp = last_sent_text_timestamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLast_sent_text() {
        return last_sent_text;
    }

    public void setLast_sent_text(String last_sent_text) {
        this.last_sent_text = last_sent_text;
    }

    public String getLast_sent_text_timestamp() {
        return last_sent_text_timestamp;
    }

    public void setLast_sent_text_timestamp(String last_sent_text_timestamp) {
        this.last_sent_text_timestamp = last_sent_text_timestamp;
    }

    public int getUser_icon() {
        return user_icon;
    }

    public void setUser_icon(int user_icon) {
        this.user_icon = user_icon;
    }
}
