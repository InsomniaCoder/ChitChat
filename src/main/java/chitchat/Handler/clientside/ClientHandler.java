package chitchat.Handler.clientside;

import chitchat.message.ChitChatMessage;
import chitchat.message.MessageType;
import chitchat.view.client.ClientPanel;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PorPaul on 7/12/2558.
 */
public class ClientHandler {

    private static ClientHandler ourInstance = new ClientHandler();

    public static ClientHandler getInstance() {
        return ourInstance;
    }


    private ClientPanel clientPanel = null;
    private List<String> membersList = new ArrayList<String>();
    private ObjectOutputStream outToServer = null;
    private ObjectInputStream inFromServer = null;

    private ClientHandler() {
    }


    public void setMembersList(List<String> membersList) {
        this.membersList = membersList;
    }

    public void setOutToServer(ObjectOutputStream outToServer) {
        this.outToServer = outToServer;
    }


    public void setInFromServer(ObjectInputStream inFromServer) {
        this.inFromServer = inFromServer;
    }

    public void setClientPanel(ClientPanel clientPanel) {
        this.clientPanel = clientPanel;
    }

    public void announce(String message) throws IOException {

        ChitChatMessage chitChatMessage = new ChitChatMessage(MessageType.ANNOUNCE);
        chitChatMessage.setName(clientPanel.getUserName());
        chitChatMessage.setMessage(message);

        synchronized (outToServer) {
            outToServer.writeObject(chitChatMessage);
            outToServer.flush();
        }
    }

    public void sendImOk() throws IOException {

        ChitChatMessage returnMessage = new ChitChatMessage(MessageType.IMOK);
        synchronized (outToServer) {
            outToServer.writeObject(returnMessage);
            outToServer.flush();
        }
        System.out.println("reply I'M OK");
    }

    public void register() throws IOException {
        ChitChatMessage register = new ChitChatMessage(MessageType.REGISTER);
        register.setName(clientPanel.getUserName());
        synchronized (outToServer) {
            outToServer.writeObject(register);
            outToServer.flush();
        }
        System.out.println("sent name to server ...");
    }

}
