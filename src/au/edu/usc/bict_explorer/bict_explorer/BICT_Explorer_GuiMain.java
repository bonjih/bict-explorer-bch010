
package au.edu.usc.bict_explorer.bict_explorer;

import au.edu.usc.bict_explorer.rules.Degree;
import au.edu.usc.bict_explorer.rules.Option;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * @author Ben Hamilton (aka bonjih)
 *
 * /**
 * Main run class for the bict-explorer GUI
 */

public class BICT_Explorer_GuiMain extends Application {
    static Degree myDegree;
    //  static Option careerChoice ;//= null;
    static Option extraSelectedMinor;
    static Set<Option> downStreamCourse;
    static Option selectedCareer;

    public static void main(String[] args) throws FileNotFoundException, NoSuchElementException {
        Application.launch( args );

    }

    /**
     * @param stage
     * @throws java.io.FileNotFoundException
     */

    @Override
    public void start(Stage stage) throws IOException {

        Parent root = FXMLLoader.load( getClass().getResource( "BICT_fxml.fxml" ) );
        Pane pane = new Pane();
        pane.getChildren().add( root );


        Scene scene = new Scene( pane, 1370, 705 );

        // for testing of various heights and widths
        scene.widthProperty().addListener( (observableValue, oldSceneWidth, newSceneWidth) -> System.out.println( "Width: " + newSceneWidth ) );
        scene.heightProperty().addListener( (observableValue, oldSceneHeight, newSceneHeight) -> System.out.println( "Height: " + newSceneHeight ) );


        scene.getStylesheets().add( getClass().getResource( "bictCss.css" ).toExternalForm() );
        stage.setMinWidth( 1405 );
        stage.setMinHeight( 740 );
//        stage.sizeToScene();
        stage.setScene( scene );
        stage.setTitle( "BICT EXPLORER" );
        stage.show();
    }
}
