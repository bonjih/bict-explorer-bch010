package au.edu.usc.bict_explorer.resources;


import au.edu.usc.bict_explorer.rules.Degree;
import au.edu.usc.bict_explorer.rules.Option;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;

/**
 * @author Ben Hamilton (aka bonjih)
 */

/**
 * Main run class for the bict-explorer GUI
 */

public class GuiMain extends Application {


    static Degree myDegree;


    public GuiMain() {

   }

    public static void main(String[] args) {
        launch( args );
    }



    @Override
    public void start(Stage primaryStage) throws IOException, ParseException {

        BorderPane root = FXMLLoader.load( getClass().getResource( "bitFXML.fxml" ) );

        File fileCareers = new File( "src/au/edu/usc/bict_explorer/resources/careers.options" );
        File fileMinors = new File( "src/au/edu/usc/bict_explorer/resources/minors.options" );
        File fileCourses = new File( "src/au/edu/usc/bict_explorer/resources/courses.options" );

        myDegree = new Degree( fileCareers, fileMinors, fileCourses );



//        primaryStage.setResizable(false);
        Scene scene = new Scene( root, 965, 600 );

        scene.getStylesheets().add( getClass().getResource( "bictCss.css" ).toExternalForm() );
        primaryStage.setScene( scene );
        primaryStage.setTitle( "BICT Degree Explorer" );
        primaryStage.show();
    }
}
