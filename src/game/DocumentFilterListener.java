/*
 * This class is responsible for listening to inputs in the text area
 * and notifying the Game class to respond appropriately.
 */

package game;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

// Based on https://stackoverflow.com/questions/22954961/jtextfield-and-keylistener



public class DocumentFilterListener extends DocumentFilter {

    private Game game;
    private boolean listening;

    public DocumentFilterListener(Game game) {
        this.game = game;
        this.listening = true;
    }

    public void setListening(boolean bool) {
        this.listening = bool;
    }

    public boolean getListening() {
        return this.listening;
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr) throws BadLocationException {
//        System.out.println("inserting");
        super.insertString(fb, offset, text, attr);
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        if (listening) {
            game.textWasReplaced(offset,text);
//            System.out.println("Listened to replace");
        } else {
            super.replace(fb, offset, length, text, attrs);
        }

    }

    @Override
    public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
//        System.out.println(String.format("offset = %d",offset));
//        System.out.println(String.format("length removed = %d",length));

        //Offset tells us which char was removed
        if (listening) {
            game.textWasRemoved(offset,length);
//            System.out.println("Listened to remove");
        } else {
            super.remove(fb, offset, length);
        }

    }

}