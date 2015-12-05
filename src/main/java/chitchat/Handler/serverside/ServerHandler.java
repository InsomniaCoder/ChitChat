package chitchat.Handler.serverside;

import chitchat.message.ChitChatMessage;
import chitchat.message.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

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


    private List<Socket> membersList = new ArrayList<Socket>();
    /**
     * register new member to the list and then notify all other member
     *
     * @param newMember
     */
    public void addMembers(Socket newMember) throws IOException {
        membersList.add(newMember);
        notifyListToAllMembers();
    }

    public void removeMembersFromList(Socket socket) throws IOException {
        membersList.remove(socket);
        notifyListToAllMembers();
    }

    /**
     * If attempt to read/write to Client throws IOException
     * It means that client is disconnect and will be remove off the list then notify all member
     * @throws IOException
     */
    public void checkMembersConnection() throws IOException {

        ObjectOutputStream outToServer;
        ObjectInputStream inFromServer;

        for (Socket socket : membersList) {

            try {
                outToServer = new ObjectOutputStream(socket.getOutputStream());
                ChitChatMessage pollingMessage = new ChitChatMessage(MessageType.RUOK);
                outToServer.writeObject(pollingMessage);
                outToServer.flush();
                inFromServer = new ObjectInputStream(socket.getInputStream());
                ChitChatMessage pollingResponse = (ChitChatMessage)inFromServer.readObject();
                //check if client answers with I'm OK type
                if(!MessageType.IMOK.equals(pollingResponse.getMessageType())){
                   removeMembersFromList(socket);
                }
            } catch (IOException e) {
                removeMembersFromList(socket);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void notifyListToAllMembers() throws IOException {

        ObjectOutputStream outToServer;
        for (Socket socket : membersList) {
            outToServer = new ObjectOutputStream(socket.getOutputStream());
            ChitChatMessage chitChatMessage = new ChitChatMessage(MessageType.NOTIFY,membersList);
            outToServer.writeObject(chitChatMessage);
            outToServer.flush();
        }
    }


}
