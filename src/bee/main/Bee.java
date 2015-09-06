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
	
	// ��l��
	private void Initialize() {
		GV.res = getResources();

		// ���o�_�ʨt�ΪA��
		GV.vibrator = (Vibrator) getApplication().getSystemService(
				Service.VIBRATOR_SERVICE);

		// �L���D�C
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// ���ù��]�w
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		// �ù��T�w��������V
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		// ���o�ù��j�p
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		GV.scaleWidth = dm.widthPixels;
		GV.scaleHeight = dm.heightPixels;
		GV.halfWidth = GV.scaleWidth >> 1;
		GV.halfHeight = GV.scaleHeight >> 1;
		
		// ���o�ù���G�A��
		powerManager = (PowerManager)getSystemService(Context.POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "BackLight");
		
		// ���o���O�[�t�׷P�����A��
		sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	}
	
	// ���J�귽
	public void LoadContent() {
		
		// �C�����A��l��
		GameStateClass.currentState = GameState.op;
		GameStateClass.oldState = GameState.None;
		
		// �I������
		GV.music = new Music(this, R.raw.stage1, 3);
		
		// ��l�ƹC��
		game = new Game1(this);
		
		// ��l�Ƽv������
		GV.videoPlayer = new VideoPlayer(this);
		
		// ���J�}�Y�ʵe
		GV.videoPlayer.Load(R.raw.op);
		
		// ����}�Y�ʵe
		GV.videoPlayer.Play();
	}
	
	// ����귽
	public void UnloadContent()
	{
		GV.snd.release();
		GV.snd = null;
		
		finish();
	}
	
	// �^�_�{��
	@Override
	protected void onResume() {
		wakeLock.acquire();
		
		// �[�t�׭p���U
        sensorMgr.registerListener(lsn, sensor, SensorManager.SENSOR_DELAY_GAME);
		
        // �~�򼽩�v��
		if (GV.videoPlayer.isRuningVideo)
			GV.videoPlayer.Resume();
        
		super.onResume();
		
		// �|����surfaceCreated
	}
	
	// �Ȱ��{��
	@Override
	protected void onPause() {
		GV.vibrator.cancel();
		wakeLock.release();
		
		// �[�t�׭p�ѵ��U
        sensorMgr.unregisterListener(lsn);
        
        // �Ȱ��v������
		if (GV.videoPlayer.isRuningVideo)
			GV.videoPlayer.Pause();
		
		if (game != null)
		{
			// �Ȱ����ּ���
			if (GameStateClass.currentState != GameState.Menu)
			{
				if (GV.music != null)
					GV.music.Pause();
			}
		
			// ����C���j��
			
			game.Exit();
		}
		
		super.onPause();
	}

	// Ĳ���ƥ�
	public boolean onTouchEvent(MotionEvent event) 
	{
		// ���U
		if (event.getAction() == MotionEvent.ACTION_DOWN)
		{
			switch(GameStateClass.currentState)
			{
				case op:
					GV.videoPlayer.Destroy();
					
					break;
				case Menu:
					// �p�G���b����v���h�����
					if (!GV.videoPlayer.isRuningVideo)
					{
						// ����C�������
						game.Exit();
						
						// ���J�ü���F16�_���ʵe
						GV.videoPlayer.Load(R.raw.f16);
						GV.videoPlayer.Play();
					}else
					{
						GV.videoPlayer.Destroy();
					}
					
					break;
				case Stage1:
					
					// �j����
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
	
	// ��L�ƥ�
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
	
	// ���O�[�t�׷P����
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