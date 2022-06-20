/*
 * This class is responsible for the HUD. This includes setting up the JFrame window and
 * the text areas where the user can type.
 */
package game;
import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.text.AbstractDocument;
import java.awt.*;

public class HUD {

    private JTextArea typingArea; // Text area that  the user types into
    private JTextArea textAreaTarget; // Text area showing the text that the user must type.

    public HUD(boolean visible) {

        // Typing area constants
        final int TYPING_AREA_TARGET_X = 50;
        final int TYPING_AREA_TARGET_Y = 200;
        final int TYPING_AREA_TARGET_WIDTH = 250;
        final int TYPING_AREA_TARGET_HEIGHT = 20;
        final int TYPING_AREA_TARGET_ROWS = 2;
        final int TYPING_AREA_TARGET_COLUMNS = 2;


        // Text area target constants
        final int TEXT_AREA_TARGET_X = 50;
        final int TEXT_AREA_TARGET_Y = 100;
        final int TEXT_AREA_TARGET_WIDTH = 250;
        final int TEXT_AREA_TARGET_HEIGHT = 70;
        final int TEXT_AREA_TARGET_ROWS = 2;
        final int TEXT_AREA_TARGET_COLUMNS = 2;

        // Jframe constants
        final int FRAME_WIDTH = 400;
        final int FRAME_HEIGHT = 500;

        JFrame f=new JFrame("FastTyper");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // set JFrame in center of the screen
        f.setLocationRelativeTo(null);

        typingArea=new JTextArea(TYPING_AREA_TARGET_ROWS,TYPING_AREA_TARGET_COLUMNS);
        typingArea.setBounds(TYPING_AREA_TARGET_X,TYPING_AREA_TARGET_Y, TYPING_AREA_TARGET_WIDTH,TYPING_AREA_TARGET_HEIGHT);

        textAreaTarget=new JTextArea(TEXT_AREA_TARGET_ROWS,TEXT_AREA_TARGET_COLUMNS);
        textAreaTarget.setBounds(TEXT_AREA_TARGET_X,TEXT_AREA_TARGET_Y, TEXT_AREA_TARGET_WIDTH,TEXT_AREA_TARGET_HEIGHT);


        JButton b=new JButton("Click Here");
        b.setBounds(50,50,95,30);

        f.add(b);
        f.add(typingArea);
        f.add(textAreaTarget);
        f.setSize(FRAME_WIDTH,FRAME_HEIGHT);
        Point currentFramePosition = f.getLocation();
        // Shift the frame location so that the center of the frame is at the center of the screen.
        f.setLocation((int) currentFramePosition.getX()- (FRAME_WIDTH/2),(int)currentFramePosition.getY()-(FRAME_HEIGHT/2));
        f.setLayout(null);
        f.setVisible(visible);
    }

    public void setTextShowArea(String text) {
        textAreaTarget.setText(text);
    }

    public void setTextTypeArea(String text) {
        typingArea.setText(text);
    }

    public void setTypingAreaListener(DocumentFilterListener dfl) {
        ((AbstractDocument)this.typingArea.getDocument()).setDocumentFilter(dfl);
    }

    public void clearTextTypeArea() {
        typingArea.selectAll();
        typingArea.replaceSelection("");
    }
    public void appendTextToTypeArea(String text) {
        typingArea.append(text);
    }

}
