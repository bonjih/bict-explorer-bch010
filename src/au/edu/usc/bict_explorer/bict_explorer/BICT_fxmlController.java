package au.edu.usc.bict_explorer.bict_explorer;

import au.edu.usc.bict_explorer.rules.Course;
import au.edu.usc.bict_explorer.rules.Degree;
import au.edu.usc.bict_explorer.rules.Option;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
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
 * @author Ben Hamilton
 */

/**
 * BICT GUI / FXML Controller Class
 * Handles the shape additions to the scene and shape/node behaviour
 */

public class BICT_fxmlController implements Initializable { //initialise the controller when created in the call to FXMLLoader.load
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


    /**
     * Career observable list and used to listen when list changes
     * Observability by wrapping lists with ObservableList.
     */
    ObservableList<String> careerListItems = FXCollections.observableArrayList();

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

    @FXML
    private FlowPane coursesBox;

    @FXML
    private Pane compulsoryMinorsPane;

    @FXML
    private Button confirmMinor;

    @FXML
    private FlowPane otherMinorsPane;

    @FXML
    private Text errorStatus;

    private final Map<Object, ToggleButton> courseMap = new HashMap<>();
    private Option compulsoryMinors;
    private Set<String> selectedMinorKeys = new LinkedHashSet<>();
    private Stage stage = new Stage();

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
//        Set<Option> compulsoryMinors = minors.get( "BICT" ).getDownstream();

        //compulsory minors

        compulsoryMinorsPane.getChildren().add( displayCompulsoryMinors() );
//        confirmMinor.setDisable(true);

        otherMinorsPane.getChildren().add( displayOtherMinors() );

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
    ToggleButton tbButton = new ToggleButton();

    /**
     * @throws IOException    to throw error if read file is incorrect.
     * @throws ParseException if the given format (txt) is not supplied.
     *                        Event handler for open file request.
     */
    @FXML
    void setOnfileOpenRequest() throws IOException, ParseException {

        fileChooser.setTitle( "Open file" );

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter( "Text Files", "*.txt" ),
                new FileChooser.ExtensionFilter( "All Files", "*.*" ) );


        File savedFile = fileChooser.showOpenDialog( fileOpen.getParentPopup().getScene().getWindow() );

