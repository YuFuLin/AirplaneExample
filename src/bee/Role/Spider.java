package Bee.Role;

import java.util.ArrayList;

import Bee.Object.Object2D;
import Bee.Object.Plane;
import Bee.main.GV;
import Bee.main.GV.Sound;
import android.graphics.Color;

public class Spider extends Plane{
	
	public static int width;
	public static int height;
	

	private int startSwitchStep4Frame;
	private int switchStep4DelayFrame = 300;
	

	private int startSwitchStep6Frame;
	private int switchStep6DelayFrame = 300;
	

	private int startSwitchStep8Frame;
	private int switchStep8DelayFrame = 300;
	

	public Object2D bloodObj;
	

	private static final int maxBlood = 500;
	

	private int f;

	public Spider(int destX,int destY,int destWidth,int destHeight,int srcX,int srcY,int srcWidth,int srcHeight, int speed,int color, int theta) {
		super( destX, destY, destWidth, destHeight, srcX, srcY, srcWidth, srcHeight, speed, color, theta);
		
		barrel.add(new Barrel(halfHeight,theta, theta, 5, 5));
		barrel.add(new Barrel(halfHeight,theta, theta - 45, 5, 5));
		barrel.add(new Barrel(halfHeight,theta, theta + 45, 5, 5));
		

		bloodObj = new Object2D(20, 20, GV.scaleWidth - 40, 10, Color.RED, 128);
		
		

		isAlive = true;
		

		blood = maxBlood;
	}
	
	@Override
	public void Action(int frameTime, Plane plane,ArrayList<ShootEffect> shootEffect) {
		
		switch(state)
		{
			case step1:
				

				if (moveDown(20))
				{
					state = State.step2;

					startSwitchStep4Frame = frameTime;
				}
				
				break;
			case step2:
				
	
				if (moveRight(GV.scaleWidth - halfWidth))
				{
					state = State.step3;
				}
				
				Shoot(frameTime, shootEffect);
				

				if (frameTime - startSwitchStep4Frame > switchStep4DelayFrame)
				{
					state = State.step4;
					
	
					for (f = barrel.size() -1 ;f>=0;f--)
					{
						barrel.get(f).bulletDelayFrame = 15;
					}
					

					startSwitchStep6Frame = frameTime;
				}
				
				break;
			case step3:
				
	
				if (moveLeft(halfWidth))
				{
					state = State.step2;
				}
				
				Shoot(frameTime, shootEffect);
				
				break;
			case step4:

				Shoot(frameTime, shootEffect);
				

				for (f = barrel.size() - 1; f >= 0; f--)
				{
					rotationBarrel(barrel.get(f), 1);
					
	
					if (barrel.get(f).theta > 180)
					{
						state = State.step5;
						break;
					}
				}
				
				break;
			case step5:

				Shoot(frameTime, shootEffect);
			
	
				for (f = barrel.size() - 1; f >= 0; f--)
				{
					rotationBarrel(barrel.get(f), -1);
					

					if (barrel.get(f).theta > 350)
					{
						state = State.step4;
						break;
					}
				}
				

				if (frameTime - startSwitchStep6Frame > switchStep6DelayFrame)
				{
					state = State.step6;
				}
				
				break;
			case step6:
				

				rotationBarrel(this, 1);
				if (theta > 270)
				{
					state = State.step7;
					
	
					barrel.removeAll(barrel);
					
	
					for (f = 0; f < 360; f += 15)
					{
						barrel.add(new Barrel(halfHeight >> 2,f, f, 10, 5));
					}
					
					startSwitchStep8Frame = frameTime;
				}
				
				break;
			case step7:
				
				Shoot(frameTime, shootEffect);
				

				if (frameTime - startSwitchStep8Frame > switchStep8DelayFrame)
				{	
					state = State.step8;
				}
				
				break;
			case step8:
				

				rotationBarrel(this, 1);
				if (theta == 90)
				{	

					barrel.removeAll(barrel);
					
		
					barrel.add(new Barrel(halfHeight,theta, theta, 5, 5));
					barrel.add(new Barrel(halfHeight,theta, theta - 45, 5, 5));
					barrel.add(new Barrel(halfHeight,theta, theta + 45, 5, 5));
					
					state = State.step1;
				}
				
				break;
		}
		
		super.Action(frameTime, plane, shootEffect);
	}
	
	@Override
	public void Collisioned(int frameTime, ArrayList<Boom> boom) {
		bloodObj.destRect.right = (int)((float)blood / maxBlood * (GV.scaleWidth - 20)); 
		

		if (blood < 2)
		{
			boom.add(new Boom(getX() + halfWidth - GV.halfWidth,
	   						  getY() + halfHeight - GV.halfHeight,
							  GV.halfWidth, GV.halfHeight));
		}
		
		super.Collisioned(frameTime, boom);
	}
	

	private void rotationBarrel(Object2D obj, int speed)
	{
		obj.addTheta(speed);
	}
}
