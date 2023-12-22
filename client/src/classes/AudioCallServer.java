package classes;

import gui.CallScreen;

import javax.sound.sampled.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class AudioCallServer implements Runnable {
    private Socket m_Socket = null;

    private DataInputStream m_dataInputStream = null;
    private DataOutputStream m_dataOutputStream = null;

    private AudioFormat m_audioFormat = new AudioFormat(AudioConstants.ENCODING,
            AudioConstants.SAMPLERATE,
            AudioConstants.SAMPLESIZEINBITS,
            AudioConstants.CHANNELS,
            AudioConstants.FRAMESIZE,
            AudioConstants.FRAMERATE,
            AudioConstants.ISBIGENDIAN);

    private SourceDataLine m_line = null;
    private TargetDataLine m_targetLine = null;

    private CallScreen CS = null;

    public boolean continueLoop = true;
    public boolean isMuted = false;
    public AudioCallServer(CallScreen callScreen) {this.CS = callScreen;}
    //-----------------------------------------------------------------------------------
    private void closeLine() {
        try {
            m_line.drain(); m_line.close(); m_targetLine.flush(); m_targetLine.close();
            m_dataInputStream.close(); m_dataOutputStream.close(); m_Socket.close();
        } catch (IOException e) {e.printStackTrace();}
    }//-----------------------------------------------------------------------------------
    @Override
    public void run() {
        try{

            m_Socket = new ServerSocket(1090).accept();
            m_dataInputStream = new DataInputStream( m_Socket.getInputStream() );
            m_dataOutputStream = new DataOutputStream(m_Socket.getOutputStream());

            //Create lines
            DataLine.Info targetInfo = new DataLine.Info(SourceDataLine.class, m_audioFormat);
            m_line = (SourceDataLine) AudioSystem.getLine(targetInfo);
            m_line.open(m_audioFormat);

            DataLine.Info targetInfo2 = new DataLine.Info(TargetDataLine.class, m_audioFormat, 40960);
            m_targetLine = (TargetDataLine) AudioSystem.getLine(targetInfo2);
            m_targetLine.open(m_audioFormat, 40960);

            m_line.start(); m_targetLine.start();

            byte[] abData = new byte[128000];
            byte[] abBuffer = new byte[40960];
            byte[] emptyArray = new byte[40960]; //This will be used to mute mic!! I will transfer an empty array to mute mic ;-)

            while (continueLoop){
                int nBytesRead = m_dataInputStream.read(abData, 0, 128000);
                if (nBytesRead >= 0) m_line.write(abData, 0, nBytesRead);

                m_targetLine.read(abBuffer, 0, 40960);
                if(!isMuted) m_dataOutputStream.write(abBuffer, 0, 40960);
                else m_dataOutputStream.write(emptyArray, 0 ,40960);
            }
            closeLine();
        } catch (SocketException SE) {  closeLine();}
        catch (IOException | LineUnavailableException e) {e.printStackTrace();}
    }//-----------------------------------------------------------------------------------
} //end