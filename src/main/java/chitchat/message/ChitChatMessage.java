package chitchat.message;

import java.net.InetAddress;
import java.net.Socket;
import java.util.List;
import java.util.Map;

/**
 * Created by PorPaul on 5/12/2558.
 */
public class ChitChatMessage {

    MessageType messageType;
    Map<String, Socket> membersList;
    List<String> members;
    String message;
    String name;
    InetAddress requestAddress;
    int requestPort;

    public ChitChatMessage(MessageType messageType, String message) {
        this.messageType = messageType;
        this.message = message;
    }

    public ChitChatMessage(MessageType notify, Map<String, Socket> membersList) {
        this.messageType = notify;
        this.membersList = membersList;
    }


    public ChitChatMessage(MessageType messageType) {
        this.messageType = messageType;
    }

    public InetAddress getRequestAddress() {
        return requestAddress;
    }

    public void setRequestAddress(InetAddress requestAddress) {
        this.requestAddress = requestAddress;
    }

    public int getRequestPort() {
        return requestPort;
    }

    public void setRequestPort(int requestPort) {
        this.requestPort = requestPort;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
