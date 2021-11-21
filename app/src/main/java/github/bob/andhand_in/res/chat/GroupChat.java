package github.bob.andhand_in.res.chat;

import java.util.ArrayList;

import github.bob.andhand_in.res.user.User;

public class GroupChat {
    private String chat_name;
    private ArrayList<User> users;
    private ArrayList<Message> messages;

    public String getChat_name() {
        return chat_name;
    }

    public void setChat_name(String chat_name) {
        this.chat_name = chat_name;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }
}
