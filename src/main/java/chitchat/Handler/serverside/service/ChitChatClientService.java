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
public class ChitChatClientService implements Runnable {

    Socket socket;
    ObjectOutputStream  outToServer;
    ObjectInputStream inFromServer;

    public ChitChatClientService(Socket socket) throws IOException {
        this.socket = socket;
        outToServer = new ObjectOutputStream(socket.getOutputStream());
        inFromServer = new ObjectInputStream(socket.getInputStream());
    }

    @Override
    public void run() {
        try {
            while(true){
                //block til message come
                ChitChatMessage chitChatMessage = (ChitChatMessage)inFromServer.readObject();
                determineActionOnMessage(chitChatMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void determineActionOnMessage(ChitChatMessage chitChatMessage) throws IOException {

        MessageType messageType = chitChatMessage.getMessageType();

        switch (messageType) {
            case ANNOUNCE:
                //Announce this message
                break;
            case QUIT:
                ServerHandler.getInstance().removeMembersFromList(socket);
                break;
        }

    }
}
