package Bee.Role;

import android.graphics.Color;
import Bee.Object.Object2D;

public class Boom extends Object2D{
	public static int size = 0;
	public static int halfSize = 0;
	public static int col = 5;
	public static int maxIndex = 24;
	
	public Boom(int destX,int destY,int destWidth,int destHeight) {
		super(destX, destY, destWidth, destHeight, 0, 0, size, size, 0, Color.WHITE,0);

		isAlive = true;
	}
	
	public void Animation()
	{
		setAnimationIndex(col);
		
		if (index < maxIndex)
			index++;
		else
			isAlive = false;
		
	}
}
