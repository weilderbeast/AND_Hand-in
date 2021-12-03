package github.bob.andhand_in.res.chat;

import java.util.Date;

public class Message implements Comparable<Message>{
    private String content;
    private String senderID;
    private Date timestamp = new Date();

    public Message(){
        timestamp = new Date();
    }

    public Message(String content, String sender){
        this.content = content;
        this.senderID = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public Date getTimestamp() {
        return timestamp;
    }


    @Override
    public int compareTo(Message message) {
        return getTimestamp().compareTo(message.getTimestamp());
    }
}
