/*
 * This is the FastTyper Game: A game where the user has to type the text presented as fast as possible.
 * This is the main Game class. It is responsible for the game loop, such as what to do when the user types something specific.
 *
 */

package game;

import javax.swing.*;
import java.util.TimerTask;
import java.util.Timer;

public class Game {
    private final HUD hud;
    private DocumentFilterListener typingListener;
    private String[] predictionArray;
    public int numberOfWordsCompleted;
    private int currentPredictedWordIndex;
    private String currentPredictedWord;
    private String currentTypedWord;
    private boolean finished; // True if there are no more words to predict
    private int charactersTyped; // The total number of characters of the words that the user has typed.
    private int lastCorrectWordIndex; // The index of the last character in the most recently correctly typed word.
    private long startTime; // System.nanotime() when user is allowed to type
    private long finishTime; // System.nanotime() return when user types the final word.

    /**
     * Game constructor. This fixes the predicted string and sets up the HUD that visualizes the text.
     * It also creates a document listener that notifies the Game class when a user types something.
     * @param predictionString The target text the user must type.
     */
    public Game(String predictionString, boolean visible) {
        setPredictionArray(createPredictionArray(predictionString));
        setCharactersTyped(0);
        setLastCorrectWordIndex(0);
        setFinished(false);

        hud = new HUD(visible);
        hud.setTextShowArea(predictionString);

        typingListener = new DocumentFilterListener(this);
        hud.setTypingAreaListener(typingListener);
        hud.highlightCompletedText(0);

        this.numberOfWordsCompleted = 0;
        // Start countdown
        this.startTime = System.nanoTime();
        this.finishTime = 0;

        // Add timer that updates word-per-minute label
        TimerTask task = new TimerTask() {
            public void run() {
                if(!getFinished()) {
                    long currentTime = System.nanoTime();
                    updateWPMDisplay(currentTime);
                }
            }
        };
        Timer timer = new Timer("timer");
        long delay = 2000L;
        timer.schedule(task, delay, delay);
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
        for (String s : predictionArray) {
            System.out.print(s);
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
            numberOfWordsCompleted++;
            int curPredWordIndex = getCurrentPredictedWordIndex();
            String currentPredictedWord = getCurrentPredictedWord();
            int nrCharacters = currentPredictedWord.length();
            setCharactersTyped(getCharactersTyped()+nrCharacters);
            setLastCorrectWordIndex(getLastCorrectWordIndex() + nrCharacters);
            String[] predictionArrayTemp = getPredictionArray();
            if(curPredWordIndex == predictionArrayTemp.length-1) {
                // We just finished typing the last word.
                this.finishTime = System.nanoTime();
                setFinished(true);
                setCurrentPredictedWord(null);
                setCurrentPredictedWordIndex(-1);
                showTimeResults();
                updateWPMDisplay(this.finishTime);
            } else {
                setCurrentPredictedWordIndex(getCurrentPredictedWordIndex()+1);
                curPredWordIndex = getCurrentPredictedWordIndex();
                setCurrentPredictedWord(predictionArrayTemp[curPredWordIndex]);
            }
            updateCurrentWordHighlighting();
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
        int textLength = text.length();
//        System.out.println("Text was replaced");
        String currentlyTypedWord = getCurrentTypedWord();
        // This happens for example if the user is pasting in text, which is not allowed.
        if(textLength != 1) {
            throw new RuntimeException("Replacement text is not a single character!");
        }
        // Appending char at end
        if (offset == currentlyTypedWord.length()) {
            setCurrentTypedWord(currentlyTypedWord+text);
        } else {
            setCurrentTypedWord(currentlyTypedWord.substring(0, offset)
                    + text
                    + currentlyTypedWord.substring(offset));
        }
//        System.out.printf("CurrentTypedWord = %s%n",getCurrentTypedWord());

        // Check if the current typed word is the one we are meant to predict
        currentlyTypedWord = getCurrentTypedWord();
        if(currentlyTypedWord.equals(getCurrentPredictedWord())) {
            System.out.println("You got it right!");
            hud.clearTextTypeArea();
            setCurrentTypedWord("");
            updateNextPredictedWord();
        } else {
            hud.setTextTypeArea(currentlyTypedWord);
            hud.setTypingAreaCaretPosition(offset+1);
            updateCurrentWordHighlighting();
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
            // Remove from CurrentlyTyped word the text between offset and offset+length
            String firstPart = currentlyTypedWord.substring(0,offset);
            String secondPart = currentlyTypedWord.substring(offset+length);
            setCurrentTypedWord(firstPart + secondPart);
            hud.setTextTypeArea(currentTypedWord);
            this.hud.setTypingAreaCaretPosition(offset);
            updateCurrentWordHighlighting();
        }
        typingListener.setListening(true);

    }

    /**
     * This function prints the time taken in seconds and the words-per-minute.
     * This function is called when the user has typed the final word.
     */
    public void showTimeResults() {
        double timeInSeconds = nanoToSeconds(this.finishTime-this.startTime);
        double wordsPerMinute = calculateWordsPerMinute(numberOfWordsCompleted,timeInSeconds);
        System.out.printf("It took %f seconds%n",timeInSeconds);
        System.out.printf("Words per minute: %f%n",wordsPerMinute);
    }

    /**
     * Calculates the current time and wpm and calls hud to update wpm display.
     * @param currentTime The current nanotime when the function is called
     */
    public void updateWPMDisplay(long currentTime) {
        double elapsedTimeInSeconds = nanoToSeconds(currentTime-startTime);
        double wordsPerMin = calculateWordsPerMinute(numberOfWordsCompleted,elapsedTimeInSeconds);
        hud.setWordsPerMinuteText("wpm: " + roundTwoDecimals(wordsPerMin));
    }

    /**
     * Sends parameters to HUD to highlight the currently typed word
     */
    public void updateCurrentWordHighlighting() {
        int nrCorrectCharacters = 0;
        int nrWrongCharacters = 0;

        String currentPredictedWord = getCurrentPredictedWord();
        String currentTypedWord = getCurrentTypedWord();
        // Have to check against null in case there are no more words to predict
        if (currentPredictedWord != null) {
            int predictedWordLength = currentPredictedWord.length();
            int currentWordLength = currentTypedWord.length();
            int minLength = Math.min(predictedWordLength, currentWordLength);

            for (int i = 0; i < minLength; i++) {
                if (currentPredictedWord.charAt(i) == currentTypedWord.charAt(i)) {
                    nrCorrectCharacters++;
                } else {
                    break;
                }
            }

            nrWrongCharacters = currentTypedWord.length() - nrCorrectCharacters;
        }

        hud.highlightText(getLastCorrectWordIndex(),nrCorrectCharacters,nrWrongCharacters);
    }

    /**
     * Getters and setters
     */

    public String[] getPredictionArray() {
        return predictionArray;
    }

    public void setPredictionArray(String[] newPredictionArray) {
        predictionArray = newPredictionArray;
        // Setup first predicted word
        setCurrentPredictedWordIndex(0);
        setCurrentPredictedWord(getPredictionArray()[getCurrentPredictedWordIndex()]);
        setCurrentTypedWord("");
    }

    public int getNumberOfWordsCompleted() {
        return numberOfWordsCompleted;
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

    public int getLastCorrectWordIndex() {
        return lastCorrectWordIndex;
    }

    public void setLastCorrectWordIndex(int lastCorrectWordIndex) {
        this.lastCorrectWordIndex = lastCorrectWordIndex;
    }

    public int getCharactersTyped() {
        return charactersTyped;
    }

    public void setCharactersTyped(int charactersTyped) {
        this.charactersTyped = charactersTyped;
    }

    public long getStartTime() {
        return startTime;
    }
    public long getFinishTime() {
        return finishTime;
    }

    /**
     * Time and math functions
     */

    public double nanoToSeconds(long nanoTime) {
        return (double ) nanoTime / 1000000000;
    }

    public double calculateWordsPerMinute(int nrWords,double timeInSeconds) {
        return ((double) nrWords / timeInSeconds) * 60;
    }

    public double roundTwoDecimals(double num) {
        double result = Math.round(num * 100);
        return result/100;
    }



    public static void main(String[] args) {
//        String longPredictionString = "You are supposed to type this.\nThis is a new line hahahaha.\nThis is another line.";
        String shortPredictionString = "You are supposed to type this.\nThis is a new line.";
        Game game = new Game(shortPredictionString,true);

    }
}
