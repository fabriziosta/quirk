package listener;

import entity.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
//-----------------------------------------------------------------------------------
public class Server {
    private static final String QWIRK_FILE_PATH = "/Qwirk/file/";
    private static final String QWIRK_CHAT_PATH = "/Qwirk/chat/";
    private static final String QWIRK_GROUP_PATH = "/Qwirk/group/";
    private static final String QWIRK_CHANNEL_PATH = "/Qwirk/channel/";
    private String filename;
    private File file;
    public static void main(String[] args) {new Server().startServer();}

    private void startServer() {
        final ExecutorService clientProcessingPool = Executors.newFixedThreadPool(20);
        //----
        Runnable serverTask = () -> {
            try {
                ServerSocket serverSocket = new ServerSocket(1050);
                System.out.println("Qwirk Server started successfully!");
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    clientProcessingPool.submit(new ClientTask(clientSocket));
                }
            } catch (IOException e) {System.err.println("Unable to process client request");}
        };
        //----
        Runnable receiveTask = () -> {
            try {
                ServerSocket serverSocket = new ServerSocket(1060);
                while (true) {
                    Socket fileSocket = serverSocket.accept();
                    System.out.println("Port 1060 received a file");
                    clientProcessingPool.submit(new ReceiveFileTask(fileSocket));
                }
            } catch (IOException e) {System.err.println("Unable to process client request");}
        };
        //----
        Runnable sendTask = () -> {
            try {
                ServerSocket serverSocket = new ServerSocket(1070);
                while (true) {
                    Socket sendFileSocket = serverSocket.accept();
                    System.out.println("Port 1070 received a file request");
                    clientProcessingPool.submit(new SendFileTask(sendFileSocket));
                }
            } catch (IOException e) {System.err.println("Unable to process client request");}
        };
        //----
        new Thread(serverTask).start();
        new Thread(receiveTask).start();
        new Thread(sendTask).start();
    }
    //-----------------------------------------------------------------------------------
    private class ClientTask implements Runnable {
        private final Socket clientSocket;
        private String userWhoAsked;
        private String userWhoAccepted;
        private String userWhoRefused;
        private ClientTask(Socket clientSocket) {this.clientSocket = clientSocket;}

        @Override
        public void run() {
            System.out.println(clientSocket);
            try {
                String result = null;
                //Input Stream
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                //Output Stream
                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())), true);

                String command = in.readLine();
                System.out.println("Command requested:" + command);
                String splitString[] = command.split("-");

                switch (splitString[0]){
                    //Cases when there is a QUERY request from CLIENTS!
                    case "set_user":
                        Users.set_user(splitString[1], splitString[2], splitString[3], splitString[4], splitString[5], String.valueOf(clientSocket.getInetAddress()).substring(1)); break;
                    case "check_email":
                        result = String.valueOf(Users.check_email(splitString[1])); break;
                    case "check_password":
                        result = String.valueOf(Users.check_password(splitString[1], splitString[2])); break;
                    case "check_username":
                        result = String.valueOf(Users.check_username(splitString[1])); break;
                    case "check_ip": //[1] == email
                        //System.out.println(String.valueOf(clientSocket.getInetAddress()).substring(1));
                        result = String.valueOf(Users.check_ip(String.valueOf(clientSocket.getInetAddress()).substring(1), splitString[1])); break;
                    case "get_user_id":
                        result = String.valueOf(Users.get_user_id(splitString[1])); break;
                    case "get_user_id_by_username":
                        result = String.valueOf(Users.get_user_id_by_username(splitString[1])); break;
                    case "get_user_id_by_IP": //[1] == ip
                        result = Users.get_user_id_by_IP(splitString[1]); break;
                    case "get_email":
                        result = Users.get_email(splitString[1]); break;
                    case "get_email_token":
                        result = Users.get_email_token(splitString[1]); break;
                    case "get_name_by_EMAIL":
                        result = Users.get_name_by_EMAIL(splitString[1]); break;
                    case "get_user_cognome":
                        result = Users.get_user_cognome(splitString[1]); break;
                    case "get_user_username":
                        result = Users.get_user_username(splitString[1]); break;
                    case "get_user_username_by_ID": //[1] == id
                        result = Users.get_user_username_by_ID(splitString[1]); break;
                    case "get_status":
                        result = Users.get_status(splitString[1]); break;
                    case "get_IP_ADDR": //[1] == user_ID
                        result = Users.get_IP_ADDR(splitString[1]); break;
                    case "get_port1": //[1] == user_ID
                        result = Users.get_port1(splitString[1]); break;
                    case "get_port2": //[1] == user_ID
                        result = Users.get_port2(splitString[1]); break;
                    case "get_count_tries": //[1] == email
                        result = Users.get_count_tries(splitString[1]); break;
                    case "get_ban_date": //[1] == email
                        result = Users.get_ban_date(splitString[1]); break;
                    case "update_count_tries": //[1] == email
                        Users.update_count_tries(splitString[1]); break;
                    case "reset_count_tries": //[1] == email
                        Users.reset_count_tries(splitString[1]); break;
                    case "amIBanned": //[1] == email
                        result = String.valueOf(Users.amIBanned(splitString[1])); break;
                    case "update_ban_date": //[1] == email
                        Users.update_ban_date(splitString[1]); break;
                    case "update_fname": //[1] == fname, [2] == email
                        Users.update_fname(splitString[1], splitString[2]); break;
                    case "update_lname": //[1] == fname, [2] == email
                        Users.update_lname(splitString[1], splitString[2]); break;
                    case "update_token":
                        Users.update_token(splitString[1], splitString[2]); break;
                    case "update_password":
                        Users.update_password(splitString[1], splitString[2]); break;
                    case "update_status":
                        Users.set_status(splitString[1], splitString[2]); break;
                    case "update_ip": //[1] == user_id
                        Users.update_ip(String.valueOf(clientSocket.getInetAddress()).substring(1), splitString[1]); break;
                    case "update_ip_by_email": //[1] == email
                        Users.update_ip_by_email(String.valueOf(clientSocket.getInetAddress()).substring(1), splitString[1]); break;
                    case "delete_ip": //used when logout or HomeScreen closed. [1] == IP
                        Users.delete_ip(splitString[1]); break;
                    case "update_port1":
                        Users.update_port1(splitString[1], splitString[2]); break;
                    case "update_port2":
                        Users.update_port2(splitString[1], splitString[2]); break;
                    case "update_user_group_list": //[1] == user_id, [2] == group_name
                        Users.update_user_group_list(splitString[1], splitString[2]); break;
                    case "update_user_channel_list": //[1] == user_id, [2] == channel_name
                        Users.update_channel_list(splitString[1], splitString[2]); break;
                    case "send_email":
                        new EmailSend(splitString[1]); break;
                    //Cases when there is a MESSAGE request from CLIENTS!
                    case "send_new_message": //[1] == user_id, [2] == other_user_id, [3] == "-MESSAGE: ....."
                        try {
                            String message = command.split("-MESSAGE:")[1];
                            filename = Contacts.check_chat(Integer.parseInt(splitString[1]), Integer.parseInt(splitString[2]));

                            file = new File(QWIRK_CHAT_PATH + filename);
                            if (!file.exists()) file.createNewFile();
                            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file,true));
                            bufferedWriter.write(Users.get_user_username_by_ID(splitString[1]) + ": " + message);
                            bufferedWriter.newLine(); bufferedWriter.flush(); bufferedWriter.close();

                            //Now that server is updated, send the same message to the other client and update his chat.
                            String userUSERNAMEThatSend = Users.get_user_username_by_ID(splitString[1]);
                            new Notification("message-" + message + "-" + userUSERNAMEThatSend, splitString[2]);
                        }catch(IOException e){e.printStackTrace();}
                        break;
                    case "get_full_chat":
                        filename = Contacts.check_chat(Integer.parseInt(splitString[1]), Integer.parseInt(splitString[2]));
                        try(BufferedReader br = new BufferedReader(new FileReader(QWIRK_CHAT_PATH + filename))) {
                            StringBuilder sb = new StringBuilder();
                            String line = br.readLine();

                            while (line != null) {
                                sb.append(line);
                                sb.append(System.lineSeparator());
                                line = br.readLine();
                            }
                            result = sb.toString();
                        }catch (Exception e){result = "No previous chat. Start to chat now with your friend!";}
                        break;
                    // ------------------- CONTACTS table queries! -------------------------------------
                    case "add_friend":
                        //Firstly, Check if username exist.
                        if(Users.check_username(splitString[1]) == 1){
                            //If exist, take id and get ready to check if a friendship already exist between these two users.
                            int userToAdd_ID = Users.get_user_id_by_username(splitString[1]);
                            //If a friendship does not exist, send the request. Else, send back an error message.
                            if(Contacts.check_friendship(Integer.parseInt(splitString[2]), userToAdd_ID) == 0){
                                //Add a new request in CONTACTS table
                                Contacts.set_contact(Integer.parseInt(splitString[2]), userToAdd_ID);
                                //Try to send the update request to the other user (maybe he is online?)
                                new Notification("accept_friend?-" + Users.get_user_username_by_ID(splitString[2]) , String.valueOf(userToAdd_ID));
                                result = "Request sent successfully!";
                            }else{result = "Request already sent OR you are already friends!";}
                        }else{result = "Username does not exist!";}
                        break;
                    case "friend_request_accepted":
                        //I have the username of whom send the request and the user id that accepted. Let's update this friendship!
                        userWhoAsked = String.valueOf(Users.get_user_id_by_username(splitString[1]));
                        userWhoAccepted = splitString[2];
                        Contacts.update_accept(userWhoAsked, userWhoAccepted);
                        break;
                    case "friend_request_refused":
                        userWhoAsked = String.valueOf(Users.get_user_id_by_username(splitString[1]));
                        userWhoRefused = splitString[2];
                        Contacts.delete_contact(userWhoAsked, userWhoRefused);
                        break;
                    case "retrieve_list": //[1] == user_id
                        result = Contacts.get_all_friends(splitString[1]); break;
                    case "update_custom_name1": //[1] == ID_1, [2] == ID_2, [3] == Custom_name1
                        Contacts.update_custom_name1(splitString[1], splitString[2], splitString[3]); break;
                    case "update_custom_name2": //[1] == ID_1, [2] == ID_2, [3] == Custom_name1
                        Contacts.update_custom_name2(splitString[1], splitString[2], splitString[3]); break;
                    case "get_custom_name1": //[1] friend_id, [2] user_id
                        result = Contacts.get_custom_name1(splitString[1], splitString[2]); break;
                    case "get_custom_name2": //[1] user_id, [2] friend_id
                        result = Contacts.get_custom_name2(splitString[1], splitString[2]); break;
                    case "block_user": //[1] friend_id to block, [2] == user_id
                        Contacts.block_user(splitString[1],splitString[2]); break;
                    case "delete_contact": //[1], [2]
                        Contacts.delete_contact(splitString[1], splitString[2]); break;
                    // -------------------- GROUP table queries ------------------------------------------
                    case "check_group": //[1] == group_name
                        result = String.valueOf(Groups.check_group(splitString[1])); break;
                    case "create_group": //[1] == admin_id, [2] == group_name
                        result = Groups.set_group(Integer.parseInt(splitString[1]), splitString[2]); break;
                    case "add_group_member": //[1] == usernameToADD, [2] == group_name
                        //Here I will add user_id inside group table and group_id inside user table.
                        result = Groups.add_group_member(splitString[1], splitString[2]); break;
                    case "get_group_ID_by_name": //[1] == group_name
                        result = Groups.get_group_ID_by_name(splitString[1]); break;
                    case "get_group_admin_id": //[1] == group_name
                        result = Groups.get_group_admin_id(splitString[1]); break;
                    case "get_gr_name": //[1] == group_id
                        result = Groups.get_gr_name(Integer.parseInt(splitString[1])); break;
                    case "group_request_accepted": //[1] == group_name, [2] == user_id
                        //If I have accepted, append user_id inside USER_LIST in groups AND append group_ID inside "groups" in User table
                        Groups.update_membership(splitString[1], splitString[2]);
                        Users.update_user_group_list(splitString[1], splitString[2]); break;
                    case "group_request_refused": //[1] == group_name, [2] == user_id
                        Groups.update_membership_negative(splitString[1], splitString[2]);
                        Users.update_user_group_list_negative(splitString[1], splitString[2]); break;
                    case "retrieve_groups": //[1] == user_id
                        result = Users.get_all_groups(splitString[1]); break;
                    case "retrieve_group_users": //[1] == group_id
                        result = String.valueOf(Groups.get_user_list(splitString[1])); break;
                    case "send_new_group_message": //[1] == group_id, [2] == user_username, [3] == "-MESSAGE: ....."
                        try {
                            String message = command.split("-MESSAGE:")[1];
                            file = new File(QWIRK_GROUP_PATH + splitString[1] + ".txt");
                            if (!file.exists()) file.createNewFile();
                            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file,true));
                            bufferedWriter.write(splitString[2] + ": " + message);
                            bufferedWriter.newLine(); bufferedWriter.flush(); bufferedWriter.close();
                        }catch(IOException e){e.printStackTrace();}
                        break;
                    case "get_group_chat": //[1] == group_id
                        filename = splitString[1] + ".txt";
                        try(BufferedReader br = new BufferedReader(new FileReader(QWIRK_GROUP_PATH + filename))) {
                            StringBuilder sb = new StringBuilder();
                            String line = br.readLine();

                            while (line != null) {
                                sb.append(line);
                                sb.append(System.lineSeparator());
                                line = br.readLine();
                            }
                            result = sb.toString();
                        }catch (Exception e){result = "No previous chat in this group. Start to chat now!";}
                        break;
                    case "delete_yourself_from_group": //[1] == group_id, [2] == user_id
                        Groups.delete_yourself_from_group(splitString[1], splitString[2]); break;
                    case "delete_user_from_group": //[1] == group_id, [2] == user_name
                        int user_id = Users.get_user_id_by_username(splitString[2]);
                        Groups.delete_yourself_from_group(splitString[1], String.valueOf(user_id)); break;
                    // -------------------- CHANNEL table queries ------------------------------------------
                    case "check_channel": //[1] == channel_name
                        result = String.valueOf(Channels.check_channel(splitString[1])); break;
                    case "create_channel": //[1] admin_id, [2] channel name
                        result = Channels.set_channel(Integer.parseInt(splitString[1]), splitString[2]); break;
                    case "follow_channel": //[1] == ch_name [2] == user_id
                        int alreadyAdd = 0;
                        String user_ch = String.valueOf(Channels.get_user_list(Integer.parseInt(Channels.get_channel_ID_by_name(splitString[1]))));
                        String lista = "";

                        for(int i = 0; i < user_ch.length(); i++) {
                            char score = user_ch.charAt(i);
                            if(score == ';'){lista += " ";}
                            else lista += score;
                        }
                        if(lista.indexOf(String.valueOf(splitString[2])) > -1) alreadyAdd = 1;
                        if(alreadyAdd == 1) result = "User is already following this channel!";
                        else {
                            Users.update_channel_list(splitString[2], splitString[1]);
                            result = Channels.add_ch_user(Channels.get_channel_ID_by_name(splitString[1]), splitString[2]);
                        }
                        break;
                    //add_channel_member adds a member inside a channel but with "?" because user must ACCEPT!
                    case "add_channel_member": //[1] == usernameToADD, [2] == channel_name
                        //Here I will add user_id inside channel table and channel_id inside user table.
                        result = Channels.add_channel_member(splitString[1], splitString[2]); break;
                    case "get_channel_ID_by_name": //[1] == channel_name
                        result = Channels.get_channel_ID_by_name(splitString[1]); break;
                    case "get_channel_adminID_by_name": //[1] == channel_name
                        result = Channels.get_admin_id(splitString[1]); break;
                    case "get_ch_name": //[1] == channel_id
                        result = Channels.get_ch_name(Integer.parseInt(splitString[1])); break;
                    case "channel_request_accepted": //[1] == channel_name, [2] == user_id
                        //If I have accepted, append user_id inside USER_LIST in channels AND append group_ID inside "channels" in User table
                        Channels.update_membership(splitString[1], splitString[2]);
                        Users.update_user_channel_list(splitString[1], splitString[2]); break;
                    case "channel_request_refused": //[1] == channel_name, [2] == user_id
                        Channels.update_membership_negative(splitString[1], splitString[2]);
                        Users.update_user_channel_list_negative(splitString[1], splitString[2]); break;
                    case "retrieve_channels": //[1] == user_id
                        result = Users.get_all_channels(splitString[1]); break;
                    case "retrieve_channel_users": //[1] == channel_id
                        result = String.valueOf(Channels.get_user_list(Integer.parseInt(splitString[1]))); break;
                    case "get_channel_chat": //[1] == channel_id
                        filename = splitString[1] + ".txt";
                        try(BufferedReader br = new BufferedReader(new FileReader(QWIRK_CHANNEL_PATH + filename))) {
                            StringBuilder sb = new StringBuilder();
                            String line = br.readLine();

                            while (line != null) {
                                sb.append(line);
                                sb.append(System.lineSeparator());
                                line = br.readLine();
                            }
                            result = sb.toString();
                        }catch (Exception e){result = "No previous chat in this channel. Start to chat now!";}
                        break;
                    case "send_new_channel_message": //[1] == ch_id, [2] == user_username, [3] == "-MESSAGE: ....."
                        try {
                            String message = command.split("-MESSAGE:")[1];
                            file = new File(QWIRK_CHANNEL_PATH + splitString[1] + ".txt");
                            if (!file.exists()) file.createNewFile();
                            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file,true));
                            bufferedWriter.write(splitString[2] + ": " + message);
                            bufferedWriter.newLine(); bufferedWriter.flush(); bufferedWriter.close();
                        }catch(IOException e){e.printStackTrace();}
                        break;
                    case "delete_yourself_from_channel": //[1] == channel_id, [2] == user_id
                    Channels.delete_yourself_from_channel(splitString[1], splitString[2]); break;
                    case "delete_user_from_channel": //[1] == channel_id, [2] == user_username
                        Channels.delete_yourself_from_channel(splitString[1], String.valueOf(Users.get_user_id_by_username(splitString[2]))); break;
                    // --------------------- FILES table queries -----------------------------------------------
                    case "last_insert_ID": //[1] == filename,[2] == user_username,[3] == type_of_file
                        result = Files.get_last_insert_ID(splitString[1],splitString[2],splitString[3]); break;
                    case "get_extension": //[1] == file_id
                        result = Files.get_file_name_by_ID(splitString[1]); break;
                    default: break;
                }

                //..Send back response
                out.println(result);

                //Show in the server the query result ONLY when the command isn't a full_chat.
                if(!splitString[0].equals("get_full_chat")
                        || !splitString[0].equals("get_group_chat")
                        || !splitString[0].equals("get_channel_chat")) System.out.println("Query result: " + result);

                //Closing stream and socket
                out.close(); in.close(); clientSocket.close();
            }catch(IOException e) {e.printStackTrace();}
        } //end RUN method()
    } //end clientTask
    //-----------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    //This thread will manage file request in port 1060
    private class ReceiveFileTask implements Runnable {
        private final Socket fileSocket;
        private ReceiveFileTask(Socket socket) {this.fileSocket = socket;}

        @Override
        public void run(){
            try{
                String file_information;
                String filename;
                String username;
                String user_id;
                String type_of_file;

                //Randomize for a temporary filename, write on this file and open InputStream
                String randomFileName = String.valueOf(new Date());
                OutputStream out = new FileOutputStream(QWIRK_FILE_PATH + randomFileName);
                InputStream in = fileSocket.getInputStream();

                //Read every single data from socket and write it in the file.
                int count;
                byte[] bytes = new byte[1024];
                while ((count = in.read(bytes)) > 0) out.write(bytes, 0, count);

                //Close sockets and streams.
                out.close(); in.close(); fileSocket.close();

                //Open this file and read from start to EOF. Save only last 200 bytes, there, there are stored all file information.
                try{
                    File fileProva = new File(QWIRK_FILE_PATH + randomFileName);
                    RandomAccessFile randomAccessFile = new RandomAccessFile(fileProva, "r");

                    byte[] save_bytes = new byte[200];
                    randomAccessFile.seek(fileProva.length() - 200);
                    randomAccessFile.read(save_bytes, 0, 200);
                    file_information = new String(save_bytes);
                    randomAccessFile.close();

                    //Split string and get information.
                    //This is a key point to get info. First split let me separate trash bytes from important info.
                    //Now I have to split again to get single information.
                    String info_splitted[] = file_information.split("@QWIRK@");
                    filename = info_splitted[info_splitted.length -1].split("@@@")[0];
                    username = info_splitted[info_splitted.length -1].split("@@@")[1];
                    type_of_file = info_splitted[info_splitted.length -1].split("@@@")[2];
                    user_id = String.valueOf(Users.get_user_id_by_username(username));

                    //Set a row in FILES table. type_of_file == 0 normal file, type_of_file == 1 profile picture.
                    int file_personal_id = Files.set_file(filename, user_id, type_of_file);

                    //Rename File - I am not able to know at this point if it's a profile picture or a normal file, so just rename it
                    //using his personal id!
                    fileProva.renameTo(new File(QWIRK_FILE_PATH + file_personal_id));
                    fileProva.delete();

                    //Create another serversocket to send back file ID! This is necessary because when I get file_personal_id it works correctly,
                    //but I don't have the time to send it back because thread work slower than my client and no ID is sent back.
                    ServerSocket serverSocket = new ServerSocket(1061);
                    Socket IDrequest = serverSocket.accept();

                    PrintWriter sendIDback = new PrintWriter(new BufferedWriter(new OutputStreamWriter(IDrequest.getOutputStream())), true);
                    String command = new BufferedReader(new InputStreamReader(IDrequest.getInputStream())).readLine();

                    sendIDback.println(file_personal_id);
                    serverSocket.close();
                }catch(IOException e1) {e1.printStackTrace();}
            } catch (IOException e2){e2.printStackTrace();}
        } //end Run() method!
    } //end ReceiveFileTask class
    //-----------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    //This thread will manage and send back files and images request in port 1070
    private class SendFileTask implements Runnable{
        private final Socket fileSocket;
        private SendFileTask(Socket socket) {this.fileSocket = socket;}

        @Override
        public void run() {
            String command = "";
            String user_id = "";
            String type_of_file = ""; //1 user want profile pic, 0 if it's a general file or picture
            String file_filename = "";
            String file_id = "";
            try{
                //Input Stream - read user_id received from port 1070, from client
                BufferedReader readCommand = new BufferedReader( new InputStreamReader(fileSocket.getInputStream()));
                command = readCommand.readLine();
                System.out.println("File Command Requested:" + command);

                user_id = command.split("-")[0];
                type_of_file = command.split("-")[1];
                file_filename = Files.get_file_name(user_id, type_of_file);

                //it's a profile picture and then get file_id in a different way.
                if(type_of_file.equals("1")) file_id = Files.get_file_id(user_id, type_of_file); //Get information about this specific file from DB
                // it's a file and we have received file_id with socket directly
                else if(type_of_file.equals("0")) file_id = command.split("-")[2];

                //Open stream for reading local file AND SEND IT through socket!
                InputStream readLocalFile = new FileInputStream(new File(QWIRK_FILE_PATH + file_id));
                OutputStream out = fileSocket.getOutputStream();

                //Send it.
                int count;
                byte[] bytes = new byte[1024];
                while ((count = readLocalFile.read(bytes)) > 0) out.write(bytes, 0, count);

                out.close(); readLocalFile.close(); fileSocket.close(); readCommand.close();
            } catch (IOException ignored){}
        }
    }
    //-----------------------------------------------------------------------------------
} //end Server