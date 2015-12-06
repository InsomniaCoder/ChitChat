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
        String clientName = "someThingFromTheForm";
        ChitChatMessage register = new ChitChatMessage(MessageType.REGISTER);
        register.setName(clientName);
        outToServer.writeObject(register);
        outToServer.flush();
        System.out.println("registered");
    }

    private void determineActionOnMessage(ChitChatMessage messageFromServer) throws IOException {

        MessageType messageType = messageFromServer.getMessageType();

        switch (messageType) {
            case ANNOUNCE:
                doAnnounce(messageFromServer.getMessage());
                break;
            case PRIVATE:
                //todo
                doPrivate();
                break;
            case NOTIFY:
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
    }

    /**
     * get the updated list and show in the members list
     *
     * @param membersList
     */
    private void doNotify(List<String> membersList) {
        //assign map to online list
    }


    /**
     * get announce from server and show in the board
     *
     * @param message
     */
    private void doAnnounce(String message) {

    }

    /**
     * show dialogue box that we get the incoming request for private chat
     */
    private void doPrivate() throws IOException {

    }

}
