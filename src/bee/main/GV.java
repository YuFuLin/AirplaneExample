/*copyright(c) 2010/11/18 by NQU CSIE 劉柏賢 
 * 
 * 
 */

package Bee.main;

import Bee.Object.Music;
import android.content.res.Resources;
import android.graphics.Rect;
import android.media.SoundPool;
import android.os.Vibrator;

// 所有類別共用變數
public class GV {
	public enum Sound{
		boom,
		alert,
	}
	
	// 影片播放
	public static VideoPlayer videoPlayer = null;

	// 播放音樂
	public static Music music = null;
	
	// 播放遊戲音效
	public static SoundPool snd = null;
	
	// 音效ID
	public static int soundIDBoom = 0;
	
	// 音效ID
	public static int soundIDAlert = 0;
	
	// 螢幕大小
	public static int scaleWidth = 0;
	public static int scaleHeight = 0;
	public static int halfWidth = 0;
	public static int halfHeight = 0;
	
	// 螢幕範圍
	public static Rect screenRect = null;
	
	// 震動
	public static Vibrator vibrator = null;
	
	// 資源
	public static Resources res = null;
	
	// 自機的位置
	public static int x , y; 
	
	// 畫布
	public static Surface surface = null;
	
	// 碰撞偵測
	public static boolean isCollision(Rect a, Rect b)
	{
		if (a.left < b.right)
			if (a.right > b.left)
				if (a.top < b.bottom)
					if (a.bottom > b.top)
						return true;
		
		return false;
	}
	
	// 是否在螢幕內
	public static boolean isInScreen(Rect obj)
	{
		return isCollision(obj, screenRect);
	}
	
	// 三角函數
	public static float[] Cosine = new float[360];
	public static float[] Sine = new float[360];
	
    // 求角度
    public static int getTheta(double XDistance, double YDistance)
    {
        // 不可除以0
        if (XDistance == 0) XDistance = 1;

        // 角度
        int theta = (int)(Math.atan(YDistance / XDistance) * 180 / Math.PI);
        
        // 象限轉換
        if (XDistance >= 0 && theta <= 0)
        	theta += 360;
        else if (XDistance <= 0)
        	theta += 180;

        return theta;
    }
    
    // 播放音效
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
