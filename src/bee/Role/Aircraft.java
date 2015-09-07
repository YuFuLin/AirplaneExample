package Bee.Role;

import java.util.ArrayList;

import android.util.StateSet;
import Bee.Object.Object2D;
import Bee.Object.Plane;
import Bee.main.GV;

public class Aircraft extends Plane{
	
	// State kind
	private State kind = State.step1;
	
	// Width
	public static int width = 0;
	
	// Height
	public static int height = 0;
	
	public static int halfWidth = 0;
	public static int halfHeight = 0;
	
	// Target aim frame
	private int startTargetFrame = 0;
	
	// Switch Shoot Delay time
	private static final int switchShootDelayFrame = 60;
	
	// Start End Frame
	private int startEndFrame = 0;
	
	// Switch End Delay frame
	private static final int switchEndDelayFrame = 300; 
	
	// Check left and right
	private int tempX;
	
	// move down
	private int movDestY;
	
	// temp angle
	private int tempTheta;
	
	public Aircraft(int destX,int destY,int destWidth,int destHeight,int srcX,int srcY,int srcWidth,int srcHeight,int speed,int color,int theta,State kind) {
		super( destX, destY, destWidth, destHeight, srcX, srcY, srcWidth, srcHeight,speed, color,theta);
		
		// Action cycle
		this.kind = kind;
		
		switch(kind)
		{
			case step1:
				
				// add barrel
				barrel.add(new Barrel(halfHeight, theta, theta, 5, 60));
				
				blood = 4;
				
				break;
			case step2:
				blood = 4;
				
				break;
			case step3:
			case step4:
				tempTheta = theta;
				setTheta(90);
				
				blood = 3;
				
				break;
		}
		
		// set alive
		isAlive = true;
		

		index = 10;
		

		movDestY = destY + GV.scaleHeight;
	}
	
	@Override
	public void Action(int frameTime, Plane myPlane,ArrayList<ShootEffect> shootEffect) 
	{
		tempX = getX();
		
		switch(kind)
		{
			case step1:
				switch(state)
				{
					case step1:
			
						if (moveDown(movDestY))
						{
							state = State.step2;
							
							startTargetFrame = frameTime;
							startEndFrame = frameTime;
						}
						
						break;
					case step2:
						
						rotationPlane(getTargetTheta(myPlane.destRect,this.destRect));
						
						if (frameTime - startTargetFrame > switchShootDelayFrame)
						{
							state = State.step3;
						}
						
						break;
					case step3:
						
						Shoot(frameTime,shootEffect);

						startTargetFrame = frameTime;
						state = State.step2;

						if (frameTime - startEndFrame > switchEndDelayFrame)
						{
							state = State.step4;
						}
						
						break;
					case step4:
						if (moveUp(-destHeight))
							isAlive = false;
						
						break;
				}
				break;
			case step2:
				switch(state)
				{
					case step1:
						

						if (moveDown(GV.halfHeight >> 1))
						{
							state = State.step2;
						}
						
						break;
					case step2:
						
	
						setTheta(getTargetTheta(myPlane.destRect,this.destRect));
						move();
						
						if (frameTime - startEndFrame > switchEndDelayFrame)
						{
							state = State.step3;
						}
						
						break;
					case step3:
						move();
						if (!GV.isInScreen(destRect))
						{
							isAlive = false;
						}
						
						break;
				}
				
				break;
			case step3:
				switch(state)
				{
					case step1:
						if (moveDown(movDestY))
						{
							theta = tempTheta;
							state = State.step2;
						}
						
						break;
						
					case step2:
						move();
						if (!GV.isInScreen(destRect))
						{
							isAlive = false;
						}
						
						break;
				}
				break;
			case step4:
				switch(state)
				{
					case step1:
						if (moveDown(destHeight << 2))
						{
							theta = tempTheta;
							state = State.step2;
						}
				
						break;
					case step2:					
						move();
						if (!GV.isInScreen(destRect))
						{
							isAlive = false;
						}
										
						break;
				}
				
				break;
		}

		tempX = getX() - tempX;
		
		if (tempX > 0)
			offset = 1;
		else if (tempX < 0)
			offset = -1;
		

		planeAnimation();
		
		super.Action(frameTime, myPlane,shootEffect);
	}
}
