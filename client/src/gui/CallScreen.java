package gui;

import classes.AudioCall;
import classes.AudioCallServer;
import classes.CommonMethods;
import com.github.sarxos.webcam.Webcam;
import socket.SocketSender;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class CallScreen extends JFrame{
    private JPanel pnlCall;
    private JLabel lblAvatar1;
    private JButton btnClose;
    private JLabel lblAvatar2;
    private JLabel lblPerson1;
    private JLabel lblPerson2;
    private JButton btnMute;
    public AudioCall AC = null;
    public AudioCallServer ACS = null;

    private Socket sock = null;
    private boolean continueLoop = true;

    public boolean manageMicIcon = true; //Manage microphone
    //-----------------------------------------------------------------------------------
    //AUDIO CALL CONSTRUCTOR
    public CallScreen(HomeScreen HS,String friend_id, String type){
        super("Qwirk Conference"); this.setSize(900, 400); this.setLocationRelativeTo(null); setContentPane(pnlCall);
        super.setIconImage(new ImageIcon(getClass().getResource("/img/icon.png")).getImage());
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        lblPerson1.setText(HS.user_username);
        lblPerson2.setText(new SocketSender().queryTheServer("get_user_username_by_ID-" + friend_id));
        btnClose.addActionListener(e -> {
            continueLoop = false; //Stop video calls
            //Stop audio calls
            if(type.equals("audio-caller") || type.equals("video-caller"))          AC.continueLoop = false;
            else if(type.equals("audio-receiver") || type.equals("video-receiver")) ACS.continueLoop = false;
            HS.isCallStarted = false;
            dispose();
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e){
            continueLoop = false; //Stop video calls
            //Stop audio calls
            if(type.equals("audio-caller") || type.equals("video-caller"))          AC.continueLoop = false;
            else if(type.equals("audio-receiver") || type.equals("video-receiver")) ACS.continueLoop = false;
            HS.isCallStarted = false;
            }
        });
        btnMute.addActionListener(e -> {
            if (manageMicIcon){
                btnMute.setIcon(new ImageIcon(getClass().getResource("/img/mute_mic.png")));
                if(type.equals("audio-caller") || type.equals("video-caller")) AC.isMuted = true; //Necessary to understand if I am caller or receiver
                else if(type.equals("audio-receiver") || type.equals("video-receiver"))ACS.isMuted = true;
                manageMicIcon = false;
            } else if(!manageMicIcon){
                btnMute.setIcon(new ImageIcon(getClass().getResource("/img/mic.png")));
                if(type.equals("audio-caller") || type.equals("video-caller")) AC.isMuted = false; //Necessary to understand if I am caller or receiver
                else if(type.equals("audio-receiver") || type.equals("video-receiver")) ACS.isMuted = false;
                manageMicIcon = true;
            }
        });
        this.setVisible(true); this.setResizable(false); this.pack();

        switch (type) {
            case "audio-caller":
                try {
                    setProfilePicture(HS, friend_id);

                    sock = new Socket(HS.friend_ip, 1080);

                    //Send username and wait for response
                    new PrintWriter(new BufferedWriter(new OutputStreamWriter(sock.getOutputStream())), true).println(HS.user_username);

                    //Who received the call request ACCEPTED! Let's start to enstablish connection.
                    if (new BufferedReader(new InputStreamReader(sock.getInputStream())).readLine().equals("1")) {
                        this.AC = new AudioCall(this, HS.friend_ip);
                        new Thread(AC).start();
                        HS.isCallStarted = true;
                    } else JOptionPane.showMessageDialog(null,"Audio Call Refused.");
                    sock.close();
                } catch (IOException ignored) {}
                break;
            case "audio-receiver":
                setProfilePicture(HS, friend_id);

                this.ACS = new AudioCallServer(this);
                new Thread(ACS).start();
                HS.isCallStarted = true;
                break;
            case "video-caller":
                try {
                    //Create socket and send user_username to video-receiver
                    sock = new Socket(HS.friend_ip, 1100);
                    //Send username and wait for response
                    new PrintWriter(new BufferedWriter(new OutputStreamWriter(sock.getOutputStream())), true).println(HS.user_username);
                    //Who received the video request ACCEPTED! Let's start to enstablish connection.
                    if (new BufferedReader(new InputStreamReader(sock.getInputStream())).readLine().equals("1")) {
                        sock.close(); //close old socket and create new one!

                        //Start audio!
                        this.AC = new AudioCall(this, HS.friend_ip);
                        new Thread(AC).start();
                        HS.isCallStarted = true;

                        new Thread(() -> {
                            try{
                                sock = new Socket(HS.friend_ip, 1110);
                                ObjectOutputStream sendWebcam = new ObjectOutputStream(sock.getOutputStream());
                                ObjectInputStream rcv2 = new ObjectInputStream(sock.getInputStream());
                                Webcam webcam = Webcam.getDefault();
                                webcam.open(true);

                                ImageIcon imageFrameReceived;
                                ImageIcon imageFrameSended;
                                while (continueLoop) {
                                    imageFrameSended = new ImageIcon(webcam.getImage());

                                    //Show your personal webcam on the left (client side)...AND Send your webcam image to other user (client side)!
                                    lblAvatar1.setIcon(imageFrameSended);
                                    sendWebcam.writeUnshared(imageFrameSended); sendWebcam.reset();

                                    //Receive images on the right (client side)
                                    imageFrameReceived = (ImageIcon) rcv2.readUnshared();
                                    lblAvatar2.setIcon(imageFrameReceived);
                                }
                                webcam.close(); sock.close();
                            } catch (IOException | ClassNotFoundException e1) {e1.printStackTrace();}
                        }).start();
                    } //if other user accepted video call
                } catch (IOException ignored) {}
                break;
            case "video-receiver":
                //Start audio!
                this.ACS = new AudioCallServer(this);
                new Thread(ACS).start();
                HS.isCallStarted = true;

                new Thread(() -> {
                    try{
                        sock = new ServerSocket(1110).accept();
                        ObjectInputStream rcv = new ObjectInputStream(sock.getInputStream());
                        ObjectOutputStream sendWebcam2 = new ObjectOutputStream(sock.getOutputStream());
                        Webcam webcam2 = Webcam.getDefault();
                        webcam2.open(true);

                        ImageIcon imageFrameReceived2;
                        ImageIcon imageFrameSended2;
                        while (continueLoop) {
                            //Receive video-caller images on the right (server side)
                            imageFrameReceived2 = (ImageIcon) rcv.readUnshared();
                            lblAvatar2.setIcon(imageFrameReceived2);

                            //Show your personal webcam on the left (server side)....AND send it!
                            imageFrameSended2 = new ImageIcon(webcam2.getImage());
                            lblAvatar1.setIcon(imageFrameSended2);
                            sendWebcam2.writeUnshared(imageFrameSended2); sendWebcam2.reset();
                        }
                        webcam2.close(); sock.close();
                    } catch (IOException | ClassNotFoundException e1) {e1.printStackTrace();}
                }).start();
                break;
        } //end switch!
    }//-----------------------------------------------------------------------------------
    //SET profile picture only for Audio Calls!
    private void setProfilePicture(HomeScreen HS, String friend_id){
        try { //Get friend avatar and set it inside CallScreen.
            //Set avatar and user username.
            lblAvatar1.setIcon(new ImageIcon(new CommonMethods().createResizedCopy(ImageIO.read(new File(HS.user_picture)), 400, false)));
            lblAvatar1.setSize(400,400);
            //Set avatar and friend username.
            String friend_picture = new SocketSender().receiveFile(friend_id, "1", ""); //"1" because it's a profile picture!
            lblAvatar2.setIcon(new ImageIcon(new CommonMethods().createResizedCopy(ImageIO.read(new File(friend_picture)), 400, false)));
            lblAvatar2.setSize(400,400);
        }catch (IOException e) {e.printStackTrace();}
    }//-----------------------------------------------------------------------------------
} //end CallScreen class