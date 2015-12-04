package chitchat.message;

import chitchat.Handler.ChitChatClient;

import java.net.SocketAddress;
import java.util.List;

/**
 * Created by PorPaul on 5/12/2558.
 */
public class ChitChatMessage {

        MessageType messageType;
        List<ChitChatClient> membersList;
        String message;
        SocketAddress socketAddress;
}
