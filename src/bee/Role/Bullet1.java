package Bee.Role;

import java.util.ArrayList;

import Bee.Object.Object2D;
import Bee.main.GV;

// �l�u
public class Bullet1 extends Object2D{
	
	// �l�u���j�p
	public static int size = 15;
	public static int halfSize = size >> 1;
	
	public Bullet1(int destX,int destY,int speed,int color, int theta) {
		super( destX, destY, size, size, 0, 0, size, size,speed, color,theta);
		
		isAlive = true;
	}
	
	// �l�u����
	public boolean bulletMove()
	{
		move();
		
		// �O�_�W�X�ù�
		if (!GV.isInScreen(destRect))
			return true;
		else
			return false;
	}
}
