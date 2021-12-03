package github.bob.andhand_in.res.chat;

import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.List;

public class ChatHelper {
    private List<String> participants;
    private List<String> conversationRefs;

    public ChatHelper() {
        conversationRefs = new ArrayList<>();
    }

    public List<String> getConversationRefs() {
        return conversationRefs;
    }

    public void setConversationRefs(List<String> conversationRefs) {
        this.conversationRefs = conversationRefs;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }
}
