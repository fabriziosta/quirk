package socket;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class SocketSender {
    private static final String IP_SERVER = "192.168.1.41";
    //-------------------------------------------------------------------------------
    public String queryTheServer(String query){
        String responseFromServer = "";
        try {
            Socket socket = new Socket(IP_SERVER, 1050);

            //Input Stream
            BufferedReader in = new BufferedReader( new InputStreamReader(socket.getInputStream()));

            //Output Stream
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

            //Send a message to the server.
            out.println(query);

            //Check messages!
            System.out.println("Command sent:" + query);

            //Read server response.
            // If I am passing full chat from socket, read and save everything in a string, else just read the result.
            if(query.split("-")[0].equals("get_full_chat")
                    || query.split("-")[0].equals("get_group_chat")
                    || query.split("-")[0].equals("get_channel_chat"))
            {
                String singleLine = null;
                while((singleLine = in.readLine()) != null) responseFromServer += singleLine + "\n";
            }else responseFromServer = in.readLine();
            out.close(); in.close(); socket.close();
        }catch(IOException ignored){}
        return responseFromServer;
    } //END sendMessage
    //-------------------------------------------------------------------------------
    //This method is used to send chat, group and channel messaged to the server to save conversation
    public void sendMessage(String userMessage){
        try {
            Socket socket = new Socket(IP_SERVER, 1050);

            //Output Stream
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
            PrintWriter out = new PrintWriter(new BufferedWriter(outputStreamWriter), true);

            //Send a message to the server and print it.
            out.println(userMessage);
            System.out.println("Command sent:" + userMessage);

            out.close(); socket.close();
        }catch (IOException e) {JOptionPane.showMessageDialog(null, "Can't reach the server! Try again later.");}
    } //END sendMessage
    //-------------------------------------------------------------------------------
    //Send files to the server and receive back his ID.
    public String sendFile(String pathName){
        String last_ID = "";
        try {
            Socket socket = new Socket(IP_SERVER, 1060);
            byte[] bytes = new byte[1024];
            InputStream in = new FileInputStream(new File(pathName));
            OutputStream out = socket.getOutputStream();

            int count;
            while ((count = in.read(bytes)) > 0) out.write(bytes, 0, count);
            out.close(); in.close(); socket.close();

            //Try to get last_insert_ID - I cannot get last ID from the previous socket because it was too fast and no ID was received back from server!
            socket = new Socket(IP_SERVER, 1061);
            PrintWriter requestID = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            BufferedReader receiveID = new BufferedReader( new InputStreamReader(socket.getInputStream()));
            requestID.println("give me ID");
            last_ID = receiveID.readLine();

            requestID.close(); receiveID.close(); socket.close();
        } catch (IOException e) {JOptionPane.showMessageDialog(null, "Can't reach the server! Try again later.");}
        return last_ID;
    } //END sendFile()
    //-------------------------------------------------------------------------------
    // Receive files from the server
    public String receiveFile(String user_id, String type_of_file, String file_id){
        String filename = "";
        String extension = "";
        OutputStream writeInAFile = null;

        try {
            Socket socket = new Socket(IP_SERVER, 1070);

            //Input Stream - We receive back images
            InputStream in = socket.getInputStream();

            //Output Stream - Send strings
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

            //Send a message to the server.
            String cmd = user_id + "-" + type_of_file + "-" + file_id;
            System.out.println("Command sent: " + cmd);
            out.println(cmd);

            //Output Stream - Save file object in a file.
            if(type_of_file.equals("0")) filename = new SocketSender().queryTheServer("get_extension-" + file_id); //It's a general file!
            else filename = user_id + ".jpg"; //It's a profile picture

            writeInAFile = new FileOutputStream(new File(filename));

            int count;
            byte[] bytes = new byte[1024];
            while ((count = in.read(bytes)) > 0) writeInAFile.write(bytes, 0, count);

            out.close(); in.close(); socket.close(); writeInAFile.close();
        } catch (IOException ignored) {}
        return filename;
    } //END sendFile()
    //-------------------------------------------------------------------------------
} //end SocketSender

