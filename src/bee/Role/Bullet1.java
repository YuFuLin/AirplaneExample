package Bee.Role;

import java.util.ArrayList;

import Bee.Object.Object2D;
import Bee.main.GV;

// 子彈
public class Bullet1 extends Object2D{
	
	// 子彈的大小
	public static int size = 15;
	public static int halfSize = size >> 1;
	
	public Bullet1(int destX,int destY,int speed,int color, int theta) {
		super( destX, destY, size, size, 0, 0, size, size,speed, color,theta);
		
		isAlive = true;
	}
	
	// 子彈移動
	public boolean bulletMove()
	{
		move();
		
		// 是否超出螢幕
		if (!GV.isInScreen(destRect))
			return true;
		else
			return false;
	}
}
