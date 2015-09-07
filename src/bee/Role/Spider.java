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
	
	// 切換成炮管左右來回旋轉的延遲時間
	private int startSwitchStep4Frame;
	private int switchStep4DelayFrame = 300;
	
	// 切換到stpe6的延遲時間 
	private int startSwitchStep6Frame;
	private int switchStep6DelayFrame = 300;
	
	// 切換到stpe8的延遲時間 
	private int startSwitchStep8Frame;
	private int switchStep8DelayFrame = 300;
	
	// 血條物件
	public Object2D bloodObj;
	
	// 最大血量
	private static final int maxBlood = 500;
	
	// 迴圈變數
	private int f;

	public Spider(int destX,int destY,int destWidth,int destHeight,int srcX,int srcY,int srcWidth,int srcHeight, int speed,int color, int theta) {
		super( destX, destY, destWidth, destHeight, srcX, srcY, srcWidth, srcHeight, speed, color, theta);
		
		barrel.add(new Barrel(halfHeight,theta, theta, 5, 5));
		barrel.add(new Barrel(halfHeight,theta, theta - 45, 5, 5));
		barrel.add(new Barrel(halfHeight,theta, theta + 45, 5, 5));
		
		// 初始化血條
		bloodObj = new Object2D(20, 20, GV.scaleWidth - 40, 10, Color.RED, 128);
		
		
		// 設為存活
		isAlive = true;
		
		// 血量
		blood = maxBlood;
	}
	
	@Override
	public void Action(int frameTime, Plane plane,ArrayList<ShootEffect> shootEffect) {
		
		switch(state)
		{
			case step1:
				
				// 移進螢幕
				if (moveDown(20))
				{
					state = State.step2;
					
					// 切換到 step 4 計時
					startSwitchStep4Frame = frameTime;
				}
				
				break;
			case step2:
				
				// 向右移動並發射子彈
				if (moveRight(GV.scaleWidth - halfWidth))
				{
					state = State.step3;
				}
				
				Shoot(frameTime, shootEffect);
				
				// 時間到則切換step4
				if (frameTime - startSwitchStep4Frame > switchStep4DelayFrame)
				{
					state = State.step4;
					
					// 增加子彈再度發射的延遲時間
					for (f = barrel.size() -1 ;f>=0;f--)
					{
						barrel.get(f).bulletDelayFrame = 15;
					}
					
					// 切換到step6計時
					startSwitchStep6Frame = frameTime;
				}
				
				break;
			case step3:
				
				// 向左移動並發射子彈
				if (moveLeft(halfWidth))
				{
					state = State.step2;
				}
				
				Shoot(frameTime, shootEffect);
				
				break;
			case step4:

				Shoot(frameTime, shootEffect);
				
				// 順轉炮管
				for (f = barrel.size() - 1; f >= 0; f--)
				{
					rotationBarrel(barrel.get(f), 1);
					
					// 旋轉次數
					if (barrel.get(f).theta > 180)
					{
						state = State.step5;
						break;
					}
				}
				
				break;
			case step5:

				Shoot(frameTime, shootEffect);
			
				// 逆轉炮管
				for (f = barrel.size() - 1; f >= 0; f--)
				{
					rotationBarrel(barrel.get(f), -1);
					
					// 旋轉角度
					if (barrel.get(f).theta > 350)
					{
						state = State.step4;
						break;
					}
				}
				
				// 時間到就切換到state6
				if (frameTime - startSwitchStep6Frame > switchStep6DelayFrame)
				{
					state = State.step6;
				}
				
				break;
			case step6:
				
				// 蜘蛛旋轉
				rotationBarrel(this, 1);
				if (theta > 270)
				{
					state = State.step7;
					
					// 移除所有炮管
					barrel.removeAll(barrel);
					
					// 新增炮管
					for (f = 0; f < 360; f += 15)
					{
						barrel.add(new Barrel(halfHeight >> 2,f, f, 10, 5));
					}
					
					startSwitchStep8Frame = frameTime;
				}
				
				break;
			case step7:
				
				Shoot(frameTime, shootEffect);
				
				// 時間到就切到狀態8
				if (frameTime - startSwitchStep8Frame > switchStep8DelayFrame)
				{	
					state = State.step8;
				}
				
				break;
			case step8:
				
				// 旋轉蜘蛛
				rotationBarrel(this, 1);
				if (theta == 90)
				{	
					// 移除所有炮管
					barrel.removeAll(barrel);
					
					// 還原初始炮管
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
		
		// 新增爆炸
		if (blood < 2)
		{
			boom.add(new Boom(getX() + halfWidth - GV.halfWidth,
	   						  getY() + halfHeight - GV.halfHeight,
							  GV.halfWidth, GV.halfHeight));
		}
		
		super.Collisioned(frameTime, boom);
	}
	
	// 旋轉炮管
	private void rotationBarrel(Object2D obj, int speed)
	{
		obj.addTheta(speed);
	}
}
