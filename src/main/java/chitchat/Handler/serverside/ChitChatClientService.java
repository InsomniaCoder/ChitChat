package chitchat.Handler.serverside;

import java.net.Socket;

/**
 * Created by PorPaul on 5/12/2558.
 */
public class ChitChatClientService implements Runnable {

    Socket socket;

    public ChitChatClientService(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

    }
}
