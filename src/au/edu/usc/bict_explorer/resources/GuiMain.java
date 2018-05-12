package au.edu.usc.bict_explorer.resources;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
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
    public void start(Stage primaryStage) throws IOException {

        BorderPane root = FXMLLoader.load( getClass().getResource( "bitFXML.fxml" ) );

        primaryStage.setResizable(false);
        Scene scene = new Scene( root, 900, 600 );
        scene.getStylesheets().add( getClass().getResource( "bictCss.css" ).toExternalForm() );
        primaryStage.setScene( scene );
        primaryStage.setTitle( "BICT Degree Explorer" );
        primaryStage.show();
    }
}
