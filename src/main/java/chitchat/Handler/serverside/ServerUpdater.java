package chitchat.Handler.serverside;

/**
 * Created by PorPaul on 5/12/2558.
 */

import java.io.IOException;

/**
 *
 * This class is used for being a checker to check the connection continuously.
 */
public class ServerUpdater implements Runnable {

    @Override
    public void run() {

        while(true){

            try {
                //sleep for 20 sec
                Thread.sleep(20000);
                ServerHandler updater = ServerHandler.getInstance();
                updater.checkMembersConnection();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
