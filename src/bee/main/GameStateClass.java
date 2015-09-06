package Bee.main;

import XNA.lbs.DrawableGameComponent;
import XNA.lbs.Game;

public class GameStateClass {
	// 遊戲狀態
    public enum GameState
    {
        None,
        op,
        Menu,
        Stage1,
    }

    public static GameState currentState;   // 目前遊戲狀態
    public static GameState oldState;       // 上一次的遊戲狀態
   
    // 切換關卡
    public static void changeState(GameState newState,DrawableGameComponent nowGameClass,Game game)
    {
        // 從Game1元件中移除
        game.Components.remove(nowGameClass);

        // 釋放Class
        nowGameClass.Dispose();

        currentState = newState;
    }
}

// Copyright(c) 2010/11/22 by 金門大學資工四 劉柏賢
