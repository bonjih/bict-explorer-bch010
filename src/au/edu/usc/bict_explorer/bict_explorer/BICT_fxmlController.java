package au.edu.usc.bict_explorer.bict_explorer;

import au.edu.usc.bict_explorer.rules.Course;
import au.edu.usc.bict_explorer.rules.Degree;
import au.edu.usc.bict_explorer.rules.Option;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.text.ParseException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * BICT (GUI) Controller class
 *
 * @author Ben Hamilton
 */

public class BICT_fxmlController implements Initializable {
    Degree myDegree;
    Option selectedCareer;
    Option selectedCourses;
    File fileCareers;
    File fileMinors;
    File fileCourses;
    Map<String, Option> courses;
    Map<String, Option> careers;
    Map<String, Option> minors;
    Set<String> careerKeys;
    Set<String> minorKeys;
    Set<String> coursesKeys;
    String careerChoice;
    String coursesChoice;
    Set<Option> minorCourseForthisCareer;

    ObservableList<String> careerListItems = FXCollections.observableArrayList();
//    ObservableList<String> courseListItems = FXCollections.observableArrayList();



    @FXML
    ListView<String> careersList;

    @FXML
    ListView<String> coursesList;

    @FXML
    CheckBox soft_dev_cb;

    @FXML
    CheckBox game_prog_cb;

    @FXML
    CheckBox database_cb;

    @FXML
    CheckBox telcoms_cb;

    @FXML
    CheckBox info_systems;

    @FXML
    private MenuItem guiClose;

    @FXML
    private MenuItem fileOpen;

    @FXML
    private MenuItem fileSave;

    @FXML
    private MenuItem onAbout;

//    @FXML
//    private AnchorPane minorBox;

    @FXML
    private FlowPane coursesBox;

    //Map<String,CheckBox> checkBoxMap = new HashMap<>();
    //ObservableList<CheckBox> minorsCheckBox  = FXCollections.observableArrayList(soft_dev_cb,game_prog_cb,database_cb,telcom_net_cb);
    Set<String> selectedMinorKeys = new LinkedHashSet<>();

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */


    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            // TODO File careersFile = new File(".....careers.options");

            fileCareers = new File( "src/au/edu/usc/bict_explorer/resources/careers.options" );
            fileMinors = new File( "src/au/edu/usc/bict_explorer/resources/minors.options" );
            fileCourses = new File( "src/au/edu/usc/bict_explorer/resources/courses.options" );

