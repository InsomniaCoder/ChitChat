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
    public void addMembersWithNameToMap(String clientName, Socket clientSocket) throws IOException {

        membersMap.put(clientName, clientSocket);
        notifyListToAllMembers();
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
                    removeMembersFromList(member.getKey());
                }
            } catch (IOException e) {
                removeMembersFromList(member.getKey());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void removeMembersFromList(String memberToBeDeleted) throws IOException {
        membersMap.remove(memberToBeDeleted);
        notifyListToAllMembers();
    }

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

}
