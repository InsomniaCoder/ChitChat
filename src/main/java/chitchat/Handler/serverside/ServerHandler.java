package chitchat.Handler.serverside;

import chitchat.message.ChitChatMessage;
import chitchat.message.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by PorPaul on 5/12/2558.
 */
public class ServerHandler {

    private static ServerHandler ourInstance = new ServerHandler();

    private ServerHandler() {
    }

    public static ServerHandler getInstance() {
        return ourInstance;
    }


    private Map<String, Socket> membersMap = new HashMap<String, Socket>();

    /**
     * register member to the Map and notify
     *
     * @param clientName
     * @param clientSocket
     */
    public void registerMember(String clientName, Socket clientSocket) throws IOException {
        membersMap.put(clientName, clientSocket);
        notifyListToAllMembers();
    }

    /**
     * remove member from Map and Notify
     * @param memberToBeDeleted
     * @throws IOException
     */
    public void removeMember(String memberToBeDeleted) throws IOException {
        membersMap.remove(memberToBeDeleted);
        notifyListToAllMembers();
        announce("member name : " + memberToBeDeleted + " has left the Chat!!");
    }

    /**
     * send the updated map to all members
     * @throws IOException
     */
    public void notifyListToAllMembers() throws IOException {

        ObjectOutputStream outToClient;
        for (Map.Entry<String, Socket> member : membersMap.entrySet()) {
            Socket eachClient = member.getValue();
            outToClient = new ObjectOutputStream(eachClient.getOutputStream());
            ChitChatMessage chitChatMessage = new ChitChatMessage(MessageType.NOTIFY, membersMap);
            outToClient.writeObject(chitChatMessage);
            outToClient.flush();
        }
    }

    /**
     * Announce message to all members
     * @param message
     */
    public void announce(String message) throws IOException {

        ObjectOutputStream outToClient;
        for (Map.Entry<String, Socket> member : membersMap.entrySet()) {
            Socket eachClient = member.getValue();
            outToClient = new ObjectOutputStream(eachClient.getOutputStream());
            ChitChatMessage chitChatMessage = new ChitChatMessage(MessageType.ANNOUNCE, message);
            outToClient.writeObject(chitChatMessage);
            outToClient.flush();
        }
    }

    /**
     * If attempt to read/write to Client throws IOException
     * It means that client is disconnect and will be remove off the list then notify all member
     *
     * @throws IOException
     */
    public void checkMembersConnection() throws IOException {

        ObjectOutputStream outToClient;
        ObjectInputStream inFromClient;
        /**
         * iterate over the map
         */
        for (Map.Entry<String, Socket> member : membersMap.entrySet()) {
            Socket eachSocket = member.getValue();
            try {
                outToClient = new ObjectOutputStream(eachSocket.getOutputStream());
                ChitChatMessage pollingMessage = new ChitChatMessage(MessageType.RUOK);
                outToClient.writeObject(pollingMessage);
                outToClient.flush();
                inFromClient = new ObjectInputStream(eachSocket.getInputStream());
                ChitChatMessage pollingResponse = (ChitChatMessage) inFromClient.readObject();
                //check if client answers with I'm OK type
                if (!MessageType.IMOK.equals(pollingResponse.getMessageType())) {
                    removeMember(member.getKey());
                }
            } catch (IOException e) {
                removeMember(member.getKey());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
