/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chitchat.Handler.serverside.service;

import chitchat.Handler.serverside.ServerUpdater;
import chitchat.view.server.ServerInitiation;
import chitchat.view.server.ServerPanel;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author GiftzyEiei
 */
public class StartServerService implements Runnable {
    private ServerSocket serverSocket;
    private int port;
    private ServerInitiation serverInitiation;
    private ServerPanel serverPanel;

    public StartServerService(int port, ServerInitiation serverInitiation, ServerPanel serverPanel) {
        this.port = port;
        this.serverInitiation = serverInitiation;
        this.serverPanel = serverPanel;
    }
    
    public void run() {
        try{
            serverSocket = new ServerSocket(port);
        }
        catch(IOException ex){
            JOptionPane.showMessageDialog(serverPanel,
                    "Cannot start server.",
                    "Message",
                    JOptionPane.INFORMATION_MESSAGE);
            serverPanel.dispose();
            return;
        }

        serverPanel.setVisible(true);
        serverInitiation.setVisible(false);
        
        serverPanel.displayServerInfo();
        serverPanel.displayClientList();
        
        //start checker thread
        new Thread(new ServerUpdater()).start();
        
        while (true) {
            //waiting for client to connect
            try {
                java.net.Socket clientSocket = serverSocket.accept();
                //start service each client
                new Thread(new ChitChatServerService(clientSocket)).start();
                serverPanel.setSocket(serverSocket);
            } catch (IOException ex) {
                Logger.getLogger(ServerPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
