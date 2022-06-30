package game;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class GameTest {


    /**
     * CreatePredictionArray tests
     */
    @Test
    public void testCreatePredictionArrayOneSentence1() {
        String string1 = "I like to party.";
        String[] answer1 = {"I ", "like ", "to ", "party."};
        String[] prediction1 = Game.createPredictionArray(string1);
        assertArrayEquals(answer1,prediction1);

    }

    @Test
    public void testCreatePredictionArrayMultipleSentences1() {
        String string1 = "I like to party.\nThis is great.\nSuper fun.";
        String[] answer1 = {"I ", "like ", "to ", "party. ", "This ","is ","great. ","Super ","fun."};
        String[] prediction1 = Game.createPredictionArray(string1);
        assertArrayEquals(answer1,prediction1);
    }

    /**
     * GetSetPredictionArray tests
     */
    @Test
    public void testGetSetPredictionArray1() {
        String[] answerEmpty = {""};
        String[] answer1 = {"I ", "like ", "to ", "party. ", "This ","is ","great. ","Super ","fun."};

        Game game = new Game("",false);
        String[] gamePredictionArray1 = game.getPredictionArray();
        assertArrayEquals(gamePredictionArray1,answerEmpty);

        game.setPredictionArray(answer1);
        String[] gamePredictionArray2 = game.getPredictionArray();
        assertArrayEquals(gamePredictionArray2,answer1);
    }

    /**
     * CurrentPredictedWordIndex tests
     */

    @Test
    public void testGetSetCurrentPredictedWordIndexOneWord() {
        Game game = new Game("",false);
        int gameCurrentlyPredictedWordIndex1 = game.getCurrentPredictedWordIndex();
        assertEquals(gameCurrentlyPredictedWordIndex1,0);

        game.setCurrentPredictedWordIndex(5);
        int gameCurrentlyPredictedWordIndex2 = game.getCurrentPredictedWordIndex();
        assertEquals(gameCurrentlyPredictedWordIndex2,5);
    }

    /**
     * updateNextPredictedWord tests
     */

    @Test
    public void testUpdateNextPredictedWordSimple1() {
        String predictionString1 = "I like to party.";

        Game game = new Game(predictionString1,false);

        String currentlyPredictedWord = game.getCurrentPredictedWord();
        assertEquals(currentlyPredictedWord,"I ");

        game.updateNextPredictedWord();
        currentlyPredictedWord = game.getCurrentPredictedWord();
        assertEquals(currentlyPredictedWord,"like ");

        game.updateNextPredictedWord();
        currentlyPredictedWord = game.getCurrentPredictedWord();
        assertEquals(currentlyPredictedWord,"to ");

        game.updateNextPredictedWord();
        currentlyPredictedWord = game.getCurrentPredictedWord();
        assertEquals(currentlyPredictedWord,"party.");

        game.updateNextPredictedWord();

        assertTrue(game.getFinished());
        assertNull(game.getCurrentPredictedWord());
    }

    @Test
    public void testUpdateNextPredictedWordLarger1() {
        String predictionString1 = "Party time.";

        Game game = new Game(predictionString1,false);

        String currentlyPredictedWord = game.getCurrentPredictedWord();
        assertEquals(currentlyPredictedWord,"Party ");

        game.updateNextPredictedWord();

        currentlyPredictedWord = game.getCurrentPredictedWord();
        assertEquals(currentlyPredictedWord,"time.");
        int currentlyPredictedWordIndex = game.getCurrentPredictedWordIndex();
        assertEquals(1,currentlyPredictedWordIndex);

        game.updateNextPredictedWord();

        assertTrue(game.getFinished());
        assertNull(game.getCurrentPredictedWord());
        currentlyPredictedWordIndex = game.getCurrentPredictedWordIndex();
        assertEquals(-1,currentlyPredictedWordIndex);

//        No more words to predict
        Exception exception = assertThrows(RuntimeException.class, () -> game.updateNextPredictedWord());
        assertEquals("There is no next predicted word!",exception.getMessage());
        currentlyPredictedWord = game.getCurrentPredictedWord();
        assertNull(currentlyPredictedWord);
        currentlyPredictedWordIndex = game.getCurrentPredictedWordIndex();
        assertEquals(-1,currentlyPredictedWordIndex);

    }


    /**
     * CurrentPredictedWord tests
     */

    @Test
    public void testGetSetCurrentPredictedWordSimple1() {
        String predictionString1 = "Party";
        String predictionString2 = "Dance";

        Game game = new Game(predictionString1,false);
        String gameCurrentPredictedWord1 = game.getCurrentPredictedWord();
        assertEquals(gameCurrentPredictedWord1,predictionString1);

        game.setCurrentPredictedWord(predictionString2);
        String gameCurrentPredictedWord2 = game.getCurrentPredictedWord();
        assertEquals(gameCurrentPredictedWord2,predictionString2);


    }

    /**
     * CurrentTypedWord tests
     */

    @Test
    public void testCurrentTypedWordSimple1() {
        Game game = new Game("",false);
        game.setCurrentTypedWord("");

        String gameCurrentTypedWord = game.getCurrentTypedWord();
        assertEquals(gameCurrentTypedWord,"");

        game.setCurrentTypedWord("This is super cool!");
        gameCurrentTypedWord = game.getCurrentTypedWord();
        assertEquals(gameCurrentTypedWord,"This is super cool!");
    }

    /**
     * TextWasReplaced tests and charactersTypedTests
     */

    @Test
    public void testTextWasReplacedOneSentence1() {
        // Type in the characters in the prediction string one at a time
        String predictionString1 = "Party.";
        Game game = new Game(predictionString1,false);

        int charactersTyped = game.getCharactersTyped();
        int expectedCharactersTyped = 0;
        assertEquals(expectedCharactersTyped,charactersTyped);

        String currentTypedWord;
        String expectedCurrentlyTypedWord;
        String[] predictionArray = game.getPredictionArray();

        int offset;
        for (String predictedWord: predictionArray) {
            offset = 0;

            expectedCurrentlyTypedWord = "";
            currentTypedWord = game.getCurrentTypedWord();
            assertEquals(expectedCurrentlyTypedWord,currentTypedWord);
            char[] predictedWordCharArray = predictedWord.toCharArray();

            for (int i = 0; i < predictedWordCharArray.length; i++) {
                char character = predictedWordCharArray[i];

                String characterString = "" + character;
                game.textWasReplaced(offset,characterString);
                offset++;
                expectedCharactersTyped++;
                // final character edge case
                if(i == predictedWordCharArray.length-1) {
                    expectedCurrentlyTypedWord = "";
                } else {
                    expectedCurrentlyTypedWord = expectedCurrentlyTypedWord + character;
                }

                charactersTyped = game.getCharactersTyped();
                assertEquals(charactersTyped,expectedCharactersTyped);

                currentTypedWord = game.getCurrentTypedWord();
                assertEquals(expectedCurrentlyTypedWord,currentTypedWord);
            }
        }
    }

    @Test
    public void testTextWasReplacedMultiSentence1() {
        // Type in the characters in the prediction string one at a time
        String predictionString1 = "Party.\nTime to have fun.";
        Game game = new Game(predictionString1,false);

        int charactersTyped = game.getCharactersTyped();
        int expectedCharactersTyped = 0;
        assertEquals(expectedCharactersTyped,charactersTyped);

        String currentTypedWord;
        String expectedCurrentlyTypedWord;
        String[] predictionArray = game.getPredictionArray();

        int offset;
        for (String predictedWord: predictionArray) {
            offset = 0;

            expectedCurrentlyTypedWord = "";
            currentTypedWord = game.getCurrentTypedWord();
            assertEquals(expectedCurrentlyTypedWord,currentTypedWord);
            char[] predictedWordCharArray = predictedWord.toCharArray();

            for (int i = 0; i < predictedWordCharArray.length; i++) {
                char character = predictedWordCharArray[i];

                String characterString = "" + character;
                game.textWasReplaced(offset,characterString);
                offset++;
                expectedCharactersTyped++;
                // final character edge case
                if(i == predictedWordCharArray.length-1) {
                    expectedCurrentlyTypedWord = "";
                } else {
                    expectedCurrentlyTypedWord = expectedCurrentlyTypedWord + character;
                }

                charactersTyped = game.getCharactersTyped();
                assertEquals(charactersTyped,expectedCharactersTyped);

                currentTypedWord = game.getCurrentTypedWord();
                assertEquals(expectedCurrentlyTypedWord,currentTypedWord);
            }
        }
    }



    @Test
    public void testLastCorrectWordIndexSimple1() {
        String predictionString1 = "Party time.\nGreat.";
        Game game = new Game(predictionString1,false);
        int lastCorrectWordIndex = game.getLastCorrectWordIndex();
        assertEquals(0,lastCorrectWordIndex);

        game.updateNextPredictedWord();
        lastCorrectWordIndex = game.getLastCorrectWordIndex();
        assertEquals(6,lastCorrectWordIndex);

        game.updateNextPredictedWord();
        lastCorrectWordIndex = game.getLastCorrectWordIndex();
        assertEquals(12,lastCorrectWordIndex);

        game.updateNextPredictedWord();
        lastCorrectWordIndex = game.getLastCorrectWordIndex();
        assertEquals(18,lastCorrectWordIndex);
    }

    @Test
    public void calculateWordsPerMinuteOneWord1() throws InterruptedException {
        // 1 word / 4 second =  15 words / minute
        String predictionString1 = "Party.";
        double delta = 0.1;
        Game game = new Game(predictionString1,false);

        // Wait 5 seconds before getting the next word
        Thread.sleep(4000);
        game.updateNextPredictedWord();

        assertTrue(game.getFinished());
        int numberWordsFinished = game.getNumberOfWordsCompleted();
        assertEquals(1,numberWordsFinished);
        double timeInSeconds = game.nanoToSeconds(game.getFinishTime()-game.getStartTime());
        double wordsPerMin = game.calculateWordsPerMinute(numberWordsFinished,timeInSeconds);

        assertTrue(Math.abs(wordsPerMin-15) <= delta);
        assertTrue(wordsPerMin >= 12);
    }


    @Test
    public void testGame() {
        String predictionString1 = "I like to party.";
        String[] answer1 = {"I ", "like ", "to ", "party."};

        Game game = new Game(predictionString1,false);

        String[] gamePredictionArray = game.getPredictionArray();
        assertArrayEquals(answer1,gamePredictionArray);

        String gameCurrentTypedWord = game.getCurrentTypedWord();
        assertEquals(gameCurrentTypedWord,"");

    }

}