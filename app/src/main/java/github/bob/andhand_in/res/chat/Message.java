package github.bob.andhand_in.res.chat;

import java.util.Calendar;

public class Message {
    private String sender;
    private String receiver;
    private String body;
    private String timestamp;

    public Message(String sender, String receiver, String body) {
        this.sender = sender;
        this.receiver = receiver;
        this.body = body;
        timestamp = Calendar.getInstance().getTime().toString();
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
