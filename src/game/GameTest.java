package game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

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