package com.hcd.jbox2d.game.utils;

import com.hcd.jbox2d.game.activity.R;

import android.app.Activity;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundFactory {

	private static SoundFactory soundFactory;
	private static SoundPool sp;
	private static int sounds[];
	
	public SoundFactory(Activity ma) {
		sounds = new int[5];
		sp = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
		sounds[0] = sp.load(ma, R.raw.cut, 1);
		sounds[1] = sp.load(ma, R.raw.success, 1);
	}
	
	public void palySound(int i) {
		sp.play(sounds[i], 1, 1, 0, 0, 1);
	}
	
	public static synchronized SoundFactory getInstance(Activity ma) {
		if (soundFactory != null) {
			return soundFactory;
		} else {
			soundFactory = new SoundFactory(ma);
			return soundFactory;
		}
	}
}
