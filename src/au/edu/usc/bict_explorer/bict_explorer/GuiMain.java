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
    public void start(Stage stage) throws IOException {

        Parent root = FXMLLoader.load( getClass().getResource( "BICT_fxml.fxml" ) );
        Scene scene = new Scene( root );

        // for testing of various heights and widths
        scene.widthProperty().addListener( (observableValue, oldSceneWidth, newSceneWidth) -> System.out.println("Width: " + newSceneWidth) );
        scene.heightProperty().addListener( (observableValue, oldSceneHeight, newSceneHeight) -> System.out.println("Height: " + newSceneHeight) );

        scene.getStylesheets().add( getClass().getResource( "bictCss.css" ).toExternalForm() );
        stage.setMinWidth( 1550 );
        stage.setMinHeight( 800 );
        stage.sizeToScene();
        stage.setScene( scene );
        stage.setTitle( "BICT EXPLORER" );
        stage.show();
    }
}
