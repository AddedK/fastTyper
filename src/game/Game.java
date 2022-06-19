/*
 * This is the FastTyper Game: A game where the user has to type the text presented as fast as possible.
 * This is the main Game class. It is responsible for the game loop, such as what to do when the user types something specific.
 *
 */

package game;

public class Game {
    private final HUD hud;
    private DocumentFilterListener typingListener;
    private String[] predictionArray;


    private int currentPredictedWordIndex;
    private String currentPredictedWord;
    private String currentTypedWord;


    /**
     * Game constructor. This fixes the predicted string and sets up the HUD that visualizes the text.
     * It also create a document listener that notifies the Game class when a user types something.
     * @param predictionString The target text the user must type.
     */
    public Game(String predictionString, boolean visible) {
        // TODO: problem is that words that begin after newline are not aligned to the left, looks bad.

        predictionArray = createPredictionArray(predictionString);
        // Setup first predicted word
        currentPredictedWordIndex = 0;
        currentPredictedWord = this.predictionArray[currentPredictedWordIndex];
        currentTypedWord = "";

        hud = new HUD(visible);
        hud.setTextShowArea(predictionString);

        typingListener = new DocumentFilterListener(this);
        hud.setTypingAreaListener(typingListener);
    }

    /**
     * Turns an unformated string to be typed into an array of words that
     * can be used to compare to what the user has typed.
     * @param predictionString what the user should type.
     * @return predictionArray the prediction string in a special format of words.
     */
    public static String[] createPredictionArray(String predictionString) {
        String[] predictionArray = predictionString.split("\\s+");
        // Add space to end of each word except for the last one.
        for (int i = 0; i < predictionArray.length-1; i++) {
            predictionArray[i] += " ";
        }
        for (int i = 0; i < predictionArray.length; i++) {
            System.out.print(predictionArray[i]);
        }
        return predictionArray;
    }

    /**
     * This method decides what the next predicted word should be.
     */
    public void updateNextPredictedWord(){
        currentPredictedWordIndex+=1;
        if(currentPredictedWordIndex < predictionArray.length) {
            this.currentPredictedWord = predictionArray[currentPredictedWordIndex];
        } else {
            System.out.println("There are no more words to predict!");
        }
    }


    /**
     * This method is called whenever the user has typed something.
     * The method considers where the user has typed, and if
     * the current typed word is the predicted word.
     * @param offset where the user has typed something. Offset 0 means at the beginning.
     * @param text the string that the user has typed. Oftentimes a char.
     */
    public void textWasReplaced(int offset, String text){
        System.out.println("Text was replaced");
        // Appending char at end
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

    /**
     * This method is called whenever the user has removed text from the text type area in the HUD.
     * @param offset the position of the first character that is being removed
     * @param length the length of the text that is being removed.
     */
    public void textWasRemoved(int offset, int length) {
        if(!currentTypedWord.equals("")) {
            // This means we are removing the entire word
            if (length == currentTypedWord.length()) {
                this.hud.clearTextTypeArea();
                clearCurrentTypedWord();
            }
           else {
               // Removing the last character
               if(offset == currentTypedWord.length()) {
                    currentTypedWord = currentTypedWord.substring(0,offset);
                } else {
//                System.out.print(currentTypedWord.length());
                    String firstPart = currentTypedWord.substring(0,offset);
//                System.out.println(firstPart);
                    String secondPart = currentTypedWord.substring(offset+1);
//                System.out.println(secondPart);
                    currentTypedWord = firstPart + secondPart;
                }
                hud.setTextTypeArea(currentTypedWord);
            }
        }
        typingListener.setListening(true);

    }

    public void clearCurrentTypedWord() {
        currentTypedWord = "";
    }

    public String getCurrentTypedWord() {
        return currentTypedWord;
    }

    public String[] getPredictionArray() {
        return predictionArray;
    }

    public static void main(String[] args) {
        // System.out.println("Hello, world!");
        String predictionString = "You are supposed to type this.\n This is a new line hahahaha.\n This is another line.";
        Game game = new Game(predictionString,true);

    }
}
