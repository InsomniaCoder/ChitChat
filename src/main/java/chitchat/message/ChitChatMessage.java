package chitchat.message;

import java.io.Serializable;

/**
 * Created by PorPaul on 5/12/2558.
 */
public class ChitChatMessage implements Serializable{

    MessageType messageType;
    String[] membersArray;
    String message;
    String name;

    public ChitChatMessage(MessageType notify, String[] membersArrayr) {
        this.messageType = notify;
        this.membersArray = membersArrayr;
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

    public String[] getMembersArray() {
        return membersArray;
    }

    public void setMembersArray(String[] membersArray) {
        this.membersArray = membersArray;
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
