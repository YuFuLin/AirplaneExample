package Bee.main;

import Bee.main.R;
import Bee.main.GameStateClass.GameState;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.view.MotionEvent;
import android.widget.VideoView;

public class VideoPlayer
{
	private myVideo video;
	private int videoPosition = 0;
	private Bee bee;
	public boolean isRuningVideo;
	
	public VideoPlayer(Bee bee) {
		this.bee = bee;
		
		video = new myVideo(bee);
		
		Load(R.raw.f16);
		
		video.setOnCompletionListener(new OnCompletionListener() {
			
			public void onCompletion(MediaPlayer arg0) {
				Destroy();
			}
		});
	}
	
	public void Load(int movie)
	{
		Uri uri = Uri.parse("android.resource://" + bee.getPackageName() + "/" + movie);
		video.setVideoURI(uri);
	}
	
	public void Play()
	{
		isRuningVideo = true;
		
		bee.setContentView(video);
		video.requestFocus();
		
		video.start();
	}
	
	public void Resume() {
		video.seekTo(videoPosition);
		video.start();
	}
	
	public void Pause() {
		video.pause();
		videoPosition = video.getCurrentPosition();
	}
	
	public void Stop()
	{
		isRuningVideo = false;
		video.stopPlayback();
	}
	
	public void Destroy() {
		Stop();
		
		video.destroyDrawingCache();
		video.clearAnimation();
		video.clearFocus();
		
		// Switch the stage
		switch(GameStateClass.currentState)
		{
			case op:
				
				// Init the canvas view
				GameStateClass.currentState = GameState.Menu;
				GV.surface = new Surface(bee);
				
				break;
			case Menu:
				GameStateClass.changeState(GameState.Stage1, bee.game.menu, bee.game);
				
				break;
			case Stage1:
				
				break;
		}
		
		bee.setContentView(GV.surface);
	}
	
	class myVideo extends VideoView
	{
		public myVideo(Context context) {
			super(context);
		}
		
		//Set the video full screen
		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			setMeasuredDimension(GV.scaleWidth, GV.scaleHeight);
		}
		
		@Override
		public boolean onTouchEvent(MotionEvent ev) {
			
			return false;
		}
	}
}

