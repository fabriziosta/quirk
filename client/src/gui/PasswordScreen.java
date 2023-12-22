package gui;

import socket.SocketSender;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PasswordScreen extends JFrame implements ActionListener {
    final private String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private JPanel rootPassword;
    private JButton btnConfirm;
    private JLabel imgLogo;
    private JLabel lblEmail;
    private JTextField textEmailForPass;
    private JButton btnToken;
    private JButton btnBack;
    private Border oldBorder;

    PasswordScreen(){
        super("Quirk IM - Password"); this.setSize(380, 420); this.setLocationRelativeTo(null);
        setContentPane(rootPassword);
        super.setIconImage(new ImageIcon(getClass().getResource("/img/icon.png")).getImage());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        oldBorder = textEmailForPass.getBorder();//Save the dafault color of border

        btnBack.addActionListener(this);
        setVisible(true); setResizable(false);
        btnConfirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = textEmailForPass.getText();

                if (email.matches(emailPattern)){
                    textEmailForPass.setBorder(oldBorder);
                    if (new SocketSender().queryTheServer("check_email-" + email.toLowerCase()).equals("1")){
                        textEmailForPass.setBorder(oldBorder);
                        new SocketSender().queryTheServer("send_email-" + email);
                    }else{
                        textEmailForPass.setBorder(BorderFactory.createLineBorder(Color.red));
                        JOptionPane.showMessageDialog(null, "Email not found!");
                    }
                }
                else{
                    textEmailForPass.setBorder(BorderFactory.createLineBorder(Color.red));
                    JOptionPane.showMessageDialog(null, "Insert a valid email");
                }

            } //end ActionPerformed()
        });

        btnToken.addActionListener(e -> new TokenScreen(this).setAlwaysOnTop(true));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnBack) {
            this.dispose();
            new LandingScreen();
        }
    }
} //end PasswordScreen()
