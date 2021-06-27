package application;

import controller.FXMLUtilsController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import model.system.GameConfig;
import model.utils.ImageCollection;
import model.utils.SoundCollection;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		preLoader();

		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../resources/fxml/StartMenu.fxml"));
			Parent root = fxmlLoader.load();
			// Scene scene = new Scene(root, STAGE_WIDTH, STAGE_HEIGHT);
			Scene scene = new Scene(root);
			primaryStage.setTitle(GameConfig.getGameTitle());
			primaryStage.getIcons().add(FXMLUtilsController.getImageByName("Kancolle_kai_logo.jpg", "icon"));
			primaryStage.setScene(scene);
			primaryStage.show();
			System.gc();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	public static void preLoader() {
		SoundCollection.INSTANCE.initSoundCollection();
		ImageCollection.INSTANCE.initImageCollection();
	}
}
