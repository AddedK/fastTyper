/*
 * This class is responsible for the HUD. This includes setting up the JFrame window and
 * the text areas where the user can type.
 */
package game;
import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class HUD {

    private JTextArea typingArea; // Text area that  the user types into
    private JTextArea textAreaTarget; // Text area showing the text that the user must type.

    private Highlighter h; // Highlighter for textTarget
    Highlighter.HighlightPainter greenPainter;
    Highlighter.HighlightPainter cyanPainter;
    Highlighter.HighlightPainter redPainter;
    private JLabel wordPerMinuteLabel;
    private JButton nextTextButton;


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

        // JFrame constants
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
        greenPainter = new DefaultHighlighter.DefaultHighlightPainter(Color.green);
        cyanPainter = new DefaultHighlighter.DefaultHighlightPainter(Color.cyan);
        redPainter = new DefaultHighlighter.DefaultHighlightPainter(Color.red);

        nextTextButton=new JButton("Get Next Text");
        nextTextButton.setBounds(120,300,120,40);

        f.add(nextTextButton);
        f.add(typingArea);
        f.add(textAreaTarget);
        f.setSize(FRAME_WIDTH,FRAME_HEIGHT);
        Point currentFramePosition = f.getLocation();
        // Shift the frame location so that the center of the frame is at the center of the screen.
        f.setLocation((int) currentFramePosition.getX()- (FRAME_WIDTH/2),(int)currentFramePosition.getY()-(FRAME_HEIGHT/2));
        f.setLayout(null);

        // From https://www.javatpoint.com/java-jlabel
        this.wordPerMinuteLabel = new JLabel();
        wordPerMinuteLabel.setText("wpm:");
        wordPerMinuteLabel.setBounds(250,10, 100,50);
        f.add(wordPerMinuteLabel);

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

    public void setTextShowArea(String text) {
        textAreaTarget.setText(text);
        textAreaTarget.setFont(textAreaTarget.getFont().deriveFont(15f));
        // Also, make sure cursor is on typing area
        typingArea.requestFocusInWindow();

    }

    public void setWordsPerMinuteText(String wordsPerMinute) {
        this.wordPerMinuteLabel.setText(wordsPerMinute);
    }


    /**
     * Highlights the show area so that completed words are green, currently typed words are cyan and
     * characters that are wrong are red.
     * @param lastCompletedWordIndex The last character of the last fully typed word
     * @param nrCorrectCharacters How many consecutive characters currentPredictedWord shares with currentTypedWord
     * @param nrWrongCharacters How many currently typed characters are wrong
     */
    public void highlightText(int lastCompletedWordIndex,int nrCorrectCharacters, int nrWrongCharacters) {
        // Help from https://stackoverflow.com/questions/5949524/highlight-sentence-in-textarea
        // https://stackoverflow.com/questions/20341719/how-to-highlight-a-single-word-in-a-jtextarea
        h.removeAllHighlights();
        highlightCompletedText(lastCompletedWordIndex);
        highlightCurrentAndPredictedWord(lastCompletedWordIndex,nrCorrectCharacters,nrWrongCharacters);
    }

    /**
     * Highlight the target text words that the user has typed correctly
     */
    public void highlightCompletedText(int lastCompletedWordIndex) {
        try {
            // Highlight words that are correctly typed
            h.addHighlight(0 , lastCompletedWordIndex, greenPainter);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Highlight the currently predicted word and how much of the word the user has typed correctly
     */
    public void highlightCurrentAndPredictedWord(int lastCompletedWordIndex,int nrCorrectCharacters, int nrWrongCharacters) {
        try {
            // Highlight the currently correctly typed characters cyan
            h.addHighlight(lastCompletedWordIndex ,lastCompletedWordIndex + nrCorrectCharacters, cyanPainter);
            // Highlight the currently incorrectly typed characters red
            h.addHighlight(lastCompletedWordIndex + nrCorrectCharacters,
                    lastCompletedWordIndex + nrCorrectCharacters + nrWrongCharacters,
                    redPainter);
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

    public void setNextTextButtonActionListener(ButtonListener bl) {
        this.nextTextButton.addActionListener(bl);
    }


    public void setTypingAreaCaretPosition(int position) {
        this.typingArea.setCaretPosition(position);
    }
}
