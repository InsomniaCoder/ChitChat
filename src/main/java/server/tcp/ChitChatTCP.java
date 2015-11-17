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
    private List<Socket> connectingClientList = new ArrayList();
    private List<InputStream> clientsInputStreamList = new ArrayList<InputStream>();
    private List<OutputStream> clientsOutputStreamList = new ArrayList<OutputStream>();


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
                //add socket into list.
                connectingClientList.add(clientSocket);
                clientsInputStreamList.add(clientSocket.getInputStream());
                clientsOutputStreamList.add(clientSocket.getOutputStream());
                //start servicing client.
                new Thread(new ChitChatService(connectingClientList,clientsInputStreamList,clientsOutputStreamList)).start();


            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        }//end while
    }
}
