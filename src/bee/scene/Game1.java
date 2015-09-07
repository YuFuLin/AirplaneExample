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

		// �]�w�R�A�ܼƪ����
		F16.bigBoom = 1;
		F16.startBigBoomFrame = 0;

		super.Initialize();
	}

	@Override
	protected void LoadContent() {
		
		// �C������
		GV.snd = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		
		// �z��
		GV.soundIDBoom = GV.snd.load(bee, R.raw.explosion, 0);
		
		// ĵ�i
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

				// �s�W�Ĥ@��
				stage1 = new Stage1(this);
				Components.add(stage1);

				// ����I������
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

		// �M��
		canvas.drawColor(Color.BLACK);

		super.Draw();
	}
}