package gui;

import socket.SocketSender;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CreateGroupChannelScreen extends JDialog{
    private JPanel pnlCreate;
    private JButton btnCreate;
    private JTextField txtInsert;
    private JLabel lblCreate;

    CreateGroupChannelScreen(HomeScreen HS, String groupORchannel) {
        this.setLocationRelativeTo(HS); setAlwaysOnTop(true); HS.setEnabled(false);
        setContentPane(pnlCreate); SwingUtilities.getRootPane(btnCreate).setDefaultButton(btnCreate);
        super.setIconImage(new ImageIcon(getClass().getResource("/img/icon.png")).getImage());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        if(groupORchannel.equals("Group")) lblCreate.setText("New Group:");
        else lblCreate.setText("New Channel:");

        btnCreate.addActionListener(e -> {
            String name = txtInsert.getText();
            Integer alreadyExist = 0;
            String response = "";
            String admin_id = HS.user_id;

            if(!name.isEmpty() && name.length() > 2){
                if(groupORchannel.equals("Group")) alreadyExist = Integer.valueOf(new SocketSender().queryTheServer("check_group-" + name));
                else if(groupORchannel.equals("Channel")) alreadyExist = Integer.valueOf(new SocketSender().queryTheServer("check_channel-" + name));

                if(alreadyExist != 1){
                    if(groupORchannel.equals("Group")) response = new SocketSender().queryTheServer("create_group-" + admin_id + "-" + name);
                    else if(groupORchannel.equals("Channel")) response = new SocketSender().queryTheServer("create_channel-" + admin_id + "-" + name);
                    JOptionPane.showMessageDialog(null, response);

                    //Now, before closing this window, append group or channel into the user row to remember that he is inside that group or channel.
                    if(groupORchannel.equals("Group")) {
                        HS.groupModel.addElement(name);
                        HS.listGroups.setModel(HS.groupModel);
                    }
                    else if(groupORchannel.equals("Channel")) {
                        HS.channelModel.addElement(name);
                        HS.listChannels.setModel(HS.channelModel);
                    }
                    HS.setEnabled(true); dispose();

                }else{JOptionPane.showMessageDialog(null, groupORchannel + " already exists!");}
            }else{JOptionPane.showMessageDialog(null, groupORchannel + " cannot be named with less than 2 characters!");}
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {HS.setEnabled(true);}
        });
        setVisible(true); setResizable(false); this.pack();
    }
}
