package pl.pawellukaszewski.testFX;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("loginView.fxml"));
        primaryStage.setTitle("ContactApp");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.setResizable(true);
        primaryStage.initStyle(StageStyle.DECORATED);
        primaryStage.setAlwaysOnTop(false);
        primaryStage.getIcons().add(new Image("http://www.pvhc.net/img139/vymaqvkutwobusehhxqd.png"));

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);

    }
}
