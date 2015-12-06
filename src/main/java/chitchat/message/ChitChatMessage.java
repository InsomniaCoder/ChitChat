package chitchat.message;

import java.util.List;

/**
 * Created by PorPaul on 5/12/2558.
 */
public class ChitChatMessage {

    MessageType messageType;
    List<String> membersList;
    String message;
    String name;

    public ChitChatMessage(MessageType messageType, String message) {
        this.messageType = messageType;
        this.message = message;
    }

    public ChitChatMessage(MessageType notify, List<String> membersList) {
        this.messageType = notify;
        this.membersList = membersList;
    }

    public ChitChatMessage(MessageType messageType) {
        this.messageType = messageType;
    }

    public ChitChatMessage(MessageType messageType, String destinationClient, String message) {
        this.messageType = messageType;
        this.name = destinationClient;
        this.message = message;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public List<String> getMembersList() {
        return membersList;
    }

    public void setMembersList(List<String> membersList) {
        this.membersList = membersList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
