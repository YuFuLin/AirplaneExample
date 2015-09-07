package Bee.main;

import XNA.lbs.DrawableGameComponent;
import XNA.lbs.Game;

public class GameStateClass {
	// Game state
    public enum GameState
    {
        None,
        op,
        Menu,
        Stage1,
    }

    public static GameState currentState;   // Game state in current
    public static GameState oldState;       // Game state in last play
   
    // Switch the stage
    public static void changeState(GameState newState,DrawableGameComponent nowGameClass,Game game)
    {
        // Remove the components
        game.Components.remove(nowGameClass);

        // Release the class
        nowGameClass.Dispose();

        currentState = newState;
    }
}

// Modified in 2013/11/12
