package au.edu.usc.bict_explorer.resources;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
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
        ToggleButton visibilityControl = new ToggleButton("Winterfell");
        VBox layout = new VBox(10);
        layout.setAlignment( Pos.CENTER);
        layout.getChildren().setAll(visibilityControl);

        primaryStage.setResizable(false);
        Scene scene = new Scene( root, 900, 600 );

        scene.getStylesheets().add( getClass().getResource( "bictCss.css" ).toExternalForm() );
        primaryStage.setScene( scene );
        primaryStage.setTitle( "BICT Degree Explorer" );
        primaryStage.show();
    }
}
