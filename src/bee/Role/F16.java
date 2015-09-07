package Bee.Role;

import java.util.ArrayList;

import Bee.Object.Plane;
import Bee.main.GV;

public class F16 extends Plane {
	
	// 大絕招
	public static int bigBoom = 3;
	
	// 使用大絕招
	public static boolean isTouchBoom;
	
	// 開始使用大絕招的時間
	public static int startBigBoomFrame = 0;
	
	// 再度使用大絕招的延遲時間
	public static int bigBoomDelayFrame = 30; // 一秒後
	
	// 大絕招的破壞力
	public static int bigBoomPower = 10;

	public F16(int destX, int destY, int destWidth, int destHeight, int srcX, int srcY, int srcWidth, int srcHeight,int speed, int color,int theta) {
		super(destX, destY, destWidth, destHeight, srcX, srcY, srcWidth, srcHeight, speed, color,theta);

		// 新增炮管(距離飛機中心的距離,距離飛機中心的絕對距離)
		barrel.add(new Barrel(halfHeight, theta, theta, 10, 10));
		barrel.add(new Barrel(halfHeight, theta - 20, theta - 15, 10, 12));
		barrel.add(new Barrel(halfHeight, theta + 20, theta + 15, 10, 12));
		barrel.add(new Barrel(halfHeight, theta - 45, theta - 30, 10, 12));
		barrel.add(new Barrel(halfHeight, theta + 45, theta + 30, 10, 12));
		
		// 設定大絕招的數量
		bigBoom = 3;
		
		// 大絕開始使用時間歸零
		startBigBoomFrame = 0;
		
		// 生命
		life = 5;
		
		lifeInitialize();
	}
	
	private void lifeInitialize()
	{
		
		// 可使用大絕的次數
		bigBoom = 3;
		
		// 動畫正面
		index = 10;
		
		// 設為已存活
		isAlive = true;
		
		// 血量
		blood = 10;
	}

	// 自機行動
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

		// 防止超出邊界
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

		// 自動發射子彈
		Shoot(frameTime,shootEffect);
		
		// 自機動畫
		planeAnimation();
		
		super.Action(frameTime,shootEffect);
	}
	
	// 被撞擊後
	@Override
	public void Collisioned(int frameTime,ArrayList<Boom> boom) {
		
		// 震動0.1秒
		GV.vibrator.vibrate(100);
		
		super.Collisioned(frameTime, boom);
	}
	
	// 重生
	@Override
	protected void Rebirth() {
		
		GV.x = GV.halfWidth;
		GV.y = GV.halfHeight + destHeight;
		setX(GV.halfWidth - halfWidth);
		setY(GV.scaleHeight << 1);
		
		lifeInitialize();
	}	
}
