package gui;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class EmoticonScreen extends JFrame implements MouseListener{
    private HomeScreen HS;
    private JPanel pnlEmoticons;
    private JLabel emo1;
    private JLabel emo2;
    private JLabel emo3;
    private JLabel emo4;
    private JLabel emo5;
    private JLabel emo6;
    //-----------------------------------------------------------------------------------
    EmoticonScreen(HomeScreen myHomeScreen){
        this.HS = myHomeScreen; this.setSize(300,300); setContentPane(pnlEmoticons);
        this.setLocationRelativeTo(myHomeScreen);
        super.setIconImage(new ImageIcon(getClass().getResource("/img/icon.png")).getImage());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); HS.setEnabled(false);

        emo1.addMouseListener(this); emo2.addMouseListener(this); emo3.addMouseListener(this);
        emo4.addMouseListener(this); emo5.addMouseListener(this); emo6.addMouseListener(this);
        setVisible(true); setResizable(false); this.setAlwaysOnTop(true); this.pack();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {HS.setEnabled(true);}
        });
    }
    //-----------------------------------------------------------------------------------
    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getSource() == emo1) HS.txtAreaSEND.setText(HS.txtAreaSEND.getText() + " ;-) ");
        else if(e.getSource() == emo2) HS.txtAreaSEND.setText(HS.txtAreaSEND.getText() + " <3 ");
        else if(e.getSource() == emo3) HS.txtAreaSEND.setText(HS.txtAreaSEND.getText() + " *-* ");
        else if(e.getSource() == emo4) HS.txtAreaSEND.setText(HS.txtAreaSEND.getText() + " :-( ");
        else if(e.getSource() == emo5) HS.txtAreaSEND.setText(HS.txtAreaSEND.getText() + " :-P ");
        else if(e.getSource() == emo6) HS.txtAreaSEND.setText(HS.txtAreaSEND.getText() + " T-T ");
        HS.btnSend.setEnabled(true);
    }
    //-----------------------------------------------------------------------------------
    @Override
    public void mousePressed(MouseEvent e) {    }
    //-----------------------------------------------------------------------------------
    @Override
    public void mouseReleased(MouseEvent e) {    }
    //-----------------------------------------------------------------------------------
    @Override
    public void mouseEntered(MouseEvent e) {    }
    //-----------------------------------------------------------------------------------
    @Override
    public void mouseExited(MouseEvent e) {    }
    //-----------------------------------------------------------------------------------
}
