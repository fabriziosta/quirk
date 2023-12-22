package classes;

import gui.CallScreen;

import javax.sound.sampled.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class AudioCall implements Runnable{
    private static final int INTERNAL_BUFFER_SIZE = 40960;
    private TargetDataLine m_targetLine = null;
    private SourceDataLine m_line = null;

    private Socket m_callSocket = null;
    private AudioFormat m_audioFormat = new AudioFormat(AudioConstants.ENCODING,
            AudioConstants.SAMPLERATE,
            AudioConstants.SAMPLESIZEINBITS,
            AudioConstants.CHANNELS,
            AudioConstants.FRAMESIZE,
            AudioConstants.FRAMERATE,
            AudioConstants.ISBIGENDIAN);

    private DataOutputStream m_dataOutputStream = null;
    private DataInputStream m_dataInputStream = null;

    private CallScreen CS = null;
    public boolean continueLoop = true;
    public boolean isMuted = false;
    //-----------------------------------------------------------------------------------
    public AudioCall(CallScreen callScreen, String other_user_ip) {
        this.CS = callScreen;
        try {

            //Establish connection
            m_callSocket = new Socket(other_user_ip, 1090);
            m_dataOutputStream = new DataOutputStream(m_callSocket.getOutputStream());
            m_dataInputStream = new DataInputStream( m_callSocket.getInputStream() );

            //Create lines
            DataLine.Info targetInfo = new DataLine.Info(TargetDataLine.class, m_audioFormat, INTERNAL_BUFFER_SIZE);
            m_targetLine = (TargetDataLine) AudioSystem.getLine(targetInfo);
            m_targetLine.open(m_audioFormat, INTERNAL_BUFFER_SIZE);

            DataLine.Info targetInfo2 = new DataLine.Info(SourceDataLine.class, m_audioFormat);
            m_line = (SourceDataLine) AudioSystem.getLine(targetInfo2);
            m_line.open(m_audioFormat);

            if (m_targetLine != null && m_line != null){
                System.out.println("Start audio streaming...");
                m_targetLine.start(); m_line.start();
            }

        } catch (Exception e) {e.printStackTrace();}
    }//-----------------------------------------------------------------------------------
    private void closeLine(){
        try {
            m_targetLine.close(); m_line.close();
            m_dataOutputStream.close(); m_dataInputStream.close();
            m_callSocket.close();
        } catch (IOException e) {e.printStackTrace();}
    }//-----------------------------------------------------------------------------------
    @Override
    public void run() {

        byte[] abBuffer = new byte[INTERNAL_BUFFER_SIZE];
        byte[] abData = new byte[128000];
        byte[] emptyArray = new byte[INTERNAL_BUFFER_SIZE]; //This will be used to mute mic!! I will transfer an empty array to mute mic ;-)

        try {
            while (continueLoop) {
                //Capture a block of audio and add it into the buffer AND //Send data to receiver
                m_targetLine.read(abBuffer, 0, 40960);
                if(!isMuted) m_dataOutputStream.write(abBuffer, 0, 40960);
                else m_dataOutputStream.write(emptyArray, 0 ,40960);

                int nBytesRead = m_dataInputStream.read(abData, 0, 128000);
                if (nBytesRead >= 0) m_line.write(abData, 0, nBytesRead);
            }
            closeLine();
        } catch (SocketException SE) {  closeLine();
        } catch (IOException e) {e.printStackTrace();}
    }
    //-----------------------------------------------------------------------------------
} //end AudioCall class