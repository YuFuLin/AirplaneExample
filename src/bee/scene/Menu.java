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
	
	// Init the frame
	private int frameTime;
	
	// Init the background
	private Bitmap menuBitmap;
	private Object2D menuObj;
	
	// Show the txet"Touch"
	private Text menuText;
	private Text menuShadow;
	
	// Mune Title
	private Text menuTitleText;
	private Text menuTitleShadow;
	
	public Menu(Game1 game) {
		this.game = game;
	}
	
	@Override
	protected void Initialize() {
		
		// Menu Title text
		menuTitleText = new Text(65, 50, 36, "小飛機", Color.YELLOW);
		
		menuTitleShadow = new Text(menuTitleText.x + 10, menuTitleText.y - 5, menuTitleText.size, menuTitleText.message, Color.BLACK);
		
		// Text "TOUCH"
		menuText = new Text(50, GV.scaleHeight - 70, 12, "Touch screen to start", Color.YELLOW);
		menuText.delayFrame = 20;
		
		menuShadow = new Text(menuText.x + 10, menuText.y - 5, menuText.size, menuText.message, Color.BLACK);
		
		// Load background
		menuBitmap = BitmapFactory.decodeResource(GV.res, Bee.main.R.drawable.menu);
		
		// Init the background
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
		
		// Get the frame time
		frameTime = (int)game.totalFrames;
		
		// The text "Touch" flashing
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
		
		// Draw the bitmap
		subCanvas.drawBitmap(menuBitmap, menuObj.srcRect, menuObj.destRect, menuObj.paint);
		
		// Draw title shadow
		subCanvas.drawText(menuTitleShadow.message, menuTitleShadow.x, menuTitleShadow.y, menuTitleShadow.paint);
		
		// Draw text
		subCanvas.drawText(menuTitleText.message, menuTitleText.x, menuTitleText.y, menuTitleText.paint);
		
		// Flash
		if (menuText.isVisible)
		{
			// Draw Shadow
			subCanvas.drawText(menuShadow.message, menuShadow.x, menuShadow.y, menuShadow.paint);
			
			// Draw text
			subCanvas.drawText(menuText.message, menuText.x, menuText.y, menuText.paint);
		}
		
		super.Draw();
	}
}
