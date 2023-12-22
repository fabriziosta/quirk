package entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Contacts {
    //INSERT new CONTACT
    public static void set_contact(int id1, int id2) {
        try {
            DB_Connection.prepareConnection().executeUpdate(
                    "INSERT INTO contacts(ID_1, ID_2, CUSTOM_NAME_1, CUSTOM_NAME_2)VALUES(" +id1+ "," +id2+ ",' ',' ')");
        } catch (SQLException e) {e.printStackTrace();}
        DB_Connection.close_conn();
    }//-----------------------------------------------------------------------------------
    // Check friendship by IDs. This method is called when trying to add new friends.
    public static int check_friendship(int id1, int id2) {
        int friendship_founded = 0;
        try {
            ResultSet res = DB_Connection.prepareConnection().executeQuery("SELECT * FROM contacts " +
                    "WHERE (ID_1=" + id1 + " AND ID_2=" + id2 + ") OR (ID_1=" + id2 + " AND ID_2=" + id1 + ")");
            res.last();
            friendship_founded = res.getRow();
        } catch (SQLException e) {e.printStackTrace();}
        DB_Connection.close_conn();
        return friendship_founded;
    }//-----------------------------------------------------------------------------------
    // Check what is chat name between two contacts
    public static String check_chat(int id1, int id2) {
        String chat_filename = null;
        try {
            ResultSet res = DB_Connection.prepareConnection().executeQuery("SELECT ID_1,ID_2 FROM contacts " +
                    "WHERE (ID_1=" + id1 + " AND ID_2=" + id2 + ") OR (ID_1=" + id2 + " AND ID_2=" + id1 + ")");
            res.last();
            chat_filename = res.getString("ID_1") + "_" + res.getString("ID_2") + ".txt";
        } catch (SQLException e) {e.printStackTrace();}
        DB_Connection.close_conn();
        return chat_filename;
    }//-----------------------------------------------------------------------------------
    //GET 2nd ID with 1st ID
    public static int get_ID_USER_2(int ID_1) {
        int ID_2 = 0;
        try {
            ResultSet res = DB_Connection.prepareConnection().executeQuery("SELECT ID_2 FROM contacts WHERE ID_1 = " + ID_1 + "");
            res.next();
            ID_2 = res.getInt("ID_2");
        } catch (SQLException e) {e.printStackTrace();}
        DB_Connection.close_conn();
        return ID_2;
    }//-----------------------------------------------------------------------------------
    //UPDATE Custom_name1 by ID1 and ID2
    public static void  update_custom_name1(String ID_1, String ID_2, String Custom_name1) {
        try {DB_Connection.prepareConnection().executeUpdate("UPDATE contacts SET CUSTOM_NAME_1='" + Custom_name1 + "' WHERE ID_1=" +ID_1+ " AND ID_2=" +ID_2);}
        catch (SQLException e) {e.printStackTrace();}
        DB_Connection.close_conn();
    }//---------------------------------------------------------------------------------—
    //UPDATE Custom_name2 by ID1 and ID2
    public static void  update_custom_name2(String ID_1, String ID_2, String Custom_name2) {
        try {DB_Connection.prepareConnection().executeUpdate("UPDATE contacts SET CUSTOM_NAME_2='" +Custom_name2+ "' WHERE ID_1=" +ID_1+ " AND ID_2=" +ID_2);}
        catch (SQLException e) {e.printStackTrace();}
        DB_Connection.close_conn();
    }//----------------------------------------------------------------------------------
    //GET Custom_name1 by ID1 and ID2
    public static String get_custom_name1(String ID_1, String ID_2) {
        String custom_name = "";
        try {
            ResultSet res = DB_Connection.prepareConnection().executeQuery(
                    "SELECT CUSTOM_NAME_1 FROM contacts WHERE ID_1 = " +ID_1+ " AND ID_2 = " + ID_2+ "");
            res.next();
            custom_name = res.getString("CUSTOM_NAME_1");
        } catch (SQLException ignored) {}
        DB_Connection.close_conn();
        return custom_name;
    }//-----------------------------------------------------------------------------------
    //GET Custom_name2 by ID1 and ID2
    public static String get_custom_name2(String ID_1, String ID_2) {
        String custom_name = "";
        try {
            ResultSet res = DB_Connection.prepareConnection().executeQuery(
                    "SELECT CUSTOM_NAME_2 FROM contacts WHERE ID_1 = " +ID_1+ " AND ID_2 = " + ID_2+ "");
            res.next();
            custom_name = res.getString("CUSTOM_NAME_2");
        } catch (SQLException ignored) {}
        DB_Connection.close_conn();
        return custom_name;
    }//-----------------------------------------------------------------------------------
    //UPDATE ACCEPT
    public static void update_accept(String userIDwhoAsked, String userIDwhoAccepted){
        try {
            DB_Connection.prepareConnection().executeUpdate("UPDATE contacts SET ACCEPT=1 WHERE ID_1="+userIDwhoAsked+" AND ID_2="+userIDwhoAccepted);
        }catch (Exception e){e.printStackTrace();}
        DB_Connection.close_conn();
    }//-----------------------------------------------------------------------------------
    //GET friends ACCEPT = 1 (friends)
    public static ArrayList get_accept_1(int ID) {
        ArrayList<Integer> friendLIST = new ArrayList<Integer>();
        try {
            ResultSet res = DB_Connection.prepareConnection().executeQuery("SELECT ID_1,ID_2 FROM contacts " +
                    "WHERE ID_1 = " + ID + " OR ID_2 = " + ID + " AND ACCEPT= 1");
            while (res.next()){
                if(res.getInt("ID_2")==ID) {
                    friendLIST.add(res.getInt("ID_1"));
                }
                else{
                    friendLIST.add(res.getInt("ID_2"));
                }
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        return friendLIST;
    }//-----------------------------------------------------------------------------------
    //SELECT ACCEPT = 0 (friend request)
    public static ArrayList get_accept_0(int ID) {
        ArrayList<Integer> requestLIST = new ArrayList<Integer>();
        try {
            ResultSet res = DB_Connection.prepareConnection().executeQuery("SELECT ID_1,ID_2 FROM contacts " +
                    "WHERE ID_1 = " + ID + " OR ID_2 = " + ID + " AND ACCEPT= 0");
            while (res.next()){
                if(res.getInt("ID_2")==ID) {
                    requestLIST.add(res.getInt("ID_1"));
                }
                else{
                    requestLIST.add(res.getInt("ID_2"));
                }
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        DB_Connection.close_conn();
        return requestLIST;
    }//-----------------------------------------------------------------------------------
    public static String get_all_friends(String id) {
        String friendList = "";
        int my_id = Integer.parseInt(id);

        try {
            ResultSet res = DB_Connection.prepareConnection().executeQuery("SELECT ID_1, ID_2, ACCEPT FROM contacts " +
                            "WHERE (ID_1 =" + id + " AND (ACCEPT=1 OR ACCEPT=2)) OR (ID_2=" + id + ")");

            while (res.next()){
                if(res.getInt("ID_1") == my_id && res.getInt("ACCEPT") == 1) {
                    friendList += Users.get_user_username_by_ID(String.valueOf(res.getInt("ID_2"))) + "@";
                }else if(res.getInt("ID_1") == my_id && res.getInt("ACCEPT") == 2){
                    friendList += Users.get_user_username_by_ID(String.valueOf(res.getInt("ID_2"))) + " - BLOCKED" + "@";
                }else if(res.getInt("ID_2") == my_id && res.getInt("ACCEPT") == 0){
                    friendList += Users.get_user_username_by_ID(String.valueOf(res.getInt("ID_1"))) + " - Pending request!" + "@";
                }else if(res.getInt("ID_2") == my_id && res.getInt("ACCEPT") == 1){
                    friendList += Users.get_user_username_by_ID(String.valueOf(res.getInt("ID_1"))) + "@";
                }else if(res.getInt("ID_2") == my_id && res.getInt("ACCEPT") == 2){
                    friendList += Users.get_user_username_by_ID(String.valueOf(res.getInt("ID_1"))) + " - BLOCKED" + "@";
                }
            }

        } catch (SQLException e) {e.printStackTrace();}
        DB_Connection.close_conn();
        return friendList;
    }//-----------------------------------------------------------------------------------
    //BLOCK USER - WHEN ACCEPT=2 means that person has been blocked!
    public static void block_user(String friend_id, String user_id){
        try{DB_Connection.prepareConnection().executeUpdate("UPDATE contacts SET ACCEPT=2 " +
                "WHERE (ID_1=" + friend_id + " AND ID_2=" + user_id + ") OR (ID_1=" + user_id + " AND ID_2=" + friend_id + ")");
        }catch (Exception ignored){}
        DB_Connection.close_conn();
    }//-----------------------------------------------------------------------------------
    //DELETE a friend or a contact
    public static void delete_contact(String userIDwhoAsked, String userIDwhoRefused){
        try {
            DB_Connection.prepareConnection().executeUpdate("DELETE FROM contacts WHERE ID_1="+userIDwhoAsked+" AND ID_2="+userIDwhoRefused);
        }catch (Exception e){e.printStackTrace();}
        DB_Connection.close_conn();
    }//-----------------------------------------------------------------------------------
} //end Contacts
