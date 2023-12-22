package gui;

import socket.SocketSender;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static classes.CommonMethods.GUIadapter;

public class DeleteUserScreen extends JFrame{
    private JButton NOButton;
    private JButton YESButton;
    private JPanel pnlDelete;
    public JList listUser;
    public String group_id;
    public String channel_id;
    public String group_or_channel_id;

    //Models for Jlists
    public DefaultListModel usersModel = new DefaultListModel();

    DeleteUserScreen(HomeScreen HS, String type, String id){
        System.out.println("type: "+type);
        System.out.println("ID: "+id);
        group_or_channel_id = id;
        this.setSize(500, 500); this.setLocationRelativeTo(HS); setContentPane(pnlDelete);
        setAlwaysOnTop(true); HS.setEnabled(false);
        super.setIconImage(new ImageIcon(getClass().getResource("/img/icon.png")).getImage());
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        //listUser.addMouseListener((MouseListener) this);
        //listUser.addListSelectionListener((ListSelectionListener) this);

        if(type.equals("group")){ //Load Group Users List!
            String group_users_list = new SocketSender().queryTheServer("retrieve_group_users-" + group_or_channel_id);
            String group_users[] = group_users_list.split(";");
            String group_user_name ="";
            usersModel.removeAllElements();

            if(group_users.length > 1){
                //int i=2 because i=1 is the admin!
                for (int i = 1; i < group_users.length; i++){
                    String lastChar = String.valueOf(group_users[i].charAt(group_users[i].length()-1));
                    if(lastChar.equals("?")){
                        group_user_name = new SocketSender().queryTheServer("get_user_username_by_ID-"+group_users[i].substring(0,group_users[i].length()-1));
                        usersModel.addElement(group_user_name + " - Pending Request!");
                    }else{
                        group_user_name = new  SocketSender().queryTheServer("get_user_username_by_ID-" + group_users[i]);
                        usersModel.addElement(group_user_name);
                    }
                }
                listUser.setModel(usersModel);
            }
        }
        else if(type.equals("channel")){ //Load Channel USER List!
            String channel_users_list = new SocketSender().queryTheServer("retrieve_channel_users-" + group_or_channel_id);
            String channel_users[] = channel_users_list.split(";");
            String channel_user_name ="";
            usersModel.removeAllElements();

            if(channel_users.length > 1){
                //int i=2 because i=1 is the admin!
                for (int i = 1; i < channel_users.length; i++){
                    String lastChar = String.valueOf(channel_users[i].charAt(channel_users[i].length()-1));
                    if(lastChar.equals("?")){
                        channel_user_name = new SocketSender()
                                .queryTheServer("get_user_username_by_ID-"+channel_users[i].substring(0,channel_users[i].length()-1));
                        usersModel.addElement(channel_user_name + " - Pending Request!");
                    }else{
                        channel_user_name = new  SocketSender()
                                .queryTheServer("get_user_username_by_ID-" + channel_users[i]);
                        usersModel.addElement(channel_user_name);
                    }
                }
                listUser.setModel(usersModel);
            }
        }

        YESButton.addActionListener(e -> {

            if(listUser.getSelectedValue() != null){ //delete friend
                if(type.equals("group"))new SocketSender().queryTheServer("delete_user_from_group-" + id +"-"+ listUser.getSelectedValue());
                else if(type.equals("channel"))new SocketSender().queryTheServer("delete_user_from_channel-" + id +"-"+ listUser.getSelectedValue());

                usersModel.removeElementAt(listUser.getSelectedIndex());
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

        listUser.addListSelectionListener(e -> {
            System.out.println(listUser.getSelectedIndex());
            System.out.println(listUser.getSelectedValue());
        });
    } //end constructor()
} //end DeleteUserScreen()
