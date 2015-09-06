/*copyright(c) 2010/11/18 by NQU CSIE �B�f�� 
 * 
 * 
 */

package Bee.main;

import Bee.Object.Music;
import android.content.res.Resources;
import android.graphics.Rect;
import android.media.SoundPool;
import android.os.Vibrator;

// �Ҧ����O�@���ܼ�
public class GV {
	public enum Sound{
		boom,
		alert,
	}
	
	// �v������
	public static VideoPlayer videoPlayer = null;

	// ���񭵼�
	public static Music music = null;
	
	// ����C������
	public static SoundPool snd = null;
	
	// ����ID
	public static int soundIDBoom = 0;
	
	// ����ID
	public static int soundIDAlert = 0;
	
	// �ù��j�p
	public static int scaleWidth = 0;
	public static int scaleHeight = 0;
	public static int halfWidth = 0;
	public static int halfHeight = 0;
	
	// �ù��d��
	public static Rect screenRect = null;
	
	// �_��
	public static Vibrator vibrator = null;
	
	// �귽
	public static Resources res = null;
	
	// �۾�����m
	public static int x , y; 
	
	// �e��
	public static Surface surface = null;
	
	// �I������
	public static boolean isCollision(Rect a, Rect b)
	{
		if (a.left < b.right)
			if (a.right > b.left)
				if (a.top < b.bottom)
					if (a.bottom > b.top)
						return true;
		
		return false;
	}
	
	// �O�_�b�ù���
	public static boolean isInScreen(Rect obj)
	{
		return isCollision(obj, screenRect);
	}
	
	// �T�����
	public static float[] Cosine = new float[360];
	public static float[] Sine = new float[360];
	
    // �D����
    public static int getTheta(double XDistance, double YDistance)
    {
        // ���i���H0
        if (XDistance == 0) XDistance = 1;

        // ����
        int theta = (int)(Math.atan(YDistance / XDistance) * 180 / Math.PI);
        
        // �H���ഫ
        if (XDistance >= 0 && theta <= 0)
        	theta += 360;
        else if (XDistance <= 0)
        	theta += 180;

        return theta;
    }
    
    // ���񭵮�
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
