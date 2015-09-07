package Bee.scene;

import java.util.ArrayList;
import Bee.Object.Music;
import Bee.Object.Object2D;
import Bee.Object.Plane;
import Bee.Object.Text;
import Bee.Object.Object2D.State;
import Bee.Role.Aircraft;
import Bee.Role.Boom;
import Bee.Role.Bullet1;
import Bee.Role.F16;
import Bee.Role.RedMask;
import Bee.Role.ShootEffect;
import Bee.Role.Spider;
import Bee.main.GV;
import Bee.main.GameStateClass;
import Bee.main.R;
import Bee.main.GameStateClass.GameState;
import XNA.lbs.DrawableGameComponent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;

public class Stage1 extends DrawableGameComponent {

	private Game1 game;

	private Canvas subCanvas;

	// 顯示FPS文字
	private Text fpsText;

	// 自機圖片
	private Bitmap f16Bitmap;

	// 自機物件
	private ArrayList<Plane> myPlane;

	// 民航機圖片
	private Bitmap aircraftBitmap;

	// 民航機物件
	private ArrayList<Plane> aircraft;

	// 子彈圖片
	private Bitmap bulletBitmap1;

	// 一般背景圖片
	private Bitmap backGroundImage;

	// 一般背景物件
	private Object2D backGroundObj;

	// 爆炸圖片
	private Bitmap boomBitmap;

	// 爆炸
	private ArrayList<Boom> boom = new ArrayList<Boom>();

	// 子彈暫存用物件
	private Bullet1 bullet;

	// 爆炸暫存
	private Boom subBoom;

	// 飛機暫存
	private Plane plane;

	// 迴圈變數
	private int f, j, k;

	// 發射特效圖片
	private Bitmap shootEffectBitmap;

	// 發射特效物件
	public ArrayList<ShootEffect> shootEffect = new ArrayList<ShootEffect>();

	// 是否啟用黑色遮罩
	private boolean isBlackMask;
	private Object2D blackMask;

	// 是否啟用紅色遮罩
	private RedMask redMask;

	// 黑色遮罩開始啟用時間
	private int startBlackMaskFrame;

	// 從黑色遮罩到重新遊戲的時間
	private int restartDelayFrame = 150;

	// 顯示GameOver文字
	private Text gameOverText;
	
	// 蜘蛛
	private Bitmap spiderBitmap;
	private ArrayList<Plane> spiderObj;
	
	// 畫格總數
	private int frameTime;
	
	// 蜘蛛背景
	private Bitmap spiderBackGroundBitmap;
	
	// 蜘蛛背景物件
	private Object2D spiderBackGroundObj;
	
	// 破關
	private boolean breakOff;

	public Stage1(Game1 game) {
		this.game = game;
	}

	@Override
	protected void Initialize() {

		// 預先算好三角函數
		for (int f = 0; f < 360; f++) {
			GV.Cosine[f] = (float) Math.cos(f * Math.PI / 180);
			GV.Sine[f] = (float) Math.sin(f * Math.PI / 180);
		}

		GV.screenRect = new Rect(0, 0, GV.scaleWidth, GV.scaleHeight);

		// 歸零
		game.totalFrames = 0;

		super.Initialize();
	}

