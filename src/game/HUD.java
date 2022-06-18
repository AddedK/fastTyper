/*
 * This class is responsible for the HUD. This includes setting up the JFrame window and
 * the text areas where the user can type.
 */
package game;
import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.text.AbstractDocument;


public class HUD {

    private JTextArea typingArea; // This is the one the user types into
    private JTextArea textAreaShower;

    public HUD() {

        JFrame f=new JFrame("Button Example");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        typingArea=new JTextArea(2,2);
        textAreaShower=new JTextArea(2,2);


        textAreaShower.setBounds(50,100, 250,70);

        typingArea.setBounds(50,200, 250,20);



        JButton b=new JButton("Click Here");
        b.setBounds(50,50,95,30);

        f.add(b);
        f.add(typingArea);
        f.add(textAreaShower);
        f.setSize(400,800);
        f.setLayout(null);
        f.setVisible(true);

    }

    public void setTextShowArea(String text) {
        textAreaShower.setText(text);
    }

    public void setTextTypeArea(String text) {
        typingArea.setText(text);
    }

    public void setTypingAreaListener(DocumentFilterListener dfl) {
        ((AbstractDocument)this.typingArea.getDocument()).setDocumentFilter(dfl);
    }

    public void clearTextTypeArea() {
//        System.out.println("Clearing text type area");
        typingArea.selectAll();
        typingArea.replaceSelection("");
    }
    public void appendTextToTypeArea(String text) {
//        System.out.println(String.format("set text typed %s",text));
        typingArea.append(text);
    }

}
