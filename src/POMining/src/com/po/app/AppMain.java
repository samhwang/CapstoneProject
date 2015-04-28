package com.po.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class AppMain extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {
			// Stuart Barker 14/4/2015
			Stage theStage = primaryStage;

			Parent root = FXMLLoader.load(getClass().getResource(
					"Controller.fxml"));
			// Stuart Barker 14/4/2015
			// Parent root2 =
			// FXMLLoader.load(getClass().getResource("Controller2.fxml"));

			Scene scene = new Scene(root);
			// scene.getStylesheets().add(getClass().getResource("Controller.css").toExternalForm());
			// Stuart Barker 14/4/2015
			// Scene scene2 = new Scene(root2);

			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Application.launch(AppMain.class, args);
	}
}
