package github.bob.andhand_in.res.user;

import github.bob.andhand_in.ui.fragments.friend_requests.FriendRequestsViewModel;

public class FriendRequest {
    private String request_id;
    private String sender_id;
    private String receiver_id;

    public FriendRequest(){

    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(String receiver_id) {
        this.receiver_id = receiver_id;
    }
}
