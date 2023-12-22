package gui;

import classes.CommonMethods;
import socket.SocketListener;
import socket.SocketSender;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static classes.CommonMethods.*;

public class HomeScreen extends JFrame implements ActionListener, MouseListener, ListSelectionListener, KeyListener{
    private JPanel pnlRoot; private JPanel pnlEast;
    private JPanel pnlWest; private JPanel pnlCenter;
    private JLabel lblSetting; private JLabel lblLogout;
    private JLabel lblAvatar;
    private JPanel pnlWestNorth;
    private JComboBox cBoxStatus;
    private JPanel pnlWestCenter;
    private JButton btnAddFriend; private JButton btnCreateGroup;
    private JButton btnCreateChannel; private JButton btnFollowChannel;
    public JLabel lblAvatar2; public JLabel lblName;
    public JLabel lblName2; public JLabel lblStatus2;
    public JLabel lblCall; public JLabel lblVideoCall;
    private JPanel pnlCenterTop; private JPanel pnlCenterSouth;
    public JButton btnEmoticons; public JButton btnSend;
    public JTextPane txtAreaSEND; public JTextPane txtAreaCHAT;
    private JPanel pnlWestSouth;
    public JList listFriends; public JLabel lblFriends;
    public JLabel lblGroups; public JList listGroups;
    public JLabel lblChannels; public JList listChannels;
    public JLabel lblUsername;
    public JLabel lblPlus; public JLabel lblMinus;
    public JLabel lblCustomName;

    //Logged user information.
    public String user_id = null;
    public String user_fname = null; public String user_lname = null;
    public String user_email = null; public String user_username = null;
    public String user_status = null; public String user_picture = null;
    //Loaded friend information.
    public String friend_id = null;
    public String friend_fname = null; public String friend_lname = null;
    public String friend_email = null; public String friend_username = null;
    public String friend_status = null;
    public String friend_ip = null;
    public String friend_port1 = null; public String friend_port2 = null;
    public String friend_custom_name = null;
    public String friend_picture = null;
    //Loaded group information.
    public String group_id = null; public String group_adminID = null;
    public String group_admin_username = null;
    public String group_name = null;
    //Loaded channel information.
    public String channel_id = null; public String channel_admin_username = null;
    public String channel_adminID = null;
    public String channel_name = null;

    //Models for Jlists
    public DefaultListModel friendModel = new DefaultListModel();
    public DefaultListModel groupModel = new DefaultListModel();
    public DefaultListModel channelModel = new DefaultListModel();
    private JPopupMenu friendContextMenu = new JPopupMenu();
    private JPopupMenu groupContextMenu = new JPopupMenu();
    private JPopupMenu channelContextMenu = new JPopupMenu();
    private JMenuItem renameItem = new JMenuItem("Rename");
    private JMenuItem blockItem = new JMenuItem("Block");
    private JMenuItem deleteItem = new JMenuItem("Delete");
    private JMenuItem leaveGroupItem = new JMenuItem("Leave group");
    private JMenuItem leaveChannelItem = new JMenuItem("Leave channel");
    private JMenuItem addItem = new JMenuItem("Add Member"); //only for groups
    //Variables to contain chats.
    private String cleanText; private String fullChat;

    public Boolean isCallStarted = false;
    private CallScreen CS = null;
    //Manage DragNDrop
    public DropTarget dt;
    private String extFile = "";
    private String pathFILE = "";

    private SocketListener socketListener;
    //-----------------------------------------------------------------------------------
    HomeScreen(String id, String email){
        super("Quirk IM"); this.setSize(1100, 635); this.setLocationRelativeTo(null); setContentPane(pnlRoot);
        super.setIconImage(new ImageIcon(getClass().getResource("/img/icon.png")).getImage());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);
        lblLogout.setCursor(handCursor); lblSetting.setCursor(handCursor); lblCall.setCursor(handCursor);
        lblVideoCall.setCursor(handCursor); lblPlus.setCursor(handCursor); lblMinus.setCursor(handCursor);

