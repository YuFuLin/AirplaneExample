/*copyright(c) 2010/11/18 by NQU CSIE 劉柏賢 
 * 
 * 
 */

package Bee.main;

import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class Surface extends SurfaceView implements SurfaceHolder.Callback
{
	public SurfaceHolder mHolder;
	private Bee bee;
	
	public Surface(Bee context) {
		super(context);
		
		bee = context;
		
		mHolder = this.getHolder();
		mHolder.addCallback(this);
	}
	
	public void surfaceCreated(SurfaceHolder arg0) {
		// 如果影片不是播放的狀態才執行遊戲執行緒
		if (!GV.videoPlayer.isRuningVideo)
		{
			// 以新的執行緒執行
			bee.game.Run();
			
			switch(GameStateClass.currentState)
			{
				case Menu:
					
					
					break;
				case Stage1:
					// 播放音樂
					GV.music.Play();
					
					break;
			}
		}
	}
	
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) { }
	
	public void surfaceDestroyed(SurfaceHolder arg0) { }
}