	@Override
	protected void LoadContent() {

		// 取得單一影格的寬高
		int width, height;

		// 民航機
		aircraftBitmap = (Bitmap) BitmapFactory.decodeResource(GV.res,
				R.drawable.aircraft);

		width = aircraftBitmap.getWidth() / 4;
		height = aircraftBitmap.getHeight() / 5;

		aircraft = new ArrayList<Plane>();
		Aircraft.width = width;
		Aircraft.height = height;
		Aircraft.halfWidth = width >> 1;
		Aircraft.halfHeight = height >> 1;

		// 自機
		f16Bitmap = (Bitmap) BitmapFactory.decodeResource(GV.res,
				R.drawable.f16);

		width = f16Bitmap.getWidth() / 4;
		height = f16Bitmap.getHeight() / 5;

		myPlane = new ArrayList<Plane>();
		myPlane.add(new F16(GV.halfWidth - (width >> 1), GV.scaleHeight, width,
				height, 0, 0, width, height, 5, Color.WHITE, 270));

		// 設定觸控位置在中下方
		GV.x = GV.halfWidth;
		GV.y = GV.scaleHeight - height;

		// 子彈
		bulletBitmap1 = BitmapFactory
				.decodeResource(GV.res, R.drawable.bullet1);

		// 背景
		backGroundImage = (Bitmap) BitmapFactory.decodeResource(GV.res,
				R.drawable.water);
		
		// 背景物件
		backGroundObj = new Object2D(0, 0, GV.scaleWidth / backGroundImage.getWidth(), GV.scaleHeight / backGroundImage.getHeight(), 0, 0, 0, 0, 0, 0, 0);
		backGroundObj.isAlive = true;

		// 文字
		fpsText = new Text(GV.halfWidth, 20, 12, "FPS", Color.YELLOW);

		// 爆炸
		boomBitmap = BitmapFactory.decodeResource(GV.res, R.drawable.boom2);
		Boom.size = boomBitmap.getWidth() / Boom.col;
		Boom.halfSize = Boom.size >> 1;

		// 發射的特效
		shootEffectBitmap = BitmapFactory.decodeResource(GV.res,
				R.drawable.effect);

		ShootEffect.width = shootEffectBitmap.getWidth();
		ShootEffect.height = shootEffectBitmap.getHeight();
		ShootEffect.halfWidth = ShootEffect.width >> 1;
		ShootEffect.halfHeight = ShootEffect.height >> 1;

		// 紅色Mask
		redMask = new RedMask(Color.RED, 0);

		// 黑色Mask
		blackMask = new Object2D(0, 0, GV.scaleWidth, GV.scaleHeight,
				Color.BLACK, 128);
		
		gameOverText = new Text(20, GV.halfHeight-18, 36, "Game Over", Color.WHITE);
		
		// 蜘蛛
		spiderBitmap = BitmapFactory.decodeResource(GV.res, R.drawable.spider);
		
		Spider.width = spiderBitmap.getWidth();
		Spider.height = spiderBitmap.getHeight();
		
		spiderObj = new ArrayList<Plane>();
		
		// 蜘蛛背景
		spiderBackGroundBitmap = BitmapFactory.decodeResource(GV.res, R.drawable.spider_scene);
		
		// 蜘蛛背景物件
		spiderBackGroundObj = new Object2D(0,-GV.scaleHeight,GV.scaleWidth,GV.scaleHeight,0, 0, spiderBackGroundBitmap.getWidth(), spiderBackGroundBitmap.getHeight(),0,Color.WHITE,0);

		super.LoadContent();
	}

	@Override
	protected void UnloadContent() {

		super.UnloadContent();
	}

