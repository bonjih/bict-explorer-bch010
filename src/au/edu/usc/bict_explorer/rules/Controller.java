package au.edu.usc.bict_explorer.rules;

import au.edu.usc.bict_explorer.rules.Degree;
import au.edu.usc.bict_explorer.rules.Option;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    File fileCareers = new File( "src/au/edu/usc/bict_explorer/resources/careers.options" );
    File fileMinors = new File( "src/au/edu/usc/bict_explorer/resources/minors.options" );
    File fileCourses = new File( "src/au/edu/usc/bict_explorer/resources/courses.options" );

    @FXML
    private BorderPane pane;

    @FXML
    private Button labelAP;

//    @FXML
//    void onSelectAddMinorSDGP(ActionEvent event) {
//        labelSD.

    @FXML
    private Button lableDA;

    @FXML
    private Button labelNA;

    @FXML
    private Button labelBA;

    @FXML
    private Button labelSD;

    @FXML
    private Button labelGP;

    @FXML
    private Button labelTN;

    @FXML
    private Button labelDM;

    @FXML
    private Button labelIS;


    private Degree degree = new Degree( fileCareers, fileMinors, fileCourses );

    public Controller() throws IOException, ParseException {

    }

    @FXML
    private void onSelectAddMinorSDGP(javafx.event.ActionEvent event) throws Exception {

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


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        assert pane != null;

    }
}
