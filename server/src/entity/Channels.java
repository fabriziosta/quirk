package entity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Channels {
    //CHECK if CHANNEL exist, 1 if yes, 0 if not.
    public static int check_channel(String channel_name) {
        int alreadyExist = 0;
        try {
            ResultSet res = DB_Connection.prepareConnection().executeQuery("SELECT * FROM channels WHERE CH_NAME = '" + channel_name + "'");
            res.last();
            alreadyExist = res.getRow();
        } catch (SQLException e) {e.printStackTrace();}
        DB_Connection.close_conn();
        return alreadyExist;
    }//-----------------------------------------------------------------------------------
    //INSERT new CHANNEL
    public static String set_channel(int admin_id, String ch_name) {
        try {
            DB_Connection.prepareConnection().executeUpdate("INSERT into channels(ADMIN_ID, CH_NAME, USER_LIST)" +
                    "values('"+ admin_id +"','"+ ch_name +"','" + admin_id + ";')");
            //Now, add the group id to user channel list.
            ResultSet res = DB_Connection.prepareConnection().executeQuery("SELECT ID FROM channels WHERE CH_NAME='" + ch_name + "'");
            res.last();
            String ch_id = String.valueOf(res.getInt("ID"));
            res = DB_Connection.prepareConnection().executeQuery("SELECT channels FROM users WHERE ID='" + admin_id + "'");
            res.last();
            String updated_list = res.getString("channels") + ch_id + ";";
            DB_Connection.prepareConnection().executeUpdate("UPDATE users SET channels='" + updated_list + "' WHERE ID='" + admin_id + "'");

        } catch (SQLException e) {return "An error occurred while creating a new channel!";}
        DB_Connection.close_conn();
        return "Channel created successfully!";
    }//-----------------------------------------------------------------------------------
    // ADD USER
    public static String add_ch_user(String ch_id, String user_id) {
        String user_list = get_user_list(Integer.parseInt(ch_id));
        user_list = user_list + user_id + ";";
        try {DB_Connection.prepareConnection().executeQuery("UPDATE channels SET USER_LIST ='" + user_list + "' WHERE ID=" + ch_id);
        }catch (SQLException e) {e.printStackTrace();}
        DB_Connection.close_conn();
        return "Channel added successfully!";
    }//-----------------------------------------------------------------------------------
    //ADD ID followed by "?" inside USER_LIST and user row waiting for the accept or refuse request.
    public static String add_channel_member(String usernameToADD, String ch_name){

        try{
            ResultSet res = DB_Connection.prepareConnection().executeQuery("SELECT ID,CHANNELS FROM users WHERE USERNAME='" + usernameToADD + "'");
            ResultSet res2 = DB_Connection.prepareConnection().executeQuery("SELECT ID,USER_LIST FROM channels WHERE CH_NAME='" + ch_name + "'");
            res.next(); res2.next();

            String ch_id = String.valueOf(res2.getInt("ID"));
            String user_id = String.valueOf(res.getInt("ID"));
            String updated_user_ch = res.getString("channels") + ch_id + "?;";
            String updated_user_list = res2.getString("USER_LIST") + user_id + "?;";

            DB_Connection.prepareConnection().executeUpdate("UPDATE channels SET USER_LIST='" + updated_user_list + "'" +
                    "WHERE CH_NAME='" + ch_name + "'");

            DB_Connection.prepareConnection().executeUpdate("UPDATE users SET channels='" + updated_user_ch + "'" +
                    "WHERE USERNAME='" + usernameToADD + "'");
        } catch (SQLException e) {return "An error occurred!";}
        DB_Connection.close_conn();
        return "Request sent successfully!";
    }//-----------------------------------------------------------------------------------
    //GET ADMIN ID by ID
    public static String get_channel_ID_by_name(String ch_name) {
        String ch_id = "";
        try {
            ResultSet res = DB_Connection.prepareConnection().executeQuery("SELECT ID FROM channels WHERE CH_NAME='" + ch_name + "'");
            res.next();
            ch_id = String.valueOf(res.getInt("ID"));
        } catch (SQLException e) {e.printStackTrace();}
        DB_Connection.close_conn();
        return ch_id;
    }//-----------------------------------------------------------------------------------
    // GET ADMIN ID by ID
    public static String get_admin_id(String ch_name) {
        String admin_id = "";
        try {
            ResultSet res = DB_Connection.prepareConnection().executeQuery("SELECT ADMIN_ID FROM channels WHERE CH_NAME='" + ch_name + "'");
            res.next();
            admin_id = String.valueOf(res.getInt("ADMIN_ID"));
        } catch (SQLException e) {e.printStackTrace();}
        DB_Connection.close_conn();
        return admin_id;
    }//-----------------------------------------------------------------------------------
    //GET USER LIST by ID
    public static String get_user_list(int id) {
        String user_list = "";
        try {
            ResultSet res = DB_Connection.prepareConnection().executeQuery("SELECT USER_LIST FROM channels WHERE ID=" + id);
            res.next();
            user_list = res.getString("USER_LIST");
        } catch (SQLException ignored) {}
        DB_Connection.close_conn();
        return user_list;
    }//-----------------------------------------------------------------------------------
    //GET CH NAME by ID
    public static String get_ch_name(int id) {
        String ch_name = "";
        try {
            ResultSet res = DB_Connection.prepareConnection().executeQuery("SELECT CH_NAME FROM channels WHERE ID= " + id + "");
            res.next();
            ch_name = res.getString("CH_NAME");
        } catch (SQLException e) {e.printStackTrace();}
        DB_Connection.close_conn();
        return ch_name;
    }//-----------------------------------------------------------------------------------
    //ADD CHANNEL MEMBER, when He ACCEPT
    public static void update_membership(String ch_name, String user_id){
        try{
            String lastChar= "";
            //First of all, get userlist for this group.
            ResultSet res = DB_Connection.prepareConnection().executeQuery("SELECT USER_LIST FROM channels WHERE CH_NAME='" + ch_name + "'");
            res.next();
            String user_list = res.getString("USER_LIST");

            //Delete the "?" symbol near the user_id.
            String list_splitted[] = user_list.split(";");
            for(int i = 0; i < list_splitted.length; i++){

                lastChar = String.valueOf(list_splitted[i].charAt(list_splitted[i].length()-1));
                if(lastChar.equals("?") && list_splitted[i].substring(0, list_splitted[i].length()-1).equals(user_id)){
                    list_splitted[i] = user_id;
                }
            }
            //Join again back the full string and update DB
            String updated_user_list = String.join(";", list_splitted);

            DB_Connection.prepareConnection().executeUpdate("UPDATE channels SET USER_LIST='" + updated_user_list + "'" +
                    "WHERE GR_NAME='" + ch_name + "'");
        } catch (SQLException e) {e.printStackTrace();}
        DB_Connection.close_conn();
    }//-----------------------------------------------------------------------------------
    // REMOVE CHANNEL MEMBER, when He REFUSE
    public static void update_membership_negative(String ch_name, String user_id){
        try{
            String lastChar= "";
            //First of all, get userlist for this group.
            ResultSet res = DB_Connection.prepareConnection().executeQuery("SELECT USER_LIST FROM channels WHERE CH_NAME='" + ch_name + "'");
            res.next();
            String user_list = res.getString("USER_LIST");

            //Delete the "?" symbol near the user_id.
            String list_splitted[] = user_list.split(";");
            for(int i = 0; i < list_splitted.length; i++){

                lastChar = String.valueOf(list_splitted[i].charAt(list_splitted[i].length()-1));
                if(lastChar.equals("?") && list_splitted[i].substring(0, list_splitted[i].length()-1).equals(user_id)){
                    list_splitted[i] = "";
                }
            }
            //Join again back the full string and update DB
            String updated_user_list = String.join(";", list_splitted);

            DB_Connection.prepareConnection().executeUpdate("UPDATE channels SET USER_LIST='" + updated_user_list + "'" +
                    "WHERE GR_NAME='" + ch_name + "'");
        } catch (SQLException e) {e.printStackTrace();}
        DB_Connection.close_conn();
    }//-----------------------------------------------------------------------------------
    //DELETE YOURSELF FROM CHANNEL!!!
    public static void delete_yourself_from_channel(String ch_id, String user_id){
        ResultSet res;
        try{
            //Firstly, update user_list in CHANNELS
            res = DB_Connection.prepareConnection().executeQuery("SELECT USER_LIST FROM channels WHERE ID=" + ch_id);
            res.next();
            String user_list = res.getString("USER_LIST");
            String list_splitted[] = user_list.split(";");

            for(int i = 0; i < list_splitted.length; i++){
                if(list_splitted[i].equals(user_id)) list_splitted[i] = "";
            }
            user_list = String.join(";", list_splitted);
            user_list+=";";

            StringBuilder retval_user_list = new StringBuilder();

            char[] stringamy1=user_list.toCharArray();
            for(int k=0; k<stringamy1.length; k++){

                if(stringamy1[k]==';' && (k+1)<stringamy1.length){
                    if(stringamy1[k+1]==';') continue;
                }
                retval_user_list.append(stringamy1[k]);
            }
            DB_Connection.prepareConnection().executeUpdate("UPDATE channels SET USER_LIST='" +retval_user_list+ "' WHERE ID=" +ch_id);

            //Secondly, update GROUPS row in USERS
            res = DB_Connection.prepareConnection().executeQuery("SELECT channels FROM users WHERE ID=" + user_id);
            res.next();
            String channels = res.getString("channels");
            String list_splitted2[] = channels.split(";");

            for(int i = 0; i < list_splitted2.length; i++){
                if(list_splitted2[i].equals(ch_id)) list_splitted2[i] = "";

            }

            channels = String.join(";", list_splitted2);
            channels+=";";

            StringBuilder retval_channel = new StringBuilder();

            char[] stringamy2=channels.toCharArray();
            for(int k=0; k<stringamy2.length; k++){
                if(stringamy2[k]==';' && (k+1)<stringamy2.length){
                    if(stringamy2[k+1]==';') continue;
                }
                retval_channel.append(stringamy2[k]);
            }
            DB_Connection.prepareConnection().executeUpdate("UPDATE users SET channels='" +retval_channel+ "' WHERE ID=" +user_id);
        }catch (Exception ignored){}
        DB_Connection.close_conn();
    }//-----------------------------------------------------------------------------------
} //end Channels
