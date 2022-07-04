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
        final int TYPING_AREA_TARGET_WIDTH = 280;
        final int TYPING_AREA_TARGET_HEIGHT = 20;
        final int TYPING_AREA_TARGET_ROWS = 2;
        final int TYPING_AREA_TARGET_COLUMNS = 2;


        // Text area target constants
        final int TEXT_AREA_TARGET_X = 50;
        final int TEXT_AREA_TARGET_Y = 90;
        final int TEXT_AREA_TARGET_WIDTH = 280;
        final int TEXT_AREA_TARGET_HEIGHT = 80;
        final int TEXT_AREA_TARGET_ROWS = 2;
        final int TEXT_AREA_TARGET_COLUMNS = 1;

        // JFrame constants
        final int FRAME_WIDTH = 400;
        final int FRAME_HEIGHT = 380;

        // NextText button constants
        final int NEXT_TEXT_BUTTON_X = 120;
        final int NEXT_TEXT_BUTTON_Y = 250;
        final int NEXT_TEXT_BUTTON_WIDTH = 120;
        final int NEXT_TEXT_BUTTON_HEIGHT = 40;

        // WPM label constants
        final int WPM_LABEL_X = 150;
        final int WPM_LABEL_Y = 20;
        final int WPM_LABEL_WIDTH = 100;
        final int WPM_LABEL_HEIGHT = 50;


        JFrame f=new JFrame("FastTyper");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setResizable(false);
        // set JFrame in center of the screen
        f.setLocationRelativeTo(null);


        typingArea=new JTextArea(TYPING_AREA_TARGET_ROWS,TYPING_AREA_TARGET_COLUMNS);
        typingArea.setBounds(TYPING_AREA_TARGET_X,TYPING_AREA_TARGET_Y, TYPING_AREA_TARGET_WIDTH,TYPING_AREA_TARGET_HEIGHT);
        typingArea.setFont(typingArea.getFont().deriveFont(17f));

        textAreaTarget=new JTextArea(TEXT_AREA_TARGET_ROWS,TEXT_AREA_TARGET_COLUMNS);
        textAreaTarget.setBounds(TEXT_AREA_TARGET_X,TEXT_AREA_TARGET_Y, TEXT_AREA_TARGET_WIDTH,TEXT_AREA_TARGET_HEIGHT);
        textAreaTarget.setFont(textAreaTarget.getFont().deriveFont(17f));

        h = textAreaTarget.getHighlighter();
        greenPainter = new DefaultHighlighter.DefaultHighlightPainter(Color.green);
        cyanPainter = new DefaultHighlighter.DefaultHighlightPainter(Color.cyan);
        redPainter = new DefaultHighlighter.DefaultHighlightPainter(Color.red);

        nextTextButton=new JButton("Get Next Text");
        nextTextButton.setBounds(NEXT_TEXT_BUTTON_X,NEXT_TEXT_BUTTON_Y,NEXT_TEXT_BUTTON_WIDTH,NEXT_TEXT_BUTTON_HEIGHT);

        f.add(nextTextButton);
        f.add(typingArea);
        f.add(textAreaTarget);
        f.setSize(FRAME_WIDTH,FRAME_HEIGHT);
        Point currentFramePosition = f.getLocation();
        // Shift the frame location so that the center of the frame is at the center of the screen.
        f.setLocation((int) currentFramePosition.getX()- (FRAME_WIDTH/2),(int)currentFramePosition.getY()-(FRAME_HEIGHT/2));
        f.setLayout(null);

        // From https://www.javatpoint.com/java-jlabel
        wordPerMinuteLabel = new JLabel();
        wordPerMinuteLabel.setText("wpm:");
        wordPerMinuteLabel.setBounds(WPM_LABEL_X,WPM_LABEL_Y, WPM_LABEL_WIDTH,WPM_LABEL_HEIGHT);
        wordPerMinuteLabel.setFont(this.wordPerMinuteLabel.getFont().deriveFont(17f));
        setWordsPerMinuteText("wpm: 0.0");
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
