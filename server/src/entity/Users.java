package entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class Users {
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
    //-----------------------------------------------------------------------------------
    //INSERT new USER
    public static void set_user(String nome, String cognome, String email, String username, String password, String ip) {
        try {
            DB_Connection.prepareConnection().executeUpdate("INSERT into " +
                    "users(FNAME, LNAME, EMAIL, USERNAME, PASSWORD, STATUS, PORT1, PORT2, IP) " +
                    "values('"+nome+"','"+cognome+"','"+email+"','"+username+"','"+password+"','Hello World','1050','1051','"+ip+"')");
        } catch (SQLException e) {e.printStackTrace();}
        DB_Connection.close_conn();
    }//-----------------------------------------------------------------------------------
    //GET USER STATUS
    public static String get_status(String email) {
        String status = "";
        try {
            ResultSet res = DB_Connection.prepareConnection().executeQuery("SELECT STATUS FROM users WHERE EMAIL= '" + email + "'");
            res.next();
            status = res.getString("STATUS");
        } catch (SQLException e) {e.printStackTrace();}
        DB_Connection.close_conn();
        return status;
    }//-----------------------------------------------------------------------------------
    // GET ID USER with email
    public static int get_user_id(String email) {
        int Id = 0;
        try {
            ResultSet res = DB_Connection.prepareConnection().executeQuery("SELECT ID FROM users WHERE EMAIL= '" + email + "'");
            res.next();
            Id = res.getInt("ID");
        } catch (SQLException e) {e.printStackTrace();}
        DB_Connection.close_conn();
        return Id;
    }//-----------------------------------------------------------------------------------
    // GET ID USER by IP
    public static String get_user_id_by_IP(String ip) {
        String ID ="";
        try {
            ResultSet res = DB_Connection.prepareConnection().executeQuery("SELECT ID FROM users WHERE IP= '" + ip + "'");
            res.next();
            ID = String.valueOf(res.getInt("ID"));
        } catch (SQLException e) {e.printStackTrace();}
        DB_Connection.close_conn();
        return ID;
    }//-----------------------------------------------------------------------------------
    // GET ID USER with username
    public static int get_user_id_by_username(String username) {
        int id = 0;
        try {
            ResultSet res = DB_Connection.prepareConnection().executeQuery("SELECT ID FROM users WHERE USERNAME= '" + username + "'");
            res.last();
            id = res.getInt("ID");
        } catch (SQLException e) {e.printStackTrace();}
        DB_Connection.close_conn();
        return id;
    }//-----------------------------------------------------------------------------------
    //Get USER NAME with email
    public static String get_name_by_EMAIL(String email) {
        String nome ="";
        try {
            ResultSet res = DB_Connection.prepareConnection().executeQuery("SELECT FNAME FROM users WHERE EMAIL= '" + email + "'");
            res.next();
            nome = res.getString("FNAME");
        } catch (SQLException e) {e.printStackTrace();}
        DB_Connection.close_conn();
        return nome;
    }//-----------------------------------------------------------------------------------
    // Get EMAIL with ID
    public static String get_email(String ID) {
        String email ="";
        try {
            ResultSet res = DB_Connection.prepareConnection().executeQuery("SELECT EMAIL FROM users WHERE ID= '" + ID + "'");
            res.next();
            email = res.getString("EMAIL");
        } catch (SQLException e) {e.printStackTrace();}
        DB_Connection.close_conn();
        return email;
    }//-----------------------------------------------------------------------------------
    // Get USER NAME with ID
    public static String get_name_by_ID(String ID) {
        String name ="";
        try {
            ResultSet res = DB_Connection.prepareConnection().executeQuery("SELECT FNAME FROM users WHERE ID=" + ID);
            res.next();
            name = res.getString("FNAME");
        } catch (SQLException e) {e.printStackTrace();}
        DB_Connection.close_conn();
        return name;
    }//-----------------------------------------------------------------------------------
    //Get USER LNAME with email
    public static String get_user_cognome(String email) {
        String lname = "";
        try {
            ResultSet res = DB_Connection.prepareConnection().executeQuery("SELECT LNAME FROM users WHERE EMAIL= '" + email + "'");
            res.next();
            lname = res.getString("LNAME");
        } catch (SQLException e) {e.printStackTrace();}
        DB_Connection.close_conn();
        return lname;
    }//-----------------------------------------------------------------------------------
    //Get "1" if EMAIL exist or "0" if email does not exist
    public static int check_email(String email_insert) {
        int sizeEmail = 0;
        try {
            ResultSet res = DB_Connection.prepareConnection().executeQuery("SELECT * FROM users WHERE EMAIL = '" + email_insert + "'");
            res.last();
            sizeEmail = res.getRow();
        } catch (SQLException e) {e.printStackTrace();}
        DB_Connection.close_conn();
        return sizeEmail;
    }//-----------------------------------------------------------------------------------
    //GET USER USERNAME with email
    public static String get_user_username(String email) {
        String username = "";
        try {
            ResultSet res = DB_Connection.prepareConnection().executeQuery("SELECT USERNAME FROM users WHERE EMAIL= '" + email + "'");
            res.next();
            username = res.getString("USERNAME");
        } catch (SQLException e) {e.printStackTrace();}
        DB_Connection.close_conn();
        return username;
    }//-----------------------------------------------------------------------------------
    // GET USER USERNAME with ID
    public static String get_user_username_by_ID(String ID) {
        String username = "";
        try {
            ResultSet res = DB_Connection.prepareConnection().executeQuery("SELECT USERNAME FROM users WHERE ID= '" + ID + "'");
            res.next();
            username = res.getString("USERNAME");
        } catch (SQLException e) {e.printStackTrace();}
        DB_Connection.close_conn();
        return username;
    }//-----------------------------------------------------------------------------------
    //Return "1" if username exist or "0" if it does not exist
    public static int check_username(String username) {
        int sizeUsername = 0;
        try {
            ResultSet res = DB_Connection.prepareConnection().executeQuery("SELECT USERNAME FROM users WHERE USERNAME = '" + username + "'");
            res.last();
            sizeUsername = res.getRow();
        } catch (SQLException e) {e.printStackTrace();}
        DB_Connection.close_conn();
        return sizeUsername;
    }//-----------------------------------------------------------------------------------
    // CHECK ip and return 1 if they are the same, 0 if they are different.
    public static int check_ip(String ip, String email) {
        int isSameIP = 0;
        String ip_in_database = "";
        try {
            ResultSet res = DB_Connection.prepareConnection().executeQuery(
                    "SELECT IP FROM users WHERE EMAIL='" + email + "' AND IP='" + ip + "'");
            res.next();
            ip_in_database = res.getString("IP");

            if(ip_in_database.equals(ip)) isSameIP = 1;

        } catch (SQLException ignored) {}
        DB_Connection.close_conn();
        return isSameIP;
    }//-----------------------------------------------------------------------------------
    //GET USER PASSWORD and return it.
    public static String get_user_password(String email) {
        String password = "";
        try {
            ResultSet res = DB_Connection.prepareConnection().executeQuery("SELECT PASSWORD FROM users WHERE EMAIL= '" + email + "'");
            res.next();
            password = res.getString("PASSWORD");
        } catch (SQLException e) {e.printStackTrace();}
        DB_Connection.close_conn();
        return password;
    }//-----------------------------------------------------------------------------------
    // GET IP_ADDR by ID
    public static String get_IP_ADDR(String ID) {
        String ip_addr = "";
        try{
            ResultSet res = DB_Connection.prepareConnection().executeQuery("SELECT IP FROM users WHERE ID= '" + ID + "'");
            res.next();
            ip_addr = res.getString("IP");
        } catch (SQLException e) {e.printStackTrace();}
        DB_Connection.close_conn();
        return ip_addr;
    }//-----------------------------------------------------------------------------------
    // GET PORT1 by ID
    public static String get_port1(String ID) {
        String port = "";
        try{
            ResultSet res = DB_Connection.prepareConnection().executeQuery("SELECT PORT1 FROM users WHERE ID= '" + ID + "'");
            res.next();
            port = res.getString("PORT1");
        } catch (SQLException e) {e.printStackTrace();}
        DB_Connection.close_conn();
        return port;
    }//-----------------------------------------------------------------------------------
    // GET PORT2 by ID
    public static String get_port2(String ID) {
        String port = "";
        try{
            ResultSet res = DB_Connection.prepareConnection().executeQuery("SELECT PORT2 FROM users WHERE ID= '" + ID + "'");
            res.next();
            port = res.getString("PORT2");
        } catch (SQLException e) {e.printStackTrace();}
        DB_Connection.close_conn();
        return port;
    }//-----------------------------------------------------------------------------------
    // GET count_tries by EMAIL
    public static String get_count_tries(String email) {
        int count_tries = 0;
        try{
            ResultSet res = DB_Connection.prepareConnection().executeQuery("SELECT COUNT_TRIES FROM users WHERE EMAIL= '" + email + "'");
            res.next();
            count_tries = res.getInt("COUNT_TRIES");
        } catch (SQLException e) {e.printStackTrace();}
        DB_Connection.close_conn();
        return String.valueOf(count_tries);
    }//-----------------------------------------------------------------------------------
    // GET ban_Date by EMAIL
    public static String get_ban_date(String email) {
        String date = "";
        try{
            ResultSet res = DB_Connection.prepareConnection().executeQuery("SELECT BAN_DATE FROM users WHERE EMAIL= '" + email + "'");
            res.next();
            date = res.getString("BAN_DATE");
        } catch (SQLException e) {e.printStackTrace();}
        DB_Connection.close_conn();
        return date;
    }//-----------------------------------------------------------------------------------
    public static String get_all_groups(String id) {
        String groupList = null;
        try {
            ResultSet res = DB_Connection.prepareConnection().executeQuery("SELECT GROUPS FROM users WHERE ID=" + id);
            res.next();
            groupList = res.getString("GROUPS");
        } catch (SQLException e) {e.printStackTrace();}
        DB_Connection.close_conn();
        return groupList;
    }//-----------------------------------------------------------------------------------
    public static String get_all_channels(String id) {
        String channelList = null;
        try {
            ResultSet res = DB_Connection.prepareConnection().executeQuery("SELECT CHANNELS FROM users WHERE ID=" + id);
            res.next();
            channelList = res.getString("CHANNELS");
        } catch (SQLException e) {e.printStackTrace();}
        DB_Connection.close_conn();
        return channelList;
    }//-----------------------------------------------------------------------------------
    //Return "1" if PASSWORD exists and "0" if password does not exist
    public static int check_password(String email, String password) {
        int result = 0;
        try {
            Statement cmd = DB_Connection.prepareConnection();
            ResultSet res = cmd.executeQuery("SELECT PASSWORD FROM users WHERE EMAIL= '" + email + "'");
            res.next();
            if (res.getString("PASSWORD").equals(password)) result = 1;
        } catch (SQLException e) {e.printStackTrace();}
        DB_Connection.close_conn();
        return result;
    }//-----------------------------------------------------------------------------------
    //UPDATE FNAME by EMAIL
    public static void update_fname(String fname, String email){
        try {
            DB_Connection.prepareConnection().executeUpdate("UPDATE users SET FNAME='"+fname+"' WHERE EMAIL = '"+email+"' ");
        }catch (Exception e){e.printStackTrace();}
        DB_Connection.close_conn();
    }//-----------------------------------------------------------------------------------
    //UPDATE LNAME by EMAIL
    public static void update_lname(String lname, String email){
        try {
            DB_Connection.prepareConnection().executeUpdate("UPDATE users SET LNAME='"+lname+"' WHERE EMAIL = '"+email+"' ");
        }catch (Exception e){e.printStackTrace();}
        DB_Connection.close_conn();
    }//-----------------------------------------------------------------------------------
    //UPDATE PASSWORD by EMAIL
    public static void update_password(String password, String email){
        try {
            DB_Connection.prepareConnection().executeUpdate("UPDATE  users SET PASSWORD ='"+password+"' WHERE EMAIL = '"+email+"' ");
        }catch (Exception e){e.printStackTrace();}
        DB_Connection.close_conn();
    }//-----------------------------------------------------------------------------------
    //UPDATE STATUS
    public static void set_status(String text, String email) {
        try {
            DB_Connection.prepareConnection().executeUpdate("UPDATE users SET STATUS='"+ text + "' WHERE EMAIL= '" + email + "'");
        } catch (SQLException e) {e.printStackTrace();}
        DB_Connection.close_conn();
    }//-----------------------------------------------------------------------------------
    //UPDATE IP by ID
    public static void update_ip(String ip, String ID){
        try {
            DB_Connection.prepareConnection().executeUpdate("UPDATE users SET IP='"+ip+"' WHERE ID = "+ID);
        }catch (Exception e){e.printStackTrace();}
        DB_Connection.close_conn();
    }//-----------------------------------------------------------------------------------
    // UPDATE IP by EMAIL
    public static void update_ip_by_email(String ip, String email){
        try {
            DB_Connection.prepareConnection().executeUpdate("UPDATE users SET IP='"+ip+"' WHERE EMAIL ='" + email + "'");
        }catch (Exception e){e.printStackTrace();}
        DB_Connection.close_conn();
    }//-----------------------------------------------------------------------------------
    //UPDATE PORT1
    public static void update_port1(String port1, String email){
        try {
            DB_Connection.prepareConnection().executeUpdate("UPDATE  users SET PORT1='"+port1+"' WHERE EMAIL = '"+email+"'");
        }catch (Exception e){e.printStackTrace();}
        DB_Connection.close_conn();
    }//-----------------------------------------------------------------------------------
    //UPDATE PORT2
    public static void update_port2(String port2, String email){
        try {
            DB_Connection.prepareConnection().executeUpdate("UPDATE users SET PORT2='"+port2+"' WHERE EMAIL = '"+email+"' ");
        }catch (Exception e){e.printStackTrace();}
        DB_Connection.close_conn();
    }//-----------------------------------------------------------------------------------
    // UPDATE count_tries by EMAIL
    public static void update_count_tries(String email) {
        int count_tries = 0;
        try{
            ResultSet res = DB_Connection.prepareConnection().executeQuery("SELECT COUNT_TRIES FROM users WHERE EMAIL='" + email +"'");
            res.next();
            int count = res.getInt("COUNT_TRIES");
            count += 1;
            DB_Connection.prepareConnection().executeUpdate("UPDATE users SET COUNT_TRIES=" + count + " WHERE EMAIL='"+email+"'");

            if (count == 10 || count == 20 || count == 30) update_ban_date(email);
        } catch (SQLException e) {e.printStackTrace();}
        DB_Connection.close_conn();
    }//-----------------------------------------------------------------------------------
    // SET to 0 count_tries by EMAIL
    public static void reset_count_tries(String email) {
        try{
            DB_Connection.prepareConnection().executeUpdate("UPDATE users SET COUNT_TRIES=0 WHERE EMAIL='"+email+"'");
        } catch (SQLException e) {e.printStackTrace();}
        DB_Connection.close_conn();
    }//-----------------------------------------------------------------------------------
    // Compare two date to check if ban is expired or not!
    public static int amIBanned(String email){
        String current_time = simpleDateFormat.format(new Timestamp(System.currentTimeMillis()));
        int count_tries;
        int am_I_Banned = 0;

        try{
            ResultSet res = DB_Connection.prepareConnection().executeQuery("SELECT COUNT_TRIES, BAN_DATE FROM users WHERE EMAIL='" + email + "'");
            res.last();
            String ban_time = res.getString("BAN_DATE");
            count_tries = res.getInt("COUNT_TRIES");

            //Convert date strings to Date obj!
            Date current_time_date = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").parse(current_time);
            Date ban_time_date = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").parse(ban_time);

            long diffInSeconds = (current_time_date.getTime() - ban_time_date.getTime()) /1000;
            System.out.println("Second passed from ban: " + diffInSeconds);

            if(count_tries < 10) am_I_Banned = 0;
            else if(count_tries > 9 && count_tries < 20 && diffInSeconds > 300) am_I_Banned = 0;        //5 min ban finish!
            else if(count_tries > 9 && count_tries < 20 && diffInSeconds < 300) am_I_Banned = 1;        //I am banned yet.
            else if(count_tries > 19 && count_tries < 30 && diffInSeconds > 604800) am_I_Banned = 0;    //1 WEEK ban finish!
            else if(count_tries > 19 && count_tries < 30 && diffInSeconds < 604800) am_I_Banned = 1;    //I am banned yet.
            else if(count_tries > 29 && diffInSeconds > 5184000) am_I_Banned = 0;                       //2 MONTHS ban finish!
            else if(count_tries > 29 && diffInSeconds < 5184000) am_I_Banned = 1;                       //I am banned yet.

        }catch(SQLException | ParseException e){e.printStackTrace();}
        DB_Connection.close_conn();
        return am_I_Banned;
    }//-----------------------------------------------------------------------------------
    //UPDATE USER GROUP LIST
    public static void update_user_group_list(String group_name, String user_id){
        try{
            String lastChar = "";

            //First of all, let's get group id.
            ResultSet res = DB_Connection.prepareConnection().executeQuery("SELECT ID FROM groups WHERE GR_NAME='" + group_name + "'");
            res.next();
            String group_id = String.valueOf(res.getInt("ID"));

            //Secondly, get userlist for this group.
            ResultSet res2 = DB_Connection.prepareConnection().executeQuery("SELECT groups FROM users WHERE ID=" + user_id);
            res2.next();
            String groups_list = res2.getString("groups");

            //Delete the "?" symbol near the user_id.
            String list_splitted[] = groups_list.split(";");

            for(int i = 0; i < list_splitted.length; i++){
                if(list_splitted[i].equals(group_id + "?")) list_splitted[i] = group_id;
            }

            //Join again back the full string and update DB
            String updated_groups_list = String.join(";", list_splitted);
            updated_groups_list += ";";

            DB_Connection.prepareConnection().executeUpdate("UPDATE users SET groups='" + updated_groups_list + "' WHERE ID=" + user_id);
        } catch (SQLException e) {e.printStackTrace();}
        DB_Connection.close_conn();
    }//-----------------------------------------------------------------------------------
    // UPDATE USER GROUP LIST
    public static void update_user_group_list_negative(String group_name, String user_id){
        try{
            String lastChar= "";

            //First of all, let's get group id.
            ResultSet res = DB_Connection.prepareConnection().executeQuery("SELECT ID FROM groups WHERE GR_NAME='" + group_name + "'");
            res.next();
            String group_id = String.valueOf(res.getInt("ID"));

            //Secondly, get userlist for this group.
            ResultSet res2 = DB_Connection.prepareConnection().executeQuery("SELECT groups FROM users WHERE ID='" + user_id + "'");
            res2.next();
            String groups_list = res2.getString("groups");

            //Delete the "?" symbol near the user_id.
            String list_splitted[] = groups_list.split(";");
            for(int i = 0; i < list_splitted.length; i++){

                lastChar = String.valueOf(list_splitted[i].charAt(list_splitted[i].length()-1));
                if(lastChar.equals("?") && list_splitted[i].substring(0, list_splitted[i].length()-1).equals(group_id)){
                    list_splitted[i] = "";
                }
            }

            //Join again back the full string and update DB
            String updated_groups_list = String.join(";", list_splitted);

            DB_Connection.prepareConnection().executeUpdate("UPDATE users SET groups='" + updated_groups_list + "'" +
                    "WHERE ID='" + user_id + "'");
        } catch (SQLException e) {e.printStackTrace();}
        DB_Connection.close_conn();
    }//-----------------------------------------------------------------------------------
    //UPDATE USER CHANNEL LIST when HE ACCEPTS
    public static void update_user_channel_list(String ch_name, String user_id){
        try{
            String lastChar= "";

            //First of all, let's get group id.
            ResultSet res = DB_Connection.prepareConnection().executeQuery("SELECT ID FROM channels WHERE CH_NAME='" + ch_name + "'");
            res.next();
            String ch_id = String.valueOf(res.getInt("ID"));

            //Secondly, get userlist for this group.
            ResultSet res2 = DB_Connection.prepareConnection().executeQuery("SELECT channels FROM users WHERE ID='" + user_id + "'");
            res2.next();
            String channel_list = res2.getString("channels");

            //Delete the "?" symbol near the user_id.
            String list_splitted[] = channel_list.split(";");
            for(int i = 0; i < list_splitted.length; i++){

                lastChar = String.valueOf(list_splitted[i].charAt(list_splitted[i].length()-1));
                if(lastChar.equals("?") && list_splitted[i].substring(0, list_splitted[i].length()-1).equals(ch_id)){
                    list_splitted[i] = ch_id;
                }
            }

            //Join again back the full string and update DB
            String updated_ch_list = String.join(";", list_splitted);

            DB_Connection.prepareConnection().executeUpdate("UPDATE users SET groups='" + updated_ch_list + "' WHERE ID='" + user_id + "'");
        } catch (SQLException e) {e.printStackTrace();}
        DB_Connection.close_conn();
    }//-----------------------------------------------------------------------------------
    // UPDATE USER CHANNEL LIST when HE ACCEPTS
    public static void update_user_channel_list_negative(String ch_name, String user_id){
        try{
            String lastChar= "";

            //First of all, let's get group id.
            ResultSet res = DB_Connection.prepareConnection().executeQuery("SELECT ID FROM channels WHERE CH_NAME='" + ch_name + "'");
            res.next();
            String ch_id = String.valueOf(res.getInt("ID"));

            //Secondly, get userlist for this group.
            ResultSet res2 = DB_Connection.prepareConnection().executeQuery("SELECT channels FROM users WHERE ID='" + user_id + "'");
            res2.next();
            String channel_list = res2.getString("channels");

            //Delete the "?" symbol near the user_id.
            String list_splitted[] = channel_list.split(";");
            for(int i = 0; i < list_splitted.length; i++){

                lastChar = String.valueOf(list_splitted[i].charAt(list_splitted[i].length()-1));
                if(lastChar.equals("?") && list_splitted[i].substring(0, list_splitted[i].length()-1).equals(ch_id)){
                    list_splitted[i] = "";
                }
            }

            //Join again back the full string and update DB
            String updated_ch_list = String.join(";", list_splitted);

            DB_Connection.prepareConnection().executeUpdate("UPDATE users SET groups='" + updated_ch_list + "' WHERE ID='" + user_id + "'");
        } catch (SQLException e) {e.printStackTrace();}
        DB_Connection.close_conn();
    }//-----------------------------------------------------------------------------------
    // UPDATE USER CHANNEL LIST
    public static void update_channel_list(String user_id, String channel_name){
        try{
            ResultSet res = DB_Connection.prepareConnection().executeQuery("SELECT id FROM channels WHERE CH_NAME='" + channel_name + "'");
            res.last();
            int channel_id = res.getInt("ID");
            res = DB_Connection.prepareConnection().executeQuery("SELECT channels FROM users WHERE ID='" + user_id + "'");
            res.last();
            String updated_list = res.getString("channels") + channel_id + ";";
            DB_Connection.prepareConnection().executeUpdate("UPDATE users SET channels='" + updated_list + "' WHERE ID='" + user_id + "'");
        } catch (SQLException e) {e.printStackTrace();}
        DB_Connection.close_conn();
    }//-----------------------------------------------------------------------------------
    // UPDATE ban_Date by EMAIL
    public static void update_ban_date(String email) {
        String date = simpleDateFormat.format(new Timestamp(System.currentTimeMillis()));
        try{
            DB_Connection.prepareConnection().executeUpdate("UPDATE users SET BAN_DATE='" + date + "' WHERE EMAIL='" + email + "'");
        } catch (SQLException e) {e.printStackTrace();}
        DB_Connection.close_conn();
    }//-----------------------------------------------------------------------------------
    //UPDATE TOKEN
    public static void update_token(String token, String email){
        try {
            DB_Connection.prepareConnection().executeUpdate("UPDATE  users SET TOKEN='"+token+"' WHERE EMAIL = '"+email+"' ");
        }catch (Exception e){e.printStackTrace();}
        DB_Connection.close_conn();
    }//-----------------------------------------------------------------------------------
    //UPDATE IP by ID
    public static void delete_ip(String ID){
        try {
            DB_Connection.prepareConnection().executeUpdate("UPDATE users SET IP='0.0.0.0' WHERE ID =" + ID);
        }catch (Exception e){e.printStackTrace();}
        DB_Connection.close_conn();
    }//-----------------------------------------------------------------------------------
    //SELECT EMAIL with TOKEN
    public static String get_email_token(String token) {
        String Email = "";
        try {
            ResultSet res = DB_Connection.prepareConnection().executeQuery("SELECT EMAIL FROM users WHERE TOKEN= '" + token + "'");
            res.next();
            Email = res.getString("EMAIL");
        } catch (SQLException e) {e.printStackTrace();}
        DB_Connection.close_conn();
        return Email;
    }//-----------------------------------------------------------------------------------
    //UPDATE IMG by EMAIL
    public static void update_img(String img, String email){
        try {
            DB_Connection.prepareConnection().executeUpdate("UPDATE users SET IMG_NAME='"+img+"' WHERE EMAIL = '"+email+"' ");
        }catch (Exception e){e.printStackTrace();}
        DB_Connection.close_conn();
    }//-----------------------------------------------------------------------------------
    //GET IMG by EMAIL
    public static String get_img(String email) {
        String img = "";
        try {
            ResultSet res = DB_Connection.prepareConnection().executeQuery("SELECT IMG_NAME FROM users WHERE EMAIL= '" + email + "'");
            res.next();
            img = res.getString("IMG_NAME");
        } catch (SQLException e) {e.printStackTrace();}
        DB_Connection.close_conn();
        return img;
    }//-----------------------------------------------------------------------------------
} //end Users