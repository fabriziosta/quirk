package gui;

import classes.CommonMethods;
import socket.SocketSender;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static classes.CommonMethods.hashPassword;

public class RegisterScreen extends JFrame implements ActionListener {
    private JPanel rootRegister;
    private JButton btnConfirm;
    private JLabel lblUsername;
    private JTextField textUsername;
    private JLabel lblPassword;
    private JPasswordField textPassword;
    private JLabel lblRepeatPassword;
    private JPasswordField textRepeatPassword;
    private JButton btnBack;
    private JLabel lblEmail;
    private JTextField textEmail;
    private JLabel imgLogo;
    private JTextField textSurname;
    private JTextField textName;
    private JButton btnBrowse;
    private JLabel lblProfilePicture;
    private JLabel lblName;
    private JLabel lblSurname;
    private JLabel lblFileLoaded;
    private JLabel lblFile;

    private String pathImg;
    private String ImageName = "";
    private JFileChooser file;

    RegisterScreen() {
        super("Quirk IM - Registration"); this.setSize(380, 680); this.setLocationRelativeTo(null);
        setContentPane(rootRegister); SwingUtilities.getRootPane(btnConfirm).setDefaultButton(btnConfirm);
        super.setIconImage(new ImageIcon(getClass().getResource("/img/icon.png")).getImage());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        btnConfirm.addActionListener(this); btnBack.addActionListener(this); btnBrowse.addActionListener(this);
        setVisible(true); setResizable(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";

        if (e.getSource() == btnBack) {
            this.dispose(); new LandingScreen();
        } else if (e.getSource() == btnConfirm) {
            String name = textName.getText();
            String surname = textSurname.getText();
            String email = textEmail.getText().toLowerCase();
            String username = textUsername.getText();
            String password = String.valueOf(textPassword.getPassword());
            String repeatpassword = String.valueOf(textRepeatPassword.getPassword());
            String usernameExist = "";
            String emailExist = "";

            //If username is not empty query the server.
            if(!username.isEmpty()) usernameExist = new SocketSender().queryTheServer("check_username-" + username);
            //If email is not empty query the server.
            if(!email.isEmpty()) emailExist = new SocketSender().queryTheServer("check_email-" + email);

            if(usernameExist.equals("0")){
                if (email.matches(EMAIL_PATTERN) && emailExist.equals("0")){
                    if (password.length() >= 8 && password.length() <= 16 && password.matches(PASSWORD_PATTERN)){
                        if (password.equals(repeatpassword)){
                            if(!ImageName.isEmpty()) {

                                //Register a new user!
                                new SocketSender().queryTheServer("set_user-" + name + "-" + surname + "-" + email + "-" + username + "-" + hashPassword(password));
                                //Now, open the new profile picture and append at EOF important information that I need to send to the server.
                                try {
                                    FileOutputStream output = new FileOutputStream(pathImg, true);
                                    output.write(("@QWIRK@" + ImageName + "@@@" + username + "@@@1").getBytes()); output.close();
                                }catch(IOException e1) {e1.printStackTrace();}
                                new SocketSender().sendFile(pathImg);
                                JOptionPane.showMessageDialog(null, "User registered successfully!");
                                this.dispose(); new LandingScreen();

                            }else JOptionPane.showMessageDialog(null, "You must upload an image picture!");
                        }else JOptionPane.showMessageDialog(null, "Passwords does not match!");
                    }else JOptionPane.showMessageDialog(null, "Passwords must be 8-16 chars and use *[0-9], *[a-z], *[A-Z], *[@#$%^&+=]!");
                }else JOptionPane.showMessageDialog(null, "Email already exists or wrong!");
            }else JOptionPane.showMessageDialog(null, "Username already exists or wrong!");
        }else if (e.getSource() == btnBrowse) {
            file = new JFileChooser();
            file.setCurrentDirectory(new File(System.getProperty("user.home")));
            file.addChoosableFileFilter(new FileNameExtensionFilter("*.Image", "jpg", "png"));

            if (file.showSaveDialog(this) == JFileChooser.APPROVE_OPTION){
                pathImg = file.getSelectedFile().getAbsolutePath();
                ImageName = new File(pathImg).getName();

                try {
                    //Reduce img and maintain proportions
                    lblFile.setIcon(new ImageIcon(new CommonMethods().createResizedCopy(ImageIO.read(new File(pathImg)), 80, false)));
                } catch (IOException ignored) {}
            }

        } //end else if btnBrowse
        //-----------------------------------------------------------------------------------
    } //end ActionPerformed()
} //end RegisterScreen()
