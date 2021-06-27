package model.utils;

import controller.FXMLUtilsController;
import javafx.scene.media.AudioClip;
import model.system.GameConfig;

public enum SoundCollection {
	INSTANCE;
	AudioClip STARTMENUBACKGROUND;
	AudioClip SETUPBACKGROUNDSOUND;
	AudioClip GAMEPLAYBACKGROUND;
	AudioClip VICTORYSOUND;
	AudioClip DEFEATSOUND;
	AudioClip DRAWSOUND;
	AudioClip HITSFX;
	AudioClip MISSSFX;
	AudioClip BUTTONCLICKING;
	AudioClip SHIPCORRECT;
	AudioClip SHIPWRONG;
	AudioClip SHIPEXPLOSION;
	AudioClip CONFIRMSFX;
	
	AudioClip destroyerSink;
	AudioClip cruiserSink;
	AudioClip battleshipSink;
	AudioClip carrierSink;
	
	AudioClip LinkStart;
	
	public void initSoundCollection() {
		STARTMENUBACKGROUND = FXMLUtilsController.getAudioByName("Intense3.wav");
		SETUPBACKGROUNDSOUND = FXMLUtilsController.getAudioByName("Intense4.wav");
		GAMEPLAYBACKGROUND = FXMLUtilsController.getAudioByName("Thrilling1.wav");
		CONFIRMSFX = FXMLUtilsController.getAudioByName("Confirm_SEF.wav");
		HITSFX = FXMLUtilsController.getAudioByName("hit.wav");
		MISSSFX = FXMLUtilsController.getAudioByName("water_miss.wav");
		BUTTONCLICKING = FXMLUtilsController.getAudioByName("Button_Click.wav");
		SHIPCORRECT = FXMLUtilsController.getAudioByName("Place_ship_correct.wav");
		SHIPWRONG = FXMLUtilsController.getAudioByName("Place_ship_wrong.wav");
		SHIPEXPLOSION = FXMLUtilsController.getAudioByName("Ship_explosion.wav");
		VICTORYSOUND = FXMLUtilsController.getAudioByName("victoryAnnounce.mp3");
		DEFEATSOUND = FXMLUtilsController.getAudioByName("defeatedAnnounce.wav");
		DRAWSOUND = FXMLUtilsController.getAudioByName("IndianHarryPortter.wav");
		
		destroyerSink = FXMLUtilsController.getAudioByName("soldierCry.wav");
		cruiserSink = FXMLUtilsController.getAudioByName("soldierCry.wav");
		battleshipSink = FXMLUtilsController.getAudioByName("soldierCry.wav");
		carrierSink = FXMLUtilsController.getAudioByName("soldierCry.wav");
		
		LinkStart = FXMLUtilsController.getAudioByName("battleStart.m4a");
	}

	public void playStartMenuBackGroundIntro() {
		if (GameConfig.checkBGM() == false) {
			// stop then start
			STARTMENUBACKGROUND.stop();
		}else {
			// run the background indefinite
			STARTMENUBACKGROUND.setCycleCount(AudioClip.INDEFINITE);
			STARTMENUBACKGROUND.play();
		}
	}
	
	public void stopStartMenuBackGroundIntro() {
		STARTMENUBACKGROUND.stop();
	}
	
	public void playSetupFormationBackGroundSound () {
		if (GameConfig.checkBGM() == false) {
			SETUPBACKGROUNDSOUND.stop();
		}else {
			SETUPBACKGROUNDSOUND.setCycleCount(AudioClip.INDEFINITE);
			SETUPBACKGROUNDSOUND.play();
		}
	}
	
	public void stopSetupFormationBackGroundSound() {
		SETUPBACKGROUNDSOUND.stop();
	}
	
	public void playGamePlayBackGroundSound () {
		if (GameConfig.checkBGM() == false) {
			GAMEPLAYBACKGROUND.stop();
		}else {
			GAMEPLAYBACKGROUND.setCycleCount(AudioClip.INDEFINITE);
			GAMEPLAYBACKGROUND.play();
		}
	}
	
	public void stopGamePlayBackGroundSound() {
		GAMEPLAYBACKGROUND.stop();
	}
	
	public void playVictorySound () {
		if (GameConfig.checkBGM() == false) {
			VICTORYSOUND.stop();
		}else {
			VICTORYSOUND.setCycleCount(AudioClip.INDEFINITE);
			VICTORYSOUND.play();
		}
	}

	public void playDefeatSound () {
		if (GameConfig.checkBGM() == false) {
			DEFEATSOUND.stop();
		}else {
			DEFEATSOUND.setCycleCount(AudioClip.INDEFINITE);
			DEFEATSOUND.play();
		}
	}
	
	public void playDrawSound () {
		if (GameConfig.checkBGM() == false) {
			DRAWSOUND.stop();
		}else {
			DRAWSOUND.setCycleCount(AudioClip.INDEFINITE);
			DRAWSOUND.play();
		}
	}
	
	public void playButtonClickSound () {
		if (GameConfig.checkSE() == false)
			return;
		BUTTONCLICKING.play();
	}
	
	public void playConfirmSound () {
		if (GameConfig.checkSE() == false)
			return;
		CONFIRMSFX.play();
	}
	
	public void playShipCorrect () {
		if (GameConfig.checkSE() == false)
			return;
		SHIPCORRECT.play();
	}
	
	public void playShipWrong () {
		if (GameConfig.checkSE() == false)
			return;
		SHIPWRONG.play();
	}
	
	public void playHitSFX() {
		if (GameConfig.checkSE() == false)
			return;
		HITSFX.play();
	}

	public void playMissSFX() {
		if (GameConfig.checkSE() == false)
			return;
		MISSSFX.play();
	}
	
	public void playLinkStartSFX() {
		if (GameConfig.checkSE() == false)
			return;
		LinkStart.play();
	}

	public void playShipExploSFX() {
		if (GameConfig.checkSE() == false)
			return;
		SHIPEXPLOSION.play();
	}
	
	public void playShipSinkSFX(int ShipTypeID) {
		if (GameConfig.checkSE() == false) 
			return;
		if (ShipTypeID == 3) {
			destroyerSink.play();
		}else if (ShipTypeID == 2) {
			cruiserSink.play();
		}else if (ShipTypeID == 1) {
			battleshipSink.play();
		}else if (ShipTypeID == 0) {
			carrierSink.play();
		}
	}
	// add background music to this function to play
	public void playSetupBackGroundMusic() {
		// run the background indefinite
		SETUPBACKGROUNDSOUND.setCycleCount(AudioClip.INDEFINITE);
		SETUPBACKGROUNDSOUND.play();
	}
	
	public void playGameplayBackGroundMusic() {
		// run the background indefinite
		GAMEPLAYBACKGROUND.setCycleCount(AudioClip.INDEFINITE);
		GAMEPLAYBACKGROUND.play();
	}

	// add all background music to this function to stop
	public void stopAllBackGroundMusic() {
		STARTMENUBACKGROUND.stop();
		SETUPBACKGROUNDSOUND.stop();
		GAMEPLAYBACKGROUND.stop();
		VICTORYSOUND.stop();
		DEFEATSOUND.stop();
		DRAWSOUND.stop();
		
		destroyerSink.stop();
		cruiserSink.stop();
		battleshipSink.stop();
		carrierSink.stop();
	}
	
	public void stopGameOverMusic() {
		VICTORYSOUND.stop();
		DEFEATSOUND.stop();
		DRAWSOUND.stop();
	}
}
