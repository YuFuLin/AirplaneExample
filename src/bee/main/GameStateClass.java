package Bee.main;

import XNA.lbs.DrawableGameComponent;
import XNA.lbs.Game;

public class GameStateClass {
	// �C�����A
    public enum GameState
    {
        None,
        op,
        Menu,
        Stage1,
    }

    public static GameState currentState;   // �ثe�C�����A
    public static GameState oldState;       // �W�@�����C�����A
   
    // �������d
    public static void changeState(GameState newState,DrawableGameComponent nowGameClass,Game game)
    {
        // �qGame1���󤤲���
        game.Components.remove(nowGameClass);

        // ����Class
        nowGameClass.Dispose();

        currentState = newState;
    }
}

// Copyright(c) 2010/11/22 by �����j�Ǹ�u�| �B�f��
