package socket;

import classes.CommonMethods;
import gui.CallScreen;
import gui.HomeScreen;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketListener {
    private HomeScreen HS;
    private int user_port1;
    private int user_port2;
    private Thread userTaskThread;
    private Runnable userTask;
    public Runnable getUserTask() {return userTask;}
    public Thread getUserTaskThread() {return userTaskThread;}
    public void setUserTaskThread(Thread userTaskThread) {this.userTaskThread = userTaskThread;}
    //-----------------------------------------------------------------------------------
    public SocketListener(HomeScreen homeScreen) {
        this.HS = homeScreen;
        this.user_port1 = Integer.parseInt(new SocketSender().queryTheServer("get_port1-" + HS.user_id));
        this.user_port2 = Integer.parseInt(new SocketSender().queryTheServer("get_port2-" + HS.user_id));
    }
    //-----------------------------------------------------------------------------------
    //Client SocketListener must be executed in a different thread because it must work asynchronously with the program and listen to everything.
    public void startToListen() {
        final ExecutorService clientProcessingPool = Executors.newFixedThreadPool(10);

        userTask = () -> {
            ServerSocket serverSocket;
            try {
                try{ serverSocket = new ServerSocket(user_port1);}
                catch (Exception e){serverSocket = new ServerSocket(user_port2);}

                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    clientProcessingPool.submit(new ClientTask(clientSocket));
                }
            } catch (IOException e) {System.err.println("Unable to process client request");}
        };

        Runnable audioCallTask = () -> {
            try {

                while(true){
                    Socket socket = new ServerSocket(1080).accept();

                    String calling_username = new BufferedReader(new InputStreamReader(socket.getInputStream())).readLine();
                    PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

                    if (JOptionPane.showConfirmDialog(null,"Accept call request from " + calling_username + "?",
                            "Audio Call request", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                        //If there is an affermative response, send back "1"!
                        out.println("1");

                        String friend_id = new SocketSender().queryTheServer("get_user_id_by_username-" + calling_username);
                        new CallScreen(HS, friend_id,"audio-receiver");
                    } else out.println("0");
                }
            } catch (IOException e) {System.err.println("Unable to process client request");}
        };

        Runnable videoCallTask = () -> {
            try {

                while(true){
                    Socket socket = new ServerSocket(1100).accept();

                    String calling_username = new BufferedReader(new InputStreamReader(socket.getInputStream())).readLine();
                    PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

                    if (JOptionPane.showConfirmDialog(null,"Accept video request from " + calling_username + "?",
                            "Video Call request", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
                        out.println("1"); //If there is an affermative response, send back "1"!

                        String friend_id = new SocketSender().queryTheServer("get_user_id_by_username-" + calling_username);
                        new CallScreen(HS, friend_id,"video-receiver");
                    } else out.println("0");
                }
            } catch (IOException e) {System.err.println("Unable to process client request");}
        };

        userTaskThread = new Thread(userTask);
        userTaskThread.start(); //I saved this thread because I need to stop it when user change ports in OptionScreen!
        new Thread(audioCallTask).start();
        new Thread(videoCallTask).start();
    }
    //-----------------------------------------------------------------------------------
    private class ClientTask implements Runnable {
        private final Socket clientSocket;
        private ClientTask(Socket clientSocket) {this.clientSocket = clientSocket;}

        @Override
        public void run() {
            try {
                //Input Stream
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String command = in.readLine();
                System.out.println("Command requested:" + command);
                String splitString[] = command.split("-");

                switch (splitString[0]){
                    case "accept_friend?": //[1] == friend_username
                        JOptionPane.showMessageDialog(HS, "New friend request from '" + splitString[1] + "'!");
                        HS.friendModel.addElement(splitString[1] + " - Pending request!");
                        break;
                    case "message": //[1] == message, [2] == friend_username
                        Toolkit.getDefaultToolkit().beep();
                        if(HS.listFriends.getSelectedValue().equals(splitString[2])) new CommonMethods().append_new_message(splitString[1], HS.txtAreaCHAT, splitString[2]);
                        break;
                }
                in.close(); clientSocket.close();
            }catch(IOException e) {e.printStackTrace();}
        } //end RUN method()
    } //end clientTask
    //-----------------------------------------------------------------------------------
    //GETTER AND SETTER
    public int getUser_port1() {return user_port1;}
    public void setUser_port1(int user_port1) {this.user_port1 = user_port1;}
    public int getUser_port2() {return user_port2;}
    public void setUser_port2(int user_port2) {this.user_port2 = user_port2;}
    //-----------------------------------------------------------------------------------
} //end SocketListener()