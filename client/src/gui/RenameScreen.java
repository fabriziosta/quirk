package gui;

import socket.SocketSender;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class RenameScreen extends JDialog {
    private JPanel pnlRename;
    private JTextField textField;
    private JButton btnOk;

    RenameScreen(HomeScreen HS, String id, String friend_id){
        this.setSize(200, 120); setContentPane(pnlRename); this.setLocationRelativeTo(HS);
        SwingUtilities.getRootPane(btnOk).setDefaultButton(btnOk); setAlwaysOnTop(true);
        super.setIconImage(new ImageIcon(getClass().getResource("/img/icon.png")).getImage());
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE); HS.setEnabled(false);

        btnOk.addActionListener(e -> {
            new SocketSender().queryTheServer("update_custom_name1-" + friend_id +"-"+ id + "-" + textField.getText());
            new SocketSender().queryTheServer("update_custom_name2-" + id +"-"+ friend_id + "-" + textField.getText());
            HS.setEnabled(true); dispose();
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {HS.setEnabled(true);}
        });

        this.setVisible(true); this.setResizable(false); this.pack();
    } //end Constructor()

} //end RenameScreen()