	@Override
	protected void Update() {

		frameTime = (int)game.totalFrames;
		
		// 在遊戲中初始化飛機(對映到下方的Action)
		switch ((int) game.totalFrames) {
		case 150: // 5秒
			aircraft.add(new Aircraft(GV.halfWidth - Aircraft.halfWidth,
					-Aircraft.height, Aircraft.width, Aircraft.height, 0, 0,
					Aircraft.width, Aircraft.height, 5, Color.WHITE, 90,
					State.step2));
			break;
		case 240: // 8秒
			aircraft.add(new Aircraft(20, -Aircraft.height, Aircraft.width,
					Aircraft.height, 0, 0, Aircraft.width, Aircraft.height, 5,
					Color.WHITE, 90, State.step2));
			break;
		case 330: // 11秒
			aircraft.add(new Aircraft(GV.scaleWidth - Aircraft.width - 20,
					-Aircraft.height, Aircraft.width, Aircraft.height, 0, 0,
					Aircraft.width, Aircraft.height, 5, Color.WHITE, 90,
					State.step1));

			aircraft.add(new Aircraft(30, -Aircraft.height, Aircraft.width,
					Aircraft.height, 0, 0, Aircraft.width, Aircraft.height, 5,
					Color.WHITE, 90, State.step2));

			aircraft.add(new Aircraft(GV.scaleWidth - Aircraft.width - 20,
					-Aircraft.height, Aircraft.width, Aircraft.height, 0, 0,
					Aircraft.width, Aircraft.height, 5, Color.GREEN, 90,
					State.step1));

			aircraft.add(new Aircraft(GV.scaleWidth - Aircraft.width - 30,
					-Aircraft.height, Aircraft.width, Aircraft.height, 0, 0,
					Aircraft.width, Aircraft.height, 5, Color.GREEN, 90,
					State.step2));

			break;
		case 450: // 15秒 左邊連續出現
			for (f = 0; f < 5; f++) {
				aircraft.add(new Aircraft(20, (-Aircraft.halfHeight - 20)
						* (f + 1), Aircraft.width, Aircraft.height, 0, 0,
						Aircraft.width, Aircraft.height, 5, Color.RED, 90,
						State.step1));
			}

			break;
		case 540: // 18秒 中間連續出現
			for (f = 0; f < 5; f++) {
				aircraft.add(new Aircraft(GV.halfWidth - Aircraft.halfWidth,
						(-Aircraft.halfHeight - 20) * (f + 1), Aircraft.width,
						Aircraft.height, 0, 0, Aircraft.width, Aircraft.height,
						5, Color.BLUE, 90, State.step2));
			}

			break;
		case 630: // 21秒 右邊連續出現
			for (f = 0; f < 5; f++) {
				aircraft.add(new Aircraft(GV.scaleWidth - Aircraft.width - 20,
						(-Aircraft.halfHeight - 20) * (f + 1), Aircraft.width,
						Aircraft.height, 0, 0, Aircraft.width, Aircraft.height,
						5, Color.GRAY, 90, State.step1));
			}

			break;

		case 750: // 25秒 左跟右連續出現
			for (f = 0; f < 5; f++) {
				aircraft.add(new Aircraft(20, (-Aircraft.halfHeight - 20)
						* (f + 1), Aircraft.width, Aircraft.height, 0, 0,
						Aircraft.width, Aircraft.height, 5, Color.WHITE, 90,
						State.step2));
			}

			for (f = 0; f < 5; f++) {
				aircraft.add(new Aircraft(GV.scaleWidth - Aircraft.width - 20,
						(-Aircraft.halfHeight - 20) * (f + 1), Aircraft.width,
						Aircraft.height, 0, 0, Aircraft.width, Aircraft.height,
						5, Color.YELLOW, 90, State.step2));
			}

			break;
		case 930: // 31秒
			for (f = 0; f < 5; f++) {
				aircraft.add(new Aircraft(GV.halfWidth - Aircraft.halfWidth,
						(-Aircraft.halfHeight - 20) * (f + 1), Aircraft.width,
						Aircraft.height, 0, 0, Aircraft.width, Aircraft.height,
						5, Color.BLACK, 90, State.step1));
			}

			break;
		case 1020: // 34
			for (f = 0; f < 3; f++) {
				aircraft.add(new Aircraft(-Aircraft.width,
						(-Aircraft.halfHeight - 20) * (f + 1), Aircraft.width,
						Aircraft.height, 0, 0, Aircraft.width, Aircraft.height,
						5, Color.WHITE, 45, State.step3));
			}

			break;
		case 1110: // 37
			for (f = 0; f < 3; f++) {
				aircraft.add(new Aircraft(GV.scaleWidth - Aircraft.width - 20,
						(-Aircraft.halfHeight - 20) * (f + 1), Aircraft.width,
						Aircraft.height, 0, 0, Aircraft.width, Aircraft.height,
						5, Color.WHITE, 135, State.step4));
			}

			for (f = 0; f < 3; f++) {
				aircraft.add(new Aircraft(20, (-Aircraft.halfHeight - 20)
						* (f + 1), Aircraft.width, Aircraft.height, 0, 0,
						Aircraft.width, Aircraft.height, 5, Color.WHITE, 45,
						State.step4));
			}

			break;
		case 1200: // 40
			for (f = 0; f < 3; f++) {
				aircraft.add(new Aircraft(GV.scaleWidth - Aircraft.width - 20,
						(-Aircraft.halfHeight - 20) * (f + 1), Aircraft.width,
						Aircraft.height, 0, 0, Aircraft.width, Aircraft.height,
						5, Color.WHITE, 135, State.step4));
			}

			for (f = 0; f < 3; f++) {
				aircraft.add(new Aircraft(20, (-Aircraft.halfHeight - 20)
						* (f + 1), Aircraft.width, Aircraft.height, 0, 0,
						Aircraft.width, Aircraft.height, 5, Color.WHITE, 45,
						State.step4));
			}

			break;
		case 1290: // 43
			for (f = 0; f < 3; f++) {
				aircraft.add(new Aircraft(GV.scaleWidth - Aircraft.width - 20,
						(-Aircraft.halfHeight - 20) * (f + 1), Aircraft.width,
						Aircraft.height, 0, 0, Aircraft.width, Aircraft.height,
						5, Color.WHITE, 135, State.step4));
			}

			for (f = 0; f < 3; f++) {
				aircraft.add(new Aircraft(20, (-Aircraft.halfHeight - 20)
						* (f + 1), Aircraft.width, Aircraft.height, 0, 0,
						Aircraft.width, Aircraft.height, 5, Color.WHITE, 45,
						State.step4));
			}

			break;
		case 1380: // 46
			for (f = 0; f < 3; f++) {
				aircraft.add(new Aircraft(20, -GV.halfHeight
						+ (-Aircraft.halfHeight - 20) * (f + 1),
						Aircraft.width, Aircraft.height, 0, 0, Aircraft.width,
						Aircraft.height, 5, Color.WHITE, 45, State.step3));
			}

			for (f = 0; f < 3; f++) {
				aircraft.add(new Aircraft(GV.scaleWidth - Aircraft.width - 20,
						-GV.halfHeight + (-Aircraft.halfHeight - 20) * (f + 1),
						Aircraft.width, Aircraft.height, 0, 0, Aircraft.width,
						Aircraft.height, 5, Color.WHITE, 135, State.step3));
			}

			break;
		case 1470: // 49
			for (f = 0; f < 3; f++) {
				aircraft.add(new Aircraft(20, -GV.halfHeight
						+ (-Aircraft.halfHeight - 20) * (f + 1),
						Aircraft.width, Aircraft.height, 0, 0, Aircraft.width,
						Aircraft.height, 5, Color.WHITE, 45, State.step3));
			}

			for (f = 0; f < 3; f++) {
				aircraft.add(new Aircraft(GV.scaleWidth - Aircraft.width - 20,
						-GV.halfHeight + (-Aircraft.halfHeight - 20) * (f + 1),
						Aircraft.width, Aircraft.height, 0, 0, Aircraft.width,
						Aircraft.height, 5, Color.WHITE, 135, State.step3));
			}

			break;
		case 1560: // 52
			for (f = 0; f < 3; f++) {
				aircraft.add(new Aircraft(20, -GV.halfHeight
						+ (-Aircraft.halfHeight - 20) * (f + 1),
						Aircraft.width, Aircraft.height, 0, 0, Aircraft.width,
						Aircraft.height, 5, Color.WHITE, 45, State.step3));
			}
			for (f = 0; f < 3; f++) {
				aircraft.add(new Aircraft(20, -GV.halfHeight
						+ (-Aircraft.halfHeight - 20) * (f + 1),
						Aircraft.width, Aircraft.height, 0, 0, Aircraft.width,
						Aircraft.height, 5, Color.WHITE, 45, State.step3));
			}

			break;
		case 1650: // 55

			aircraft.add(new Aircraft(GV.halfWidth - Aircraft.width,
					-Aircraft.halfHeight - 20, Aircraft.width, Aircraft.height,
					0, 0, Aircraft.width, Aircraft.height, 5, Color.WHITE, 90,
					State.step1));

			aircraft.add(new Aircraft(GV.halfWidth + Aircraft.width,
					-Aircraft.halfHeight - 20, Aircraft.width, Aircraft.height,
					0, 0, Aircraft.width, Aircraft.height, 5, Color.WHITE, 90,
					State.step1));

			aircraft.add(new Aircraft(GV.halfWidth - (Aircraft.width << 1),
					-(Aircraft.halfHeight << 1), Aircraft.width,
					Aircraft.height, 0, 0, Aircraft.width, Aircraft.height, 5,
					Color.WHITE, 90, State.step2));

			aircraft.add(new Aircraft(GV.halfWidth + (Aircraft.width << 1),
					-(Aircraft.halfHeight << 1), Aircraft.width,
					Aircraft.height, 0, 0, Aircraft.width, Aircraft.height, 5,
					Color.WHITE, 90, State.step2));

			aircraft.add(new Aircraft(GV.halfWidth + Aircraft.width * 3,
					-(Aircraft.halfHeight * 3), Aircraft.width,
					Aircraft.height, 0, 0, Aircraft.width, Aircraft.height, 5,
					Color.WHITE, 135, State.step3));

			aircraft.add(new Aircraft(GV.halfWidth - (Aircraft.width * 3),
					-(Aircraft.halfHeight * 3), Aircraft.width,
					Aircraft.height, 0, 0, Aircraft.width, Aircraft.height, 5,
					Color.WHITE, 45, State.step3));

			aircraft.add(new Aircraft(20, -(Aircraft.halfHeight << 2),
					Aircraft.width, Aircraft.height, 0, 0, Aircraft.width,
					Aircraft.height, 5, Color.WHITE, 45, State.step4));

			aircraft.add(new Aircraft(GV.scaleWidth - Aircraft.width - 20,
					-(Aircraft.halfHeight << 2), Aircraft.width,
					Aircraft.height, 0, 0, Aircraft.width, Aircraft.height, 5,
					Color.WHITE, 135, State.step4));

			break;
		case 1800: // 60
			redMask.isAlive = true;
			
			break;
		case 1950: // 65
			
			// 蜘蛛背景音樂
			GV.music.player.release();
			GV.music = new Music(game.bee, R.raw.spidermusic, 1);
			GV.music.Play();
			
			spiderObj.add(new Spider(GV.halfWidth - (Spider.width >> 1), -Spider.height , Spider.width, Spider.height, 0, 0, spiderBitmap.getWidth(),spiderBitmap.getHeight() , 5, Color.WHITE, 90));
			
			break;
		}
		
		// 蜘蛛
		for (f = spiderObj.size() - 1; f >= 0; f--)
		{
			plane = spiderObj.get(f);
			
			// 子彈移動
			plane.AllBulletMove();
			
			if (plane.isAlive)
			{
				// 被大絕打到
				if (F16.isTouchBoom)
				{
					// 損血
					plane.blood -= F16.bigBoomPower;
					
					// 直接進行碰撞
					plane.Collisioned(frameTime, boom);
				}
				
				// 蜘蛛行動
				plane.Action(frameTime, myPlane.get(0), shootEffect);
			}
			else
			{
				// 破關
				breakOff = true;
				
				// 蜘蛛炮管
				for (j = plane.barrel.size() - 1; j >= 0; j--) 
				{
					// 子彈數量
					if (plane.barrel.get(j).bullet.size() != 0)
						break;
				}

				// 所有子彈消失後移除蜘蛛
				if (j == -1)
					spiderObj.remove(f);
			}
			
			// 檢查子彈與F16碰撞
			plane.CheckBulletCollision(frameTime, myPlane, boom);
		}
		
		// 檢查蜘蛛與自機碰撞
		Plane.CheckPlaneCollision((int) game.totalFrames, spiderObj, myPlane,boom);
		
		// 檢查民航機與自機碰撞
		Plane.CheckPlaneCollision((int) game.totalFrames, aircraft, myPlane, boom);

		// aircraft民航機行動
		for (f = aircraft.size() - 1; f >= 0; f--) {
			plane = aircraft.get(f);
			
			// 被大絕招打到
			if (F16.isTouchBoom)
			{
				// 損血
				plane.blood -= F16.bigBoomPower;
				
				// 直接進行碰撞
				plane.Collisioned(frameTime, boom);
			}

			// 子彈移動
			plane.AllBulletMove();

			if (plane.isAlive)
				plane.Action((int) game.totalFrames, myPlane.get(0), shootEffect);
			else 
			{
				// 民航機炮管
				for (j = plane.barrel.size() - 1; j >= 0; j--) 
				{
					// 子彈數量
					if (plane.barrel.get(j).bullet.size() != 0)
						break;
				}

				// 所有子彈消失後移除民航機
				if (j == -1)
					aircraft.remove(f);
			}
		}

		// 自機
		for (f = myPlane.size() - 1; f >= 0; f--) {
			plane = myPlane.get(f);

			// 子彈移動
			plane.AllBulletMove();

			// 自機行動
			if (plane.isAlive)
				plane.Action((int) game.totalFrames, shootEffect);

			// 檢查自機子彈與民航機碰撞
			plane.CheckBulletCollision((int) game.totalFrames, aircraft, boom);
			
			// 檢查自機子彈與蜘蛛碰撞
			plane.CheckBulletCollision(frameTime, spiderObj, boom);
		}

		// 檢查民航機子彈與自機的碰撞
		for (f = aircraft.size() - 1; f >= 0; f--) {
			aircraft.get(f).CheckBulletCollision((int) game.totalFrames,
					myPlane, boom);
		}

		// 大絕招
		if (F16.isTouchBoom) {
			
			// 新增爆炸
			boom.add(new Boom(0, 0, GV.scaleWidth, GV.scaleHeight));
			F16.isTouchBoom = false;
		}

		// 爆炸動畫
		for (f = boom.size() - 1; f >= 0; f--) {
			subBoom = boom.get(f);

			subBoom.Animation();

			if (!subBoom.isAlive)
				boom.remove(f);
		}

		// 發射特效
		for (f = shootEffect.size() - 1; f >= 0; f--) {
			if (shootEffect.get(f).Animation()) {
				shootEffect.remove(f);
			}
		}
		
		// 紅色遮罩淡入淡出
		if (redMask.isAlive)
		{
			redMask.Action(frameTime);
			
			// 設為切換背景
			spiderBackGroundObj.isAlive = true;
		}
		
		// 一般背景捲動
		if (backGroundObj.isAlive)
		{
			backGroundObj.addY(1);
			
			// 不斷循環
			backGroundObj.setY(backGroundObj.getY() % backGroundImage.getHeight());
		}
		
		// 如果是一般背景狀態
		if (spiderBackGroundObj.isAlive)
		{
			// 蜘蛛背景捲動
			if (spiderBackGroundObj.getY() < 0)
			{
				spiderBackGroundObj.addY(1);
			}
			else
				backGroundObj.isAlive = false;
		}

		// 更新fps顯示
		fpsText.message = (int) game.actualFPS + " FPS (" + (int) game.fps
				+ ") " + (int) game.totalFrames;
		
		// 重新初始化
		if (myPlane.get(0).life == 0 || breakOff) {
			if (!isBlackMask)
			{
				isBlackMask = true;
				gameOverText.isVisible = true;
				startBlackMaskFrame = frameTime;
			}
			
			// 重新遊戲
			if (frameTime - startBlackMaskFrame > restartDelayFrame)
			{
				GV.music.Stop();
				GameStateClass.changeState(GameState.Menu, this, game);
			}
		}
		
		// 10ms後震動100ms
		// vibrator.vibrate(new long[]{10,100},-1);

		super.Update();
	}

