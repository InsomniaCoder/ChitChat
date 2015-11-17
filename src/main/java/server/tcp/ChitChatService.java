package server.tcp;

import java.net.Socket;

/**
 * Created by PorPaul on 17/11/2558.
 */
public class ChitChatService implements Runnable {

    Socket clientSocket;

    public ChitChatService(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {

    }
}
