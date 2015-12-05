package chitchat.message;

import java.net.Socket;
import java.net.SocketAddress;
import java.util.Map;

/**
 * Created by PorPaul on 5/12/2558.
 */
public class ChitChatMessage {

    MessageType messageType;
    Map<String, Socket> membersList;
    String message;
    SocketAddress socketAddress;


    public ChitChatMessage(MessageType messageType, String message) {
        this.messageType = messageType;
        this.message = message;
    }

    public ChitChatMessage(MessageType messageType, SocketAddress socketAddress) {
        this.messageType = messageType;
        this.socketAddress = socketAddress;
    }

    public ChitChatMessage(MessageType notify, Map<String, Socket> membersList) {
        this.messageType = notify;
        this.membersList = membersList;
    }

    public ChitChatMessage(MessageType messageType) {
        this.messageType = messageType;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public Map<String, Socket> getMembersList() {
        return membersList;
    }

    public void setMembersList(Map<String, Socket> membersList) {
        this.membersList = membersList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public SocketAddress getSocketAddress() {
        return socketAddress;
    }

    public void setSocketAddress(SocketAddress socketAddress) {
        this.socketAddress = socketAddress;
    }
}
