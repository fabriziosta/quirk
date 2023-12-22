package listener;

import entity.Users;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

class Notification {
    //-----------------------------------------------------------------------------------
    //This method is used to notify chats!
    Notification(String request, String id_receiver) {
        //Get IP, port1 and port2 of the other client.
        Socket socket;
        String other_client_IP = Users.get_IP_ADDR(id_receiver);
        String other_client_port1 = Users.get_port1(id_receiver);
        String other_client_port2 = Users.get_port2(id_receiver);

        try {
            try {
                socket = new Socket(other_client_IP, Integer.parseInt(other_client_port1));
            }catch (IOException e){
                System.out.println("Port " + other_client_port1 + " out of range! Try port2: " + other_client_port2);
                socket = new Socket(other_client_IP, Integer.parseInt(other_client_port2));
            }
            //Output Stream
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
            PrintWriter out = new PrintWriter(new BufferedWriter(outputStreamWriter), true);

            //Send a message to the server and print it.
            out.println(request);
            System.out.println("Request sent to " + other_client_IP + ": " + request);

            //Closing connection...and return result from the server
            out.close(); socket.close();
        }catch (IOException e) {System.out.println("Can't reach client! Try again later.");}
    } //end Contructor
    //-----------------------------------------------------------------------------------
} //end SocketSender class