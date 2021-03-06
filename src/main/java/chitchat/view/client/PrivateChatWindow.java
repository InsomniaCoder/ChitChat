/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chitchat.view.client;

import chitchat.Handler.clientside.ClientHandler;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author GiftzyEiei
 */
public class PrivateChatWindow extends javax.swing.JFrame {
    private String userName;
    private String receiverClientName;
    
    private PrivateChatWindow() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Creates new form PrivateChatWindow
     * @param userName name of the current user of this application
     * @param receiverClientName name of the selected client from client list
     */
    public PrivateChatWindow(String userName, String receiverClientName) {
        initComponents();
        this.userName = userName;
        this.receiverClientName = receiverClientName;
        receiverClientLabel.setText(receiverClientName);//display selected client name on top left of window
        this.setTitle(userName+" -> "+receiverClientName+" [Private Chat]");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        msgInputTextField = new javax.swing.JTextField();
        sendButton = new javax.swing.JButton();
        receiverClientLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        msgDisplayTextArea = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        msgInputTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                msgInputTextFieldActionPerformed(evt);
            }
        });

        sendButton.setText("Send");
        sendButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sendButtonMouseClicked(evt);
            }
        });

        receiverClientLabel.setText("client");

        msgDisplayTextArea.setEditable(false);
        msgDisplayTextArea.setColumns(20);
        msgDisplayTextArea.setRows(5);
        jScrollPane1.setViewportView(msgDisplayTextArea);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 453, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(msgInputTextField)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sendButton))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(receiverClientLabel)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(receiverClientLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(msgInputTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sendButton))
                .addContainerGap())
        );

        msgDisplayTextArea.setAutoscrolls(true);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void msgInputTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_msgInputTextFieldActionPerformed
        // press Enter on keyboard to send text in private chat
        String message = msgInputTextField.getText();
        if(message.length() == 0){
            return;
        }
        try {
            // send private message
            ClientHandler.getInstance().sendPrivate(receiverClientName, message);
        } catch (IOException ex) {
            Logger.getLogger(PrivateChatWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        msgInputTextField.setText("");
        msgDisplayTextArea.append(userName+" : "+message+"\n");
    }//GEN-LAST:event_msgInputTextFieldActionPerformed

    private void sendButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sendButtonMouseClicked
        // click send button to send text in private chat
        String message = msgInputTextField.getText();
        if(message.length() == 0){
            return;
        }
        try {
            // send private message
            ClientHandler.getInstance().sendPrivate(receiverClientName, message);
        } catch (IOException ex) {
            Logger.getLogger(PrivateChatWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        msgInputTextField.setText("");
        msgDisplayTextArea.append(userName+" : "+message+"\n");
    }//GEN-LAST:event_sendButtonMouseClicked

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // close window
        ClientHandler.getInstance().removePrivateChatWindow(receiverClientName);
        this.dispose();
    }//GEN-LAST:event_formWindowClosed

    public void displayNewChatMessage(String message) {
        msgDisplayTextArea.append(message);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PrivateChatWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PrivateChatWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PrivateChatWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PrivateChatWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PrivateChatWindow().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea msgDisplayTextArea;
    private javax.swing.JTextField msgInputTextField;
    private javax.swing.JLabel receiverClientLabel;
    private javax.swing.JButton sendButton;
    // End of variables declaration//GEN-END:variables
}
