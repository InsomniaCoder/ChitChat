package server.tcp;

import server.ChitChatServer;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by PorPaul on 17/11/2558.
 */
public class ChitChatTCP implements ChitChatServer, Runnable{

    private int port;
    private ServerSocket chitChatServer;

    public ChitChatTCP(int port) {
        this.port = port;
    }

    @Override
    public void run() {

        try {
            chitChatServer = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println(e.getMessage()+"Cannot establish server ");
            System.exit(-1);
        }

    }
}
