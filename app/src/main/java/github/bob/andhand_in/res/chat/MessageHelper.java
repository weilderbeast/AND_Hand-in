package github.bob.andhand_in.res.chat;

import java.util.ArrayList;
import java.util.List;

public class MessageHelper {
    private List<String> conversationRefs;
    private List<String> participants;

    public MessageHelper() {
        conversationRefs = new ArrayList<>();
        participants = new ArrayList<>();
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
