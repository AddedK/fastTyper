/*
 * This class is responsible for the HUD. This includes setting up the JFrame window and
 * the text areas where the user can type.
 */
package game;
import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class HUD {

    private JTextArea typingArea; // Text area that  the user types into
    private JTextArea textAreaTarget; // Text area showing the text that the user must type.

    private int targetSelectionStart;
    private int targetSelectionEnd;
    private Highlighter h; // Highlighter for textTarget


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
        final int TEXT_AREA_TARGET_COLUMNS = 1;

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
        h = textAreaTarget.getHighlighter();


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
        // From https://docs.oracle.com/javase/tutorial/uiswing/misc/focus.html
        //Make textField get the focus whenever frame is activated.
        f.addWindowFocusListener(new WindowAdapter() {
            public void windowGainedFocus(WindowEvent e) {
                typingArea.requestFocusInWindow();
            }
        });
    }

    public void clearTextTypeArea() {
        typingArea.selectAll();
        typingArea.replaceSelection("");
    }
    public void appendTextToTypeArea(String text) {
        typingArea.append(text);
    }

    public void setTextShowArea(String text) {
        textAreaTarget.setText(text);
        textAreaTarget.setFont(textAreaTarget.getFont().deriveFont(15f));
    }

    /**
     * Highlight the target text that the user has typed correctly
     */
    public void highlightText() {
        // Help from https://stackoverflow.com/questions/5949524/highlight-sentence-in-textarea
        // https://stackoverflow.com/questions/20341719/how-to-highlight-a-single-word-in-a-jtextarea
        h.removeAllHighlights();
        try {
            h.addHighlight(0 , getTargetSelectionEnd(), DefaultHighlighter.DefaultPainter);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

    }

    public void setTextTypeArea(String text) {
        typingArea.setText(text);
    }

    public void setTypingAreaListener(DocumentFilterListener dfl) {
        ((AbstractDocument)this.typingArea.getDocument()).setDocumentFilter(dfl);
    }

    public int getTargetSelectionStart() {
        return targetSelectionStart;
    }

    public void setTargetSelectionStart(int targetSelectionStart) {
        this.targetSelectionStart = targetSelectionStart;
    }

    public int getTargetSelectionEnd() {
        return targetSelectionEnd;
    }

    public void setTargetSelectionEnd(int targetSelectionEnd) {
        this.targetSelectionEnd = targetSelectionEnd;
    }

}
