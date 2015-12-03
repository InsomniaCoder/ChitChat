package server.tcp;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PorPaul on 17/11/2558.
 */
public class ChitChatTCP implements Runnable {

    private int port;
    private ServerSocket chitChatServer;
    //keep each Socket
    private List<ClientContact> connectingClientList = new ArrayList<ClientContact>();


    public ChitChatTCP(int port) {
        this.port = port;
    }

    @Override
    public void run() {

        //create frame for server monitoring...

        //start server
        try {
            chitChatServer = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println(e.getMessage() + "Cannot establish server ");
            Thread.currentThread().interrupt();
        }
        //server keep waiting
        while (true) {
            try {
                //wait until client connects and get Socket instance.
                Socket clientSocket = chitChatServer.accept();

                //create ClientContact class contains everything needed for contacting with client.
                ClientContact clientContact = new ClientContact(clientSocket, clientSocket.getInputStream(), clientSocket.getOutputStream());
                //add it to the list
                connectingClientList.add(clientContact);
                //start servicing client.
                new Thread(new ChitChatService(clientSocket,connectingClientList)).start();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        }//end while
    }
}
