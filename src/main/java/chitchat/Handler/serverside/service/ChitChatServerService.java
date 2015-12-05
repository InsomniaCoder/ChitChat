package chitchat.Handler.serverside.service;

import chitchat.Handler.serverside.ServerHandler;
import chitchat.message.ChitChatMessage;
import chitchat.message.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by PorPaul on 5/12/2558.
 */
public class ChitChatServerService implements Runnable {

    Socket socket;
    ObjectOutputStream outToClient;
    ObjectInputStream inFromClient;

    public ChitChatServerService(Socket socket) throws IOException {
        this.socket = socket;
        outToClient = new ObjectOutputStream(socket.getOutputStream());
        inFromClient = new ObjectInputStream(socket.getInputStream());
    }

    @Override
    public void run() {

        initialProtocolAction();

        try {
            while(true){
                //block til message come
                ChitChatMessage messageFromClient = (ChitChatMessage) inFromClient.readObject();
                determineActionOnMessage(messageFromClient);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initialProtocolAction() {

    }

    private void determineActionOnMessage(ChitChatMessage messageFromClient) throws IOException {

        MessageType messageType = messageFromClient.getMessageType();

        switch (messageType) {
            case ANNOUNCE:
                doAnnounce();
                break;
            case QUIT:
                doQuit();
                break;
        }
    }

    /**
     * get message and then announce to the board and all members
     */
    private void doAnnounce() {
        //add to message board
        //announce to members
    }

    private void doQuit() throws IOException {
        ServerHandler.getInstance().removeMembersFromList(socket);
    }


}
