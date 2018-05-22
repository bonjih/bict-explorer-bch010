package au.edu.usc.bict_explorer.bict_explorer;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;


/**
 * @author Ben Hamilton (aka bonjih)
 */

/**
 * Main run class for the bict-explorer GUI
 */

public class GuiMain extends Application {

    public static void main(String[] args) {
        launch( args );
    }


    @Override
    public void start(Stage stage) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("BICT_fxml.fxml"));




        Scene scene =new Scene(root);
        scene.getStylesheets().add( getClass().getResource( "bictCss.css" ).toExternalForm() );
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle("BICT EXPLORER");
        stage.show();
    }
}
