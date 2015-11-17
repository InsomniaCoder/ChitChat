package server.tcp;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

/**
 * Created by PorPaul on 17/11/2558.
 */

/**
 * This class provide service for room chat
 */
public class ChitChatService implements Runnable {

    Socket socket;
    List<ClientContact> connectingClientList;

    public ChitChatService(Socket clientSocket, List<ClientContact> connectingClientList) {
        this.socket = clientSocket;
        this.connectingClientList = connectingClientList;
    }

    @Override
    public void run() {





    }
}
