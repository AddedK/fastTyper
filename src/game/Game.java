/*
 * This is the FastTyper Game: A game where the user has to type the text presented as fast as possible.
 * This is the main Game class. It is responsible for the game loop, such as what to do when the user types something specific.
 *
 */

package game;

public class Game {
    private HUD hud;
    private DocumentFilterListener typingListener;

    public Game(String predictionString) {
        hud = new HUD();
        hud.setTextShowAre(predictionString);

        typingListener = new DocumentFilterListener(this);
        hud.setTypingAreaListener(typingListener);
    }

    public void textWasReplaced(String text){
        System.out.println("Text was replaced");
        if(text.equals("x")) {
            hud.clearTextTypeArea();
        } else {
            hud.appendTextToTypeArea(text);
        }

        typingListener.setListening(true);
    }

    public static void main(String[] args) {
        // System.out.println("Hello, world!");
        String predictionString = "You are supposed to type this. \n This is a new line hahahaha.";
        Game game = new Game(predictionString);

    }
}
