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

	// Show the FPS
	private Text fpsText;

	// Player bitmap
	private Bitmap f16Bitmap;

	// Player object
	private ArrayList<Plane> myPlane;

	// Aircraft Bitmap
	private Bitmap aircraftBitmap;

	// Aircraft object
	private ArrayList<Plane> aircraft;

	// Bullet Bitmap
	private Bitmap bulletBitmap1;

	// Background Bitmap
	private Bitmap backGroundImage;

	// 2D Bitmap
	private Object2D backGroundObj;

	// Explode Bitmap
	private Bitmap boomBitmap;

	// Explode
	private ArrayList<Boom> boom = new ArrayList<Boom>();

	// Bullet buffer
	private Bullet1 bullet;

	// Explode buffer
	private Boom subBoom;

	// Aircraft buffer
	private Plane plane;

	// loop counter
	private int f, j, k;

	// Shooe effect bitmap
	private Bitmap shootEffectBitmap;

	// shoot
	public ArrayList<ShootEffect> shootEffect = new ArrayList<ShootEffect>();

	// Blackmask bloolean
	private boolean isBlackMask;
	private Object2D blackMask;

	// Redmask bloolean
	private RedMask redMask;

	// Black mask start time
	private int startBlackMaskFrame;

	// restart delay time
	private int restartDelayFrame = 150;

	// GameOver text
	private Text gameOverText;
	
	// spider 
	private Bitmap spiderBitmap;
	private ArrayList<Plane> spiderObj;
	
	// frame time
	private int frameTime;
	
	// Spider background
	private Bitmap spiderBackGroundBitmap;
	
	// Spider background object
	private Object2D spiderBackGroundObj;
	
	// finish the game
	private boolean breakOff;

	public Stage1(Game1 game) {
		this.game = game;
	}

	@Override
	protected void Initialize() {

		// Trigonometric function
		for (int f = 0; f < 360; f++) {
			GV.Cosine[f] = (float) Math.cos(f * Math.PI / 180);
			GV.Sine[f] = (float) Math.sin(f * Math.PI / 180);
		}

		GV.screenRect = new Rect(0, 0, GV.scaleWidth, GV.scaleHeight);

		// Counter to zero
		game.totalFrames = 0;

		super.Initialize();
	}

	@Override
	protected void LoadContent() {

		// set width anf height for frame
		int width, height;

		// aircraft Bitmap
		aircraftBitmap = (Bitmap) BitmapFactory.decodeResource(GV.res,
				R.drawable.aircraft);

		width = aircraftBitmap.getWidth() / 4;
		height = aircraftBitmap.getHeight() / 5;

		aircraft = new ArrayList<Plane>();
		Aircraft.width = width;
		Aircraft.height = height;
		Aircraft.halfWidth = width >> 1;
		Aircraft.halfHeight = height >> 1;

		// self plane
		f16Bitmap = (Bitmap) BitmapFactory.decodeResource(GV.res,
				R.drawable.f16);

		width = f16Bitmap.getWidth() / 4;
		height = f16Bitmap.getHeight() / 5;

		myPlane = new ArrayList<Plane>();
		myPlane.add(new F16(GV.halfWidth - (width >> 1), GV.scaleHeight, width,
				height, 0, 0, width, height, 5, Color.WHITE, 270));

		// set the touching area
		GV.x = GV.halfWidth;
		GV.y = GV.scaleHeight - height;

		// bullet
		bulletBitmap1 = BitmapFactory
				.decodeResource(GV.res, R.drawable.bullet1);

		// background
		backGroundImage = (Bitmap) BitmapFactory.decodeResource(GV.res,
				R.drawable.water);
		
		// background object
		backGroundObj = new Object2D(0, 0, GV.scaleWidth / backGroundImage.getWidth(), GV.scaleHeight / backGroundImage.getHeight(), 0, 0, 0, 0, 0, 0, 0);
		backGroundObj.isAlive = true;

		// text
		fpsText = new Text(GV.halfWidth, 20, 12, "FPS", Color.YELLOW);

		// explode
		boomBitmap = BitmapFactory.decodeResource(GV.res, R.drawable.boom2);
		Boom.size = boomBitmap.getWidth() / Boom.col;
		Boom.halfSize = Boom.size >> 1;

		// shooting effect
		shootEffectBitmap = BitmapFactory.decodeResource(GV.res,
				R.drawable.effect);

		ShootEffect.width = shootEffectBitmap.getWidth();
		ShootEffect.height = shootEffectBitmap.getHeight();
		ShootEffect.halfWidth = ShootEffect.width >> 1;
		ShootEffect.halfHeight = ShootEffect.height >> 1;

		// Red Mask
		redMask = new RedMask(Color.RED, 0);

		// Black Mask
		blackMask = new Object2D(0, 0, GV.scaleWidth, GV.scaleHeight,
				Color.BLACK, 128);
		
		gameOverText = new Text(20, GV.halfHeight-18, 36, "Game Over", Color.WHITE);
		
		// Spider
		spiderBitmap = BitmapFactory.decodeResource(GV.res, R.drawable.spider);
		
		Spider.width = spiderBitmap.getWidth();
		Spider.height = spiderBitmap.getHeight();
		
		spiderObj = new ArrayList<Plane>();
		
		// Spider background
		spiderBackGroundBitmap = BitmapFactory.decodeResource(GV.res, R.drawable.spider_scene);
		
		// Spider background object
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
		
		// Init the plane
		switch ((int) game.totalFrames) {
		case 150: // 5 second
			aircraft.add(new Aircraft(GV.halfWidth - Aircraft.halfWidth,
					-Aircraft.height, Aircraft.width, Aircraft.height, 0, 0,
					Aircraft.width, Aircraft.height, 5, Color.WHITE, 90,
					State.step2));
			break;
		case 240: // 8 second
			aircraft.add(new Aircraft(20, -Aircraft.height, Aircraft.width,
					Aircraft.height, 0, 0, Aircraft.width, Aircraft.height, 5,
					Color.WHITE, 90, State.step2));
			break;
		case 330: // 11 second
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
		case 450: // 15 second
			for (f = 0; f < 5; f++) {
				aircraft.add(new Aircraft(20, (-Aircraft.halfHeight - 20)
						* (f + 1), Aircraft.width, Aircraft.height, 0, 0,
						Aircraft.width, Aircraft.height, 5, Color.RED, 90,
						State.step1));
			}

			break;
		case 540: // 18 second
			for (f = 0; f < 5; f++) {
				aircraft.add(new Aircraft(GV.halfWidth - Aircraft.halfWidth,
						(-Aircraft.halfHeight - 20) * (f + 1), Aircraft.width,
						Aircraft.height, 0, 0, Aircraft.width, Aircraft.height,
						5, Color.BLUE, 90, State.step2));
			}

			break;
		case 630: // 21 second
			for (f = 0; f < 5; f++) {
				aircraft.add(new Aircraft(GV.scaleWidth - Aircraft.width - 20,
						(-Aircraft.halfHeight - 20) * (f + 1), Aircraft.width,
						Aircraft.height, 0, 0, Aircraft.width, Aircraft.height,
						5, Color.GRAY, 90, State.step1));
			}

			break;

		case 750: // 25 second
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
		case 930: // 31 second
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
			
			// Spider background music
			GV.music.player.release();
			GV.music = new Music(game.bee, R.raw.spidermusic, 1);
			GV.music.Play();
			
			spiderObj.add(new Spider(GV.halfWidth - (Spider.width >> 1), -Spider.height , Spider.width, Spider.height, 0, 0, spiderBitmap.getWidth(),spiderBitmap.getHeight() , 5, Color.WHITE, 90));
			
			break;
		}
		
		// Spider
		for (f = spiderObj.size() - 1; f >= 0; f--)
		{
			plane = spiderObj.get(f);
			
			// bullet moving
			plane.AllBulletMove();
			
			if (plane.isAlive)
			{
				// Hit by bomb
				if (F16.isTouchBoom)
				{
					// Hitpoint reduce
					plane.blood -= F16.bigBoomPower;
					
					// Plane Collision
					plane.Collisioned(frameTime, boom);
				}
				
				// Spider moving
				plane.Action(frameTime, myPlane.get(0), shootEffect);
			}
			else
			{
		
				breakOff = true;
				
		
				for (j = plane.barrel.size() - 1; j >= 0; j--) 
				{
			
					if (plane.barrel.get(j).bullet.size() != 0)
						break;
				}

		
				if (j == -1)
					spiderObj.remove(f);
			}
			
		
			plane.CheckBulletCollision(frameTime, myPlane, boom);
		}
		
	
		Plane.CheckPlaneCollision((int) game.totalFrames, spiderObj, myPlane,boom);
		

		Plane.CheckPlaneCollision((int) game.totalFrames, aircraft, myPlane, boom);


		for (f = aircraft.size() - 1; f >= 0; f--) {
			plane = aircraft.get(f);
			
	
			if (F16.isTouchBoom)
			{
		
				plane.blood -= F16.bigBoomPower;
				
	
				plane.Collisioned(frameTime, boom);
			}

	
			plane.AllBulletMove();

			if (plane.isAlive)
				plane.Action((int) game.totalFrames, myPlane.get(0), shootEffect);
			else 
			{
		
				for (j = plane.barrel.size() - 1; j >= 0; j--) 
				{
			
					if (plane.barrel.get(j).bullet.size() != 0)
						break;
				}

				
				if (j == -1)
					aircraft.remove(f);
			}
		}

		// self plane
		for (f = myPlane.size() - 1; f >= 0; f--) {
			plane = myPlane.get(f);

			// Bullet moving
			plane.AllBulletMove();

			// Self moving
			if (plane.isAlive)
				plane.Action((int) game.totalFrames, shootEffect);

			// Collision checking with aircraft
			plane.CheckBulletCollision((int) game.totalFrames, aircraft, boom);
			
			// Collision checking with bullet
			plane.CheckBulletCollision(frameTime, spiderObj, boom);
		}

		// Collision checking
		for (f = aircraft.size() - 1; f >= 0; f--) {
			aircraft.get(f).CheckBulletCollision((int) game.totalFrames,
					myPlane, boom);
		}

		// Bome skill
		if (F16.isTouchBoom) {
			
			// Add explode
			boom.add(new Boom(0, 0, GV.scaleWidth, GV.scaleHeight));
			F16.isTouchBoom = false;
		}

		// Explode animation
		for (f = boom.size() - 1; f >= 0; f--) {
			subBoom = boom.get(f);

			subBoom.Animation();

			if (!subBoom.isAlive)
				boom.remove(f);
		}

		// Shoot Effect
		for (f = shootEffect.size() - 1; f >= 0; f--) {
			if (shootEffect.get(f).Animation()) {
				shootEffect.remove(f);
			}
		}
		
		// Redmask fade in and out
		if (redMask.isAlive)
		{
			redMask.Action(frameTime);
			
			// Switch the background
			spiderBackGroundObj.isAlive = true;
		}
		

		if (backGroundObj.isAlive)
		{
			backGroundObj.addY(1);
			
			// Loop
			backGroundObj.setY(backGroundObj.getY() % backGroundImage.getHeight());
		}
		

		if (spiderBackGroundObj.isAlive)
		{
			// Spider background
			if (spiderBackGroundObj.getY() < 0)
			{
				spiderBackGroundObj.addY(1);
			}
			else
				backGroundObj.isAlive = false;
		}

		// Refresf FPS
		fpsText.message = (int) game.actualFPS + " FPS (" + (int) game.fps
				+ ") " + (int) game.totalFrames;
		
		// Reinit
		if (myPlane.get(0).life == 0 || breakOff) {
			if (!isBlackMask)
			{
				isBlackMask = true;
				gameOverText.isVisible = true;
				startBlackMaskFrame = frameTime;
			}
			
			// Game reset
			if (frameTime - startBlackMaskFrame > restartDelayFrame)
			{
				GV.music.Stop();
				GameStateClass.changeState(GameState.Menu, this, game);
			}
		}
		

		super.Update();
	}

	@Override
	protected void Draw() {

		// get canvas
		subCanvas = game.canvas;

		// Background
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
		
		// Spider Background
		if (spiderBackGroundObj.isAlive)
		{
			subCanvas.drawBitmap(spiderBackGroundBitmap, spiderBackGroundObj.srcRect,spiderBackGroundObj.destRect,null);
		}
		
		// Spider
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
			
			// Bullet
			for (j = plane.barrel.size() - 1; j >= 0; j--)
			{
				for (k = plane.barrel.get(j).bullet.size() - 1; k >= 0 ;k--)
				{
					bullet = plane.barrel.get(j).bullet.get(k);
					subCanvas.drawBitmap(bulletBitmap1, bullet.getX(), bullet.getY(),bullet.paint);
				}
			}
		}

		// Aircraft
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

			// Aircraft bullet
			for (j = plane.barrel.size() - 1; j >= 0; j--) {
				for (k = plane.barrel.get(j).bullet.size() - 1; k >= 0; k--) {
					bullet = plane.barrel.get(j).bullet.get(k);
					subCanvas.drawBitmap(bulletBitmap1, bullet.getX(), bullet
							.getY(), bullet.paint);
				}
			}
		}

		// Self plane
		for (f = myPlane.size() - 1; f >= 0; f--) {
			plane = myPlane.get(f);

			if (plane.isAlive)
				subCanvas.drawBitmap(f16Bitmap, plane.srcRect, plane.destRect,
						plane.paint);

			// Barrel
			for (j = plane.barrel.size() - 1; j >= 0; j--) {
				// Bullet
				for (k = plane.barrel.get(j).bullet.size() - 1; k >= 0; k--) {
					bullet = plane.barrel.get(j).bullet.get(k);
					subCanvas.drawBitmap(bulletBitmap1, bullet.getX(), bullet
							.getY(), bullet.paint);
				}
			}
		}

		// Boom effect
		for (f = 0; f < boom.size(); f++) {
			subBoom = boom.get(f);
			subCanvas.drawBitmap(boomBitmap, subBoom.srcRect, subBoom.destRect,
					subBoom.paint);
		}

		// Shooting Effect
		for (f = shootEffect.size() - 1; f >= 0; f--) {
			subCanvas.drawBitmap(shootEffectBitmap, shootEffect.get(f).srcRect,
					shootEffect.get(f).destRect, shootEffect.get(f).paint);
		}

		// FPS text
		subCanvas.drawText(fpsText.message, fpsText.x, fpsText.y, fpsText.paint);
		
		// Red Mask
		if (redMask.isAlive)
		{
			subCanvas.drawRect(redMask.destRect, redMask.paint);
			
			// Warning message
			subCanvas.drawText(redMask.warningText.message, redMask.warningText.x, redMask.warningText.y, redMask.warningText.paint);
		}

		// Black mask
		if (isBlackMask)
			subCanvas.drawRect(blackMask.destRect, blackMask.paint);
		
		// gameover
		if (gameOverText.isVisible)
			subCanvas.drawText(gameOverText.message, gameOverText.x, gameOverText.y, gameOverText.paint);
		
		super.Draw();
	}
}
