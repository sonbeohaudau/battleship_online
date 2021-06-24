package model.system;

import controller.GameplayController;
import model.player.Player;
import model.utils.SoundCollection;

public class GameConfig {
	private final static String gameTitle = "Battleship";

	private static GameMode gameMode;
	private static boolean isBGMEnabled;
	private static boolean isSEEnabled;
	private static boolean advancedMode = false;	// Toggle AdvancedMode in ShipFormationController
	
	private static Player player1;
	private static Player player2;
	private static int result;
	
	public final static int CELL_WIDTH = 50;
	public final static int CELL_HEIGHT = 50;
	public final static int NUM_OF_ROWS = 10;
	public final static int NUM_OF_COLS = 10;
	
	public static GameplayController currentGameMatch;
	
	public static String getGameTitle() {
		return gameTitle;
	}
	
	public static Player getPlayer1() {
		return player1;
	}
	
	public static Player getPlayer2() {
		return player2;
	}
	
	public static void loadDataPlayer1(Player player) {
		player1 = player;
	}
	
	public static void loadDataPlayer2(Player player) {
		player2 = player;
	}
	
	public static void setGameMode (GameMode gm) {
		gameMode = gm;
	}
	
	public static GameMode getGameMode () {
		return gameMode;
	}
	
	public static void saveGameResult(int result) {
		GameConfig.result = result;
	}
	
	public static int getGameResult() {
		return GameConfig.result;
	}

	public static boolean checkBGM() {
		return isBGMEnabled;
	}
	
	public static void setBGMOn(boolean checked) {
		isBGMEnabled = checked;
		if (!isBGMEnabled) {
			SoundCollection.INSTANCE.stopAllBackGroundMusic();
		}else {
			SoundCollection.INSTANCE.playStartMenuBackGroundIntro();
		}
	}

	public static void setBGM1On(boolean checked) {
		isBGMEnabled = checked;
		if (!isBGMEnabled) {
			SoundCollection.INSTANCE.stopAllBackGroundMusic();
		}else {
			SoundCollection.INSTANCE.playSetupBackGroundMusic();
		}
	}
	
	public static void setBGM2On(boolean checked) {
		isBGMEnabled = checked;
		if (!isBGMEnabled) {
			SoundCollection.INSTANCE.stopAllBackGroundMusic();
		}else {
			SoundCollection.INSTANCE.playGameplayBackGroundMusic();
		}
	}

	public static boolean checkSE() {
		return isSEEnabled;
	}

	public static void setSEOn(boolean checked) {
		isSEEnabled = checked;
	}

	public static boolean isAdvancedMode() {
		return advancedMode;
	}

	public static void setAdvancedMode(boolean checked) {
		advancedMode = checked;
	}
	
	public static void setGameMatch(GameplayController match) {
		currentGameMatch = match;
	}
	
	public static GameplayController getGameMatch() {
		return currentGameMatch;
	}
	
}
