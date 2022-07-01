/*
 * This class is responsible for listening for clicks on the Get Next Text button
 * and notifying the Game class to respond appropriately.
 */

package game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonListener implements ActionListener {
    private Game game;

    public ButtonListener(Game game) {
        this.game = game;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Button Clicked!");
        this.game.getNextText();
    }
}
