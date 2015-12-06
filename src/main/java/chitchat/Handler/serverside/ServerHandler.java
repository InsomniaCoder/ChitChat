package chitchat.Handler.serverside;

import chitchat.message.ChitChatMessage;
import chitchat.message.MessageType;
import chitchat.view.server.ServerPanel;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by PorPaul on 5/12/2558.
 */
public class ServerHandler {

    private static ServerHandler ourInstance = new ServerHandler();
    private static ServerPanel serverPanel = null;


    private ServerHandler() {
    }

    public static ServerHandler getInstance() {
        return ourInstance;
    }


    private Map<String, Socket> membersMap = new HashMap<String, Socket>();

    private List<String> membersList = new ArrayList<String>();

    public static void setServerPanel(ServerPanel serverPanel) {
        ServerHandler.serverPanel = serverPanel;
    }

    public Map<String, Socket> getMembersMap() {
        return membersMap;
    }

    public List<String> getMembersList() {
        return membersList;
    }

    public void setMembersList(List<String> membersList) {
        this.membersList = membersList;
    }

    /**
     * register member to the Map and notify
     *
     * @param clientName
     * @param clientSocket
     */
    public void registerMember(String clientName, Socket clientSocket) throws IOException {
        membersMap.put(clientName, clientSocket);
        membersList.add(clientName);
        notifyListToAllMembers();
        ServerHandler.getInstance().announce(clientName, "member name : " + clientName + " has joined the Chat!!");
        System.out.println("member joined");
    }

    /**
     * remove member from Map and Notify
     *
     * @param memberToBeDeleted
     * @throws IOException
     */
    public void removeMember(String memberToBeDeleted) throws IOException {
        membersMap.remove(memberToBeDeleted);
        membersList.remove(memberToBeDeleted);
        notifyListToAllMembers();
        announce(memberToBeDeleted, "member name : " + memberToBeDeleted + " has left the Chat!!");
        System.out.println("member left");
    }

    /**
     * send the updated map to all members
     *
     * @throws IOException
     */
    public void notifyListToAllMembers() throws IOException {

        ObjectOutputStream outToClient;
        for (Map.Entry<String, Socket> member : membersMap.entrySet()) {
            Socket eachClient = member.getValue();
            outToClient = new ObjectOutputStream(eachClient.getOutputStream());
            ChitChatMessage chitChatMessage = new ChitChatMessage(MessageType.NOTIFY, membersList);
            outToClient.writeObject(chitChatMessage);
            outToClient.flush();
        }
        serverPanel.displayClientList();
    }

    /**
     * Announce message to all members
     *
     * @param clientName
     * @param message
     */
    public void announce(String clientName, String message) throws IOException {

        ObjectOutputStream outToClient;
        for (Map.Entry<String, Socket> member : membersMap.entrySet()) {
            Socket eachClient = member.getValue();
            outToClient = new ObjectOutputStream(eachClient.getOutputStream());

            ChitChatMessage chitChatMessage = new ChitChatMessage(MessageType.ANNOUNCE);
            chitChatMessage.setName(clientName);
            chitChatMessage.setMessage(message);

            outToClient.writeObject(chitChatMessage);
            outToClient.flush();
            System.out.println("announced");
        }
    }

    public void sendPrivateMessage(String sendingClient, String destinationClient, String message) throws IOException {

            ObjectOutputStream outToClient;
            Socket eachClient = membersMap.get(destinationClient);
            outToClient = new ObjectOutputStream(eachClient.getOutputStream());
            ChitChatMessage chitChatMessage = new ChitChatMessage(MessageType.PRIVATE, sendingClient, message);
            outToClient.writeObject(chitChatMessage);
            outToClient.flush();
            System.out.println("private sent");
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
        System.out.println("connection checked!!");
    }
}
