package Bee.scene;

import Bee.Object.Object2D;
import Bee.Object.Text;
import Bee.main.GV;
import XNA.lbs.DrawableGameComponent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;

public class Menu extends DrawableGameComponent{
	private Game1 game;
	private Canvas subCanvas;
	
	// �e��ɶ�
	private int frameTime;
	
	// �I��
	private Bitmap menuBitmap;
	private Object2D menuObj;
	
	// "Touch"��r
	private Text menuText;
	private Text menuShadow;
	
	// ���D��r
	private Text menuTitleText;
	private Text menuTitleShadow;
	
	public Menu(Game1 game) {
		this.game = game;
	}
	
	@Override
	protected void Initialize() {
		
		// ���D��r
		menuTitleText = new Text(65, 50, 36, "�p����", Color.YELLOW);
		
		menuTitleShadow = new Text(menuTitleText.x + 10, menuTitleText.y - 5, menuTitleText.size, menuTitleText.message, Color.BLACK);
		
		// "Touch"��r
		menuText = new Text(50, GV.scaleHeight - 70, 12, "Touch screen to start", Color.YELLOW);
		menuText.delayFrame = 20;
		
		menuShadow = new Text(menuText.x + 10, menuText.y - 5, menuText.size, menuText.message, Color.BLACK);
		
		// ���J�I��
		menuBitmap = BitmapFactory.decodeResource(GV.res, Bee.main.R.drawable.menu);
		
		// ��l�ƭI������
		menuObj = new Object2D(0, 0, GV.scaleWidth, GV.scaleHeight, 0, 0, menuBitmap.getWidth(), menuBitmap.getHeight(), 0, Color.WHITE, 0);
		menuObj.paint.setAlpha(170);
		
		super.Initialize();
	}
	
	@Override
	protected void UnloadContent() {
		
		super.UnloadContent();
	}
	
	@Override
	protected void Update() {
		
		// ���o�ثe���e��ɶ�
		frameTime = (int)game.totalFrames;
		
		// �{�t"Touch"��r
		if (frameTime - menuText.startFrame > menuText.delayFrame)
		{
			menuText.startFrame = frameTime;
			
			menuText.isVisible = !menuText.isVisible;
		}
		
		super.Update();
	}
	
	@Override
	protected void Draw() {
		
		subCanvas = game.canvas;
		
		// �e�X�I��
		subCanvas.drawBitmap(menuBitmap, menuObj.srcRect, menuObj.destRect, menuObj.paint);
		
		// �e�X���D���v
		subCanvas.drawText(menuTitleShadow.message, menuTitleShadow.x, menuTitleShadow.y, menuTitleShadow.paint);
		
		// �e�X���D
		subCanvas.drawText(menuTitleText.message, menuTitleText.x, menuTitleText.y, menuTitleText.paint);
		
		// �{�t
		if (menuText.isVisible)
		{
			// �e�X"Touch"���v
			subCanvas.drawText(menuShadow.message, menuShadow.x, menuShadow.y, menuShadow.paint);
			
			// �e�X"Touch"��r
			subCanvas.drawText(menuText.message, menuText.x, menuText.y, menuText.paint);
		}
		
		super.Draw();
	}
}
