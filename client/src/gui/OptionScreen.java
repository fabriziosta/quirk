package gui;

import socket.SocketListener;
import socket.SocketSender;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static classes.CommonMethods.hashPassword;

public class OptionScreen extends JFrame implements ActionListener{
    private JTabbedPane tabbedPane;
    private JPanel pnlOption;
    private JPanel tabPorts;
    private JLabel lblPort2;
    private JLabel lblPort1;
    private JTextField txtPort2;
    private JTextField txtPort1;
    private JButton btnCancel;
    private JButton btnOk;
    private JButton btnApply;
    private JPanel tabProfile;
    private JButton btnCancel2;
    private JButton btnOk2;
    private JTextField Fname;
    private JTextField Lname;
    private JButton btnApply2;
    private JPanel tabPassword;
    private JButton btnCancel3;
    private JButton btnOk3;
    private JButton btnApply3;
    private JPasswordField Opass;
    private JPasswordField Npass;
    private JPasswordField Rpass;
    private JLabel lblName;
    private JLabel lblSurname;
    private JLabel lblOPass;
    private JLabel lblNPass;
    private JLabel lblRPass;
    private Border oldBorder = txtPort1.getBorder();//Save the dafault color of border;
    private int changeApply;

    private HomeScreen HS;
    private SocketListener SL;
    //-----------------------------------------------------------------------------------
    OptionScreen(HomeScreen homeScreen, SocketListener socketListener){
        super("Qwirk IM"); this.setSize(1100, 635); this.setLocationRelativeTo(homeScreen);
        setContentPane(pnlOption); super.setIconImage(new ImageIcon(getClass().getResource("/img/icon.png")).getImage());

        btnCancel.addActionListener(this); btnCancel2.addActionListener(this); btnCancel3.addActionListener(this);
        btnOk.addActionListener(this); btnOk2.addActionListener(this); btnOk3.addActionListener(this);
        btnApply.addActionListener(this); btnApply2.addActionListener(this); btnApply3.addActionListener(this);

        this.setVisible(true); this.setResizable(false); this.pack(); this.setAlwaysOnTop(true);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.HS = homeScreen; this.SL = socketListener; HS.setEnabled(false);
        txtPort1.setText(new SocketSender().queryTheServer("get_port1-" + HS.user_id));
        txtPort2.setText(new SocketSender().queryTheServer("get_port2-" + HS.user_id));

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {HS.setEnabled(true);}
        });
    }
    //-----------------------------------------------------------------------------------
    @Override
    public void actionPerformed(ActionEvent e){
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";

        if(e.getSource() == btnCancel || e.getSource() == btnCancel2 || e.getSource() == btnCancel3){
            HS.setEnabled(true); dispose();
        }else if(e.getSource() == btnOk || e.getSource() == btnApply){
            //1.Update user ports
            String port1 = txtPort1.getText();
            String port2 = txtPort2.getText();
            if(!port1.equals("")) {
                new SocketSender().queryTheServer("update_port1-" + txtPort1.getText() +"-"+ HS.user_email);
                SL.setUser_port1(Integer.parseInt(port1));
            }
            if(!port2.equals("")) {
                new SocketSender().queryTheServer("update_port2-" + txtPort2.getText() +"-"+ HS.user_email);
                SL.setUser_port2(Integer.parseInt(port2));
            }
            //2.Restart SocketListener to receive notification from other user without the need to restart full application.
            if(!port1.equals("") || !port2.equals("")){
                SL.getUserTaskThread().interrupt();
                SL.setUserTaskThread(new Thread(SL.getUserTask()));
                SL.getUserTaskThread().start();
            }

        }else if(e.getSource() == btnOk2 || e.getSource() == btnApply2){
            new SocketSender().queryTheServer("update_fname-" + Fname.getText() + "-" + HS.user_email);
            new SocketSender().queryTheServer("update_lname-" + Lname.getText() + "-" + HS.user_email);
        }

        if(e.getSource() == btnOk) {HS.setEnabled(true); dispose();}
        else if(e.getSource() == btnOk2) {
            HS.lblName.setText(Fname.getText() +" "+Lname.getText());
            HS.setEnabled(true); dispose();
        }

        //Do same thing for Apply button 3 without dispose()
        else if(e.getSource() == btnOk3){
            String oPass = String.valueOf(Opass.getPassword());
            String nPass = String.valueOf(Npass.getPassword());
            String rPass = String.valueOf(Rpass.getPassword());

            //I don't care about old password pattern
            if(changeApply != 1) {
                if (new SocketSender().queryTheServer("check_password-" + HS.user_email + "-" + hashPassword(oPass)).equals("1")) {
                    Opass.setBorder(oldBorder);
                    if (nPass.length() >= 8 && nPass.length() <= 16 && nPass.matches(PASSWORD_PATTERN)) {
                        Npass.setBorder(oldBorder);
                        if (nPass.equals(rPass)) {
                            new SocketSender().queryTheServer("update_password-" + hashPassword(nPass) + "-" + HS.user_email);
                            HS.setEnabled(true); dispose();
                        } else {
                            JOptionPane.showMessageDialog(null, "Passwords does not match!");
                            Npass.setBorder(BorderFactory.createLineBorder(Color.red));
                            Rpass.setBorder(BorderFactory.createLineBorder(Color.red));
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Passwords must be 8-16 chars and use *[0-9], *[a-z], *[A-Z], *[@#$%^&+=]!");
                        Npass.setBorder(BorderFactory.createLineBorder(Color.red));
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Wrong Old Password!");
                    Opass.setBorder(BorderFactory.createLineBorder(Color.red));
                }
            }
            else HS.setEnabled(true); dispose();
        } //end btnOk3
        else if(e.getSource() == btnApply3){
            String oPass = String.valueOf(Opass.getPassword());
            String nPass = String.valueOf(Npass.getPassword());
            String rPass = String.valueOf(Rpass.getPassword());

            //Non mi interessa sapere se la vecchia password rispetta il pattern
            if(new SocketSender().queryTheServer("check_password-" + HS.user_email + "-" + hashPassword(oPass)).equals("1")){
                Opass.setBorder(oldBorder);
                if (nPass.length() >= 8 && nPass.length() <= 16 && nPass.matches(PASSWORD_PATTERN)) {
                    Npass.setBorder(oldBorder);
                    if (nPass.equals(rPass)){
                        new SocketSender().queryTheServer("update_password-" + hashPassword(nPass) + "-" + HS.user_email);
                        changeApply = 1;
                    }else {
                        JOptionPane.showMessageDialog(null, "Passwords does not match!");
                        Npass.setBorder(BorderFactory.createLineBorder(Color.red));
                        Rpass.setBorder(BorderFactory.createLineBorder(Color.red));
                    }
                }else {
                    JOptionPane.showMessageDialog(null, "Passwords must be 8-16 chars and use *[0-9], *[a-z], *[A-Z], *[@#$%^&+=]!");
                    Npass.setBorder(BorderFactory.createLineBorder(Color.red));
                }
            }else {
                JOptionPane.showMessageDialog(null, "Wrong Old Password!");
                Opass.setBorder(BorderFactory.createLineBorder(Color.red));
            }
        } //end btnApply3

    } //end ActionPerformed()
    //-----------------------------------------------------------------------------------
} //end OptionScreen class
