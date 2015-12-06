package chitchat.Handler.clientside.service;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import chitchat.view.client.ClientInitiation;
import chitchat.view.client.ClientPanel;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author GiftzyEiei
 */
public class StartClientService implements Runnable {
    private Socket socket;
    private String ip;
    private int port;
    private ClientInitiation clientInitiation;
    private ClientPanel clientPanel;

    public StartClientService(String ip, int port, ClientInitiation clientInitiation, ClientPanel clientPanel) {
        this.ip = ip;
        this.port = port;
        this.clientInitiation = clientInitiation;
        this.clientPanel = clientPanel;
    }

    public void run() {
        //initial connection with Server
        try{
            socket = new Socket(ip, port);
        }
        catch(ConnectException ex){
            Logger.getLogger(StartClientService.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(clientPanel,
                    "Cannot connect to server.",
                    "Message",
                    JOptionPane.INFORMATION_MESSAGE);
            clientPanel.dispose();
            return;
        }
        catch(SocketException ex){
            Logger.getLogger(StartClientService.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(clientPanel,
                    "Cannot connect to server.",
                    "Message",
                    JOptionPane.INFORMATION_MESSAGE);
            clientPanel.dispose();
            return;
        } catch (IOException ex) {
            Logger.getLogger(StartClientService.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(clientPanel,
                    "Cannot connect to server.",
                    "Message",
                    JOptionPane.INFORMATION_MESSAGE);
            clientPanel.dispose();
            return;
        }
        
        clientPanel.displayInfo();
        clientPanel.setVisible(true);
        clientInitiation.setVisible(false);
        
        try {
            //start service
            new Thread(new ChitChatClientService(socket)).start();
        } catch (IOException ex) {
            Logger.getLogger(ClientPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        clientPanel.displayClientList();
    }
}
