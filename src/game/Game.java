/*
 * This is the FastTyper Game: A game where the user has to type the text presented as fast as possible.
 * This is the main Game class. It is responsible for the game loop, such as what to do when the user types something specific.
 *
 */

package game;

public class Game {
    private final HUD hud;
    private DocumentFilterListener typingListener;
    private String predictionString;
    private String[] predictionArray;


    private int currentPredictedWordIndex;
    private String currentPredictedWord;
    private String currentTypedWord;

    private String whatIsBeingTyped;

    public Game(String predictionString) {
        /*
        * Turn the prediction string into an array of words.
        *  */
        predictionArray = predictionString.split("\\s+");
        // Add space to end of each word except for the last one.
        for (int i = 0; i < predictionArray.length-1; i++) {
            predictionArray[i] += " ";
        }
        for (int i = 0; i < predictionArray.length; i++) {
            System.out.print(predictionArray[i]);
        }

        currentPredictedWordIndex = 0;
        currentPredictedWord = this.predictionArray[currentPredictedWordIndex];
        predictionString = predictionString;
        currentTypedWord = "";
        hud = new HUD();
        hud.setTextShowArea(predictionString);

        typingListener = new DocumentFilterListener(this);
        hud.setTypingAreaListener(typingListener);
    }

    public void updateNextPredictedWord(){
        currentPredictedWordIndex+=1;
        if(currentPredictedWordIndex < predictionArray.length) {
            this.currentPredictedWord = predictionArray[currentPredictedWordIndex];
        } else {
            System.out.println("There are no more words to predict!");
        }
    }

    public void clearCurrentTypedWord() {
        currentTypedWord = "";
    }

    public void textWasReplaced(int offset, String text){
        // The listener calls this method when a user has typed in something.
        System.out.println("Text was replaced");
        // Modify the current word
//        System.out.println("offset in text was replaced");
//        System.out.println(offset);
        // Appending char
        if (offset == currentTypedWord.length()) {
            hud.appendTextToTypeArea(text);
            currentTypedWord += text;
        } else {
            currentTypedWord = currentTypedWord.substring(0, offset)
                    + text
                    + currentTypedWord.substring(offset);
        }

        System.out.println(String.format("CurrentTypedWord = %s",this.currentTypedWord));

        // Check if the current typed word is the one we are meant to predict
        if(currentTypedWord.equals(currentPredictedWord)) {
            System.out.println("You got it right!");
            hud.clearTextTypeArea();
            clearCurrentTypedWord();
            updateNextPredictedWord();
        } else {
            hud.setTextTypeArea(currentTypedWord);

        }
        typingListener.setListening(true);
    }

    public void textWasRemoved(int offset, int length) {
        // Modify the current word
        if(!currentTypedWord.equals("")) {
            // This means we are removing the entire word
            if (length == currentTypedWord.length()) {
                this.hud.clearTextTypeArea();
                clearCurrentTypedWord();
            }
            // Removing last character
           else {
               if(offset == currentTypedWord.length()) {
                    currentTypedWord = currentTypedWord.substring(0,offset);
                } else {
//                System.out.print(currentTypedWord.length());
                    String firstPart = currentTypedWord.substring(0,offset);
//                System.out.println(firstPart);
                    String secondPart = currentTypedWord.substring(offset+1,currentTypedWord.length());
//                System.out.println(secondPart);
                    currentTypedWord = firstPart + secondPart;
                }
                // Update text field
                hud.setTextTypeArea(currentTypedWord);
            }
        }
        typingListener.setListening(true);

    }

    public static void main(String[] args) {
        // System.out.println("Hello, world!");
        String predictionString = "You are supposed to type this. \n This is a new line hahahaha.";
        Game game = new Game(predictionString);

    }
}
