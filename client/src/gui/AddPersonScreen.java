package gui;

import socket.SocketSender;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AddPersonScreen extends JDialog{
    private JPanel pnlAddFriend;
    private JTextField txtUsername;
    private JButton btnAdd;
    private JLabel lblUsername;
    //-----------------------------------------------------------------------------------
    AddPersonScreen(HomeScreen HS, String tipology){
        this.setLocationRelativeTo(HS);setContentPane(pnlAddFriend); setAlwaysOnTop(true); HS.setEnabled(false);
        SwingUtilities.getRootPane(btnAdd).setDefaultButton(btnAdd);
        super.setIconImage(new ImageIcon(getClass().getResource("/img/icon.png")).getImage());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        btnAdd.addActionListener(e -> {
            String usernameToADD = txtUsername.getText();
            String responseFromServer = "";

            if(!usernameToADD.isEmpty() && usernameToADD.length() > 2){
                if(!usernameToADD.equals(HS.user_username)) {

                    switch (tipology){
                        case "friend":
                            responseFromServer = new SocketSender().queryTheServer("add_friend-" + usernameToADD + "-" + HS.user_id); break;
                        case "group":
                            responseFromServer = new SocketSender()
                                    .queryTheServer("add_group_member-" + usernameToADD + "-" + HS.listGroups.getSelectedValue().toString()); break;
                        case "channel":
                            responseFromServer = new SocketSender()
                                    .queryTheServer("add_channel_member-" + usernameToADD + "-" + HS.listChannels.getSelectedValue().toString()); break;
                    }
                    JOptionPane.showMessageDialog(null, responseFromServer);
                    HS.setEnabled(true); dispose();

                }else{JOptionPane.showMessageDialog(null, "You can't add yourself!");}
            }else{JOptionPane.showMessageDialog(null, "Username cannot be less than 2 characters!");}

        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {HS.setEnabled(true);}
        });
        setVisible(true); setResizable(false); this.pack();
    }
} //end AddPersonScreen class
