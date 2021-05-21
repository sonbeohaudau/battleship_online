package controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.imageio.ImageIO;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import javafx.scene.control.ButtonBar;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FXMLUtilsController {
	public static void loadSubStage(String fxml, String option, String titleStage) {
		try {
			// load fxml
			FXMLLoader fxmlLoader = new FXMLLoader(ClassLoader.getSystemResource("resources/fxml/" + fxml));
			Parent root = fxmlLoader.load();
			// initialize configuration for new stage
			Stage stage = new Stage();
			stage.setTitle(titleStage);
			stage.getIcons().add(FXMLUtilsController.getImageByName("Kancolle_kai_logo.jpg", "icon"));
			stage.setScene(new Scene(root));
			stage.initModality(Modality.APPLICATION_MODAL);
			if (option.compareTo("show") == 0) {
				stage.show();				
			} else {
				stage.showAndWait();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void loadPopUp(String alertType, String message) {
		Alert a = new Alert(AlertType.NONE);
		// set title
		a.setTitle("Alert from the Laputa");
		// set content text
		a.setContentText(message);
		// set alert type
		switch (alertType) {
		case "information":
			a.setAlertType(AlertType.INFORMATION);
			break;
		case "error":
			a.setAlertType(AlertType.ERROR);
			break;
		default:
			a.setContentText("Hmm, I wonder why you got this alert ><");
			a.close();
			break;
		}
		a.showAndWait();
	}
	
	public static void loadMultiplePopUp(String message, List<String> dataStream) {
		Alert a = new Alert(AlertType.INFORMATION);
		// set title
		a.setTitle("Alert from the Laputa");
		// set content text
		a.setContentText(message);
		ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
		ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
		a.getButtonTypes().setAll(okButton, noButton);
		Optional<ButtonType> resultClicked = a.showAndWait();
		if (resultClicked.get() == okButton) {
			String messageStream = "";
			for (String dataS : dataStream) {
				messageStream += dataS + "\n";
			}
			loadPopUp("information", messageStream);
		}
	}
	
	public static Image getImageByName(String imageName, String type) {
		String src = "resources/";
		if (type.equals("icon")) {
			src += "icon/";
		} else if (type.equals("image")){
			src += "imgs/";
		} else if (type.equals("ship")){
			src = "";
		}
		try {
			return new Image(ClassLoader.getSystemResource(src + imageName).toURI().toString());
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static BufferedImage getBufferedImageByName(String imageName) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(ClassLoader.getSystemResource(imageName));
		} catch (IOException e) {
			System.out.println("Can not load the demand image");
		}
		return image;
	}
	
	public static AudioClip getAudioByName(String audioName) {
		AudioClip clip = null;
        try {
            clip = new AudioClip(ClassLoader.getSystemResource("resources/audio/" + audioName).toURI().toString());
            return clip;
        } catch (URISyntaxException ignored) {
        	return null;
        }
	}
}