	@Override
	protected void Draw() {

		// 取得canvas
		subCanvas = game.canvas;

		// 一般背景
		if (backGroundObj.isAlive)
		{
			for (f = 0; f <= backGroundObj.destWidth; f++) 
			{
				for (j = -1; j <= backGroundObj.destHeight; j++) 
				{
					subCanvas.drawBitmap(backGroundImage,
										 f * backGroundImage.getWidth() + backGroundObj.destRect.left,
										 j * backGroundImage.getHeight() + backGroundObj.destRect.top,
										 null);
				}
			}
		}
		
		// 蜘蛛背景
		if (spiderBackGroundObj.isAlive)
		{
			subCanvas.drawBitmap(spiderBackGroundBitmap, spiderBackGroundObj.srcRect,spiderBackGroundObj.destRect,null);
		}
		
		// 蜘蛛
		for (f = spiderObj.size() - 1; f >= 0; f--)
		{
			plane = spiderObj.get(f);
			
			if (plane.isAlive)
			{
				subCanvas.save();
				
				subCanvas.rotate(plane.theta - 90, plane.getX() + plane.halfWidth, plane.getY() + plane.halfHeight);
				subCanvas.drawBitmap(spiderBitmap, plane.srcRect, plane.destRect, plane.paint);
				
				subCanvas.restore();
				
				subCanvas.drawRect(((Spider)plane).bloodObj.destRect,((Spider)plane).bloodObj.paint);
			}
			
			// 子彈
			for (j = plane.barrel.size() - 1; j >= 0; j--)
			{
				for (k = plane.barrel.get(j).bullet.size() - 1; k >= 0 ;k--)
				{
					bullet = plane.barrel.get(j).bullet.get(k);
					subCanvas.drawBitmap(bulletBitmap1, bullet.getX(), bullet.getY(),bullet.paint);
				}
			}
		}

		// 民航機
		for (f = aircraft.size() - 1; f >= 0; f--) {
			plane = aircraft.get(f);

			if (plane.isAlive) {
				subCanvas.save();

				subCanvas.rotate(plane.theta - 90, plane.getX()
						+ Aircraft.halfWidth, plane.getY()
						+ Aircraft.halfHeight);
				subCanvas.drawBitmap(aircraftBitmap, plane.srcRect,
						plane.destRect, plane.paint);

				subCanvas.restore();
			}

			// 民航機子彈
			for (j = plane.barrel.size() - 1; j >= 0; j--) {
				for (k = plane.barrel.get(j).bullet.size() - 1; k >= 0; k--) {
					bullet = plane.barrel.get(j).bullet.get(k);
					subCanvas.drawBitmap(bulletBitmap1, bullet.getX(), bullet
							.getY(), bullet.paint);
				}
			}
		}

		// 自機
		for (f = myPlane.size() - 1; f >= 0; f--) {
			plane = myPlane.get(f);

			if (plane.isAlive)
				subCanvas.drawBitmap(f16Bitmap, plane.srcRect, plane.destRect,
						plane.paint);

			// 自機炮管
			for (j = plane.barrel.size() - 1; j >= 0; j--) {
				// 自機子彈
				for (k = plane.barrel.get(j).bullet.size() - 1; k >= 0; k--) {
					bullet = plane.barrel.get(j).bullet.get(k);
					subCanvas.drawBitmap(bulletBitmap1, bullet.getX(), bullet
							.getY(), bullet.paint);
				}
			}
		}

		// 爆炸
		for (f = 0; f < boom.size(); f++) {
			subBoom = boom.get(f);
			subCanvas.drawBitmap(boomBitmap, subBoom.srcRect, subBoom.destRect,
					subBoom.paint);
		}

		// 發射特效
		for (f = shootEffect.size() - 1; f >= 0; f--) {
			subCanvas.drawBitmap(shootEffectBitmap, shootEffect.get(f).srcRect,
					shootEffect.get(f).destRect, shootEffect.get(f).paint);
		}

		// FPS文字
		subCanvas.drawText(fpsText.message, fpsText.x, fpsText.y, fpsText.paint);
		
		// 紅色遮罩
		if (redMask.isAlive)
		{
			subCanvas.drawRect(redMask.destRect, redMask.paint);
			
			// 警告訊息
			subCanvas.drawText(redMask.warningText.message, redMask.warningText.x, redMask.warningText.y, redMask.warningText.paint);
		}

		// 黑色遮罩
		if (isBlackMask)
			subCanvas.drawRect(blackMask.destRect, blackMask.paint);
		
		// gameover
		if (gameOverText.isVisible)
			subCanvas.drawText(gameOverText.message, gameOverText.x, gameOverText.y, gameOverText.paint);
		
		super.Draw();
	}
}
