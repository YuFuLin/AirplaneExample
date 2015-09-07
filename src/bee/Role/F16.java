package Bee.Role;

import java.util.ArrayList;

import Bee.Object.Plane;
import Bee.main.GV;

public class F16 extends Plane {
	
	// �j����
	public static int bigBoom = 3;
	
	// �ϥΤj����
	public static boolean isTouchBoom;
	
	// �}�l�ϥΤj���۪��ɶ�
	public static int startBigBoomFrame = 0;
	
	// �A�רϥΤj���۪�����ɶ�
	public static int bigBoomDelayFrame = 30; // �@���
	
	// �j���۪��}�a�O
	public static int bigBoomPower = 10;

	public F16(int destX, int destY, int destWidth, int destHeight, int srcX, int srcY, int srcWidth, int srcHeight,int speed, int color,int theta) {
		super(destX, destY, destWidth, destHeight, srcX, srcY, srcWidth, srcHeight, speed, color,theta);

		// �s�W����(�Z���������ߪ��Z��,�Z���������ߪ�����Z��)
		barrel.add(new Barrel(halfHeight, theta, theta, 10, 10));
		barrel.add(new Barrel(halfHeight, theta - 20, theta - 15, 10, 12));
		barrel.add(new Barrel(halfHeight, theta + 20, theta + 15, 10, 12));
		barrel.add(new Barrel(halfHeight, theta - 45, theta - 30, 10, 12));
		barrel.add(new Barrel(halfHeight, theta + 45, theta + 30, 10, 12));
		
		// �]�w�j���۪��ƶq
		bigBoom = 3;
		
		// �j���}�l�ϥήɶ��k�s
		startBigBoomFrame = 0;
		
		// �ͩR
		life = 5;
		
		lifeInitialize();
	}
	
	private void lifeInitialize()
	{
		
		// �i�ϥΤj��������
		bigBoom = 3;
		
		// �ʵe����
		index = 10;
		
		// �]���w�s��
		isAlive = true;
		
		// ��q
		blood = 10;
	}

	// �۾����
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

		// ����W�X���
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

		// �۰ʵo�g�l�u
		Shoot(frameTime,shootEffect);
		
		// �۾��ʵe
		planeAnimation();
		
		super.Action(frameTime,shootEffect);
	}
	
	// �Q������
	@Override
	public void Collisioned(int frameTime,ArrayList<Boom> boom) {
		
		// �_��0.1��
		GV.vibrator.vibrate(100);
		
		super.Collisioned(frameTime, boom);
	}
	
	// ����
	@Override
	protected void Rebirth() {
		
		GV.x = GV.halfWidth;
		GV.y = GV.halfHeight + destHeight;
		setX(GV.halfWidth - halfWidth);
		setY(GV.scaleHeight << 1);
		
		lifeInitialize();
	}	
}
