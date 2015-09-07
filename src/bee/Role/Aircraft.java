package Bee.Role;

import java.util.ArrayList;

import android.util.StateSet;
import Bee.Object.Object2D;
import Bee.Object.Plane;
import Bee.main.GV;

public class Aircraft extends Plane{
	
	// 狀態種類
	private State kind = State.step1;
	
	// 長
	public static int width = 0;
	
	// 寬
	public static int height = 0;
	
	public static int halfWidth = 0;
	public static int halfHeight = 0;
	
	// 瞄準目標時間
	private int startTargetFrame = 0;
	
	// 切換到射擊狀態的時間
	private static final int switchShootDelayFrame = 60;
	
	// 開始結束狀態時間
	private int startEndFrame = 0;
	
	// 切換到結束狀態的時間
	private static final int switchEndDelayFrame = 300; 
	
	// 用來判斷左右
	private int tempX;
	
	// 向下移動到目的位置
	private int movDestY;
	
	// 暫存角度
	private int tempTheta;
	
	public Aircraft(int destX,int destY,int destWidth,int destHeight,int srcX,int srcY,int srcWidth,int srcHeight,int speed,int color,int theta,State kind) {
		super( destX, destY, destWidth, destHeight, srcX, srcY, srcWidth, srcHeight,speed, color,theta);
		
		// Action的狀態圈
		this.kind = kind;
		
		switch(kind)
		{
			case step1:
				
				// 新增炮管
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
		
		// 設為已存活
		isAlive = true;
		
		// 動畫正面
		index = 10;
		
		// 向下移動到目的位置
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
						
						// 向下移動
						if (moveDown(movDestY))
						{
							state = State.step2;
							
							startTargetFrame = frameTime;
							startEndFrame = frameTime;
						}
						
						break;
					case step2:
						
						// 瞄準目標
						rotationPlane(getTargetTheta(myPlane.destRect,this.destRect));
						
						// 切換下一個狀態
						if (frameTime - startTargetFrame > switchShootDelayFrame)
						{
							state = State.step3;
						}
						
						break;
					case step3:
						
						// 射擊
						Shoot(frameTime,shootEffect);
						
						// 切換成瞄準狀態
						startTargetFrame = frameTime;
						state = State.step2;
						
						// 切換成結束狀態
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
						
						// 移動到螢幕四分之一的高度
						if (moveDown(GV.halfHeight >> 1))
						{
							state = State.step2;
						}
						
						break;
					case step2:
						
						// 向F16衝撞
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
		
		// 自機動畫
		planeAnimation();
		
		super.Action(frameTime, myPlane,shootEffect);
	}
}
