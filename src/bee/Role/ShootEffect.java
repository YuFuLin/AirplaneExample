package Bee.Role;

import android.graphics.Color;
import Bee.Object.Object2D;
import Bee.Object.Object2D.State;

public class ShootEffect extends Object2D{
	
	public static int width;
	public static int height;
	public static int halfWidth;
	public static int halfHeight;
	
	public ShootEffect(int destX,int destY) {
		super( destX, destY, width, height, 0, 0, width, height,0, Color.WHITE,0);

		alpha = 255;
	}
	
	public boolean Animation()
	{
		destRect.left++;
		destRect.top++;
		destRect.right--;
		destRect.bottom--;
		
		alpha-=5;
		paint.setAlpha(alpha);
		
		if (destWidth <= 0 || destHeight <= 0 || alpha <= 0)
			return true;
		else
			return false;

	}
}
