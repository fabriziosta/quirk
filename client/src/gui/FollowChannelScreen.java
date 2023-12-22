package gui;

import socket.SocketSender;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class FollowChannelScreen extends JDialog {
    private JPanel pnlFollow;
    private JTextField txtFollow;
    private JButton btnFollow;
    private JLabel lblFollow;

    FollowChannelScreen(HomeScreen HS) {
        this.setLocationRelativeTo(HS); setAlwaysOnTop(true); HS.setEnabled(false);
        setContentPane(pnlFollow); SwingUtilities.getRootPane(btnFollow).setDefaultButton(btnFollow);
        super.setIconImage(new ImageIcon(getClass().getResource("/img/icon.png")).getImage());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        btnFollow.addActionListener(e -> {
            String name = txtFollow.getText();

            if(!name.isEmpty() && name.length() > 2){
                if(new SocketSender().queryTheServer("check_channel-" + name).equals("1")){
                    String response = new SocketSender().queryTheServer("follow_channel-" + name + "-" + HS.user_id);
                    if(response.equals("User is already following this channel!")) JOptionPane.showMessageDialog(null, response);
                    else HS.channelModel.addElement(name); HS.listChannels.setModel(HS.channelModel); //Append channel to Jlist
                    HS.setEnabled(true); dispose();
                }else{JOptionPane.showMessageDialog(null, "We are sorry, channel does not exist!");}
            }else{JOptionPane.showMessageDialog(null, "Channels cannot be names less than 2 characters!");}
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {HS.setEnabled(true);}
        });
        setVisible(true); setResizable(false); this.pack();
    }
}
