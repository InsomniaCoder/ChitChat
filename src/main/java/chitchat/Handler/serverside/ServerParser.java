package chitchat.Handler.serverside;

/**
 * Created by PorPaul on 5/12/2558.
 */
public class ServerParser {
    private static ServerParser ourInstance = new ServerParser();

    public static ServerParser getInstance() {
        return ourInstance;
    }

    private ServerParser() {
    }


}
