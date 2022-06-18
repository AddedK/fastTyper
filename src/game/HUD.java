/*
 * This class is responsible for the HUD. This includes setting up the JFrame window and
 * the text areas where the user can type.
 */
package game;
import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.text.AbstractDocument;

public class HUD {

    private JTextArea typingArea; // Text area that  the user types into
    private JTextArea textAreaTarget; // Text area showing the text that the user must type.

    public HUD(boolean visible) {
        JFrame f=new JFrame("FastTyper");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        typingArea=new JTextArea(2,2);
        textAreaTarget=new JTextArea(2,2);

        textAreaTarget.setBounds(50,100, 250,70);
        typingArea.setBounds(50,200, 250,20);

        JButton b=new JButton("Click Here");
        b.setBounds(50,50,95,30);

        f.add(b);
        f.add(typingArea);
        f.add(textAreaTarget);
        f.setSize(400,800);
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