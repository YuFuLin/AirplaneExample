package Bee.Role;

import java.util.ArrayList;

import android.util.StateSet;
import Bee.Object.Object2D;
import Bee.Object.Plane;
import Bee.main.GV;

public class Aircraft extends Plane{
	
	// ���A����
	private State kind = State.step1;
	
	// ��
	public static int width = 0;
	
	// �e
	public static int height = 0;
	
	public static int halfWidth = 0;
	public static int halfHeight = 0;
	
	// �˷ǥؼЮɶ�
	private int startTargetFrame = 0;
	
	// ������g�����A���ɶ�
	private static final int switchShootDelayFrame = 60;
	
	// �}�l�������A�ɶ�
	private int startEndFrame = 0;
	
	// �����쵲�����A���ɶ�
	private static final int switchEndDelayFrame = 300; 
	
	// �ΨӧP�_���k
	private int tempX;
	
	// �V�U���ʨ�ت���m
	private int movDestY;
	
	// �Ȧs����
	private int tempTheta;
	
	public Aircraft(int destX,int destY,int destWidth,int destHeight,int srcX,int srcY,int srcWidth,int srcHeight,int speed,int color,int theta,State kind) {
		super( destX, destY, destWidth, destHeight, srcX, srcY, srcWidth, srcHeight,speed, color,theta);
		
		// Action�����A��
		this.kind = kind;
		
		switch(kind)
		{
			case step1:
				
				// �s�W����
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
		
		// �]���w�s��
		isAlive = true;
		
		// �ʵe����
		index = 10;
		
		// �V�U���ʨ�ت���m
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
						
						// �V�U����
						if (moveDown(movDestY))
						{
							state = State.step2;
							
							startTargetFrame = frameTime;
							startEndFrame = frameTime;
						}
						
						break;
					case step2:
						
						// �˷ǥؼ�
						rotationPlane(getTargetTheta(myPlane.destRect,this.destRect));
						
						// �����U�@�Ӫ��A
						if (frameTime - startTargetFrame > switchShootDelayFrame)
						{
							state = State.step3;
						}
						
						break;
					case step3:
						
						// �g��
						Shoot(frameTime,shootEffect);
						
						// �������˷Ǫ��A
						startTargetFrame = frameTime;
						state = State.step2;
						
						// �������������A
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
						
						// ���ʨ�ù��|�����@������
						if (moveDown(GV.halfHeight >> 1))
						{
							state = State.step2;
						}
						
						break;
					case step2:
						
						// �VF16�ļ�
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
		
		// �۾��ʵe
		planeAnimation();
		
		super.Action(frameTime, myPlane,shootEffect);
	}
}