        if (savedFile != null) {

            try (Scanner scan = new Scanner( savedFile )) {

                // grabbing the file data String
                String content = scan.useDelimiter( "/n" ).next(); //TODO
//            myTextArea.setText( content );

                dataFile = savedFile;

                // enabling fileSave
                fileSave.setDisable( false );

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @throws IOException    to throw error if write to file error has occurred.
     * @throws ParseException if the given format (txt) is not supplied.
     *                        Event handler for save file request
     */
    @FXML
    private void setOnfileSaveAsRequest() throws IOException, ParseException {

        fileChooser.setTitle( "Save file" );

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter( "Text Files", "*.txt" ),
                new FileChooser.ExtensionFilter( "All Files", "*.*" ) );

        File saveAsFile = fileChooser.showOpenDialog( fileOpen.getParentPopup().getScene().getWindow() );

        if (saveAsFile != null) {
            try (PrintStream ps = new PrintStream( saveAsFile )) {

//                ps.print( careersBox.getText() );

                // saving the file for use by the fileSave
                dataFile = saveAsFile;

                // enabling fileSave
                fileSave.setDisable( false );

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Event handler to exit from program
     */
    @FXML
    void setOnCloseRequest() {

//        guiClose.setOnAction( t -> Platform.exit() );
        guiClose.setOnAction( t -> System.exit( 0 ) ); /// ***********TODO fix
    }

    /**
     * @throws IOException    to throw error if txt file does not exist.
     * @throws ParseException if the given format (txt) is not supplied.
     * Event handler for 'about/help' menu'
     */
    @FXML
    void setOnAboutRequest(ActionEvent event) throws IOException, ParseException {
        String helpFile = new File( "src/au/edu/usc/bict_explorer/bict_explorer/menuItemHelp.txt" ).toURI().toURL().toExternalForm();

        WebView webView = new WebView();
        webView.setMaxHeight( 300 );
        webView.setMaxWidth( 550 );

        errorStatus = new Text();

         if (helpFile != null) {
            try {

                webView.getEngine().load( helpFile );
                Scene scene = new Scene( webView  );
                stage.setTitle( "Help Menu" );
                stage.setResizable( false );
                stage.setScene( scene );
                stage.show();

            } catch (Exception e) {
                e.printStackTrace();
                errorStatus.setText( "An ERROR occurred while loading the file!" );
            }
        }
    }

    /**
     * @param e not used
     * Mouse even handler for career selection
     */
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
    }

    /**
     * @param selectedCareer Unique code for a career
     * Defines the properties when the minor checkbox is selected or not.
     */
    private void updateMinors(Option selectedCareer) {
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

        minorCourseForthisCareer = selectedCareer.getDownstream();
        compulsoryMinors.setChosen( true );

        Object[] keys = selectedMinorKeys.toArray();
        ArrayList<Object> keysList = new ArrayList<>( Arrays.asList( keys ) );

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

    /**
     * Checks and remove ticks in cehckbox if more then 3 minors are selected
     */
    private void devChecked() {
        if (selectedMinorKeys.size() == 3) {
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
        // add courses to GUI

//        private VBox displayOtherMinors(){
//
//            VBox vbOther = new VBox(3);
//
//            otherMinors = minors.get("SD");//.getDownstream();
//            // String minorCourse =null;
//            otherMinors.getDownstream().forEach(minor->{
//                HBox detailsOtherM = new HBox(10);
//                Course cOther = (Course)minor;
//                cOther.setChosen(true);
//                detailsOtherM.getChildren().addAll(new Button(cOther.getCode()), new Label(cOther.getSemesters()));
//                vbOther.getChildren().add(detailsOtherM);
//
//            });
//
//            return vbOther;
//
//        }

        for (Map.Entry<String, Option> courseC : courses.entrySet()) {
//            ToggleButton tbCourses = new ToggleButton( courseC.getValue().getCode() );
//
//
//            courseMap.put(courseC, tbCourses);
//            tbCourses.setOnAction( event -> {
//                if (selectedCourses.isChosen()) {
//                    selectedCourses.setChosen(false);
//                } else {
//                    selectedCourses.setChosen(true );
//                }
////                selectedCourses.setChosen(!selectedCourses.isChosen());
////                updateCareers();
//
//            });
//
//            otherMinorsPane.setHgap( 10 );
//            otherMinorsPane.setVgap( 10 );
//            otherMinorsPane.getChildren().add(tbCourses );
//
//        }


//    private void updateCareers() {
//       myDegree.processChanges();
//        for (Option course : courseMap.keySet()) {
//            ToggleButton tbCourses = courseMap.get(course);
//            tbCourses.setSelected( course.isChosen() );
//        }
//    }

//
//    public class buttonEventHandler implements EventHandler<ActionEvent> {
//        @Override
//        public void handle(ActionEvent event) {
//         ToggleButton sourceButton = (ToggleButton) event.getSource();
//
//            if (!sourceButton.isSelected()) {
//                sourceButton.setSelected( true );
//
//
        }
    }

    /**
     * Software Development selection logic for checkboxes
     */
    @FXML
    public void onSoftDevChecked() {
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
            //testing
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
            //testing
            System.out.println( selectedMinorKeys.size() );
        }
    }

    /**
     * Telcom Networks selection logic for checkboxes
     */
    @FXML
    public void onTelcomChecked() {
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

            if (game_prog_cb.isSelected()) {
                game_prog_cb.setDisable( true );
            } else game_prog_cb.setDisable( false );

            if (info_systems.isSelected()) {
                info_systems.setDisable( true );
            } else info_systems.setDisable( false );
            //testing
            System.out.println( selectedMinorKeys.size() );
        }
    }

    /**
     * Data Management selection logic for checkboxes
     */
    @FXML
    public void onDataMgmentChecked() {
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

            if (game_prog_cb.isSelected()) {
                game_prog_cb.setDisable( true );
            } else game_prog_cb.setDisable( false );

            if (info_systems.isSelected()) {
                info_systems.setDisable( true );
            } else info_systems.setDisable( false );
            //testing
            System.out.println( selectedMinorKeys.size() );
        }
    }

    /**
     * Information Systems selection logic for checkboxes
     */
    @FXML
    public void onInfoSystemsChecked() {

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

            if (game_prog_cb.isSelected()) {
                game_prog_cb.setDisable( true );
            } else game_prog_cb.setDisable( false );

            if (database_cb.isSelected()) {
                database_cb.setDisable( true );
            } else database_cb.setDisable( false );

            //testing
            System.out.println( selectedMinorKeys.size() );
        }

    }

    /**
     * @return returns vBox result
     * Adds compulsory Minors to scene
     */
    private VBox displayCompulsoryMinors() {

        VBox vb = new VBox( 3 );

        compulsoryMinors = minors.get( "BICT" );//.getDownstream();
        // String minorCourse =null;
        compulsoryMinors.getDownstream().forEach( minor -> {
            HBox detailsHB = new HBox( 10 );
            Course c = (Course) minor;
            c.setChosen( true );
            detailsHB.getChildren().addAll( new Button( c.getCode() ), new Label( "        " ), new Label( c.getSemesters() ) );
            vb.getChildren().add( detailsHB );

        } );

        return vb;
    }

    private VBox displayOtherMinors() {


        VBox vbOther = new VBox( 3 );

        Option otherMinors = minors.get( "SD" );
        // String minorCourse =null;
        otherMinors.getDownstream().forEach( minor -> {
            HBox detailsOtherM = new HBox( 10 );
            Course cOther = (Course) minor;
            ToggleButton tbCourses = new ToggleButton( cOther.getCode() );
            tbCourses.setOnAction( event -> {
                if (selectedCourses.isChosen()) {
                    selectedCourses.setChosen( false );
                } else {
                    selectedCourses.setChosen( true );
                }

            } );

            cOther.setChosen( true );
            detailsOtherM.getChildren().addAll( new Button( cOther.getCode() ), new Label( cOther.getSemesters() ) );
            vbOther.getChildren().add( detailsOtherM );

        } );

        return vbOther;

    }

}
