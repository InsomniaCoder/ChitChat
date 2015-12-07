package chitchat.Handler.clientside.service;

import chitchat.message.ChitChatMessage;
import chitchat.message.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

/**
 * Created by PorPaul on 5/12/2558.
 */
public class ChitChatClientService implements Runnable {


    Socket socket;
    ObjectOutputStream outToServer;
    ObjectInputStream inFromServer;


    public ChitChatClientService(Socket socket) throws IOException {
        this.socket = socket;
        outToServer = new ObjectOutputStream(socket.getOutputStream());
        inFromServer = new ObjectInputStream(socket.getInputStream());
    }

    @Override
    public void run() {
        try {
            registerToServer();
            while (true) {
                //block til message come.. wait for server
                ChitChatMessage messageFromServer = (ChitChatMessage) inFromServer.readObject();
                System.out.println("message from server received with type : " + messageFromServer.getMessageType());
                determineActionOnMessage(messageFromServer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 1.send this name to server
     */
    private void registerToServer() throws IOException {
        String clientName = "member "+Math.random();
        ChitChatMessage register = new ChitChatMessage(MessageType.REGISTER);
        register.setName(clientName);
        outToServer.writeObject(register);
        outToServer.flush();
        System.out.println("sent name to server ...");
    }

    private void determineActionOnMessage(ChitChatMessage messageFromServer) throws IOException {

        MessageType messageType = messageFromServer.getMessageType();

        switch (messageType) {
            case ANNOUNCE:
                doAnnounce(messageFromServer.getName(),messageFromServer.getMessage());
                break;
            case PRIVATE:
                //todo
                System.out.println("doprivate");
                doPrivate(messageFromServer.getName(),messageFromServer.getMessage());
                break;
            case NOTIFY:
                System.out.println("doNotify");
                doNotify(messageFromServer.getMembersList());
                break;
            case RUOK:
                doRuok();
                break;
        }
    }


    /**
     * return the IMOK messagge to the server
     */
    private void doRuok() throws IOException {
        ChitChatMessage returnMessage = new ChitChatMessage(MessageType.IMOK);
        outToServer.writeObject(returnMessage);
        outToServer.flush();
        System.out.println("reply I'M OK");
    }

    /**
     * get the updated list and show in the members list
     *
     * @param membersList
     */
    private void doNotify(List<String> membersList) {
        //update and assign map to online list
    }


    /**
     * get announce from server and show in the board
     *
     * @param name sender name
     * @param message
     */
    private void doAnnounce(String name, String message) {
        System.out.println(name+" says >> "+message);
        //show this message to the board
    }

    /**
     * show dialogue box that we get the incoming request for private chat
     * @param name
     * @param message
     */
    private void doPrivate(String name, String message) throws IOException {
        //open windows of this name
        //have a list contain talking client
        //if on the list just assign the message to that box
        //else open the box and then assign message
    }

}
