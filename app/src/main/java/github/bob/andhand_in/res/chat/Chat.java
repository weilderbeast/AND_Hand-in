package github.bob.andhand_in.res.chat;

import github.bob.andhand_in.res.user.User;

public class Chat {
    private User user;
    private String last_sent_text;
    private String timestamp;

    public Chat() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getLast_sent_text() {
        return last_sent_text;
    }

    public void setLast_sent_text(String last_sent_text) {
        this.last_sent_text = last_sent_text;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
