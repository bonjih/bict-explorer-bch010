package au.edu.usc.bict_explorer.other;

import au.edu.usc.bict_explorer.rules.Degree;
import au.edu.usc.bict_explorer.rules.Option;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ResourceBundle;

public class Controller_TEST implements Initializable {

    final FileChooser fileChooser = new FileChooser();

    File fileCareers = new File( "src/au/edu/usc/bict_explorer/resources/careers.options" );
    File fileMinors = new File( "src/au/edu/usc/bict_explorer/resources/minors.options" );
    File fileCourses = new File( "src/au/edu/usc/bict_explorer/resources/courses.options" );

    Degree myDegree = new Degree( fileCareers, fileMinors, fileCourses );

    @FXML
    private VBox vBoxCareer;

    @FXML
    private VBox vBoxMinors;

    @FXML
    private HBox hBoxCompCourses;

    @FXML
    private MenuItem fileOpen;

    @FXML
    private MenuItem guiClose;

    public Controller_TEST() throws IOException, ParseException {

    }

    @FXML
    void setOnfileOpenRequest(ActionEvent actionEvent) throws IOException, ParseException {

        fileChooser.setTitle( "Open file" );
        fileChooser.showOpenDialog( fileOpen.getParentPopup().getScene().getWindow() );
    }

    @FXML
    void setOnfileSaveRequest(ActionEvent actionEvent) throws IOException, ParseException {

    }

    @FXML
    void setOnCloseRequest() throws IOException, ParseException {
        guiClose.setOnAction( t -> Platform.exit() );

    }

    @FXML
    void onSelectAddMinorSDGP(ActionEvent actionEvent) throws IOException, ParseException {

        myDegree.careers();
        fileCareers.getName();
//        System.out.println(myDegree.careers().values());

//        ToggleButton sourceButton = (ToggleButton) actionEvent.getSource();

//            if (!sourceButton.isSelected() ) {
//                sourceButton.setSelected(true);
//
//
//                }
//
    }


    @FXML
    private void onSelectAddMinorDMSD(javafx.event.ActionEvent event) throws Exception {

    }

    @FXML
    private void onSelectAddMinorTCNDM(javafx.event.ActionEvent event) throws Exception {

    }

    @FXML
    private void onSelectAddMinorISDM(javafx.event.ActionEvent event) throws Exception {

    }

    public class ButtonEventHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
//            ToggleButton sourceButton = (ToggleButton) event.getSource();

//            if (!sourceButton.isSelected()) {
//                sourceButton.setSelected( true );
//
//            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        for (String key : myDegree.careers().keySet()) {
            Option career = myDegree.careers().get( key );
            Button btCareer = new Button( career.getName() );
            vBoxCareer.getChildren().add( btCareer );
            vBoxCareer.setAlignment( Pos.TOP_CENTER );
            vBoxCareer.setSpacing( 25 );

//            btCareer.setOnAction( new ButtonEventHandler() );
//            btCareer.setOnMouseClicked(e ->
//            System.out.println( myDegree.careers().values() );
        }

        for (String key : myDegree.minors().keySet()) {
            Option minors = myDegree.minors().get( key );
            Button btMinors = new Button( minors.getName() );

            vBoxMinors.getChildren().add( btMinors );
            vBoxMinors.setAlignment( Pos.TOP_CENTER );
            vBoxMinors.setSpacing( 25 );
        }

        for (String key : myDegree.courses().keySet()) {
            Option courses = myDegree.courses().get( key );
            Button btCourses = new Button( courses.getCode() );

            hBoxCompCourses.setAlignment( Pos.CENTER_RIGHT );

            btCourses.setMaxWidth( Double.MAX_VALUE );
            HBox.setHgrow( btCourses, Priority.ALWAYS );
            hBoxCompCourses.setSpacing( 5 );

            hBoxCompCourses.getChildren().add( btCourses );

        }
    }
}
