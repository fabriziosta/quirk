package gui;

import socket.SocketSender;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static classes.CommonMethods.GUIadapter;

public class DeleteScreen extends JFrame{
    private JButton NOButton;
    private JButton YESButton;
    private JPanel pnlDelete;

    DeleteScreen(HomeScreen HS){
        this.setSize(500, 500); this.setLocationRelativeTo(HS); setContentPane(pnlDelete);
        setAlwaysOnTop(true); HS.setEnabled(false);
        super.setIconImage(new ImageIcon(getClass().getResource("/img/icon.png")).getImage());
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        YESButton.addActionListener(e -> {

            if(HS.listFriends.getSelectedValue() != null){ //delete friend
                //We do this twice because we don't know is friendship is, for example, 1_2 or 2_1. So one query will fail without any conseguence.
                new SocketSender().queryTheServer("delete_contact-" + HS.friend_id +"-"+ HS.user_id );
                new SocketSender().queryTheServer("delete_contact-" + HS.user_id +"-"+ HS.friend_id );
                HS.friendModel.removeElementAt(HS.listFriends.getSelectedIndex());
            }else if(HS.listGroups.getSelectedValue() != null){ //delete group
                new SocketSender().queryTheServer("delete_yourself_from_group-" + HS.group_id + "-" + HS.user_id);
                HS.groupModel.removeElementAt(HS.listGroups.getSelectedIndex());
            }else if(HS.listChannels.getSelectedValue() != null){ //delete channel
                new SocketSender().queryTheServer("delete_yourself_from_channel-" + HS.channel_id + "-" + HS.user_id);
                HS.channelModel.removeElementAt(HS.listChannels.getSelectedIndex());
            }
            //Before dispose this window, clean txtAreaCHAT and GUI!
            GUIadapter(HS, "", 0);
            HS.txtAreaCHAT.setText("");
            HS.setEnabled(true); dispose();
        });

        NOButton.addActionListener(e -> {HS.setEnabled(true); dispose();});

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {HS.setEnabled(true);}
        });
        this.setVisible(true); this.setResizable(false); this.pack(); this.setAlwaysOnTop(true);
    } //end constructor()
} //end DeleteScreen()
