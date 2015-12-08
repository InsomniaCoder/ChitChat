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
import java.util.List;

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
                doNotify(messageFromServer.getMembersList());
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
     * @param membersList
     */
    private void doNotify(List<String> membersList) {
        //update and assign map to online list
        System.out.println("getting list size " + membersList.size());
        ClientHandler.membersList = (membersList);
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
     * show dialogue box that we get the incoming request for private chat
     *
     * @param name
     * @param message
     */
    private void doPrivate(String name, String message) throws IOException {
        if (ClientHandler.getInstance().getPrivateChatWindowMap().containsKey(name)) {
            //send message to the window
            PrivateChatWindow window = ClientHandler.getInstance().getPrivateChatWindowMap().get(name);
            window.displayNewChatMessage(name+" : "+message+"\n");
        } else {
            // no private window for this client. open the window.
            PrivateChatWindow window = new PrivateChatWindow(name);
            ClientHandler.getInstance().getPrivateChatWindowMap().put(name, window);
            window.setVisible(true);
            //send message to the window
            window.displayNewChatMessage(name+" : "+message+"\n");
        }
    }

}