            myDegree = new Degree( fileCareers, fileMinors, fileCourses );

        } catch (IOException | ParseException ex) {
            Logger.getLogger( BICT_fxmlController.class.getName() ).log( Level.SEVERE, null, ex );
        }

        courses = myDegree.courses();
        careers = myDegree.careers();
        minors = myDegree.minors();

        careerKeys = careers.keySet();
        minorKeys = minors.keySet();
        coursesKeys = courses.keySet();

        //career observables
        careers.entrySet().forEach( career -> {
            careerListItems.add( career.getValue().getName() );
        } );
        careersList.setItems( careerListItems );

        //compulsory minors
        Set<Option> compulsoryMinors = minors.get( "BICT" ).getDownstream();

        soft_dev_cb.setDisable( true );
        game_prog_cb.setDisable( true );
        database_cb.setDisable( true );
        telcoms_cb.setDisable( true );
        info_systems.setDisable( true );

        //soft_dev_cb.setE
        Iterator minorKeysIterator = minorKeys.iterator();
    }

    File dataFile = null;
    final FileChooser fileChooser = new FileChooser();

    @FXML
    void setOnfileOpenRequest(ActionEvent actionEvent) throws IOException, ParseException {

        fileChooser.setTitle( "Open file" );
//        fileChooser.showOpenDialog( fileOpen.getParentPopup().getScene().getWindow() );

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter( "Text Files", "*.txt" ),
                new FileChooser.ExtensionFilter( "All Files", "*.*" ) );

        //showing the file chosen
        File isChosen = fileChooser.showOpenDialog( fileOpen.getParentPopup().getScene().getWindow() );

        // opening a scanner
        try (Scanner scan = new Scanner( isChosen )) {

            // grabbing the file data String
            String content = scan.useDelimiter( "/n" ).next();
//            myTextArea.setText( content );

            // saving the file for  use by the fileSave
            dataFile = isChosen;

            // enabling fileSave
            fileSave.setDisable( false );

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void setOnfileSaveAsRequest(ActionEvent actionEvent) throws IOException, ParseException {

        fileChooser.setTitle( "Save file" );
//        fileChooser.showOpenDialog( fileOpen.getParentPopup().getScene().getWindow() );

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter( "Text Files", "*.txt" ),
                new FileChooser.ExtensionFilter( "All Files", "*.*" ) );

        //showing the file chosen
        File isChosen = fileChooser.showOpenDialog( fileOpen.getParentPopup().getScene().getWindow() );

        if (isChosen != null) {
            try (PrintStream ps = new PrintStream( isChosen )) {

//                ps.print( careersBox.getText() );

                // saving the file for use by the fileSave
                dataFile = isChosen;

                // enabling fileSave
                fileSave.setDisable( false );

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }


    @FXML
    void setOnCloseRequest() throws IOException, ParseException {

        guiClose.setOnAction( t -> Platform.exit() );
//        guiClose.setOnAction( t -> System.exit( 0 ) ); /// ***********TODO fix
    }

    @FXML
    void setOnAboutRequest() throws IOException, ParseException {
        final TextArea textArea = new TextArea();
        VBox vBoxPopup = new VBox( textArea );

        try (Scanner sf = new Scanner( new File( "src/au/edu/usc/bict_explorer/bict_explorer/menuItemHelp.txt" ) )) {


            String line = "/n";

            while (sf.hasNextLine())
                line = sf.nextLine();

            System.out.println( line );

            textArea.appendText(line );

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        onAbout.setOnAction( new EventHandler<ActionEvent>() {

            public void handle(ActionEvent t) {

                Stage stage = new Stage();
                Scene newScenePopup = new Scene( vBoxPopup, 200, 100 );
                stage.setTitle( "Help" );
                stage.setScene( newScenePopup );
                stage.show();
            }
        } );
    }

    @FXML
    public void onCareerSelected(MouseEvent e) {

        careerChoice = careersList.getSelectionModel().getSelectedItem();
        String careerKey = null;
        //write to file
        int choiceCarrer = careersList.getSelectionModel().getSelectedIndex();
//        System.out.println("***" + choice);
        switch (choiceCarrer) {
            //Analyst programmer
            case 0:
                careerKey = "AP";
                break;
            //Network analyst
            case 1:
                careerKey = "NA";
                break;
            //Database administrator
            case 2:
                careerKey = "DA";
                break;
            //Business Analyst
            case 3:
                careerKey = "BA";
                break;

            default:
        }

        for (String key : careerKeys) {
            if (key.equals( careerKey )) {
                selectedCareer = careers.get( key );
                selectedCareer.setChosen( true );
                myDegree.processChanges();
                myDegree.careers().get( key ).setChosen( true );
                updateMinors( selectedCareer );

            }
        }

//        for (String key : myDegree.courses().keySet()) {
//            Option courses = myDegree.courses().get( key );
//            Button btCourses = new Button( courses.getCode() );
//            coursesBox.getChildren().add( btCourses );
//        }


        }



    private void updateMinors(Option selectedCareer) {
        //define the checkbox select or not selected properties
        soft_dev_cb.setDisable( false );
        game_prog_cb.setDisable( false );
        database_cb.setDisable( false );
        telcoms_cb.setDisable( false );
        info_systems.setDisable( false );
        soft_dev_cb.setSelected( false );
        game_prog_cb.setSelected( false );
        database_cb.setSelected( false );
        info_systems.setSelected( false );
        telcoms_cb.setSelected( false );

//        System.out.println("******" + soft_dev_cb.getText());
        minorCourseForthisCareer = selectedCareer.getDownstream();

        Object[] keys = selectedMinorKeys.toArray();
        ArrayList<Object> keysList = new ArrayList<>( Arrays.asList( keys ) );
        //   Object[] toArray = keysList.toArray(keys);

        if (selectedMinorKeys.size() > 2) {

            while (selectedMinorKeys.size() > 1) {
                String removeKey = (String) keysList.get( keysList.size() - 1 );
                selectedMinorKeys.remove( removeKey );
                keysList.remove( removeKey );

                if (selectedMinorKeys.size() == 1) {
                    break;
                }

            }

        }

        minorCourseForthisCareer.forEach( action -> {

            selectedMinorKeys.add( action.getCode() );

            action.setChosen( true );

            if (action.getName().equals( soft_dev_cb.getText() )) {
                soft_dev_cb.setSelected( true );
                soft_dev_cb.setDisable( true );
                soft_dev_cb.setId( "bla" );

            } else if (action.getName().equals( game_prog_cb.getText() )) {
                game_prog_cb.setSelected( true );
                game_prog_cb.setDisable( true );
            } else if (action.getName().equals( database_cb.getText() )) {
                database_cb.setSelected( true );
                database_cb.setDisable( true );
            } else if (action.getName().equals( info_systems.getText() )) {
                info_systems.setSelected( true );
                info_systems.setDisable( true );
            } else if (action.getName().equals( telcoms_cb.getText() )) {
                telcoms_cb.setSelected( true );
                telcoms_cb.setDisable( true );
            }
        } );
        System.out.println( selectedMinorKeys.size() );
    }


    private void devChecked() {
        if (selectedMinorKeys.size() == 3) {
            //selectedMinorKeys.remove(selectedMinorKeys.)
            Object[] keys = selectedMinorKeys.toArray();
            String removeKey = (String) keys[keys.length - 1];
            if (minors.get( removeKey ).getName().equals( soft_dev_cb.getText() )) {
                soft_dev_cb.setSelected( false );
            } else if (minors.get( removeKey ).getName().equals( game_prog_cb.getText() )) {
                game_prog_cb.setSelected( false );
            } else if (minors.get( removeKey ).getName().equals( database_cb.getText() )) {
                database_cb.setSelected( false );
            } else if (minors.get( removeKey ).getName().equals( telcoms_cb.getText() )) {
                telcoms_cb.setSelected( false );
            } else if (minors.get( removeKey ).getName().equals( info_systems.getText() )) {
                info_systems.setSelected( false );
            }
            selectedMinorKeys.remove( removeKey );
            System.out.println( selectedMinorKeys.size() );
        }
        for (Map.Entry<String, Option> courseC : courses.entrySet()) {
            Button btCourses = new Button( courseC.getValue().getCode() );
            coursesBox.getChildren().add( btCourses );

        }
    }

    @FXML
    public void onSoftDevChecked(Event e) {
        devChecked();

        if (selectedMinorKeys.size() == 2 && soft_dev_cb.isSelected()) {
            // String key=null;
            selectedMinorKeys.add( "SD" );

            if (game_prog_cb.isSelected()) {
                game_prog_cb.setDisable( true );
            } else game_prog_cb.setDisable( false );

            if (database_cb.isSelected()) {
                database_cb.setDisable( true );
            } else database_cb.setDisable( false );

            if (telcoms_cb.isSelected()) {
                telcoms_cb.setDisable( true );
            } else telcoms_cb.setDisable( false );

            if (info_systems.isSelected()) {
                info_systems.setDisable( true );
            } else info_systems.setDisable( false );

            System.out.println( selectedMinorKeys.size() );
        } else {
        }
    }

    @FXML
    public void onGameProgChecked(ActionEvent e) {
        devChecked();
        //System.out.println(" HELLO");

        if (selectedMinorKeys.size() == 2 && game_prog_cb.isSelected()) {
            // String key=null;
            selectedMinorKeys.add( "GP" );

            if (soft_dev_cb.isSelected()) {
                soft_dev_cb.setDisable( true );
            } else soft_dev_cb.setDisable( false );

            if (database_cb.isSelected()) {
                database_cb.setDisable( true );
            } else database_cb.setDisable( false );

            if (telcoms_cb.isSelected()) {
                telcoms_cb.setDisable( true );
            } else telcoms_cb.setDisable( false );

            if (info_systems.isSelected()) {
                info_systems.setDisable( true );
            } else info_systems.setDisable( false );

            System.out.println( selectedMinorKeys.size() );
        }
    }

    @FXML
    public void onTelcomChecked(Event e) {
        devChecked();

        if (selectedMinorKeys.size() == 2 && telcoms_cb.isSelected()) {
            // String key=null;
            selectedMinorKeys.add( "TCN" );

            if (soft_dev_cb.isSelected()) {
                soft_dev_cb.setDisable( true );
            } else soft_dev_cb.setDisable( false );

            if (database_cb.isSelected()) {
                database_cb.setDisable( true );
            } else database_cb.setDisable( false );

//                    if(telcoms_cb.isSelected()){ telcoms_cb.setDisable(true);}
//                    else telcoms_cb.setDisable(false);
            if (game_prog_cb.isSelected()) {
                game_prog_cb.setDisable( true );
            } else game_prog_cb.setDisable( false );

            if (info_systems.isSelected()) {
                info_systems.setDisable( true );
            } else info_systems.setDisable( false );

            System.out.println( selectedMinorKeys.size() );
        }
    }

    @FXML
    public void onDataMgmentChecked(Event e) {
        devChecked();

        if (selectedMinorKeys.size() == 2 && database_cb.isSelected()) {
            // String key=null;
            selectedMinorKeys.add( "DM" );

            if (soft_dev_cb.isSelected()) {
                soft_dev_cb.setDisable( true );
            } else soft_dev_cb.setDisable( false );

            if (telcoms_cb.isSelected()) {
                telcoms_cb.setDisable( true );
            } else telcoms_cb.setDisable( false );

//                    if(telcoms_cb.isSelected()){ telcoms_cb.setDisable(true);}
//                    else telcoms_cb.setDisable(false);
            if (game_prog_cb.isSelected()) {
                game_prog_cb.setDisable( true );
            } else game_prog_cb.setDisable( false );

            if (info_systems.isSelected()) {
                info_systems.setDisable( true );
            } else info_systems.setDisable( false );

            System.out.println( selectedMinorKeys.size() );
        }
    }

    @FXML
    public void onInfoSystemsChecked(Event e) {

        devChecked();

        if (selectedMinorKeys.size() == 2 && info_systems.isSelected()) {
            // String key=null;
            selectedMinorKeys.add( "IS" );

            if (soft_dev_cb.isSelected()) {
                soft_dev_cb.setDisable( true );
            } else soft_dev_cb.setDisable( false );

            if (telcoms_cb.isSelected()) {
                telcoms_cb.setDisable( true );
            } else telcoms_cb.setDisable( false );

//                    if(telcoms_cb.isSelected()){ telcoms_cb.setDisable(true);}
//                    else telcoms_cb.setDisable(false);
            if (game_prog_cb.isSelected()) {
                game_prog_cb.setDisable( true );
            } else game_prog_cb.setDisable( false );

            if (database_cb.isSelected()) {
                database_cb.setDisable( true );
            } else database_cb.setDisable( false );

            System.out.println( selectedMinorKeys.size() );
        }

    }

}
