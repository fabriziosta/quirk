package gui;


import socket.SocketSender;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

import static classes.CommonMethods.hashPassword;


public class TokenScreen extends JFrame {
    private JLabel imgLogo;
    private JTextField textToken;
    private JLabel lblToken;
    private JLabel lblPassword;
    private JLabel lblRepeatNewPassword;
    private JButton btnCancel;
    private JButton btnConfirm;
    private JPanel pnlRoot;
    private JPasswordField textPassword;
    private JPasswordField textRepeatPassword;
    private Border oldBorder;

    TokenScreen(PasswordScreen myPasswordScreen) {
        super("Quirk IM - Enter your Token"); this.setSize(380, 420);  this.setLocationRelativeTo(myPasswordScreen);
        setContentPane(pnlRoot);
        super.setIconImage(new ImageIcon(getClass().getResource("/img/icon.png")).getImage());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        oldBorder = textToken.getBorder();//Save the dafault color of border
        setVisible(true); setResizable(false); this.pack();

        btnCancel.addActionListener(e -> { dispose();  });

        btnConfirm.addActionListener(e -> {
            String token = textToken.getText();
            String password = String.valueOf(textPassword.getPassword());
            String repeatPassword = String.valueOf(textRepeatPassword.getPassword());
            String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";

            if (token.length()==32){
                textToken.setBorder(oldBorder);
                if (password.length()>=8 && password.length()<=16) {
                    textPassword.setBorder(oldBorder);
                    if (password.matches(passwordPattern)){
                        textPassword.setBorder(oldBorder);
                        if (password.equals(repeatPassword)){

                            String email = new SocketSender().queryTheServer("get_email_token-" + token);
                            new SocketSender().queryTheServer("update_token-0-" + email);
                            new SocketSender().queryTheServer("update_password-" + hashPassword(password) + "-" + email);

                        }
                        else {
                            textRepeatPassword.setBorder(BorderFactory.createLineBorder(Color.red));
                            JOptionPane.showMessageDialog(null, "Passwords do not match!");
                        }
                    }
                    else {
                        textPassword.setBorder(BorderFactory.createLineBorder(Color.red));
                        JOptionPane.showMessageDialog(null, "Use *[0-9], *[a-z], *[A-Z], *[@#$%^&+=]!");
                    }
                }
                else {
                    textPassword.setBorder(BorderFactory.createLineBorder(Color.red));
                    JOptionPane.showMessageDialog(null, "Passwords 8-16 characters!");
                }
            }
            else {
                textToken.setBorder(BorderFactory.createLineBorder(Color.red));
                JOptionPane.showMessageDialog(null, "Insert your valid token!");
            }
        });
    }
}
