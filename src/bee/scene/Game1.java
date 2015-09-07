package Bee.scene;

import Bee.Object.Music;
import Bee.Role.F16;
import Bee.main.Bee;
import Bee.main.GV;
import Bee.main.GameStateClass;
import Bee.main.R;
import XNA.lbs.Game;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;

public class Game1 extends Game {
	public Menu menu;
	public Stage1 stage1;
	public Bee bee;

	public Game1(Bee bee) {
		this.bee = bee;
	}

	@Override
	protected void Initialize() {

		// Set the init value
		F16.bigBoom = 1;
		F16.startBigBoomFrame = 0;

		super.Initialize();
	}

	@Override
	protected void LoadContent() {
		
		// Sound effect
		GV.snd = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		
		// Explode
		GV.soundIDBoom = GV.snd.load(bee, R.raw.explosion, 0);
		
		// Warning
		GV.soundIDAlert = GV.snd.load(bee, R.raw.sysalert, 0);
		
		super.LoadContent();
	}

	@Override
	protected void UnloadContent() {

		super.UnloadContent();
	}

	@Override
	protected void Update() {

		if (GameStateClass.currentState != GameStateClass.oldState) {
			switch (GameStateClass.currentState) {
			case None:
				bee.UnloadContent();

				break;
			case Menu:
				menu = new Menu(this);
				Components.add(menu);

				break;
			case Stage1:

				// Add first stage
				stage1 = new Stage1(this);
				Components.add(stage1);

				// Play BGM
				if (!GV.music.player.isPlaying()) {
					GV.music.player.release();
					GV.music = new Music(bee, R.raw.stage1,2);
					GV.music.Play();
				}

				break;
			}

			GameStateClass.oldState = GameStateClass.currentState;
		}

		super.Update();
	}

	@Override
	protected void Draw() {

		canvas = GV.surface.mHolder.lockCanvas(null);

		// Clean canvas
		canvas.drawColor(Color.BLACK);

		super.Draw();
	}
}
