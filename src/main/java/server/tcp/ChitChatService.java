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

    List<Socket> connectingClientList;
    List<InputStream> clientsInputStreamList;
    List<OutputStream> clientsOutputStreamList;

    public ChitChatService(List<Socket> connectingClientList, List<InputStream> clientsInputStreamList, List<OutputStream> clientsOutputStreamList) {
        this.connectingClientList = connectingClientList;
        this.clientsInputStreamList = clientsInputStreamList;
        this.clientsOutputStreamList = clientsOutputStreamList;
    }

    @Override
    public void run() {

    }
}
