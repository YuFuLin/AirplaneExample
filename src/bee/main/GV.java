

package Bee.main;

import Bee.Object.Music;
import android.content.res.Resources;
import android.graphics.Rect;
import android.media.SoundPool;
import android.os.Vibrator;

// Public varaible
public class GV {
	public enum Sound{
		boom,
		alert,
	}
	
	// Play the video
	public static VideoPlayer videoPlayer = null;

	// Play the music
	public static Music music = null;
	
	// Play the sound effect
	public static SoundPool snd = null;
	
	// Sound ID
	public static int soundIDBoom = 0;
	
	// Sound ID
	public static int soundIDAlert = 0;
	
	// Screen size
	public static int scaleWidth = 0;
	public static int scaleHeight = 0;
	public static int halfWidth = 0;
	public static int halfHeight = 0;
	
	// Screen Range
	public static Rect screenRect = null;
	
	// Vibrator
	public static Vibrator vibrator = null;
	
	// Sound Resource
	public static Resources res = null;
	
	// Self position
	public static int x , y; 
	
	// Add surface
	public static Surface surface = null;
	
	// Collision detection
	public static boolean isCollision(Rect a, Rect b)
	{
		if (a.left < b.right)
			if (a.right > b.left)
				if (a.top < b.bottom)
					if (a.bottom > b.top)
						return true;
		
		return false;
	}
	

	public static boolean isInScreen(Rect obj)
	{
		return isCollision(obj, screenRect);
	}
	
	public static float[] Cosine = new float[360];
	public static float[] Sine = new float[360];
	
    // get Angle
    public static int getTheta(double XDistance, double YDistance)
    {
        // Prevent 0
        if (XDistance == 0) XDistance = 1;

        // Angle
        int theta = (int)(Math.atan(YDistance / XDistance) * 180 / Math.PI);
        
        // Change the X axis and Y axis
        if (XDistance >= 0 && theta <= 0)
        	theta += 360;
        else if (XDistance <= 0)
        	theta += 180;

        return theta;
    }
    
    // Play sound
    public static void playSound(Sound sound)
    {
    	switch(sound)
    	{
    	case boom:
    		snd.play(soundIDBoom, 1, 1, 0, 0, 1);
    		break;
    	case alert:
    		snd.play(soundIDAlert, 1, 1, 0, 0, 1);
    		break;
    	}
    	
    }
}
