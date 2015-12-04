package chitchat.server;

import server.tcp.ChitChatTCP;

import javax.swing.*;

/**
 * Created by PorPaul on 17/11/2558.
 */

/**
 * This class is used for controlling the server.
 * start this class fist before running client chat side.
 */
public class ServerMain {


    public static void main(String[] args) throws InterruptedException {

        //get port number form server admin
        String port = JOptionPane.showInputDialog("Welcome to ChitChat! Please Enter port number : ");

        //cast String to int port and validate it
        int portNumber = castAndValidatePortNumber(port);

        //move the program to Server thread
        Thread serverThread = new Thread(new ChitChatTCP(portNumber));
        serverThread.start();
        //wait while program is running
        serverThread.join();
    }

    private static int castAndValidatePortNumber(String port) {

        int portNumber = 0;
        try {
            portNumber = Integer.valueOf(port);
            //check if the port is valid for network protocol
            if (portNumber <= 1024 && portNumber > 65535) {
                System.out.println("Port number is incorrect, port should be between 49151 and 65535");
                System.exit(-1);
            }
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage() + " Please enter port number in the right format");
            System.exit(-1);
        }

        return portNumber;
    }
}
