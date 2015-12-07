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
     * @param clientSocket
     * @param clientInputStream
     * @param clientOutputStream
     */
    public void registerMember(String clientName, Socket clientSocket, ObjectInputStream clientInputStream, ObjectOutputStream clientOutputStream) throws IOException {
        membersMap.put(clientName, clientSocket);
        inputMap.put(clientName, clientInputStream);
        outputMap.put(clientName, clientOutputStream);
        membersList.add(clientName);
        ServerHandler.getInstance().announce(clientName, " member name : " + clientName + " has joined the Chat!!");
        notifyListToAllMembers();
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
        System.out.println("memberList size : "+membersList.size());
        System.out.println("memberMap size : "+membersMap.size());
        for (Map.Entry<String, Socket> member : membersMap.entrySet()) {
            String eachName = member.getKey();
            outToClient = outputMap.get(eachName);
            ChitChatMessage chitChatMessage = new ChitChatMessage(MessageType.NOTIFY, membersList);
            synchronized (outToClient) {
                checkMembersConnection(eachName);
                outToClient.writeObject(chitChatMessage);
                System.out.println("sent notify list to member : "+eachName);
                outToClient.flush();
            }
        }
        serverPanel.displayClientList();
    }


    public void sendListToClient(String requestorName, ObjectOutputStream outToClient) throws IOException {
        ChitChatMessage chitChatMessage = new ChitChatMessage(MessageType.NOTIFY, membersList);

        checkMembersConnection(requestorName);

        synchronized (outToClient) {
            outToClient.writeObject(chitChatMessage);
            outToClient.flush();
            System.out.println("notify sent to "+requestorName);
        }
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
            String eachName = member.getKey();
            outToClient = outputMap.get(eachName);
            checkMembersConnection(eachName);
            ChitChatMessage chitChatMessage = new ChitChatMessage(MessageType.ANNOUNCE);
            chitChatMessage.setName(clientName);
            chitChatMessage.setMessage(message);

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
        ObjectInputStream inFromClient;
            try {

                outToClient = outputMap.get(memberName);

                synchronized (outToClient) {
                    ChitChatMessage pollingMessage = new ChitChatMessage(MessageType.RUOK);
                    outToClient.writeObject(pollingMessage);
                    outToClient.flush();
                }

                inFromClient = inputMap.get(memberName);

                synchronized (inFromClient) {
                    ChitChatMessage pollingResponse = (ChitChatMessage) inFromClient.readObject();
                    if (!MessageType.IMOK.equals(pollingResponse.getMessageType())) {
                        removeMember(memberName);
                    }
                }

                //check if client answers with I'm OK type
            } catch (IOException e) {
                removeMember(memberName);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        System.out.println("connection "+memberName + " checked!!");
    }


    public void sendPrivateMessage(String sendingClient, String destinationClient, String message) throws IOException {
        ObjectOutputStream outToClient;
        outToClient = outputMap.get(destinationClient);
        ChitChatMessage chitChatMessage = new ChitChatMessage(MessageType.PRIVATE, sendingClient, message);
        synchronized (outToClient) {
            checkMembersConnection(sendingClient);
            outToClient.writeObject(chitChatMessage);
            outToClient.flush();
            System.out.println("private sent");
        }
    }

}
