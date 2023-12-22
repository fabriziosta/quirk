package gui;

import socket.SocketListener;
import socket.SocketSender;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static classes.CommonMethods.hashPassword;


public class LandingScreen extends JFrame implements ActionListener {
    private JPanel rootLanding;
    private JButton btnLogin;
    private JTextField textEmail;
    private JPasswordField textPassword;
    private JButton btnSignup;
    private JEditorPane lblForgotPassword;
    private JLabel imgLogo;
    private JLabel lblPassword;
    private JLabel lblUsername;
    private Border defaultBorder;

    private String entered_email, entered_password;

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) UIManager.setLookAndFeel(info.getClassName()); break;
            }
        } catch (Exception e) {
            try {UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());}
            catch (Exception ignored) {}
        }
        new LandingScreen();
    }

    LandingScreen() {
        super("Quirk IM - Login form");
        super.setIconImage(new ImageIcon(getClass().getResource("/img/icon.png")).getImage());
        this.setSize(380, 680); this.setLocationRelativeTo(null);
        setContentPane(rootLanding); SwingUtilities.getRootPane(btnLogin).setDefaultButton(btnLogin);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        defaultBorder = textEmail.getBorder();
        lblForgotPassword.setText("<HTML><a href=''>Forgot your password? Click here</a></HTML>");
        lblForgotPassword.addHyperlinkListener(e -> { });
        btnLogin.addActionListener(this); btnSignup.addActionListener(this);
        lblForgotPassword.addMouseListener(new MouseListener() {
            @Override public void mouseClicked(MouseEvent e) { dispose(); new PasswordScreen(); }
            @Override public void mousePressed(MouseEvent e) {}
            @Override public void mouseReleased(MouseEvent e) {}
            @Override public void mouseEntered(MouseEvent e) {}
            @Override public void mouseExited(MouseEvent e) {}
        });
        setVisible(true); setResizable(false);

        textEmail.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) {
                changeBtnStatus();
            }
            @Override public void removeUpdate(DocumentEvent e) {
                changeBtnStatus();
            }
            @Override public void changedUpdate(DocumentEvent e) {
                changeBtnStatus();
            }
        });
        textPassword.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) {
                changeBtnStatus();
            }
            @Override public void removeUpdate(DocumentEvent e) {
                changeBtnStatus();
            }
            @Override public void changedUpdate(DocumentEvent e){ changeBtnStatus(); }
        });
    } //end Landing Screen()

    @Override
    public void actionPerformed(ActionEvent e) {
        final String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        if (e.getSource() == btnLogin) {
            entered_email = textEmail.getText().toLowerCase();
            entered_password = String.valueOf(textPassword.getPassword());
            String isEmailCorrect = new SocketSender().queryTheServer("check_email-" + entered_email);

            //If the email is valid and in the DB.
            if (entered_email.matches(emailPattern) && isEmailCorrect.equals("1")){
                String isPasswordCorrect = new SocketSender().queryTheServer("check_password-" + entered_email + "-" + hashPassword(entered_password));
                String isIPtheSame = new SocketSender().queryTheServer("check_ip-" + entered_email); //1 if Ip is the same in the DB, 0 if not.
                String amIBanned = new SocketSender().queryTheServer("amIBanned-" + entered_email); //1 if banned, 0 if not.
                textEmail.setBorder(defaultBorder);

                if(isPasswordCorrect.equals("0") && isIPtheSame.equals("0")){
                    textPassword.setBorder(BorderFactory.createLineBorder(Color.red));
                    JOptionPane.showMessageDialog(null, "Wrong Password!");
                    new SocketSender().queryTheServer("reset_count_tries-" + entered_email);
                    new SocketSender().queryTheServer("update_ip_by_email-" + entered_email);
                }else if(isPasswordCorrect.equals("0") && isIPtheSame.equals("1") && amIBanned.equals("0")){
                    textPassword.setBorder(BorderFactory.createLineBorder(Color.red));
                    JOptionPane.showMessageDialog(null, "Wrong Password!");
                    new SocketSender().queryTheServer("update_count_tries-" + entered_email);
                }else if((isPasswordCorrect.equals("0") || isPasswordCorrect.equals("1")) && isIPtheSame.equals("1") && amIBanned.equals("1")) {
                    int kindOfBAN = Integer.parseInt(new SocketSender().queryTheServer("get_count_tries-" + entered_email));
                    if(kindOfBAN > 9 && kindOfBAN < 20) JOptionPane.showMessageDialog(null, "You are banned for 5 minutes.");
                    else if(kindOfBAN > 19 && kindOfBAN < 30) JOptionPane.showMessageDialog(null, "You are banned for 1 week.");
                    else if(kindOfBAN > 29) JOptionPane.showMessageDialog(null, "You are banned for 2 months.");
                }else if(isPasswordCorrect.equals("1") && amIBanned.equals("0")){
                    // 1. UPDATE my IP, read port1 and port2 in the server.
                    String user_id = new SocketSender().queryTheServer("get_user_id-" + entered_email);
                    new SocketSender().queryTheServer("update_ip_by_email-" + entered_email);
                    // 2. Creating GUI for HomeScreen()!
                    new HomeScreen(user_id, entered_email);
                    // 2.5 Reset count_tries to 0 because user logged successfully and I don't want that he gets banned next times.
                    new SocketSender().queryTheServer("reset_count_tries-" + entered_email);
                    dispose();
                }
            }else{
                textEmail.setBorder(BorderFactory.createLineBorder(Color.red));
                JOptionPane.showMessageDialog(null, "Email not valid!");
            }
        //------------------------------------------------
        }else if (e.getSource() == btnSignup){dispose(); new RegisterScreen();}
    } //end ActionPerformed()
    //----------------------------------------------------------------------------------
    private void changeBtnStatus() {
        if (textEmail.getText().isEmpty() || textPassword.getPassword().length < 1) btnLogin.setEnabled(false);
        else btnLogin.setEnabled(true);
    }
    //----------------------------------------------------------------------------------
} //end LandingScreen()