package Bee.Role;

import java.util.ArrayList;

import Bee.Object.Plane;
import Bee.main.GV;

public class F16 extends Plane {
	
	public static int bigBoom = 3;

	public static boolean isTouchBoom;
	

	public static int startBigBoomFrame = 0;
	

	public static int bigBoomDelayFrame = 30; 
	
	public static int bigBoomPower = 10;

	public F16(int destX, int destY, int destWidth, int destHeight, int srcX, int srcY, int srcWidth, int srcHeight,int speed, int color,int theta) {
		super(destX, destY, destWidth, destHeight, srcX, srcY, srcWidth, srcHeight, speed, color,theta);


		barrel.add(new Barrel(halfHeight, theta, theta, 10, 10));
		barrel.add(new Barrel(halfHeight, theta - 20, theta - 15, 10, 12));
		barrel.add(new Barrel(halfHeight, theta + 20, theta + 15, 10, 12));
		barrel.add(new Barrel(halfHeight, theta - 45, theta - 30, 10, 12));
		barrel.add(new Barrel(halfHeight, theta + 45, theta + 30, 10, 12));
		

		bigBoom = 3;
		

		startBigBoomFrame = 0;
		

		life = 5;
		
		lifeInitialize();
	}
	
	private void lifeInitialize()
	{
		

		bigBoom = 3;
		

		index = 10;
		

		isAlive = true;
		

		blood = 10;
	}


	@Override
	public void Action(int frameTime, ArrayList<ShootEffect> shootEffect) 
	{
		
		if (GV.x - (getX() + halfWidth) > 5)
		{
			offset = 1;
			addX(speed);
		}
		else if (GV.x - (getX() + halfWidth) < -5)
		{
			offset = -1;
			addX(-speed);
		}

		if (GV.y - (getY() + halfHeight) > 5)
			addY(speed);
		else if (GV.y - (getY() + halfHeight) < -5)
			addY(-speed);


		if (getX() < 0)
		{
			offset = 0;
			GV.x = halfWidth;
			setX(0);
		}
		else if (getX() + destWidth > GV.scaleWidth)
		{
			offset = 0;
			GV.x = GV.scaleWidth - halfWidth;
			setX(GV.scaleWidth - destWidth);
		}

		if (getY() < 0)
		{
			GV.y = halfHeight;
			setY(0);
		}
		else if (getY() + destHeight > GV.scaleHeight)
		{
			if (GV.y > GV.scaleHeight - halfHeight)
				GV.y = GV.scaleHeight - halfHeight;
			
			setY(GV.scaleHeight - destHeight);
		}


		Shoot(frameTime,shootEffect);
		

		planeAnimation();
		
		super.Action(frameTime,shootEffect);
	}
	

	@Override
	public void Collisioned(int frameTime,ArrayList<Boom> boom) {

		GV.vibrator.vibrate(100);
		
		super.Collisioned(frameTime, boom);
	}
	

	@Override
	protected void Rebirth() {
		
		GV.x = GV.halfWidth;
		GV.y = GV.halfHeight + destHeight;
		setX(GV.halfWidth - halfWidth);
		setY(GV.scaleHeight << 1);
		
		lifeInitialize();
	}	
}
