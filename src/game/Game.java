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
    private boolean finished; // True if there are no more words to predict


    /**
     * Game constructor. This fixes the predicted string and sets up the HUD that visualizes the text.
     * It also create a document listener that notifies the Game class when a user types something.
     * @param predictionString The target text the user must type.
     */
    public Game(String predictionString, boolean visible) {
        // TODO: problem is that words that begin after newline are not aligned to the left, looks bad.

        setPredictionArray(createPredictionArray(predictionString));
        // Setup first predicted word
        setCurrentPredictedWordIndex(0);
        setCurrentPredictedWord(getPredictionArray()[getCurrentPredictedWordIndex()]);
        setCurrentTypedWord("");

        setFinished(false);

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
    public void updateNextPredictedWord() throws RuntimeException{
        if (getFinished()) {
            throw new RuntimeException("There is no next predicted word!");
        } else {
            int curPredWordIndex = getCurrentPredictedWordIndex();
            String[] predictionArrayTemp = getPredictionArray();
            if(curPredWordIndex == predictionArrayTemp.length-1) {
                // We just finished typing the last word.
                setFinished(true);
                setCurrentPredictedWord(null);
                System.out.println("There are no more words to predict!");
            } else {
                setCurrentPredictedWordIndex(getCurrentPredictedWordIndex()+1);
                curPredWordIndex = getCurrentPredictedWordIndex();
                setCurrentPredictedWord(predictionArrayTemp[curPredWordIndex]);
            }

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
        String currentlyTypedWord = getCurrentTypedWord();
        // Appending char at end
        if (offset == currentlyTypedWord.length()) {
            hud.appendTextToTypeArea(text);
            setCurrentTypedWord(currentlyTypedWord+text);
        } else {
            setCurrentTypedWord(currentlyTypedWord.substring(0, offset)
                    + text
                    + currentlyTypedWord.substring(offset));
        }

        System.out.println(String.format("CurrentTypedWord = %s",getCurrentTypedWord()));

        // Check if the current typed word is the one we are meant to predict
        currentlyTypedWord = getCurrentTypedWord();
        if(currentlyTypedWord.equals(getCurrentPredictedWord())) {
            System.out.println("You got it right!");
            hud.clearTextTypeArea();
            setCurrentTypedWord("");
            updateNextPredictedWord();
        } else {
            hud.setTextTypeArea(currentlyTypedWord);

        }
        typingListener.setListening(true);
    }

    /**
     * This method is called whenever the user has removed text from the text type area in the HUD.
     * @param offset the position of the first character that is being removed
     * @param length the length of the text that is being removed.
     */
    public void textWasRemoved(int offset, int length) {
        String currentlyTypedWord = getCurrentTypedWord();
        if(!currentlyTypedWord.equals("")) {
            // This means we are removing the entire word
            if (length == currentlyTypedWord.length()) {
                this.hud.clearTextTypeArea();
                setCurrentTypedWord("");
            }
           else {
               // Removing the last character
               if(offset == currentlyTypedWord.length()) {
                   setCurrentTypedWord(currentlyTypedWord.substring(0,offset));
                } else {
                    String firstPart = currentlyTypedWord.substring(0,offset);
                    String secondPart = currentlyTypedWord.substring(offset+1);
                   setCurrentTypedWord(firstPart + secondPart);
                }
                hud.setTextTypeArea(currentTypedWord);
            }
        }
        typingListener.setListening(true);

    }

    /**
     * Getters and setters
     */

    public String[] getPredictionArray() {
        return predictionArray;
    }

    public void setPredictionArray(String[] newPredictionArray) {
        predictionArray = newPredictionArray;
    }

    public int getCurrentPredictedWordIndex() {
        return currentPredictedWordIndex;
    }

    public void setCurrentPredictedWordIndex(int currentPredictedWordIndex) {
        this.currentPredictedWordIndex = currentPredictedWordIndex;
    }

    public String getCurrentPredictedWord() {
        return currentPredictedWord;
    }

    public void setCurrentPredictedWord(String currentPredictedWord) {
        this.currentPredictedWord = currentPredictedWord;
    }

    public String getCurrentTypedWord() {
        return currentTypedWord;
    }

    public void setCurrentTypedWord(String newTypedWord) {
        currentTypedWord = newTypedWord;
    }

    public boolean getFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public static void main(String[] args) {
        String predictionString = "You are supposed to type this.\n This is a new line hahahaha.\n This is another line.";
        Game game = new Game(predictionString,true);
    }
}
