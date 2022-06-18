package game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    public void testCreatePredictionArray() {
        Game game = new Game("dud",false);
        String string1 = "I like to party.";
        String[] answer1 = {"I ", "like ", "to ", "party."};
        String[] prediction1 = game.createPredictionArray(string1);
        assertArrayEquals(answer1,prediction1);



    }

}