package classes;

import gui.HomeScreen;
import socket.SocketSender;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonMethods {
    //-------------------------------------------------------------------------
    //Used to send Encrypted passwords!
    public static String hashPassword(String password) {
        MessageDigest md = null;

        try {md = MessageDigest.getInstance("MD5");}
        catch (NoSuchAlgorithmException e) {e.printStackTrace();
        }
        md.update(password.getBytes());
        byte byteData[] = md.digest();
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            String hex = Integer.toHexString(0xff & byteData[i]);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
    //-------------------------------------------------------------------------
    //Reduce IMGs size but maintain proportions!
    public BufferedImage createResizedCopy(Image originalImage, int scale, boolean preserveAlpha){
        int width = originalImage.getWidth(null);
        int height = originalImage.getHeight(null);
        int scaledWidth, scaledHeight;
        BufferedImage subScaledImg;

        if (width>height){scaledWidth=scale*width/height;   scaledHeight=scale;}
        else if(width<height){scaledHeight=scale*height/width;  scaledWidth=scale;
        }else {scaledWidth=scale;   scaledHeight=scale;}

        int imageType = preserveAlpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, imageType);
        Graphics2D g = scaledBI.createGraphics();

        if (preserveAlpha) {g.setComposite(AlphaComposite.Src);}

        g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
        g.dispose();

        int subWidth = scaledBI.getWidth();
        int subHeight = scaledBI.getHeight();

        //Cut and center the image
        if(subWidth > subHeight) subScaledImg = scaledBI.getSubimage((subWidth-80)/2, 0,80,80);
        else if(subWidth<subHeight) subScaledImg = scaledBI.getSubimage(0, (subHeight-80)/2,80,80);
        else subScaledImg = scaledBI.getSubimage(0, 0,80,80);

        return subScaledImg;
    }
    //-------------------------------------------------------------------------
    //Append string inside HTML code in JTextPane!
    public void append_new_message(String cleanText, JTextPane txtAreaCHAT, String myUsername){
        String timeStamp = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
        HTMLDocument doc = (HTMLDocument) txtAreaCHAT.getStyledDocument();
        try {doc.insertAfterEnd(doc.getCharacterElement(doc.getLength()),
                    formatted_text(timeStamp + " - (" + myUsername + "): " + cleanText + "<br>", myUsername));
        }catch (BadLocationException | IOException ignored) {}
    }
    //-------------------------------------------------------------------------
    public void append_file(String cleanText, JTextPane txtAreaCHAT, String myUsername){
        String timeStamp = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
        HTMLDocument doc = (HTMLDocument) txtAreaCHAT.getStyledDocument();
        try {doc.insertAfterEnd(doc.getCharacterElement(doc.getLength()),timeStamp + " - (" + myUsername + "): " + cleanText + "<br>");
        }catch (BadLocationException | IOException ignored) {}
    }//-------------------------------------------------------------------------
    //This method is going to replace and format my messages for HTML!
    public String formatted_text(String txtToHTML, String user_username){
        //1. --- Check and REPLACE spaces AND new lines IF and ONLY IF it is a code pasted in txtAreaSEND! ---
        if (Pattern.compile("(^[[\\s]]{2,})|(\n[[\\s]]{2,})").matcher(txtToHTML).find()) {
            txtToHTML = txtToHTML.replaceAll(" ", "&nbsp ");
            txtToHTML = "<font face='verdana' color='green'>" + txtToHTML + "</font>";
        }
        //2. --- Italic - Replace "^" with italic strings. ---
        if (Pattern.compile("(\\s\\^.*?)\\^").matcher(txtToHTML).find()) {
            txtToHTML = txtToHTML.replaceAll("\\s\\^", "<i>");
            txtToHTML = txtToHTML.replaceAll("\\^", "</i>");
        }
        //3. --- URLs - Find "HTTPS|FTP|FILE" and replace it in the right way! ---
        Matcher links = Pattern.compile("\\b((https?|ftp|file)\\://[a-zA-Z0-9\\-\\.]+\\.[a-zA-Z]{2,3}(/\\S*)?)\\b").matcher(txtToHTML);
        if(Pattern.compile("<[^<>]+>").matcher(txtToHTML).find()) { //if it's a tag, it is 100% a download link for files!
            //DO nothing, tag is already created and ready to show preview.
            System.out.println("Tag per file");
        }else if (links.find()){ //else, if it's not a tag, it's a link!
            txtToHTML = txtToHTML.replaceAll("\\b((https?|ftp|file)\\://[a-zA-Z0-9\\-\\.]+\\.[a-zA-Z]{2,3}(/\\S*)?)\\b",
                    "<a href='" + links.group(1) + "'>" + links.group(1) + "</a>");
            System.out.println("Links");
        }
        //4. --- Find and Replace emoji ---
        if (Pattern.compile("\\;\\-\\)|\\<\\d|\\*\\-\\*|\\:\\-\\(|\\:\\-[p|P]|[T]\\-[T]").matcher(txtToHTML).find()) {
            txtToHTML = txtToHTML.replaceAll("\\;\\-\\)",
                    "<img src='" + getClass().getResource("/img/emoji/emoji1.png") + "'>");
            txtToHTML = txtToHTML.replaceAll("\\<[3]",
                    "<img src='" + getClass().getResource("/img/emoji/emoji2.gif") + "'>");
            txtToHTML = txtToHTML.replaceAll("\\*\\-\\*",
                    "<img src='" + getClass().getResource("/img/emoji/emoji3.png") + "'>");
            txtToHTML = txtToHTML.replaceAll("\\:\\-\\(",
                    "<img src='" + getClass().getResource("/img/emoji/emoji4.png") + "'>");
            txtToHTML = txtToHTML.replaceAll("\\:\\-[P]",
                    "<img src='" + getClass().getResource("/img/emoji/emoji5.png") + "'>");
            txtToHTML = txtToHTML.replaceAll("[T]\\-[T]",
                    "<img src='" + getClass().getResource("/img/emoji/emoji6.png") + "'>");
        }
        //5. --- Find "*" and replace it with BOLD ---
        if (Pattern.compile("\\s\\*(.*?)\\*").matcher(txtToHTML).find()) {
            txtToHTML = txtToHTML.replaceAll("\\s\\*", "<b>");
            txtToHTML = txtToHTML.replaceAll("\\*", "</b>");
        }
        //6. --- Find "_" and replace it with UNDERLINE ---
        if (Pattern.compile("\\s\\_(.*?)\\_").matcher(txtToHTML).find()) {
            txtToHTML = txtToHTML.replaceAll("\\s\\_", "<u>");
            txtToHTML = txtToHTML.replaceAll("\\_", "</u>");
        }
        //7. --- Find "~" and replace it with STRIKETHROUGH ---
        if (Pattern.compile("\\s\\~(.*?)\\~").matcher(txtToHTML).find()) {
            txtToHTML = txtToHTML.replaceAll("\\s\\~", "<s>");
            txtToHTML = txtToHTML.replaceAll("\\~", "</s>");
        }
        //8. --- REPLACE "\n" with "<br>"!
        txtToHTML = txtToHTML.replaceAll("\n"," &nbsp<br>");
        return txtToHTML;
    }
    //-------------------------------------------------------------------------
    //Load Friend List!
    public static void load_friend_list(HomeScreen HS){
        String friend_list = new SocketSender().queryTheServer("retrieve_list-" + HS.user_id);
        String username_splitted[] = friend_list.split("@");
        HS.friendModel.removeAllElements();

        for (int i = 0; i < username_splitted.length; i++) HS.friendModel.addElement(username_splitted[i]);
        HS.listFriends.setModel(HS.friendModel);
    }
    //-------------------------------------------------------------------------
    //Load Group List!
    public static void load_group_list(HomeScreen HS){
        String group_list = new SocketSender().queryTheServer("retrieve_groups-" + HS.user_id);
        String groups[] = group_list.split(";");
        String group_name = "";
        HS.groupModel.removeAllElements();

        if(groups.length > 1) {
            for (int i = 1; i < groups.length; i++){

                String lastChar = String.valueOf(groups[i].charAt(groups[i].length()-1));
                if(lastChar.equals("?")){
                    group_name = new SocketSender().queryTheServer("get_gr_name-" + groups[i].substring(0,groups[i].length()-1));
                    HS.groupModel.addElement(group_name + " - Pending Request!");
                }else{
                    group_name = new SocketSender().queryTheServer("get_gr_name-" + groups[i]);
                    HS.groupModel.addElement(group_name);
                }

            }
            HS.listGroups.setModel(HS.groupModel);
        }
    }
    //-------------------------------------------------------------------------
    //Load Channel List!
    public static void load_channel_list(HomeScreen HS){
        String channel_list = new SocketSender().queryTheServer("retrieve_channels-" + HS.user_id);
        String channels[] = channel_list.split(";");
        String channel_name = "";
        HS.channelModel.removeAllElements();

        if(channels.length > 1) {
            for (int i = 1; i < channels.length; i++){

                String lastChar = String.valueOf(channels[i].charAt(channels[i].length()-1));
                if(lastChar.equals("?")) {
                    channel_name = new SocketSender().queryTheServer("get_ch_name-" + channels[i].substring(0, channels[i].length()-1));
                    HS.channelModel.addElement(channel_name + " - Pending Request!");
                }else{
                    channel_name = new SocketSender().queryTheServer("get_ch_name-" + channels[i]);
                    HS.channelModel.addElement(channel_name);
                }

            }
            HS.listChannels.setModel(HS.channelModel);
        }
    }
    //-------------------------------------------------------------------------
    //This method will manage GUI interface when user clicks on different Jlist!
    //isAlreadyAccepted == 0 (not accepted yet), isAlreadyAccepted == 1 (accepted), isAlreadyAccepted == 2 (blocked)
    public static void GUIadapter(HomeScreen HS, String whichJlist, int isAlreadyAccepted){
        Color white = new Color(255,255,255);
        Color gray = new Color(119,119,119);

        if(whichJlist.equals("friend") && isAlreadyAccepted == 1){ //They are friends.
            //TRUE
            HS.txtAreaCHAT.setEnabled(true); HS.txtAreaSEND.setEnabled(true); HS.btnEmoticons.setEnabled(true);
            HS.txtAreaCHAT.setBackground(white); HS.txtAreaSEND.setBackground(white); HS.txtAreaSEND.setDropTarget(HS.dt);
            HS.lblName2.setVisible(true); HS.lblAvatar2.setVisible(true); HS.lblStatus2.setVisible(true); HS.lblCustomName.setVisible(true);
            HS.lblCall.setVisible(true); HS.lblVideoCall.setVisible(true);
            //FALSE
            HS.lblMinus.setVisible(false); HS.lblPlus.setVisible(false);
        }else if((whichJlist.equals("group") || whichJlist.equals("channel")) && isAlreadyAccepted == 1) {
            //TRUE
            HS.txtAreaCHAT.setEnabled(true); HS.txtAreaSEND.setEnabled(true); HS.btnEmoticons.setEnabled(true);
            HS.txtAreaCHAT.setBackground(white); HS.txtAreaSEND.setBackground(white); HS.lblName2.setVisible(true);
            if(whichJlist.equals("group") && HS.group_adminID.equals(HS.user_id)){ //if user is group admin too
                HS.lblMinus.setVisible(true); HS.lblPlus.setVisible(true);
            }else { HS.lblMinus.setVisible(false); HS.lblPlus.setVisible(false); }
            HS.txtAreaSEND.setDropTarget(HS.dt);
            //FALSE
            HS.lblAvatar2.setVisible(false); HS.lblStatus2.setVisible(false);
            HS.lblCustomName.setVisible(false); HS.lblCall.setVisible(false); HS.lblVideoCall.setVisible(false);
        }else if(whichJlist.equals("channel") && isAlreadyAccepted == 2){
            //TRUE
            HS.txtAreaCHAT.setEnabled(true); HS.txtAreaCHAT.setBackground(white); HS.lblName2.setVisible(true);
            //FALSE
            HS.lblAvatar2.setVisible(false); HS.lblStatus2.setVisible(false); HS.txtAreaSEND.setDropTarget(null);
            HS.lblCustomName.setVisible(false); HS.lblCall.setVisible(false); HS.lblVideoCall.setVisible(false);
            HS.txtAreaSEND.setEnabled(false); HS.btnEmoticons.setEnabled(false);
            HS.txtAreaSEND.setBackground(gray); HS.lblMinus.setVisible(false); HS.lblPlus.setVisible(false);
        }

        if(isAlreadyAccepted == 0 || (whichJlist.equals("friend") && isAlreadyAccepted == 2)){
            //FALSE
            HS.txtAreaCHAT.setEnabled(false); HS.txtAreaSEND.setEnabled(false); HS.txtAreaSEND.setDropTarget(null);
            HS.btnSend.setEnabled(false); HS.btnEmoticons.setEnabled(false);
            HS.txtAreaCHAT.setBackground(gray); HS.txtAreaSEND.setBackground(gray);
            HS.lblName2.setVisible(false); HS.lblMinus.setVisible(false); HS.lblPlus.setVisible(false);
            HS.lblAvatar2.setVisible(false); HS.lblStatus2.setVisible(false);
            HS.lblCustomName.setVisible(false); HS.lblCall.setVisible(false); HS.lblVideoCall.setVisible(false);
        }

    } //end GUIadapter()
    //-------------------------------------------------------------------------
} //end CommonMethods
