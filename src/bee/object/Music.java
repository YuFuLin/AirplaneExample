package Bee.Object;

import Bee.main.R;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

public class Music{
	public MediaPlayer player;
	public boolean isComplete;
	
	public Music(Context context,int music,int volume) {
		player = MediaPlayer.create(context, music);
		
		// 音量大小(0~10)
		player.setVolume(volume, volume);
		
		isComplete = false;
		
		player.setOnCompletionListener(new OnCompletionListener() 
		{			
			public void onCompletion(MediaPlayer arg0) 
			{
				isComplete = true;
			}
		});
	}
	
	public void Play()
	{
		if (!isComplete)
			if (!player.isPlaying())
				player.start();
	}
	
	public void Stop()
	{
		player.stop();
		isComplete = false;
	}
	
	public void Pause()
	{
		player.pause();
	}
}
