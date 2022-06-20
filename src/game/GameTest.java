package game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {


    /**
     * CreatePredictionArray tests
     */
    @Test
    public void testCreatePredictionArray1() {
        String string1 = "I like to party.";
        String[] answer1 = {"I ", "like ", "to ", "party."};
        String[] prediction1 = Game.createPredictionArray(string1);
        assertArrayEquals(answer1,prediction1);

    }

    @Test
    public void testCreatePredictionArray2() {
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
        String predictionString1 = "I like to party.\nThis is great.\nSuper fun.";
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
    public void testGetSetCurrentPredictedWordIndex1() {
        String predictionString1 = "Party";
        String predictionString2 = "Dance";

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
    public void testUpdateNextPredictedWord1() {
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

        assertEquals(true,game.getFinished());
        assertNull(game.getCurrentPredictedWord());
    }

    @Test
    public void testUpdateNextPredictedWord2() {
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
        assertEquals(true,game.getFinished());
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
    public void testGetSetCurrentPredictedWord1() {
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
    public void testCurrentTypedWord1() {
        Game game = new Game("",false);
        game.setCurrentTypedWord("");

        String gameCurrentTypedWord = game.getCurrentTypedWord();
        assertEquals(gameCurrentTypedWord,"");

        game.setCurrentTypedWord("This is super cool!");
        gameCurrentTypedWord = game.getCurrentTypedWord();
        assertEquals(gameCurrentTypedWord,"This is super cool!");
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