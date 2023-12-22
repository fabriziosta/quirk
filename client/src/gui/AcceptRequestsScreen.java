package gui;

import socket.SocketSender;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
public class AcceptRequestsScreen extends JDialog{
    private JLabel lblRequest;
    private JButton btnConfirm;
    private JButton btnRefuse;
    private JPanel pnlAcceptRequest;
    //-----------------------------------------------------------------------------------
    AcceptRequestsScreen(HomeScreen HS, String name, String kind_of_request) {
        this.setLocationRelativeTo(HS); setContentPane(pnlAcceptRequest); setAlwaysOnTop(true); HS.setEnabled(false);
        SwingUtilities.getRootPane(btnConfirm).setDefaultButton(btnConfirm);
        super.setIconImage(new ImageIcon(getClass().getResource("/img/icon.png")).getImage());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        lblRequest.setText("Do you want to accept '" + name + "' " + kind_of_request + " request?");

        btnConfirm.addActionListener(e -> {
            switch(kind_of_request){
                case "friend":
                    new SocketSender().queryTheServer("friend_request_accepted-" + name + "-" + HS.user_id);
                    HS.friendModel.setElementAt(name, HS.listFriends.getSelectedIndex()); break;
                case "group":
                    new SocketSender().queryTheServer("group_request_accepted-" + name + "-" + HS.user_id);
                    HS.groupModel.setElementAt(name, HS.listGroups.getSelectedIndex()); break;
                case "channel":
                    new SocketSender().queryTheServer("channel_request_accepted-" + name + "-" + HS.user_id);
                    HS.channelModel.setElementAt(name, HS.listChannels.getSelectedIndex()); break;
            }
            HS.setEnabled(true); dispose();
        });

        btnRefuse.addActionListener(e -> {
            switch(kind_of_request){
                case "friend":
                    new SocketSender().queryTheServer("friend_request_refused-" + name + "-" + HS.user_id);
                    HS.friendModel.removeElementAt(HS.listFriends.getSelectedIndex()); break;
                case "group":
                    new SocketSender().queryTheServer("group_request_refused-" + name + "-" + HS.user_id);
                    HS.groupModel.removeElementAt(HS.listGroups.getSelectedIndex()); break;
                case "channel":
                    new SocketSender().queryTheServer("channel_request_refused-" + name + "-" + HS.user_id);
                    HS.channelModel.removeElementAt(HS.listChannels.getSelectedIndex()); break;
            }
            HS.setEnabled(true); dispose();
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {HS.setEnabled(true);}
        });

        setVisible(true); setResizable(false); setAlwaysOnTop(true); this.pack();
    }//-----------------------------------------------------------------------------------
} //end AcceptRequestsScreen class
