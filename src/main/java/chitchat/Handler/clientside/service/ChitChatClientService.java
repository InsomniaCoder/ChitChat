package chitchat.Handler.clientside.service;

import chitchat.Handler.clientside.ClientHandler;
import chitchat.message.ChitChatMessage;
import chitchat.message.MessageType;
import chitchat.view.client.ClientPanel;
import chitchat.view.client.PrivateChatWindow;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by PorPaul on 5/12/2558.
 */
public class ChitChatClientService implements Runnable {

    private ClientPanel clientPanel;
    Socket socket;
    ObjectOutputStream outToServer;
    ObjectInputStream inFromServer;


    public ChitChatClientService(Socket socket, ClientPanel clientPanel) throws IOException {
        this.socket = socket;
        outToServer = new ObjectOutputStream(socket.getOutputStream());
        inFromServer = new ObjectInputStream(socket.getInputStream());
        this.clientPanel = clientPanel;

        ClientHandler.getInstance().setInFromServer(inFromServer);
        ClientHandler.getInstance().setOutToServer(outToServer);
        ClientHandler.getInstance().setClientPanel(clientPanel);
    }

    @Override
    public void run() {
        try {
            registerToServer();
            while (true) {
                //block til message come.. wait for server
                ChitChatMessage messageFromServer;
                synchronized (inFromServer) {
                    messageFromServer = (ChitChatMessage) inFromServer.readObject();
                }
                System.out.println("message from server received with type : " + messageFromServer.getMessageType());
                determineActionOnMessage(messageFromServer);

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void determineActionOnMessage(ChitChatMessage messageFromServer) throws IOException {

        MessageType messageType = messageFromServer.getMessageType();

        switch (messageType) {
            case ANNOUNCE:
                doAnnounce(messageFromServer.getName(), messageFromServer.getMessage());
                break;
            case PRIVATE:
                System.out.println("doPrivate");
                doPrivate(messageFromServer.getName(), messageFromServer.getMessage());
                break;
            case NOTIFY:
                System.out.println("doNotify");
                doNotify(messageFromServer.getMembersArray());
                break;
            case RUOK:
                doRuok();
                break;
        }
    }

    private void registerToServer() throws IOException {
        ClientHandler.getInstance().register();
    }

    /**
     * return the IMOK message to the server
     */
    private void doRuok() throws IOException {
        ClientHandler.getInstance().sendImOk();
    }

    /**
     * get the updated list and show in the members list
     *
     * @param membersArray
     */
    private void doNotify(String[] membersArray) {
        //update and assign map to online list
        System.out.println("getting list size " + membersArray.length);
        ClientHandler.membersArray =  membersArray;
        clientPanel.displayClientList();
    }


    /**
     * get announce from server and show in the board
     *
     * @param name    sender name
     * @param message
     */
    private void doAnnounce(String name, String message) {
        System.out.println(name + " says >> " + message);
        clientPanel.displayNewChatMessage(name + " : " + message + "\n");
    }

    /**
     * Display the new chat message which has been sent by another client in the private window.
     * If the private chat window has been closed or no window yet,
     * this method will create a new one and display the message.
     *
     * @param senderClientName
     * @param message
     */
    private void doPrivate(String senderClientName, String message) throws IOException {
       ClientHandler.getInstance().displayPrivateChat(senderClientName,message);
    }

}
