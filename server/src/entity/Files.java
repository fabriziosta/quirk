package entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class Files {
    //INSERT FILE type_of_file == 0 normal file, type_of_file == 1 profile picture.
    public static int set_file(String file_name, String user_id, String type) {
        int generatedID = 0;
        try {
            Statement stmt = DB_Connection.prepareConnection();
            stmt.executeUpdate("INSERT into files(FILE_NAME, ID_USER, PROFILE_PIC)values('"+ file_name +"','"+ user_id + "',"+type+");"
                    , Statement.RETURN_GENERATED_KEYS);
            ResultSet res = stmt.getGeneratedKeys();
            res.next();
            generatedID = res.getInt(1);
        } catch (SQLException e) {e.printStackTrace();}
        DB_Connection.close_conn();
        return generatedID;
    }//-----------------------------------------------------------------------------------
    //Get FILE ID by ID_USER
    public static String get_file_id(String id_user, String type) {
        String file_id = "";
        try {
            ResultSet res = DB_Connection.prepareConnection().executeQuery("SELECT ID FROM files WHERE ID_USER='" +id_user+ "' AND PROFILE_PIC="+type);
            res.last();
            file_id = String.valueOf(res.getInt("ID"));
        } catch (SQLException e) {e.printStackTrace();}
        DB_Connection.close_conn();
        return file_id;
    }//-----------------------------------------------------------------------------------
    public static String get_last_insert_ID(String filename,String username, String type){ //[1] == filename,[2] == user_username,[3] == type_of_file
        String last_ID = "";
        String user_ID = String.valueOf(Users.get_user_id_by_username(username));
        try{
            ResultSet res = DB_Connection.prepareConnection().executeQuery(
                    "SELECT ID FROM files WHERE ID_USER='" +user_ID+ "' AND PROFILE_PIC='"+type+"' AND FILE_NAME='"+ filename +"'");
            res.last();
            last_ID = String.valueOf(res.getInt("ID"));
        }catch (Exception e){ e.printStackTrace(); }
        DB_Connection.close_conn();
        return last_ID;
    }//-----------------------------------------------------------------------------------
    //Get FILE_NAME by ID_USER
    public static String get_file_name(String id_user, String type) {
        String file_name = "";
        try {
            ResultSet res = DB_Connection.prepareConnection()
                    .executeQuery("SELECT FILE_NAME FROM files WHERE ID_USER='" +id_user+ "' AND PROFILE_PIC=" + type);
            res.last();
            file_name = res.getString("FILE_NAME");
        } catch (SQLException e) {e.printStackTrace();}
        DB_Connection.close_conn();
        return file_name;
    }//-----------------------------------------------------------------------------------
    //Get file name by ID!
    public static String get_file_name_by_ID(String file_id){
        String file_name = "";
        try {
            ResultSet res = DB_Connection.prepareConnection()
                    .executeQuery("SELECT FILE_NAME FROM files WHERE ID='" +file_id+ "'");
            res.last();
            file_name = res.getString("FILE_NAME");
        } catch (SQLException e) {e.printStackTrace();}
        DB_Connection.close_conn();
        return file_name;
    }//-----------------------------------------------------------------------------------
} //end Files
