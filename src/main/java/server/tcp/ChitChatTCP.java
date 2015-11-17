package server.tcp;


import java.io.IOException;
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
    private List<Socket> connectingClientList = new ArrayList();


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

        while(true){

            try {
                Socket clientSocket = chitChatServer.accept();
                connectingClientList.add(clientSocket);


            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }


        }//end while
    }
}