        //ActionListener
        btnSend.addActionListener(this); btnEmoticons.addActionListener(this); btnAddFriend.addActionListener(this);
        btnCreateGroup.addActionListener(this); btnCreateChannel.addActionListener(this); btnFollowChannel.addActionListener(this);
        renameItem.addActionListener(this); deleteItem.addActionListener(this); blockItem.addActionListener(this);
        addItem.addActionListener(this); leaveGroupItem.addActionListener(this); leaveChannelItem.addActionListener(this);
        //KeyListener
        txtAreaSEND.addKeyListener(this); cBoxStatus.addKeyListener(this);
        //MouseListener
        lblLogout.addMouseListener(this); lblCall.addMouseListener(this); lblVideoCall.addMouseListener(this); lblSetting.addMouseListener(this);
        listFriends.addMouseListener(this); listGroups.addMouseListener(this); listChannels.addMouseListener(this);
        lblPlus.addMouseListener(this); lblMinus.addMouseListener(this);
        //ListSelectionListener
        listFriends.addListSelectionListener(this); listGroups.addListSelectionListener(this); listChannels.addListSelectionListener(this);
        //WindowListener - Used to delete ip from DB or if I log in with another account I will send to myself the messages!
        addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e) {new SocketSender().queryTheServer("delete_ip-" + user_id);}
        });
        //HyperLinkListerner - used to show hyperlink on chat!
        txtAreaCHAT.addHyperlinkListener(e -> {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                try { //if there aren't errors, It is a normal link!
                    Desktop.getDesktop().browse(e.getURL().toURI());
                } catch (NullPointerException NPE) { //if there is NPE errors, It is a file!
                    String href = e.getDescription(); //Here I will have file_ID!!! finally
                    new SocketSender().receiveFile(user_id, "0", href);

                    JOptionPane.showMessageDialog(null,"File downloaded successfully!");
                } catch (Exception e2) {JOptionPane.showMessageDialog(null,"Cannot open this link!\n"+ e2.getMessage());}
            }
        });

        //DragANDDrop Listener ----
        dt = new DropTarget(){
            public synchronized void drop(DropTargetDropEvent e){
                try{
                    e.acceptDrop(DnDConstants.ACTION_COPY);
                    java.util.List<File> droppedFiles = (java.util.List<File>) e.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                    for (File file : droppedFiles){
                        if (file.exists() && file.length() <= 52428800){

                            pathFILE = file.getAbsolutePath();
                            int i = pathFILE.lastIndexOf('.');
                            if (i > 0) extFile = pathFILE.substring(i+1);
                            ImageIcon imageIcon = null;

                            if(extFile.equals("png") ||extFile.equals("jpg") ||extFile.equals("jpeg") ||extFile.equals("bmp")) {
                                imageIcon = new ImageIcon(new ImageIcon(
                                        file.getAbsolutePath()).getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH));

                            }else{ //if it's not a file show default icon for files.
                                imageIcon = new ImageIcon(new ImageIcon(
                                        getClass().getResource("/img/file.png")).getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH));
                            }
                            txtAreaSEND.setText("");
                            txtAreaSEND.insertIcon(imageIcon);

                            int response = JOptionPane.showConfirmDialog(null,
                                    "Send file?", "Confirm send request", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                            if (response == JOptionPane.NO_OPTION || response == JOptionPane.CLOSED_OPTION) txtAreaSEND.setText("");
                            else if (response == JOptionPane.YES_OPTION) { //SEND FILE to SERVER!
                                try { //Write info inside the file...
                                    FileOutputStream output = new FileOutputStream(pathFILE, true);
                                    output.write(("@QWIRK@" + file.getName() + "@@@" + user_username + "@@@0").getBytes()); output.close();
                                }catch(IOException e1) {e1.printStackTrace();}
                                String lastID = new SocketSender().sendFile(pathFILE); //send it!
                                //Let's prepare local chat to display link for downloading this file.
                                //If it is a picture...
                                if(extFile.equals("png") ||extFile.equals("jpg") ||extFile.equals("jpeg") ||extFile.equals("bmp")){
                                    pathFILE = "<a href='"+lastID+"'><img src='http://i.imgur.com/msCE1xp.png'> " + file.getName() + "</a>";
                                //if it is a file...
                                }else{
                                    pathFILE = "<a href='"+lastID+"'><img width=30 height=30 src='http://i.imgur.com/xP4dSag.png'> " + file.getName() + "</a>";
                                }
                                //Append this formatted text inside area chat AND, most important, send this new line message to the server.
                                new CommonMethods().append_file(pathFILE, txtAreaCHAT, user_username);
                                if(listFriends.getSelectedValue() != null){
                                    new SocketSender().sendMessage("send_new_message-" + user_id + "-" + friend_id + "-MESSAGE:" + pathFILE);
                                }else if(listGroups.getSelectedValue() != null){
                                    new SocketSender().sendMessage("send_new_group_message-" + group_id + "-" + user_username + "-MESSAGE:" + pathFILE);
                                }else if(listChannels.getSelectedValue() != null){
                                    new SocketSender().sendMessage("send_new_channel_message-" + channel_id + "-" + user_username + "-MESSAGE:" + pathFILE);
                                }
                                txtAreaSEND.setText(""); btnSend.setEnabled(false); txtAreaSEND.grabFocus();

                            } //end else if...
                        }else System.out.println("File does not exists or too big");
                    } //end for loop...
                }catch (Exception ex) {ex.printStackTrace();}
            } //end drop method...
        };
        txtAreaSEND.setDropTarget(null);

        //ComboBox Listener
        cBoxStatus.setSelectedItem(cBoxStatus.getItemAt(0));
        cBoxStatus.addActionListener(e -> {
            String status = (String)cBoxStatus.getSelectedItem();
            if (cBoxStatus.getSelectedIndex() == 4){
                cBoxStatus.setEditable(true);
                cBoxStatus.getEditor().getEditorComponent().addFocusListener(new FocusAdapter() {
                    @Override public void focusGained(FocusEvent e) {cBoxStatus.setEditable(true);}
                    @Override public void focusLost(FocusEvent e) {cBoxStatus.setEditable(false);}
                });
            }
            else{cBoxStatus.setEditable(false);}
            new SocketSender().queryTheServer("update_status-" + status + "-" + user_email);
        });
        // ********************
        friendContextMenu.add(renameItem); friendContextMenu.add(blockItem); friendContextMenu.add(deleteItem);
        groupContextMenu.add(addItem); groupContextMenu.add(leaveGroupItem);
        channelContextMenu.add(leaveChannelItem);
        setVisible(true); setResizable(false); this.pack();
        // --------------------
        //Getting all informations about the user.
        user_id = id;
        user_email = email;
        user_fname = new SocketSender().queryTheServer("get_name_by_EMAIL-" + user_email);
        user_lname = new SocketSender().queryTheServer("get_user_cognome-" + user_email);
        user_username = new SocketSender().queryTheServer("get_user_username-" + user_email);
        user_status = new SocketSender().queryTheServer("get_status-" + user_email);
        //SocketListener - create a thread that will listen for notification
        socketListener = new SocketListener(this);
        socketListener.startToListen();

        //SET PROFILE PICTURE
        try {
            user_picture = new SocketSender().receiveFile(user_id, "1", ""); //"1" because it's a profile picture that I want from server!
            lblAvatar.setIcon(new ImageIcon(new CommonMethods().createResizedCopy(ImageIO.read(new File(user_picture)), 80, false)));
        }catch (IOException e) {e.printStackTrace();}

        load_friend_list(this);
        load_group_list(this);
        load_channel_list(this);

        //Load User information in GUI.
        lblName.setText(user_fname + " " + user_lname); lblUsername.setText(user_username);
    }//-----------------------------------------------------------------------------------
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSend && !txtAreaSEND.getText().isEmpty()) {
            cleanText = txtAreaSEND.getText();
            new CommonMethods().append_new_message(cleanText, txtAreaCHAT, user_username);
            if(listFriends.getSelectedValue() != null){
                new SocketSender().sendMessage("send_new_message-" + user_id + "-" + friend_id + "-MESSAGE:" + cleanText);
            }else if(listGroups.getSelectedValue() != null){
                new SocketSender().sendMessage("send_new_group_message-" + group_id + "-" + user_username + "-MESSAGE:" + cleanText);
            }else if(listChannels.getSelectedValue() != null){
                new SocketSender().sendMessage("send_new_channel_message-" + channel_id + "-" + user_username + "-MESSAGE:" + cleanText);
            }
            txtAreaSEND.setText(""); btnSend.setEnabled(false); txtAreaSEND.grabFocus();
        }else if(e.getSource() == btnAddFriend) new AddPersonScreen(this, "friend");
        else if(e.getSource() == btnCreateGroup) new CreateGroupChannelScreen(this, "Group");
        else if(e.getSource() == btnCreateChannel) new CreateGroupChannelScreen(this, "Channel");
        else if(e.getSource() == btnFollowChannel) new FollowChannelScreen(this);
        else if(e.getSource() == btnEmoticons) new EmoticonScreen(this);
        else if(e.getSource() == renameItem) new RenameScreen(this, user_id, friend_id);
        else if(e.getSource() == deleteItem) new DeleteScreen(this);
        else if(e.getSource() == blockItem){
            new SocketSender().queryTheServer("block_user-" + friend_id + "-" + user_id);
            friendModel.setElementAt(friend_username + " - BLOCKED", listFriends.getSelectedIndex()); //Update GUI.
            listFriends.setModel(friendModel); //Update GUI.
            GUIadapter(this, "", 0); //Update GUI.
            txtAreaCHAT.setText("");
        }
        else if(e.getSource() == addItem && listGroups.getSelectedValue() != null) new AddPersonScreen(this, "group");
        else if(e.getSource() == leaveGroupItem) new DeleteScreen(this);
        else if(e.getSource() == leaveChannelItem) new DeleteScreen(this);
    }//-----------------------------------------------------------------------------------
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == lblLogout) {
            new SocketSender().queryTheServer("delete_ip-" + user_id);
            new LandingScreen(); dispose();
        }else if(e.getSource() == lblSetting) new OptionScreen(this, socketListener);
        else if(e.getSource() == lblCall && !isCallStarted){
            if (JOptionPane.showConfirmDialog(null,"Do you really want to call " + friend_username + "?",
                    "Confirm Audio Call", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                friend_ip = new SocketSender().queryTheServer("get_IP_ADDR-" + friend_id);
                if(!friend_ip.equals("0.0.0.0")) CS = new CallScreen(this, friend_id, "audio-caller");
                else JOptionPane.showMessageDialog(null,"Sorry, your friend " + friend_username + " is currently offline!");
            }
        }else if(e.getSource() == lblVideoCall && !isCallStarted){
            if (JOptionPane.showConfirmDialog(null,"Do you really want to VIDEO call " + friend_username + "?",
                    "Confirm Video Call", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                friend_ip = new SocketSender().queryTheServer("get_IP_ADDR-" + friend_id);
                if(!friend_ip.equals("0.0.0.0")) CS = new CallScreen(this, friend_id,"video-caller");
                else JOptionPane.showMessageDialog(null,"Sorry, your friend " + friend_username + " is currently offline!");
            }
        }else if(e.getSource() == lblPlus && listGroups.getSelectedValue() != null) new AddPersonScreen(this, "group");
        else if(e.getSource() == lblPlus && listChannels.getSelectedValue() != null) new AddPersonScreen(this, "channel");
        else if(e.getSource() == lblMinus && listGroups.getSelectedValue() != null) new DeleteUserScreen(this, "group", group_id);
        else if(e.getSource() == lblMinus && listChannels.getSelectedValue() != null) new DeleteUserScreen(this, "channel", channel_id);
    }//-----------------------------------------------------------------------------------
    @Override public void mousePressed(MouseEvent e) {}
    //-----------------------------------------------------------------------------------
    @Override
    public void mouseReleased(MouseEvent e) {
        int row;
        if (e.getSource() == listFriends && SwingUtilities.isRightMouseButton(e)){
            row = listFriends.locationToIndex(e.getPoint());
            listFriends.setSelectedIndex(row);
            friendContextMenu.show(e.getComponent(),e.getX(),e.getY());
        }else if(e.getSource() == listGroups && SwingUtilities.isRightMouseButton(e)){
            row = listGroups.locationToIndex(e.getPoint());
            listGroups.setSelectedIndex(row);
            groupContextMenu.show(e.getComponent(),e.getX(),e.getY());
        }else if(e.getSource() == listChannels && SwingUtilities.isRightMouseButton(e)){
            row = listChannels.locationToIndex(e.getPoint());
            listChannels.setSelectedIndex(row);
            channelContextMenu.show(e.getComponent(),e.getX(),e.getY());
        }
    }
    //-----------------------------------------------------------------------------------
    @Override public void mouseEntered(MouseEvent e) {    }
    //-----------------------------------------------------------------------------------
    @Override public void mouseExited(MouseEvent e) {    }
    //-----------------------------------------------------------------------------------
    @Override
    public void valueChanged(ListSelectionEvent e){
        String selected = "";

        //This line prevents double events AND prevent crash if no value selected.
        if (!e.getValueIsAdjusting()){

            if(e.getSource() == listFriends && listFriends.getSelectedValue() != null){
                selected = listFriends.getSelectedValue().toString();
                if(listGroups.getSelectedValue() != null) listGroups.clearSelection();
                if(listChannels.getSelectedValue() != null) listChannels.clearSelection();
                txtAreaSEND.setText("");
            }else if(e.getSource() == listGroups && listGroups.getSelectedValue() != null){
                selected = listGroups.getSelectedValue().toString();
                if(listFriends.getSelectedValue() != null) listFriends.clearSelection();
                if(listChannels.getSelectedValue() != null) listChannels.clearSelection();
                txtAreaSEND.setText("");
            }else if(e.getSource() == listChannels && listChannels.getSelectedValue() != null){
                selected = listChannels.getSelectedValue().toString();
                if(listFriends.getSelectedValue() != null) listFriends.clearSelection();
                if(listGroups.getSelectedValue() != null) listGroups.clearSelection();
                txtAreaSEND.setText("");
            }

            //Split the string because it could be a pending request! If it is a pending, [0] will be the "name" and [1] will be " - Pending Request"
            String[] myArray = selected.split(" - ");

            //If we are already friends, get all friend information
            if (e.getSource() == listFriends && myArray.length == 1 && listFriends.getSelectedValue() != null){
                friend_id = new SocketSender().queryTheServer("get_user_id_by_username-" + myArray[0]);
                friend_email = new SocketSender().queryTheServer("get_email-" + friend_id);
                friend_fname = new SocketSender().queryTheServer("get_name_by_EMAIL-" + friend_email);
                friend_lname = new SocketSender().queryTheServer("get_user_cognome-" + friend_email);
                friend_username = selected;
                friend_status = new SocketSender().queryTheServer("get_status-" + friend_email);
                friend_ip = new SocketSender().queryTheServer("get_IP_ADDR-" + friend_id);
                friend_port1 = new SocketSender().queryTheServer("get_port1-" + friend_id);
                friend_port2 = new SocketSender().queryTheServer("get_port2-" + friend_id);
                friend_custom_name = new SocketSender().queryTheServer("get_custom_name1-" + friend_id + "-" + user_id );
                if(friend_custom_name.equals("")) friend_custom_name = new SocketSender().queryTheServer("get_custom_name2-" + user_id + "-" + friend_id );

                //Get friend picture
                try {
                    friend_picture = new SocketSender().receiveFile(friend_id, "1", ""); //"1" because it's a profile picture!
                    lblAvatar2.setIcon(new ImageIcon(new CommonMethods().createResizedCopy(ImageIO.read(new File(user_picture)), 80, false)));
                }catch (IOException e2) {e2.printStackTrace();}

                //Set GUI now AND Enable txtAreaCHAT and get ready to chat!
                GUIadapter(this, "friend", 1);
                lblName2.setText(myArray[0] + "(" + friend_fname + " " + friend_lname + ")");
                lblStatus2.setText(friend_status); lblCustomName.setText(friend_custom_name);
                fullChat = new SocketSender().queryTheServer("get_full_chat-" + user_id + "-" + friend_id);
                txtAreaCHAT.setText(new CommonMethods().formatted_text(fullChat, user_username));

            //If there is a friend pending request or BLOCKED.
            }else if(e.getSource() == listFriends && myArray.length != 1 && listFriends.getSelectedValue() != null){

                //Set GUI
                GUIadapter(this, "friend", 0);
                //Let's start this new screen to accept or not this new friend!
                if(myArray[1].equals("Pending request!")) new AcceptRequestsScreen(this, myArray[0], "friend");
                else {
                    txtAreaCHAT.setText("<h1 style='color:white; text-align:center;'><b> USER BLOCKED. </b></h1>");
                    JOptionPane.showMessageDialog(this, "This user is blocked.");
                }

            //If group already joined
            }else if (e.getSource() == listGroups && myArray.length == 1 && listGroups.getSelectedValue() != null){

                group_name = listGroups.getSelectedValue().toString();
                group_id = new SocketSender().queryTheServer("get_group_ID_by_name-" + group_name);
                group_adminID = new SocketSender().queryTheServer("get_group_admin_id-" + group_name);
                //Set GUI now AND Enable txtAreaCHAT and get ready to chat!
                GUIadapter(this, "group", 1);
                lblName2.setText(group_name);
                fullChat = new SocketSender().queryTheServer("get_group_chat-" + group_id);
                txtAreaCHAT.setText(new CommonMethods().formatted_text(fullChat, user_username));

            //If there is a pending group request
            }else if (e.getSource() == listGroups && myArray.length != 1 && listGroups.getSelectedValue() != null){

                //Set GUI
                GUIadapter(this, "group", 0);
                //Let's start this new screen to accept or not this new group!
                new AcceptRequestsScreen(this, myArray[0], "group");

            //If channel already joined
            }else if (e.getSource() == listChannels && myArray.length == 1 && listChannels.getSelectedValue() != null){

                channel_name = listChannels.getSelectedValue().toString();
                channel_id = new SocketSender().queryTheServer("get_channel_ID_by_name-" + channel_name);
                channel_adminID = new SocketSender().queryTheServer("get_channel_adminID_by_name-" + channel_name);
                //Set GUI - if user_id == admin id then you can send messages, else you can only read!
                if(user_id.equals(channel_adminID)) GUIadapter(this, "channel", 1);
                else GUIadapter(this, "channel", 2);
                lblName2.setText(channel_name);
                fullChat = new SocketSender().queryTheServer("get_channel_chat-" + channel_id);
                txtAreaCHAT.setText(new CommonMethods().formatted_text(fullChat, user_username));

            //If there is a pending channel request
            }else if (e.getSource() == listChannels && myArray.length != 1 && listChannels.getSelectedValue() != null){

                //Set GUI
                GUIadapter(this, "channel", 0);
                //Let's start this new screen to accept or not this new channel!
                new AcceptRequestsScreen(this, myArray[0], "channel");
            }
        } //end getValueIsAdjusting
    } //end valueChanged()
    //-----------------------------------------------------------------------------------
    @Override public void keyTyped(KeyEvent e) {}
    //-----------------------------------------------------------------------------------
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getSource() == cBoxStatus && e.getKeyCode() == KeyEvent.VK_ENTER) cBoxStatus.setEditable(false);
        if(e.getSource() == txtAreaSEND && e.getKeyCode() == KeyEvent.VK_ENTER) btnSend.doClick();
    }//-----------------------------------------------------------------------------------
    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getSource() == txtAreaSEND && e.getKeyCode() == KeyEvent.VK_ENTER) txtAreaSEND.setText(null);
        if(e.getSource() == txtAreaSEND){
            if(txtAreaSEND.getText().isEmpty()) btnSend.setEnabled(false);
            else btnSend.setEnabled(true);
        }
    }//-----------------------------------------------------------------------------------
} //end HomeScreen
