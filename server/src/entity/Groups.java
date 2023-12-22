package entity;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Groups {
    //CHECK if GROUP exist
    public static int check_group(String group_name) {
        int alreadyExist = 0;
        try {
            ResultSet res = DB_Connection.prepareConnection().executeQuery("SELECT * FROM groups WHERE GR_NAME = '" + group_name + "'");
            res.last();
            alreadyExist = res.getRow();
        } catch (SQLException e) {e.printStackTrace();}
        DB_Connection.close_conn();
        return alreadyExist;
    }//-----------------------------------------------------------------------------------
    //GET Group ID by Name
    public static String get_group_ID_by_name(String name){
        String group_id = "";
        try {
            ResultSet res = DB_Connection.prepareConnection().executeQuery("SELECT ID FROM groups WHERE GR_NAME='" + name + "'");
            res.next();
            group_id = res.getString("ID");
        } catch (SQLException e) {e.printStackTrace();}
        DB_Connection.close_conn();
        return group_id;
    }//-----------------------------------------------------------------------------------
    // INSERT NEW GROUP
    public static String set_group(int admin_id, String gr_name) {
        try {

            DB_Connection.prepareConnection().executeUpdate("INSERT into groups(ADMIN_ID, GR_NAME, USER_LIST)" +
                    "values('"+ admin_id +"','"+ gr_name +"','" + admin_id + ";')");
            //Now, add the group id to user group list.
            ResultSet res = DB_Connection.prepareConnection().executeQuery("SELECT ID FROM groups WHERE GR_NAME='" + gr_name + "'");
            res.last();
            int group_id = res.getInt("ID");
            res = DB_Connection.prepareConnection().executeQuery("SELECT groups FROM users WHERE ID='" + admin_id + "'");
            res.last();
            String updated_list = res.getString("groups") + group_id + ";";
            DB_Connection.prepareConnection().executeUpdate("UPDATE users SET groups='" + updated_list + "' WHERE ID='" + admin_id + "'");

        } catch (SQLException e) {return "An error occurred while creating a new group!";}
        DB_Connection.close_conn();
        return "Group created successfully!";
    }//-----------------------------------------------------------------------------------
    //GET ADMIN ID by ID
    public static String get_group_admin_id(String gr_name) {
        String admin_id = "";
        try {
            ResultSet res = DB_Connection.prepareConnection().executeQuery("SELECT ADMIN_ID FROM groups WHERE GR_NAME='" + gr_name + "'");
            res.next();
            admin_id = String.valueOf(res.getInt("ADMIN_ID"));
        } catch (SQLException e) {e.printStackTrace();}
        DB_Connection.close_conn();
        return admin_id;
    }//-----------------------------------------------------------------------------------
    //GET USER LIST by ID
    public static String get_user_list(String id) {
        String user_list = "";
        try {
            ResultSet res = DB_Connection.prepareConnection().executeQuery("SELECT USER_LIST FROM groups WHERE ID=" +id);
            res.next();
            user_list = res.getString("USER_LIST");
        } catch (SQLException e) {e.printStackTrace();}
        DB_Connection.close_conn();
        return user_list;
    }//-----------------------------------------------------------------------------------
    //GET GROUP NAME by ID
    public static String get_gr_name(int id) {
        String gr_name = "";
        try {
            ResultSet res = DB_Connection.prepareConnection().executeQuery("SELECT GR_NAME FROM groups WHERE ID=" + id);
            res.next();
            gr_name = res.getString("GR_NAME");
        } catch (SQLException e) {e.printStackTrace();}
        DB_Connection.close_conn();
        return gr_name;
    }//-----------------------------------------------------------------------------------
    //ADD ID follow by "?" inside USER_LIST and user row waiting for the accept or refuse request.
    public static String add_group_member(String usernameToADD, String group_name){

        try{
            ResultSet res = DB_Connection.prepareConnection().executeQuery("SELECT ID,GROUPS FROM users WHERE USERNAME='" + usernameToADD + "'");
            ResultSet res2 = DB_Connection.prepareConnection().executeQuery("SELECT ID,USER_LIST FROM groups WHERE GR_NAME='" + group_name + "'");
            res.next(); res2.next();

            String group_id = String.valueOf(res2.getInt("ID"));
            String user_id = String.valueOf(res.getInt("ID"));
            String updated_user_groups = res.getString("groups") + group_id + "?;";
            String updated_user_list = res2.getString("USER_LIST") + user_id + "?;";

            DB_Connection.prepareConnection().executeUpdate("UPDATE groups SET USER_LIST='" + updated_user_list + "'" +
                    "WHERE GR_NAME='" + group_name + "'");

            DB_Connection.prepareConnection().executeUpdate("UPDATE users SET groups='" + updated_user_groups + "'" +
                    "WHERE USERNAME='" + usernameToADD + "'");
        } catch (SQLException e) {return "An error occurred!";}
        DB_Connection.close_conn();
        return "Request sent successfully!";
    }//-----------------------------------------------------------------------------------
    //ADD GROUP MEMBER, when He accept
    public static void update_membership(String group_name, String user_id){
        try{
            String lastChar= "";
            //First of all, get userlist for this group.
            ResultSet res = DB_Connection.prepareConnection().executeQuery("SELECT USER_LIST FROM groups WHERE GR_NAME='" + group_name + "'");
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
            updated_user_list += ";";

            DB_Connection.prepareConnection().executeUpdate("UPDATE groups SET USER_LIST='" + updated_user_list + "'" +
                    "WHERE GR_NAME='" + group_name + "'");
        } catch (SQLException e) {e.printStackTrace();}
        DB_Connection.close_conn();
    }//-----------------------------------------------------------------------------------
    // ADD GROUP MEMBER, when He REFUSE
    public static void update_membership_negative(String group_name, String user_id){
        try{
            String lastChar= "";
            //First of all, get userlist for this group.
            ResultSet res = DB_Connection.prepareConnection().executeQuery("SELECT USER_LIST FROM groups WHERE GR_NAME='" + group_name + "'");
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

            DB_Connection.prepareConnection().executeUpdate("UPDATE groups SET USER_LIST='" + updated_user_list + "'" +
                    "WHERE GR_NAME='" + group_name + "'");
        } catch (SQLException e) {e.printStackTrace();}
        DB_Connection.close_conn();
    }//-----------------------------------------------------------------------------------
    //DELETE YOURSELF FROM GROUP!!!
    public static void delete_yourself_from_group(String gr_id, String user_id){
        ResultSet res;
        try{
            //Firstly, update user_list in GROUPS
            res = DB_Connection.prepareConnection().executeQuery("SELECT USER_LIST FROM groups WHERE ID=" + gr_id);
            res.next();
            String user_list = res.getString("USER_LIST");
            String list_splitted[] = user_list.split(";");

            for(int i = 0; i < list_splitted.length; i++){
                if(list_splitted[i].equals(user_id)) list_splitted[i] = "";
            }
            user_list = String.join(";", list_splitted);
            user_list += ";";

            StringBuilder retval_user_list = new StringBuilder();

            char[] stringamy1=user_list.toCharArray();
            for(int k=0; k<stringamy1.length; k++){

                if(stringamy1[k]==';' && (k+1)<stringamy1.length){
                    if(stringamy1[k+1]==';') continue;
                }
                retval_user_list.append(stringamy1[k]);
            }
            DB_Connection.prepareConnection().executeUpdate("UPDATE groups SET USER_LIST='" +retval_user_list+ "' WHERE ID=" +gr_id);

            //Secondly, update GROUPS row in USERS
            res = DB_Connection.prepareConnection().executeQuery("SELECT groups FROM users WHERE ID=" + user_id);
            res.next();
            String groups = res.getString("groups");
            String list_splitted2[] = groups.split(";");

            for(int i = 0; i < list_splitted2.length; i++){
                if(list_splitted2[i].equals(gr_id)) list_splitted2[i] = "";//""
            }
            groups = String.join(";", list_splitted2);
            groups += ";";

            StringBuilder retval_group = new StringBuilder();

            char[] stringamy2=groups.toCharArray();
            for(int k=0; k<stringamy2.length; k++){

                if(stringamy2[k]==';' && (k+1)<stringamy2.length){
                    if(stringamy2[k+1]==';') continue;
                }
                retval_group.append(stringamy2[k]);
            }
            DB_Connection.prepareConnection().executeUpdate("UPDATE users SET groups='" +retval_group+ "' WHERE ID=" +user_id);
        }catch (Exception ignored){}
        DB_Connection.close_conn();
    }//-----------------------------------------------------------------------------------
} //end Groups


