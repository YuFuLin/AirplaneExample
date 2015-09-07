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

	// ���FPS��r
	private Text fpsText;

	// �۾��Ϥ�
	private Bitmap f16Bitmap;

	// �۾�����
	private ArrayList<Plane> myPlane;

	// ������Ϥ�
	private Bitmap aircraftBitmap;

	// ���������
	private ArrayList<Plane> aircraft;

	// �l�u�Ϥ�
	private Bitmap bulletBitmap1;

	// �@��I���Ϥ�
	private Bitmap backGroundImage;

	// �@��I������
	private Object2D backGroundObj;

	// �z���Ϥ�
	private Bitmap boomBitmap;

	// �z��
	private ArrayList<Boom> boom = new ArrayList<Boom>();

	// �l�u�Ȧs�Ϊ���
	private Bullet1 bullet;

	// �z���Ȧs
	private Boom subBoom;

	// �����Ȧs
	private Plane plane;

	// �j���ܼ�
	private int f, j, k;

	// �o�g�S�ĹϤ�
	private Bitmap shootEffectBitmap;

	// �o�g�S�Ī���
	public ArrayList<ShootEffect> shootEffect = new ArrayList<ShootEffect>();

	// �O�_�ҥζ¦�B�n
	private boolean isBlackMask;
	private Object2D blackMask;

	// �O�_�ҥά���B�n
	private RedMask redMask;

	// �¦�B�n�}�l�ҥήɶ�
	private int startBlackMaskFrame;

	// �q�¦�B�n�쭫�s�C�����ɶ�
	private int restartDelayFrame = 150;

	// ���GameOver��r
	private Text gameOverText;
	
	// �j��
	private Bitmap spiderBitmap;
	private ArrayList<Plane> spiderObj;
	
	// �e���`��
	private int frameTime;
	
	// �j��I��
	private Bitmap spiderBackGroundBitmap;
	
	// �j��I������
	private Object2D spiderBackGroundObj;
	
	// �}��
	private boolean breakOff;

	public Stage1(Game1 game) {
		this.game = game;
	}

	@Override
	protected void Initialize() {

		// �w����n�T�����
		for (int f = 0; f < 360; f++) {
			GV.Cosine[f] = (float) Math.cos(f * Math.PI / 180);
			GV.Sine[f] = (float) Math.sin(f * Math.PI / 180);
		}

		GV.screenRect = new Rect(0, 0, GV.scaleWidth, GV.scaleHeight);

		// �k�s
		game.totalFrames = 0;

		super.Initialize();
	}

	@Override
	protected void LoadContent() {

		// ���o��@�v�檺�e��
		int width, height;

		// �����
		aircraftBitmap = (Bitmap) BitmapFactory.decodeResource(GV.res,
				R.drawable.aircraft);

		width = aircraftBitmap.getWidth() / 4;
		height = aircraftBitmap.getHeight() / 5;

		aircraft = new ArrayList<Plane>();
		Aircraft.width = width;
		Aircraft.height = height;
		Aircraft.halfWidth = width >> 1;
		Aircraft.halfHeight = height >> 1;

		// �۾�
		f16Bitmap = (Bitmap) BitmapFactory.decodeResource(GV.res,
				R.drawable.f16);

		width = f16Bitmap.getWidth() / 4;
		height = f16Bitmap.getHeight() / 5;

		myPlane = new ArrayList<Plane>();
		myPlane.add(new F16(GV.halfWidth - (width >> 1), GV.scaleHeight, width,
				height, 0, 0, width, height, 5, Color.WHITE, 270));

		// �]�wĲ����m�b���U��
		GV.x = GV.halfWidth;
		GV.y = GV.scaleHeight - height;

		// �l�u
		bulletBitmap1 = BitmapFactory
				.decodeResource(GV.res, R.drawable.bullet1);

		// �I��
		backGroundImage = (Bitmap) BitmapFactory.decodeResource(GV.res,
				R.drawable.water);
		
		// �I������
		backGroundObj = new Object2D(0, 0, GV.scaleWidth / backGroundImage.getWidth(), GV.scaleHeight / backGroundImage.getHeight(), 0, 0, 0, 0, 0, 0, 0);
		backGroundObj.isAlive = true;

		// ��r
		fpsText = new Text(GV.halfWidth, 20, 12, "FPS", Color.YELLOW);

		// �z��
		boomBitmap = BitmapFactory.decodeResource(GV.res, R.drawable.boom2);
		Boom.size = boomBitmap.getWidth() / Boom.col;
		Boom.halfSize = Boom.size >> 1;

		// �o�g���S��
		shootEffectBitmap = BitmapFactory.decodeResource(GV.res,
				R.drawable.effect);

		ShootEffect.width = shootEffectBitmap.getWidth();
		ShootEffect.height = shootEffectBitmap.getHeight();
		ShootEffect.halfWidth = ShootEffect.width >> 1;
		ShootEffect.halfHeight = ShootEffect.height >> 1;

		// ����Mask
		redMask = new RedMask(Color.RED, 0);

		// �¦�Mask
		blackMask = new Object2D(0, 0, GV.scaleWidth, GV.scaleHeight,
				Color.BLACK, 128);
		
		gameOverText = new Text(20, GV.halfHeight-18, 36, "Game Over", Color.WHITE);
		
		// �j��
		spiderBitmap = BitmapFactory.decodeResource(GV.res, R.drawable.spider);
		
		Spider.width = spiderBitmap.getWidth();
		Spider.height = spiderBitmap.getHeight();
		
		spiderObj = new ArrayList<Plane>();
		
		// �j��I��
		spiderBackGroundBitmap = BitmapFactory.decodeResource(GV.res, R.drawable.spider_scene);
		
		// �j��I������
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
		
		// �b�C������l�ƭ���(��M��U�誺Action)
		switch ((int) game.totalFrames) {
		case 150: // 5��
			aircraft.add(new Aircraft(GV.halfWidth - Aircraft.halfWidth,
					-Aircraft.height, Aircraft.width, Aircraft.height, 0, 0,
					Aircraft.width, Aircraft.height, 5, Color.WHITE, 90,
					State.step2));
			break;
		case 240: // 8��
			aircraft.add(new Aircraft(20, -Aircraft.height, Aircraft.width,
					Aircraft.height, 0, 0, Aircraft.width, Aircraft.height, 5,
					Color.WHITE, 90, State.step2));
			break;
		case 330: // 11��
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
		case 450: // 15�� ����s��X�{
			for (f = 0; f < 5; f++) {
				aircraft.add(new Aircraft(20, (-Aircraft.halfHeight - 20)
						* (f + 1), Aircraft.width, Aircraft.height, 0, 0,
						Aircraft.width, Aircraft.height, 5, Color.RED, 90,
						State.step1));
			}

			break;
		case 540: // 18�� �����s��X�{
			for (f = 0; f < 5; f++) {
				aircraft.add(new Aircraft(GV.halfWidth - Aircraft.halfWidth,
						(-Aircraft.halfHeight - 20) * (f + 1), Aircraft.width,
						Aircraft.height, 0, 0, Aircraft.width, Aircraft.height,
						5, Color.BLUE, 90, State.step2));
			}

			break;
		case 630: // 21�� �k��s��X�{
			for (f = 0; f < 5; f++) {
				aircraft.add(new Aircraft(GV.scaleWidth - Aircraft.width - 20,
						(-Aircraft.halfHeight - 20) * (f + 1), Aircraft.width,
						Aircraft.height, 0, 0, Aircraft.width, Aircraft.height,
						5, Color.GRAY, 90, State.step1));
			}

			break;

		case 750: // 25�� ����k�s��X�{
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
		case 930: // 31��
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
			
			// �j��I������
			GV.music.player.release();
			GV.music = new Music(game.bee, R.raw.spidermusic, 1);
			GV.music.Play();
			
			spiderObj.add(new Spider(GV.halfWidth - (Spider.width >> 1), -Spider.height , Spider.width, Spider.height, 0, 0, spiderBitmap.getWidth(),spiderBitmap.getHeight() , 5, Color.WHITE, 90));
			
			break;
		}
		
		// �j��
		for (f = spiderObj.size() - 1; f >= 0; f--)
		{
			plane = spiderObj.get(f);
			
			// �l�u����
			plane.AllBulletMove();
			
			if (plane.isAlive)
			{
				// �Q�j������
				if (F16.isTouchBoom)
				{
					// �l��
					plane.blood -= F16.bigBoomPower;
					
					// �����i��I��
					plane.Collisioned(frameTime, boom);
				}
				
				// �j����
				plane.Action(frameTime, myPlane.get(0), shootEffect);
			}
			else
			{
				// �}��
				breakOff = true;
				
				// �j�זּ��
				for (j = plane.barrel.size() - 1; j >= 0; j--) 
				{
					// �l�u�ƶq
					if (plane.barrel.get(j).bullet.size() != 0)
						break;
				}

				// �Ҧ��l�u�����Ჾ���j��
				if (j == -1)
					spiderObj.remove(f);
			}
			
			// �ˬd�l�u�PF16�I��
			plane.CheckBulletCollision(frameTime, myPlane, boom);
		}
		
		// �ˬd�j��P�۾��I��
		Plane.CheckPlaneCollision((int) game.totalFrames, spiderObj, myPlane,boom);
		
		// �ˬd������P�۾��I��
		Plane.CheckPlaneCollision((int) game.totalFrames, aircraft, myPlane, boom);

		// aircraft��������
		for (f = aircraft.size() - 1; f >= 0; f--) {
			plane = aircraft.get(f);
			
			// �Q�j���ۥ���
			if (F16.isTouchBoom)
			{
				// �l��
				plane.blood -= F16.bigBoomPower;
				
				// �����i��I��
				plane.Collisioned(frameTime, boom);
			}

			// �l�u����
			plane.AllBulletMove();

			if (plane.isAlive)
				plane.Action((int) game.totalFrames, myPlane.get(0), shootEffect);
			else 
			{
				// ���������
				for (j = plane.barrel.size() - 1; j >= 0; j--) 
				{
					// �l�u�ƶq
					if (plane.barrel.get(j).bullet.size() != 0)
						break;
				}

				// �Ҧ��l�u�����Ჾ�������
				if (j == -1)
					aircraft.remove(f);
			}
		}

		// �۾�
		for (f = myPlane.size() - 1; f >= 0; f--) {
			plane = myPlane.get(f);

			// �l�u����
			plane.AllBulletMove();

			// �۾����
			if (plane.isAlive)
				plane.Action((int) game.totalFrames, shootEffect);

			// �ˬd�۾��l�u�P������I��
			plane.CheckBulletCollision((int) game.totalFrames, aircraft, boom);
			
			// �ˬd�۾��l�u�P�j��I��
			plane.CheckBulletCollision(frameTime, spiderObj, boom);
		}

		// �ˬd������l�u�P�۾����I��
		for (f = aircraft.size() - 1; f >= 0; f--) {
			aircraft.get(f).CheckBulletCollision((int) game.totalFrames,
					myPlane, boom);
		}

		// �j����
		if (F16.isTouchBoom) {
			
			// �s�W�z��
			boom.add(new Boom(0, 0, GV.scaleWidth, GV.scaleHeight));
			F16.isTouchBoom = false;
		}

		// �z���ʵe
		for (f = boom.size() - 1; f >= 0; f--) {
			subBoom = boom.get(f);

			subBoom.Animation();

			if (!subBoom.isAlive)
				boom.remove(f);
		}

		// �o�g�S��
		for (f = shootEffect.size() - 1; f >= 0; f--) {
			if (shootEffect.get(f).Animation()) {
				shootEffect.remove(f);
			}
		}
		
		// ����B�n�H�J�H�X
		if (redMask.isAlive)
		{
			redMask.Action(frameTime);
			
			// �]�������I��
			spiderBackGroundObj.isAlive = true;
		}
		
		// �@��I������
		if (backGroundObj.isAlive)
		{
			backGroundObj.addY(1);
			
			// ���_�`��
			backGroundObj.setY(backGroundObj.getY() % backGroundImage.getHeight());
		}
		
		// �p�G�O�@��I�����A
		if (spiderBackGroundObj.isAlive)
		{
			// �j��I������
			if (spiderBackGroundObj.getY() < 0)
			{
				spiderBackGroundObj.addY(1);
			}
			else
				backGroundObj.isAlive = false;
		}

		// ��sfps���
		fpsText.message = (int) game.actualFPS + " FPS (" + (int) game.fps
				+ ") " + (int) game.totalFrames;
		
		// ���s��l��
		if (myPlane.get(0).life == 0 || breakOff) {
			if (!isBlackMask)
			{
				isBlackMask = true;
				gameOverText.isVisible = true;
				startBlackMaskFrame = frameTime;
			}
			
			// ���s�C��
			if (frameTime - startBlackMaskFrame > restartDelayFrame)
			{
				GV.music.Stop();
				GameStateClass.changeState(GameState.Menu, this, game);
			}
		}
		
		// 10ms��_��100ms
		// vibrator.vibrate(new long[]{10,100},-1);

		super.Update();
	}

	@Override
	protected void Draw() {

		// ���ocanvas
		subCanvas = game.canvas;

		// �@��I��
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
		
		// �j��I��
		if (spiderBackGroundObj.isAlive)
		{
			subCanvas.drawBitmap(spiderBackGroundBitmap, spiderBackGroundObj.srcRect,spiderBackGroundObj.destRect,null);
		}
		
		// �j��
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
			
			// �l�u
			for (j = plane.barrel.size() - 1; j >= 0; j--)
			{
				for (k = plane.barrel.get(j).bullet.size() - 1; k >= 0 ;k--)
				{
					bullet = plane.barrel.get(j).bullet.get(k);
					subCanvas.drawBitmap(bulletBitmap1, bullet.getX(), bullet.getY(),bullet.paint);
				}
			}
		}

		// �����
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

			// ������l�u
			for (j = plane.barrel.size() - 1; j >= 0; j--) {
				for (k = plane.barrel.get(j).bullet.size() - 1; k >= 0; k--) {
					bullet = plane.barrel.get(j).bullet.get(k);
					subCanvas.drawBitmap(bulletBitmap1, bullet.getX(), bullet
							.getY(), bullet.paint);
				}
			}
		}

		// �۾�
		for (f = myPlane.size() - 1; f >= 0; f--) {
			plane = myPlane.get(f);

			if (plane.isAlive)
				subCanvas.drawBitmap(f16Bitmap, plane.srcRect, plane.destRect,
						plane.paint);

			// �۾�����
			for (j = plane.barrel.size() - 1; j >= 0; j--) {
				// �۾��l�u
				for (k = plane.barrel.get(j).bullet.size() - 1; k >= 0; k--) {
					bullet = plane.barrel.get(j).bullet.get(k);
					subCanvas.drawBitmap(bulletBitmap1, bullet.getX(), bullet
							.getY(), bullet.paint);
				}
			}
		}

		// �z��
		for (f = 0; f < boom.size(); f++) {
			subBoom = boom.get(f);
			subCanvas.drawBitmap(boomBitmap, subBoom.srcRect, subBoom.destRect,
					subBoom.paint);
		}

		// �o�g�S��
		for (f = shootEffect.size() - 1; f >= 0; f--) {
			subCanvas.drawBitmap(shootEffectBitmap, shootEffect.get(f).srcRect,
					shootEffect.get(f).destRect, shootEffect.get(f).paint);
		}

		// FPS��r
		subCanvas.drawText(fpsText.message, fpsText.x, fpsText.y, fpsText.paint);
		
		// ����B�n
		if (redMask.isAlive)
		{
			subCanvas.drawRect(redMask.destRect, redMask.paint);
			
			// ĵ�i�T��
			subCanvas.drawText(redMask.warningText.message, redMask.warningText.x, redMask.warningText.y, redMask.warningText.paint);
		}

		// �¦�B�n
		if (isBlackMask)
			subCanvas.drawRect(blackMask.destRect, blackMask.paint);
		
		// gameover
		if (gameOverText.isVisible)
			subCanvas.drawText(gameOverText.message, gameOverText.x, gameOverText.y, gameOverText.paint);
		
		super.Draw();
	}
}
