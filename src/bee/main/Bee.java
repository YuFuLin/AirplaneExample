//Modified and updated by YuFuLin 2014/07/10

package Bee.main;
import Bee.Object.Music;
import Bee.Role.F16;
import Bee.main.GameStateClass.GameState;
import Bee.scene.Game1;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.os.PowerManager.WakeLock;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

public class Bee extends Activity {
	private PowerManager powerManager;
	private WakeLock wakeLock;
	
	// Gravity Sensor
	private SensorManager sensorMgr;  
    private Sensor sensor;
	
	// Game menu
	public Game1 game;
	
	// Login point
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Init
		Initialize();
		
		// Loading all Content
		LoadContent();
	}
	
	// Init
	private void Initialize() {
		GV.res = getResources();

		// Get the service of vibrator
		GV.vibrator = (Vibrator) getApplication().getSystemService(
				Service.VIBRATOR_SERVICE);

		// Setting no title
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// Full Screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		// Fixed the screen
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		// Get the size of the screen
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		GV.scaleWidth = dm.widthPixels;
		GV.scaleHeight = dm.heightPixels;
		GV.halfWidth = GV.scaleWidth >> 1;
		GV.halfHeight = GV.scaleHeight >> 1;
		
		// Always wake the backlight
		powerManager = (PowerManager)getSystemService(Context.POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "BackLight");
		
		// Get the sensor of gravity
		sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	}
	
	// Loading
	public void LoadContent() {
		
		// Init the game state
		GameStateClass.currentState = GameState.op;
		GameStateClass.oldState = GameState.None;
		
		// BGM
		GV.music = new Music(this, R.raw.stage1, 3);
		
		// Init the game
		game = new Game1(this);
		
		// Init the video player
		GV.videoPlayer = new VideoPlayer(this);
		
		// Load the movie
		GV.videoPlayer.Load(R.raw.op);
		
		// Play the movie
		GV.videoPlayer.Play();
	}
	
	// Release the resource of movie
	public void UnloadContent()
	{
		GV.snd.release();
		GV.snd = null;
		
		finish();
	}
	
	// Resume the program
	@Override
	protected void onResume() {
		wakeLock.acquire();
		
		// Register the sensor
        sensorMgr.registerListener(lsn, sensor, SensorManager.SENSOR_DELAY_GAME);
		
        // Resume the video
		if (GV.videoPlayer.isRuningVideo)
			GV.videoPlayer.Resume();
        
		super.onResume();
		
		// Switch to the suface creator
	}
	
	// Pause the game
	@Override
	protected void onPause() {
		GV.vibrator.cancel();
		wakeLock.release();
		
		// Unregister the sensor
        sensorMgr.unregisterListener(lsn);
        
        // Pause the video
		if (GV.videoPlayer.isRuningVideo)
			GV.videoPlayer.Pause();
		
		if (game != null)
		{
			// Pause the music
			if (GameStateClass.currentState != GameState.Menu)
			{
				if (GV.music != null)
					GV.music.Pause();
			}
		
			// Stop the game
			
			game.Exit();
		}
		
		super.onPause();
	}

	// Touch panel event
	public boolean onTouchEvent(MotionEvent event) 
	{
		// Press
		if (event.getAction() == MotionEvent.ACTION_DOWN)
		{
			switch(GameStateClass.currentState)
			{
				case op:
					GV.videoPlayer.Destroy();
					
					break;
				case Menu:
					// Stop the video player if video is playing
					if (!GV.videoPlayer.isRuningVideo)
					{
						// Stop the game thread
						game.Exit();
						
						// Play the flying video
						GV.videoPlayer.Load(R.raw.f16);
						GV.videoPlayer.Play();
					}else
					{
						GV.videoPlayer.Destroy();
					}
					
					break;
				case Stage1:
					
					// Bomb skill
					if (F16.bigBoom > 0)
					{
						if ((int)game.totalFrames - F16.startBigBoomFrame > F16.bigBoomDelayFrame)
						{
							F16.startBigBoomFrame = (int)game.totalFrames;
							
							F16.isTouchBoom = true;
							F16.bigBoom--;
						}
					}
					
					break;
			}
		}
		
		return super.onTouchEvent(event);
	}
	
	// Key event
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		switch(keyCode)
		{
			case KeyEvent.KEYCODE_DPAD_LEFT:
				GV.x -=10;
				break;
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				GV.x +=10;
				break;
			case KeyEvent.KEYCODE_DPAD_UP:
				GV.y -=10;
				break;
			case KeyEvent.KEYCODE_DPAD_DOWN:
				GV.y +=10;
				break;
		}

		return super.onKeyDown(keyCode, event);
	}
	
	// The sensor of gravity
	private SensorEventListener lsn = new SensorEventListener() {  
        public void onSensorChanged(SensorEvent e) {
        	
			GV.x += (int) -e.values[SensorManager.DATA_X] << 2;
			GV.y += (int) e.values[SensorManager.DATA_Y] << 2;
        	
			if (GV.y > GV.scaleHeight)
        	{
        		GV.y = GV.scaleHeight;
        	}
        		
//            GV.z = (int)e.values[SensorManager.DATA_Z];    
        }  
          
        public void onAccuracyChanged(Sensor s, int accuracy) {  
        }  
    };  
}
