package chitchat.Handler.serverside;

import chitchat.message.ChitChatMessage;
import chitchat.message.MessageType;
import chitchat.view.server.ServerPanel;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

    private Map<String, ObjectInputStream> inputMap = new HashMap<String, ObjectInputStream>();

    private Map<String, ObjectOutputStream> outputMap = new HashMap<String, ObjectOutputStream>();

    private List<String> membersList = new ArrayList<String>();

    public static void setServerPanel(ServerPanel serverPanel) {
        ServerHandler.serverPanel = serverPanel;
    }

    public List<String> getMembersList() {
        return membersList;
    }

    /**
     * register member to the Map and notify
     *  @param clientName
     * @param clientInputStream
     * @param clientOutputStream
     */
    public void registerMember(String clientName,ObjectInputStream clientInputStream, ObjectOutputStream clientOutputStream) throws IOException {
        inputMap.put(clientName, clientInputStream);
        outputMap.put(clientName, clientOutputStream);
        membersList.add(clientName);
        ServerHandler.getInstance().announce("Server",clientName + " has joined the Chat!!");
        notifyListToAllMembers();
    }

    /**
     * remove member from Map and Notify
     *
     * @param memberToBeDeleted
     * @throws IOException
     */
    public void removeMember(String memberToBeDeleted) throws IOException {
        inputMap.remove(memberToBeDeleted);
        outputMap.remove(memberToBeDeleted);
        membersList.remove(memberToBeDeleted);
        announce("Server",memberToBeDeleted + " has left the Chat!!");
        System.out.println("member left");
        notifyListToAllMembers();
    }

    /**
     * send the updated map to all members
     *
     * @throws IOException
     */
    public void notifyListToAllMembers() throws IOException {

        System.out.println("memberList size : "+ membersList.size());

        for (String eachMember : membersList) {
            ObjectOutputStream   outToClient = outputMap.get(eachMember);

            String[] transformedArray = new String[membersList.size()];
            membersList.toArray(transformedArray);

            ChitChatMessage chitChatMessage = new ChitChatMessage(MessageType.NOTIFY, transformedArray);

            checkMembersConnection(eachMember);

            synchronized (outToClient) {
                System.out.println("sent notify list to member : "+eachMember + " size : "+membersList.size());
                outToClient.writeObject(chitChatMessage);
                outToClient.flush();
            }
        }
        serverPanel.displayClientList();
    }


    public void sendListToClient(String requester) throws IOException {

        String[] transformedArray = null;
        membersList.toArray(transformedArray);
        ChitChatMessage chitChatMessage = new ChitChatMessage(MessageType.NOTIFY, transformedArray);

        checkMembersConnection(requester);

        ObjectOutputStream outToClient = outputMap.get(requester);
        synchronized (outToClient) {
            outToClient.writeObject(chitChatMessage);
            outToClient.flush();
            System.out.println("notify sent to "+requester);
        }
    }

    /**
     * Announce message to all members
     *
     * @param clientName
     * @param message
     */
    public void announce(String clientName, String message) throws IOException {

        serverPanel.displayNewChatMessageOrLog(clientName+" : "+message+"\n");
        
        ObjectOutputStream outToClient;
        for (String eachMember : membersList) {

            checkMembersConnection(eachMember);

            ChitChatMessage chitChatMessage = new ChitChatMessage(MessageType.ANNOUNCE);
            chitChatMessage.setName(clientName);
            chitChatMessage.setMessage(message);

            outToClient = outputMap.get(eachMember);

            synchronized (outToClient) {
                outToClient.writeObject(chitChatMessage);
                outToClient.flush();
                System.out.println("announced message >> "+message);
            }
        }

    }

    /**
     * If attempt to read/write to Client throws IOException
     * It means that client is disconnect and will be remove off the list then notify all member
     *
     * @throws IOException
     */
    public void checkMembersConnection(String memberName) throws IOException {

        System.out.println("Checking "+memberName+" connection..");
        ObjectOutputStream outToClient;
            try {

                outToClient = outputMap.get(memberName);
                synchronized (outToClient) {
                    ChitChatMessage pollingMessage = new ChitChatMessage(MessageType.RUOK);
                    outToClient.writeObject(pollingMessage);
                    outToClient.flush();
                }

            } catch (IOException e) {
                removeMember(memberName);
            }
        System.out.println("connection "+memberName + " checked!!");
    }


    public void sendPrivateMessage(String sendingClient, String destinationClient, String message) throws IOException {
        ObjectOutputStream outToClient;
        outToClient = outputMap.get(destinationClient);
        ChitChatMessage chitChatMessage = new ChitChatMessage(MessageType.PRIVATE, sendingClient, message);
        checkMembersConnection(sendingClient);
        synchronized (outToClient) {
            outToClient.writeObject(chitChatMessage);
            outToClient.flush();
            System.out.println("private sent");
        }
    }

}
