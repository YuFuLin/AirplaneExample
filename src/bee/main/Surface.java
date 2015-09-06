/*copyright(c) 2010/11/18 by NQU CSIE �B�f�� 
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
		// �p�G�v�����O���񪺪��A�~����C�������
		if (!GV.videoPlayer.isRuningVideo)
		{
			// �H�s�����������
			bee.game.Run();
			
			switch(GameStateClass.currentState)
			{
				case Menu:
					
					
					break;
				case Stage1:
					// ���񭵼�
					GV.music.Play();
					
					break;
			}
		}
	}
	
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) { }
	
	public void surfaceDestroyed(SurfaceHolder arg0) { }
}