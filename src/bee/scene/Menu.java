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
	
	// 畫格時間
	private int frameTime;
	
	// 背景
	private Bitmap menuBitmap;
	private Object2D menuObj;
	
	// "Touch"文字
	private Text menuText;
	private Text menuShadow;
	
	// 標題文字
	private Text menuTitleText;
	private Text menuTitleShadow;
	
	public Menu(Game1 game) {
		this.game = game;
	}
	
	@Override
	protected void Initialize() {
		
		// 標題文字
		menuTitleText = new Text(65, 50, 36, "小飛機", Color.YELLOW);
		
		menuTitleShadow = new Text(menuTitleText.x + 10, menuTitleText.y - 5, menuTitleText.size, menuTitleText.message, Color.BLACK);
		
		// "Touch"文字
		menuText = new Text(50, GV.scaleHeight - 70, 12, "Touch screen to start", Color.YELLOW);
		menuText.delayFrame = 20;
		
		menuShadow = new Text(menuText.x + 10, menuText.y - 5, menuText.size, menuText.message, Color.BLACK);
		
		// 載入背景
		menuBitmap = BitmapFactory.decodeResource(GV.res, Bee.main.R.drawable.menu);
		
		// 初始化背景物件
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
		
		// 取得目前的畫格時間
		frameTime = (int)game.totalFrames;
		
		// 閃礫"Touch"文字
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
		
		// 畫出背景
		subCanvas.drawBitmap(menuBitmap, menuObj.srcRect, menuObj.destRect, menuObj.paint);
		
		// 畫出標題陰影
		subCanvas.drawText(menuTitleShadow.message, menuTitleShadow.x, menuTitleShadow.y, menuTitleShadow.paint);
		
		// 畫出標題
		subCanvas.drawText(menuTitleText.message, menuTitleText.x, menuTitleText.y, menuTitleText.paint);
		
		// 閃礫
		if (menuText.isVisible)
		{
			// 畫出"Touch"陰影
			subCanvas.drawText(menuShadow.message, menuShadow.x, menuShadow.y, menuShadow.paint);
			
			// 畫出"Touch"文字
			subCanvas.drawText(menuText.message, menuText.x, menuText.y, menuText.paint);
		}
		
		super.Draw();
	}
}
