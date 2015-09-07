package Bee.Role;

import android.graphics.Color;
import Bee.Object.Object2D;
import Bee.Object.Text;
import Bee.main.GV;
import Bee.main.GV.Sound;

public class RedMask extends Object2D{
	
	private int startFrame;
	private int delayFrame = 300;
	public Text warningText;
	
	public RedMask(int color,int alpha) {
		super(0, 0, GV.scaleWidth, GV.scaleHeight, color, alpha);
		
		warningText = new Text(50, GV.halfHeight, 36, "Warning", Color.BLACK);
	}
	
	public void Action(int frameTime)
	{
		switch(state)
		{
			case step1:
				startFrame = frameTime;
				state = State.step2;
				
				break;
			case step2:
				if (FadeIn())
					state = State.step3;
				
				break;
			case step3:
				if (FadeOut())
					state = State.step2;
				
				if (frameTime - startFrame > delayFrame)
				{
					isAlive = false;
				}
				
				break;
		}
	}
	
	public boolean FadeIn()
	{
		alpha+=5;
		if (alpha > 192)
		{
			alpha = 192;
			GV.playSound(Sound.alert);
			return true;
		}
			
		paint.setAlpha(alpha);
		return false;
	}
	
	public boolean FadeOut()
	{
		alpha-=5;
		if (alpha < 0)
		{
			alpha = 0;
			return true;
		}
		
		paint.setAlpha(alpha);
		return false;
	}
}
