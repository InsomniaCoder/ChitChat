package chitchat.Handler.serverside.service;

import chitchat.Handler.serverside.ServerHandler;
import chitchat.message.ChitChatMessage;
import chitchat.message.MessageType;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by PorPaul on 5/12/2558.
 */
public class ChitChatServerService implements Runnable {

    Socket socket;
    String clientName;
    ObjectOutputStream outToClient;
    ObjectInputStream inFromClient;

    public ChitChatServerService(Socket socket) throws IOException {
        this.socket = socket;
        outToClient = new ObjectOutputStream(socket.getOutputStream());
        inFromClient = new ObjectInputStream(socket.getInputStream());
    }

    /**
     * start protocol and then waiting for request/response
     */
    @Override
    public void run() {

        try {
            while (true) {

                ChitChatMessage messageFromClient = null;


                    synchronized (inFromClient) {
                        //block til message come
                        messageFromClient = (ChitChatMessage) inFromClient.readObject();
                        System.out.println("message from client received with type : " + messageFromClient.getMessageType());
                        determineActionOnMessage(messageFromClient);
                    }

            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
         }catch (IOException e){
            try {
                ServerHandler.getInstance().removeMember(clientName);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void determineActionOnMessage(ChitChatMessage messageFromClient) throws IOException, ClassNotFoundException {

        MessageType messageType = messageFromClient.getMessageType();

        switch (messageType) {
            case REGISTER:
                doRegister(messageFromClient.getName());
                break;
            case ANNOUNCE:
                doAnnounce(messageFromClient.getMessage());
                break;
            case NOTIFY:
                doNotify(messageFromClient.getName());
            case PRIVATE:
                System.out.println("doPrivate");
                doPrivate(messageFromClient.getName(),messageFromClient.getMessage());
                break;
            case QUIT:
                doQuit();
                break;
        }//end switch
    }

    /**
     * send updated list to request client
     * @param requestorName
     */
    private void doNotify(String requestorName) throws IOException {
        ServerHandler.getInstance().sendListToClient(requestorName);
    }

    /**
     * get the client name and register
     * @param name
     */
    private void doRegister(String name) throws IOException, ClassNotFoundException {
        System.out.println("registering "+name);
        this.clientName = name;
        ServerHandler.getInstance().registerMember(name, inFromClient, outToClient);
    }

    /**
     * get message and then announce to the board and all members
     *
     * @param message
     */
    private void doAnnounce(String message) throws IOException {
        //add to message board
        //announce to members
        System.out.println("Announcing : "+message);
        ServerHandler.getInstance().announce(clientName,message);
    }

    /**
     *
     * @param destinationClient the receiver of private
     * @param message
     */
    private void doPrivate(String destinationClient, String message) throws IOException {

        ServerHandler.getInstance().sendPrivateMessage(clientName,destinationClient,message);
    }

    private void doQuit() throws IOException {
        ServerHandler.getInstance().removeMember(clientName);
    }


}
